package io.kolumbus.demo.model

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey

open class Product : RealmModel {
    open var description = ""
    @PrimaryKey
    open var id = 0
    open var name = ""
    open var categories: RealmList<Category>? = null
}
