/*
 * Copyright (C) 2016 MGaetan89
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import io.kolumbus.Analyzer
import io.kolumbus.BuildConfig
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.extension.prettify
import io.realm.*
import io.realm.annotations.PrimaryKey
import java.lang.reflect.ParameterizedType

class TableActivity : AppCompatActivity() {
    private var empty: TextView? = null
    private var entries: List<RealmObject>? = null
    private var entriesListener = RealmChangeListener {
        displayTableContent()
    }
    private val realm = Realm.getDefaultInstance()
    private var scroll: ScrollView? = null
    private var table: TableLayout? = null
    private var tableClass: Class<out RealmObject>? = null

    companion object {
        private val EXTRA_TABLE_CLASS = BuildConfig.APPLICATION_ID + ".extra.TABLE_CLASS"

        fun start(context: Context, table: Class<out RealmObject>?) {
            this.start(context, table, null)
        }

        fun <T : RealmObject> start(context: Context, table: Class<out T>?, items: Array<out T>?) {
            val intent = Intent(context, TableActivity::class.java)
            intent.putExtra(EXTRA_TABLE_CLASS, table)

            if (items != null) {
                Kolumbus.items.addAll(items)
            }

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_table)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.empty = this.findViewById(android.R.id.empty) as TextView?
        this.scroll = this.findViewById(R.id.scroll) as ScrollView?
        this.table = this.findViewById(R.id.table) as TableLayout?
        this.tableClass = this.intent.getSerializableExtra(EXTRA_TABLE_CLASS) as Class<out RealmObject>
        this.title = (this.tableClass as Class<out RealmObject>).simpleName.prettify()

        if (Kolumbus.items.isNotEmpty()) {
            this.entries = Kolumbus.items.toList()

            this.displayTableContent()

            Kolumbus.items.clear()
        } else {
            this.entries = this.realm.where(this.tableClass).findAllAsync()

            (this.entries as RealmResults).addChangeListener(this.entriesListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.kolumbus_table, menu)

        val count = this.realm.where(this.tableClass).count()

        menu?.findItem(R.id.menu_clear_table)?.isVisible = count > 0

        return true
    }

    override fun onDestroy() {
        if (this.entries is RealmResults<*>) {
            (this.entries as RealmResults<*>).removeChangeListeners()
        }

        this.realm.close()

        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            this.onBackPressed()

            return true
        }

        if (item?.itemId == R.id.menu_clear_table) {
            AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.kolumbus_clear_table_confirm, this.tableClass?.simpleName))
                    .setPositiveButton(R.string.kolumbus_clear, { dialog, which ->
                        realm.executeTransaction {
                            it.clear(tableClass)
                        }

                        this.displayTableContent()
                        this.invalidateOptionsMenu()
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()

            return true
        }

        if (item?.itemId == R.id.menu_table_info) {
            TableInfoActivity.start(this, this.tableClass)

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displayTableContent() {
        val fields = Analyzer.getRealmFields(this.tableClass)
        val methods = Analyzer.getAccessors(this.tableClass, fields)

        this.table?.removeAllViews()

        if (this.entries?.size ?: 0 == 0) {
            this.empty?.visibility = View.VISIBLE
            this.scroll?.visibility = View.GONE

            return;
        }

        this.empty?.visibility = View.GONE
        this.scroll?.visibility = View.VISIBLE

        var tableRow = this.layoutInflater.inflate(R.layout.kolumbus_table_row, this.table, false) as TableRow

        this.table?.addView(tableRow)

        fields.forEach {
            val header = this.layoutInflater.inflate(R.layout.kolumbus_table_row_header, tableRow, false) as TextView

            if (it.isAnnotationPresent(PrimaryKey::class.java)) {
                header.text = "#${it.name.prettify()}"
            } else {
                header.text = it.name.prettify()
            }

            tableRow.addView(header)
        }

        this.entries!!.forEach { entry ->
            tableRow = this.layoutInflater.inflate(R.layout.kolumbus_table_row, this.table, false) as TableRow

            this.table?.addView(tableRow)

            fields.forEach {
                val value = this.layoutInflater.inflate(R.layout.kolumbus_table_row_text, tableRow, false) as TextView
                val result = methods[it.name]?.invoke(entry)

                if (result is Boolean) {
                    Kolumbus.architect.displayBoolean(value, result)
                } else if (result is Float) {
                    Kolumbus.architect.displayFloat(value, result)
                } else if (result is Int) {
                    Kolumbus.architect.displayInt(value, result)
                } else if (result is RealmList<*>) {
                    val returnType = it.genericType as ParameterizedType
                    val type = returnType.actualTypeArguments[0] as Class<RealmObject>

                    Kolumbus.architect.displayRealmList(value, result, type)
                } else if (result is RealmObject) {
                    Kolumbus.architect.displayRealmObject(value, result)
                } else if (result is String) {
                    if (result.isEmpty()) {
                        Kolumbus.architect.displayEmpty(value)
                    } else {
                        if (Patterns.WEB_URL.matcher(result).matches()) {
                            Kolumbus.architect.displayUrl(value, result)
                        } else {
                            try {
                                val color = Color.parseColor(result)

                                Kolumbus.architect.displayColor(value, result, color)
                            } catch (exception: IllegalArgumentException) {
                                Kolumbus.architect.displayString(value, result)
                            }
                        }
                    }
                } else if (result != null) {
                    Kolumbus.architect.displayAny(value, result)
                } else {
                    Kolumbus.architect.displayNull(value)
                }

                tableRow.addView(value)
            }
        }
    }
}
