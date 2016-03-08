/*
 * Copyright (C) 2016 MGaetan89
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.kolumbus.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.kolumbus.BuildConfig
import io.kolumbus.R
import io.kolumbus.adapter.TableInfoAdapter
import io.kolumbus.extension.prettify
import io.realm.RealmObject
import io.realm.annotations.Ignore
import java.lang.reflect.Modifier

class TableInfoActivity : AppCompatActivity() {
    companion object {
        private val EXTRA_TABLE_CLASS = BuildConfig.APPLICATION_ID + ".extra.TABLE_CLASS"

        fun start(context: Context, table: Class<out RealmObject>?) {
            val intent = Intent(context, TableInfoActivity::class.java)
            intent.putExtra(EXTRA_TABLE_CLASS, table)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.kolumbus_activity_table_info)

        val recyclerView = this.findViewById(android.R.id.list) as RecyclerView?
        val tableClass = this.intent.getSerializableExtra(EXTRA_TABLE_CLASS) as Class<out RealmObject>

        this.title = tableClass.simpleName.prettify()

        if (recyclerView != null) {
            val fields = tableClass.declaredFields.filter {
                !Modifier.isStatic(it.modifiers) && !it.isAnnotationPresent(Ignore::class.java)
            }

            recyclerView.adapter = TableInfoAdapter(fields, tableClass.newInstance())
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}
