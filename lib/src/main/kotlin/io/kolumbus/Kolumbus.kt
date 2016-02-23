package io.kolumbus

import android.content.Context
import io.kolumbus.activity.TablesListActivity
import io.realm.RealmObject
import kotlin.reflect.KClass

class Kolumbus private constructor() {
    companion object {
        val INSTANCE: Kolumbus by lazy { Kolumbus() }
    }

    internal val tables = mutableMapOf<String, KClass<out RealmObject>>()

    fun register(table: KClass<out RealmObject>): Kolumbus {
        this.tables.put(table.simpleName ?: "", table)

        return this
    }

    fun start(context: Context) {
        TablesListActivity.start(context)
    }

    fun unregister(table: KClass<out RealmObject>): Kolumbus {
        this.tables.remove(table.simpleName ?: "")

        return this
    }
}
