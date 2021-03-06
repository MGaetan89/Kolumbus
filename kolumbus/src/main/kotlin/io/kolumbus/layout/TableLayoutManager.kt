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

class TableLayoutManager(context: Context) : LinearLayoutManager(context) {
	override fun canScrollHorizontally() = true

	override fun canScrollVertically() = true

	override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
		super.onLayoutChildren(recycler, state)

		val size = Point(0, 0)

		// Compute the biggest dimension from all the visible items
		this.processChild(0, this.itemCount) {
			it.measure(0, 0)

			size.x = Math.max(size.x, it.measuredWidth)
			size.y = Math.max(size.y, it.measuredHeight)
		}

		// Set the proper size on all the visible items
		this.processChild(0, state?.itemCount ?: 0) {
			it.layoutParams.height = size.y
			it.layoutParams.width = size.x
			it.post {
				it.requestLayout()
			}
		}
	}

	private fun processChild(from: Int, to: Int, callback: (child: View) -> Unit) {
		(from..to)
				.mapNotNull { this.getChildAt(it) as ViewGroup? }
				.forEach {
					(0 until it.childCount)
							.map(it::getChildAt)
							.forEach(callback)
				}
	}
}
