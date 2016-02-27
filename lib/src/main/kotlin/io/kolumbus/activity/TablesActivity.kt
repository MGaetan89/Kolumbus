package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.adapter.TablesAdapter
import io.realm.Realm

class TablesActivity : AppCompatActivity() {
    private var listView: ListView? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TablesActivity::class.java)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_tables)

        val empty = this.findViewById(android.R.id.empty) as TextView?
        this.listView = this.findViewById(android.R.id.list) as ListView?

        if (empty != null) {
            empty.visibility = if (Kolumbus.tables.isEmpty()) View.VISIBLE else View.GONE
        }

        if (this.listView != null) {
            (this.listView as ListView).adapter = this.getAdapter()
            (this.listView as ListView).setOnItemClickListener { adapterView, view, i, l ->
                TableActivity.start(this, Kolumbus.tables[adapterView.getItemAtPosition(i)])
            }
            (this.listView as ListView).visibility = if (Kolumbus.tables.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.kolumbus_tables, menu)

        return Kolumbus.tables.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_clear_database) {
            AlertDialog.Builder(this)
                    .setMessage(R.string.kolumbus_clear_database_confirm)
                    .setPositiveButton(R.string.kolumbus_clear, { dialog, which ->
                        val realm = Realm.getDefaultInstance()
                        val configuration = realm.configuration
                        realm.close()

                        Realm.deleteRealm(configuration)

                        if (this.listView != null) {
                            (this.listView as ListView).adapter = this.getAdapter()
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getAdapter(): TablesAdapter {
        val realm = Realm.getDefaultInstance()
        val tablesAndCounts = mutableMapOf<String, Long>()

        for ((tableName, tableClass) in Kolumbus.tables) {
            tablesAndCounts.put(tableName, realm.where(tableClass).count())
        }

        realm.close()

        return TablesAdapter(this, tablesAndCounts)
    }
}
