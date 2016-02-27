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

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val realmConfiguration = RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .setModules(AppModule(), KolumbusModule())
                .build()
        Realm.deleteRealm(realmConfiguration)
        Realm.setDefaultConfiguration(realmConfiguration)

        with(Realm.getDefaultInstance()) {
            executeTransaction {
                // Create some categories
                val book = createObject(Category::class.java)
                book.id = 1
                book.name = "Book"

                val dvd = createObject(Category::class.java)
                dvd.id = 2
                dvd.name = "DVD"

                val game = createObject(Category::class.java)
                game.id = 3
                game.name = "Game"

                // Create some products
                with(createObject(Product::class.java)) {
                    id = 1
                    name = "The Hitchhiker's Guide to the Galaxy"
                    categories = RealmList(book, dvd)
                }
                with(createObject(Product::class.java)) {
                    id = 2
                    name = "Harry Potter and the Order of the Phoenix"
                    categories = RealmList(book, dvd, game)
                }
            }

            close()
        }

        with(Kolumbus) {
            register(Category::class.java)
            register(Product::class.java)
            start(this@DemoActivity)
        }
    }
}
