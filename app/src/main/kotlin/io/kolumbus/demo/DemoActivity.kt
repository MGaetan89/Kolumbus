package io.kolumbus.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.kolumbus.Kolumbus
import io.kolumbus.KolumbusModule
import io.kolumbus.demo.model.Category
import io.kolumbus.demo.model.Product
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.util.*

class DemoActivity : AppCompatActivity() {
    private val CATEGORIES_COUNT = 25
    private val MAX_LINKED_CATEGORIES = 10
    private val PRODUCTS_COUNT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val realmConfiguration = RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .setModules(AppModule(), KolumbusModule())
                .build()
        Realm.deleteRealm(realmConfiguration)
        Realm.setDefaultConfiguration(realmConfiguration)

        this.fillDatabase()

        with(Kolumbus) {
            register(Category::class.java)
            register(Product::class.java)
            start(this@DemoActivity)
        }
    }

    private fun fillDatabase() {
        with(Realm.getDefaultInstance()) {
            executeTransaction {
                val random = Random()

                for (i in 1..CATEGORIES_COUNT) {
                    with(createObject(Category::class.java)) {
                        color = random.nextColor()
                        id = i
                        name = "Category $i"
                    }
                }

                for (i in 1..PRODUCTS_COUNT) {
                    with(createObject(Product::class.java)) {
                        categories = RealmList<Category>()

                        for (j in 0..(random.nextInt(MAX_LINKED_CATEGORIES) - 1)) {
                            val category = where(Category::class.java).equalTo("id", random.nextInt(CATEGORIES_COUNT - 1) + 1).findFirst()

                            (categories as RealmList<Category>).add(category)
                        }

                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas mollis eget nibh et condimentum."
                        id = i
                        name = "Product $i"
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
