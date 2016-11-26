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

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import io.kolumbus.Analyzer
import io.kolumbus.BuildConfig
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.adapter.TableAdapter
import io.kolumbus.extension.prettify
import io.kolumbus.layout.TableLayoutManager
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class TableActivity : Activity(), RealmChangeListener<RealmResults<RealmModel>> {
    private var empty: TextView? = null
    private var entries: List<RealmModel>? = null
    private val realm: Realm by lazy { Realm.getDefaultInstance() }
    private var recyclerView: RecyclerView? = null
    private var tableClass: Class<out RealmModel>? = null

    companion object {
        private val EXTRA_TABLE_CLASS = BuildConfig.APPLICATION_ID + ".extra.TABLE_CLASS"

        fun start(context: Context, table: Class<out RealmModel>?) {
            this.start(context, table, null)
        }

        fun <T : RealmModel> start(context: Context, table: Class<out T>?, items: Array<out T>?) {
            val intent = Intent(context, TableActivity::class.java)
            intent.putExtra(EXTRA_TABLE_CLASS, table)

            if (items != null) {
                Kolumbus.items.addAll(items)
            } else {
                Kolumbus.items.clear()
            }

            context.startActivity(intent)
        }
    }

    override fun onChange(objects: RealmResults<RealmModel>) {
        this.displayTableContent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_table)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.actionBar?.setDisplayHomeAsUpEnabled(true)
        }

        this.empty = this.findViewById(android.R.id.empty) as TextView?
        this.recyclerView = this.findViewById(android.R.id.list) as RecyclerView?
        this.tableClass = this.intent.getSerializableExtra(EXTRA_TABLE_CLASS) as Class<out RealmModel>
        this.title = this.tableClass?.simpleName?.prettify() ?: ""

        this.recyclerView?.layoutManager = TableLayoutManager(this)

        if (Kolumbus.items.isNotEmpty()) {
            this.entries = Kolumbus.items.toList()

            this.displayTableContent()

            Kolumbus.items.clear()
        } else {
            this.entries = this.realm.where(this.tableClass).findAllAsync()

            (this.entries as RealmResults).addChangeListener(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.kolumbus_table, menu)

        val count = this.realm.where(this.tableClass).count()

        menu?.findItem(R.id.menu_clear_table)?.isVisible = count > 0
        menu?.findItem(R.id.menu_view_all)?.isVisible = count - (this.entries?.size ?: 0) > 0

        return true
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
                            it.delete(tableClass)
                        }

                        this.displayTableContent()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            this.invalidateOptionsMenu()
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()

            return true
        }

        if (item?.itemId == R.id.menu_table_info) {
            TableInfoActivity.start(this, this.tableClass)

            return true
        }

        if (item?.itemId == R.id.menu_view_all) {
            TableActivity.start(this, this.tableClass)

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        if (this.entries is RealmResults<*>) {
            val entries = this.entries as RealmResults<*>

            if (entries.isValid) {
                entries.removeChangeListeners()
            }
        }

        this.realm.close()

        super.onStop()
    }

    private fun displayTableContent() {
        if (this.entries?.size ?: 0 == 0) {
            this.empty?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE

            return
        }

        val fields = Analyzer.getRealmFields(this.tableClass)
        val methods = Analyzer.getAccessors(this.tableClass, fields)

        this.empty?.visibility = View.GONE
        this.recyclerView?.adapter = TableAdapter(this.entries ?: emptyList(), fields, methods)
        this.recyclerView?.visibility = View.VISIBLE
    }
}
