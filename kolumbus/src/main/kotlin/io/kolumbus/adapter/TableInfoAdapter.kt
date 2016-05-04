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
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

class TableInfoAdapter(val fields: List<Field>, val instance: RealmModel) : RecyclerView.Adapter<TableInfoAdapter.ViewHolder>() {
    override fun getItemCount() = this.fields.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val field = this.fields[position].apply { this.isAccessible = true }
        val genericType = field.genericType
        val value = field.get(this.instance)

        holder?.name?.text = if (field.isAnnotationPresent(PrimaryKey::class.java)) {
            "#${field.name}"
        } else {
            field.name
        }

        if (genericType is ParameterizedType) {
            val subType = genericType.actualTypeArguments[0] as Class<Any>

            holder?.type?.text = "${field.type.simpleName}<${subType.simpleName}>"
        } else {
            holder?.type?.text = field.type.simpleName
        }

        holder?.value?.text = when (field.type) {
            String::class.java -> if (value != null) "\"$value\"" else "null"
            else -> value?.toString() ?: "null"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.kolumbus_adapter_table_info, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView?
        val type: TextView?
        val value: TextView?

        init {
            this.name = view.findViewById(R.id.field_name) as TextView?
            this.type = view.findViewById(R.id.field_type) as TextView?
            this.value = view.findViewById(R.id.field_default_value) as TextView?
        }
    }
}
