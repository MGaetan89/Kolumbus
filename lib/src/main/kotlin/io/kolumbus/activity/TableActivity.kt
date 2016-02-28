package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import io.kolumbus.BuildConfig
import io.kolumbus.R
import io.kolumbus.extension.prettify
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

class TableActivity : AppCompatActivity() {
    private var empty: TextView? = null
    private var scroll: ScrollView? = null
    private var table: TableLayout? = null
    private var tableClass: Class<out RealmObject>? = null

    companion object {
        private val EXTRA_TABLE_CLASS = BuildConfig.APPLICATION_ID + ".extra.TABLE_CLASS"

        fun start(context: Context, table: Class<out RealmObject>?) {
            val intent = Intent(context, TableActivity::class.java)
            intent.putExtra(EXTRA_TABLE_CLASS, table)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_table)

        this.empty = this.findViewById(android.R.id.empty) as TextView?
        this.scroll = this.findViewById(R.id.scroll) as ScrollView?
        this.table = this.findViewById(R.id.table) as TableLayout?
        this.tableClass = this.intent.getSerializableExtra(EXTRA_TABLE_CLASS) as Class<out RealmObject>
        this.title = (this.tableClass as Class<out RealmObject>).simpleName.prettify()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.kolumbus_table, menu)

        val realm = Realm.getDefaultInstance()
        val count = realm.where(this.tableClass).count()
        realm.close()

        return count > 0
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_clear_table) {
            AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.kolumbus_clear_table_confirm, this.tableClass?.simpleName))
                    .setPositiveButton(R.string.kolumbus_clear, { dialog, which ->
                        with(Realm.getDefaultInstance()) {
                            executeTransaction {
                                it.getTable(tableClass).clear()
                            }

                            close()
                        }

                        this.displayTableContent()
                        this.invalidateOptionsMenu()
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        this.displayTableContent()
    }

    private fun displayTableContent() {
        val methods = (this.tableClass as Class<out RealmObject>).declaredMethods.filter {
            Modifier.isPublic(it.modifiers) && !Modifier.isStatic(it.modifiers) && it.parameterTypes.size == 0
        }

        val realm = Realm.getDefaultInstance()
        val count = realm.where(this.tableClass).count()

        this.table?.removeAllViews()

        if (count == 0L) {
            this.empty?.visibility = View.VISIBLE
            this.scroll?.visibility = View.GONE

            realm.close()

            return;
        }

        this.empty?.visibility = View.GONE
        this.scroll?.visibility = View.VISIBLE

        var tableRow = this.layoutInflater.inflate(R.layout.kolumbus_table_row, this.table, false) as TableRow

        this.table?.addView(tableRow)

        methods.forEach {
            val header = this.layoutInflater.inflate(R.layout.kolumbus_table_row_header, tableRow, false) as TextView
            header.text = it.name.prettify()

            tableRow.addView(header)
        }

        val entries = realm.where(this.tableClass).findAll()

        entries.forEach { entry ->
            tableRow = this.layoutInflater.inflate(R.layout.kolumbus_table_row, this.table, false) as TableRow

            this.table?.addView(tableRow)

            methods.forEach {
                val value = this.layoutInflater.inflate(R.layout.kolumbus_table_row_text, tableRow, false) as TextView
                val result = it.invoke(entry)

                if (result is Boolean) {
                    value.text = this.getString(if (result) R.string.kolumbus_yes else R.string.kolumbus_no)
                } else if (result is RealmList<*>) {
                    val returnType = it.genericReturnType as ParameterizedType
                    val genericType = returnType.actualTypeArguments[0] as Class<*>

                    if (result.isNotEmpty()) {
                        val resultStringArray = result.map {
                            it.toString()
                        }.toTypedArray()

                        value.setOnClickListener {
                            // TODO Display the result in the table
                            AlertDialog.Builder(this)
                                    .setItems(resultStringArray, null)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show()
                        }
                    }

                    value.text = Html.fromHtml(this.getString(R.string.kolumbus_linked_entries, result.size, genericType.simpleName.prettify()))
                } else if (result is String) {
                    if (result.isEmpty()) {
                        value.text = this.getString(R.string.kolumbus_empty)
                    } else {
                        if (Patterns.WEB_URL.matcher(result).matches()) {
                            value.text = Html.fromHtml(this.getString(R.string.kolumbus_link, result, result.replaceFirst("([^:/])/.*/".toRegex(), "$1/.../")))
                            value.movementMethod = LinkMovementMethod.getInstance()
                        } else {
                            try {
                                val color = Color.parseColor(result)
                                val drawable = this.getDrawable(R.drawable.kolumbus_color_preview) as LayerDrawable
                                drawable.getDrawable(1).setColorFilter(color, PorterDuff.Mode.SRC_IN)

                                value.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                                value.text = result
                            } catch (exception: IllegalArgumentException) {
                                val htmlString = Html.fromHtml(result)

                                if (htmlString.length > 50) {
                                    value.setOnClickListener {
                                        AlertDialog.Builder(this)
                                                .setMessage(htmlString)
                                                .setPositiveButton(android.R.string.ok, null)
                                                .show()
                                    }

                                    value.text = htmlString.subSequence(0, 47)
                                    value.append("...")
                                } else {
                                    value.text = htmlString
                                }
                            }
                        }
                    }
                } else if (result != null) {
                    value.text = result.toString()
                } else {
                    value.text = this.getString(R.string.kolumbus_null)
                }

                tableRow.addView(value)
            }
        }

        realm.close()
    }
}
