package io.kolumbus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.kolumbus.R
import io.kolumbus.extension.format
import io.kolumbus.extension.prettify

class TablesAdapter(context: Context, tablesAndCounts: Map<String, Long>) : ArrayAdapter<String>(context, 0, tablesAndCounts.keys.toList()) {
    private val counts: List<Long>

    init {
        this.counts = tablesAndCounts.values.toList()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(this.context).inflate(R.layout.kolumbus_adapter_tables, parent, false)

            if (view != null) {
                holder.entriesCount = view.findViewById(android.R.id.text2) as TextView?
                holder.tableName = view.findViewById(android.R.id.text1) as TextView?

                view.tag = holder
            }
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = this.getItem(position)
        val count = this.counts[position].format()

        holder.entriesCount?.text = this.context.getString(R.string.kolumbus_entries_count, count)
        holder.tableName?.text = item.prettify()

        return view
    }

    private class ViewHolder {
        var entriesCount: TextView? = null
        var tableName: TextView? = null
    }
}
