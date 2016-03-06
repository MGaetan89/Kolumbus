package io.kolumbus.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Genre : RealmObject() {
    @PrimaryKey
    open var name: String = ""
}
