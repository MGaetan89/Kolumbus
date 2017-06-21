package io.kolumbus.demo.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Product : RealmObject() {
	open var description = ""
	@PrimaryKey
	open var id = 0
	open var name = ""
	open var categories: RealmList<Category>? = null
}
