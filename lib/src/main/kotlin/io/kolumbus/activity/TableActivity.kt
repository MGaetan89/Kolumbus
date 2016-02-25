package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.kolumbus.BuildConfig
import io.realm.RealmObject

class TableActivity : AppCompatActivity() {
    companion object {
        private val EXTRA_TABLE_CLASS = BuildConfig.APPLICATION_ID + ".extra.TABLE_CLASS"

        fun start(context: Context, table: Class<out RealmObject>?) {
            val intent = Intent(context, TableActivity::class.java)
            intent.putExtra(EXTRA_TABLE_CLASS, table)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tableClass = this.intent.getSerializableExtra(EXTRA_TABLE_CLASS) as Class<out RealmObject>

        // TODO List table content
    }
}
