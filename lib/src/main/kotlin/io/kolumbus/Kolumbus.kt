package io.kolumbus

import android.content.Context
import io.kolumbus.activity.TablesListActivity
import io.realm.RealmObject

object Kolumbus {
    internal val tables = mutableMapOf<String, Class<out RealmObject>>()

    fun register(table: Class<out RealmObject>): Kolumbus {
        this.tables.put(table.simpleName, table)

        return this
    }

    fun start(context: Context) {
        TablesListActivity.start(context)
    }

    fun unregister(table: Class<out RealmObject>): Kolumbus {
        this.tables.remove(table.simpleName)

        return this
    }
}
