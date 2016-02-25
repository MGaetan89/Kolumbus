package io.kolumbus.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.kolumbus.Kolumbus
import io.kolumbus.KolumbusModule
import io.kolumbus.demo.model.Category
import io.realm.Realm
import io.realm.RealmConfiguration

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
                with(createObject(Category::class.java)) {
                    id = 1
                    name = "Hello, World!"
                }
            }

            close()
        }

        with(Kolumbus) {
            register(Category::class.java)
            start(this@DemoActivity)
        }
    }
}
