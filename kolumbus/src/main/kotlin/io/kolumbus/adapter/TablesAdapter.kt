package io.kolumbus.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.kolumbus.Kolumbus
import io.kolumbus.R
import io.kolumbus.activity.TableActivity
import io.kolumbus.extension.format
import io.kolumbus.extension.prettify

class TablesAdapter(val tables: List<String>, val counts: List<Long>) : RecyclerView.Adapter<TablesAdapter.ViewHolder>() {
    override fun getItemCount() = this.tables.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder?.entriesCount != null) {
            val count = this.counts[position]
            val countString = count.format()

            holder?.entriesCount.text = holder?.entriesCount.resources.getQuantityString(R.plurals.kolumbus_entries_count, count.toInt(), countString)
        }

        holder?.tableName?.text = this.tables[position].prettify()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.kolumbus_adapter_tables, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val entriesCount: TextView?
        val tableName: TextView?

        init {
            this.entriesCount = view.findViewById(android.R.id.text2) as TextView?
            this.tableName = view.findViewById(android.R.id.text1) as TextView?

            view.setOnClickListener {
                TableActivity.start(it.context!!, Kolumbus.tables[tables[this.adapterPosition]])
            }
        }
    }
}
