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

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.adapter.TablesAdapter
import io.realm.Realm

class TablesActivity : Activity() {
	private var recyclerView: RecyclerView? = null

	companion object {
		fun start(context: Context) {
			val intent = Intent(context, TablesActivity::class.java)

			context.startActivity(intent)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.kolumbus_activity_tables)

		val empty = this.findViewById(android.R.id.empty) as TextView
		empty.visibility = if (Kolumbus.hasTables()) View.GONE else View.VISIBLE

		this.recyclerView = this.findViewById(android.R.id.list) as RecyclerView
		this.recyclerView?.let {
			it.layoutManager = LinearLayoutManager(this)
			it.visibility = if (Kolumbus.hasTables()) View.VISIBLE else View.GONE
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		this.menuInflater.inflate(R.menu.kolumbus_tables, menu)

		return Kolumbus.hasTables()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.menu_clear_database) {
			AlertDialog.Builder(this)
					.setMessage(R.string.kolumbus_clear_database_confirm)
					.setPositiveButton(R.string.kolumbus_clear, { _, _ ->
						with(Realm.getDefaultInstance()) {
							executeTransaction {
								it.deleteAll()
							}

							close()
						}

						this.recyclerView?.adapter = this.getAdapter()
					})
					.setNegativeButton(android.R.string.cancel, null)
					.show()

			return true
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onResume() {
		super.onResume()

		this.recyclerView?.adapter = this.getAdapter()
	}

	private fun getAdapter(): TablesAdapter {
		val tables = Kolumbus.getTables()
		val realm = Realm.getDefaultInstance()
		val counts = tables.map { realm.where(it).count() }
		realm.close()

		return TablesAdapter(tables.toList(), counts)
	}
}
