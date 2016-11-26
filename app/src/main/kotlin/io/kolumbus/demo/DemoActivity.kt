package io.kolumbus.demo

import android.app.Activity
import android.os.Bundle
import io.kolumbus.Kolumbus
import io.kolumbus.demo.model.Category
import io.kolumbus.demo.model.Product
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.util.*

class DemoActivity : Activity() {
    private val CATEGORIES_COUNT = 100
    private val MAX_LINKED_CATEGORIES = 50
    private val PRODUCTS_COUNT = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this)

        val realmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        this.fillDatabase()

        with(Kolumbus) {
            explore(realmConfiguration)
            navigate(this@DemoActivity)
        }
    }

    private fun fillDatabase() {
        with(Realm.getDefaultInstance()) {
            executeTransaction {
                val random = Random()
                val categoriesCount = this.where(Category::class.java).count()
                val productsCount = this.where(Product::class.java).count()

                for (i in 1..CATEGORIES_COUNT) {
                    with(createObject(Category::class.java, (categoriesCount + i).toInt())) {
                        color = random.nextColor()
                        name = "Category $id"
                    }
                }

                for (i in 1..PRODUCTS_COUNT) {
                    with(createObject(Product::class.java, (productsCount + i).toInt())) {
                        categories = RealmList<Category>()

                        for (j in 0..(random.nextInt(MAX_LINKED_CATEGORIES) - 1)) {
                            val category = where(Category::class.java).equalTo("id", random.nextInt(CATEGORIES_COUNT - 1) + 1).findFirst()

                            if (!(categories as RealmList<Category>).contains(category)) {
                                (categories as RealmList<Category>).add(category)
                            }
                        }

                        description = "<b>Lorem ipsum</b> dolor sit amet, <i>consectetur adipiscing</i> elit. <b><u>Maecenas mollis</u></b> eget nibh et condimentum."
                        name = "Product $id"
                    }
                }
            }

            close()
        }
    }
}

fun Random.nextColor(): String {
    fun Random.nextColorHex(): String {
        return Integer.toHexString(this.nextInt(256)).padStart(2, '0')
    }

    return "#${this.nextColorHex()}${this.nextColorHex()}${this.nextColorHex()}"
}
