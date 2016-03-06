package io.kolumbus

import android.content.Context
import io.realm.RealmObject

object Kolumbus {
    fun explore(table: Class<out RealmObject>) = this

    fun forget(table: Class<out RealmObject>) = this

    fun forgetAll() = Unit

    fun navigate(context: Context) = Unit
}
