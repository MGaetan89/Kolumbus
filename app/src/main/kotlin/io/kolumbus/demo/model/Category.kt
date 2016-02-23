package io.kolumbus.demo.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category : RealmObject() {
    @PrimaryKey
    open var id = 0
    open var name = ""
}
