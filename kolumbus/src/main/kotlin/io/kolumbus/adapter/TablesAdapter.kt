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

package io.kolumbus.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.kolumbus.R
import io.kolumbus.activity.TableActivity
import io.kolumbus.extension.format
import io.kolumbus.extension.prettify
import io.realm.RealmModel

class TablesAdapter(val tables: List<Class<out RealmModel>>, val counts: List<Long>) : RecyclerView.Adapter<TablesAdapter.ViewHolder>() {
	override fun getItemCount() = this.tables.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val count = this.counts[position]
		val countString = count.format()

		holder.entriesCount.text = holder.entriesCount.resources.getQuantityString(R.plurals.kolumbus_entries_count, count.toInt(), countString)
		holder.tableName.text = this.tables[position].simpleName.prettify()
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.kolumbus_adapter_tables, parent, false)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val entriesCount = view.findViewById(android.R.id.text2) as TextView
		val tableName = view.findViewById(android.R.id.text1) as TextView

		init {
			view.setOnClickListener {
				TableActivity.start(it.context!!, tables[this.adapterPosition])
			}
		}
	}
}
