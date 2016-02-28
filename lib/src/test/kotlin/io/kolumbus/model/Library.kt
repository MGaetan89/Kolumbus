package io.kolumbus.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Library : RealmObject() {
    open var books: RealmList<Book>? = null
    @PrimaryKey
    open var name = ""
}
