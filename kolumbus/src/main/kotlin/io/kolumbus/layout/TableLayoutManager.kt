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

package io.kolumbus.layout

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class TableLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        val firstPosition = 0
        val lastPosition = state?.itemCount ?: 0
        val sizes = mutableMapOf<Int, Point>()

        // Compute the biggest dimension from all the visible items
        this.processChild(firstPosition, lastPosition) { index, child ->
            child.measure(0, 0)

            val size = sizes.getOrPut(index, { Point(0, 0) })
            size.x = Math.max(size.x, child.measuredWidth)
            size.y = Math.max(size.y, child.measuredHeight)
        }

        // Set the proper size on all the visible items
        this.processChild(firstPosition, lastPosition) { index, child ->
            val size = sizes[index]

            child.layoutParams.height = size?.y ?: 0
            child.layoutParams.width = size?.x ?: 0
            child.requestLayout()
        }
    }

    private fun processChild(from: Int, to: Int, callback: (index: Int, child: View) -> Unit) {
        for (rowIndex in from..to) {
            val row = this.getChildAt(rowIndex)

            if (row !is ViewGroup) {
                continue
            }

            for (childIndex in 0..(row.childCount - 1)) {
                val child = row.getChildAt(childIndex)

                callback(childIndex, child)
            }
        }
    }
}
