package io.kolumbus

import android.content.Context
import io.kolumbus.activity.TablesActivity
import io.realm.RealmObject

object Kolumbus {
    internal val tables = mutableMapOf<String, Class<out RealmObject>>()

    fun explore(table: Class<out RealmObject>): Kolumbus {
        this.tables.put(table.simpleName, table)

        return this
    }

    fun forget(table: Class<out RealmObject>): Kolumbus {
        this.tables.remove(table.simpleName)

        return this
    }

    fun navigate(context: Context) = TablesActivity.start(context)
}
