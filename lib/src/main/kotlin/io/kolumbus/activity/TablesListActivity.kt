package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.adapter.TablesAdapter
import io.realm.Realm

class TablesListActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TablesListActivity::class.java)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_tables_list)

        val listView = this.findViewById(android.R.id.list) as ListView?

        if (listView != null) {
            listView.adapter = this.getAdapter()
        }
    }

    private fun getAdapter(): TablesAdapter {
        val realm = Realm.getDefaultInstance()
        val tablesAndCounts = mutableMapOf<String, Long>()

        for ((tableName, tableClass) in Kolumbus.INSTANCE.tables) {
            tablesAndCounts.put(tableName, realm.where(tableClass.java).count())
        }

        realm.close()

        return TablesAdapter(this, tablesAndCounts)
    }
}
