package io.kolumbus.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Book : RealmObject() {
    open var available = true
    open var description = ""
    open var genre: RealmList<Genre>? = null
    @PrimaryKey
    open var name = ""
}
