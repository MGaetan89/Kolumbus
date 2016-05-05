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

package io.kolumbus

import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import io.kolumbus.activity.TableActivity
import io.kolumbus.extension.prettify
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel

open class Architect {
    open fun displayAny(textView: TextView, value: Any) {
        textView.text = value.toString()
    }

    open fun displayBoolean(textView: TextView, value: Boolean) {
        textView.setText(if (value) R.string.kolumbus_yes else R.string.kolumbus_no)
    }

    open fun displayColor(textView: TextView, value: String, color: Int) {
        val drawable = ContextCompat.getDrawable(textView.context, R.drawable.kolumbus_color_preview) as LayerDrawable
        drawable.getDrawable(1).setColorFilter(color, PorterDuff.Mode.SRC_IN)

        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        textView.text = value
    }

    open fun displayEmpty(textView: TextView) {
        textView.setText(R.string.kolumbus_empty)
    }

    open fun displayFloat(textView: TextView, value: Float) {
        this.displayAny(textView, value)
    }

    open fun displayInt(textView: TextView, value: Int) {
        this.displayAny(textView, value)
    }

    open fun displayNull(textView: TextView) {
        textView.setText(R.string.kolumbus_null)
    }

    open fun <T : RealmModel> displayRealmList(textView: TextView, value: RealmList<T>, type: Class<out RealmModel>) {
        if (value.isNotEmpty()) {
            val realm = Realm.getDefaultInstance()
            val resultArray = realm.copyFromRealm(value).toTypedArray<RealmModel>()
            realm.close()

            textView.setOnClickListener {
                TableActivity.start(textView.context, type, resultArray)
            }
        }

        textView.text = Html.fromHtml(textView.resources.getString(R.string.kolumbus_linked_entries, value.size, type.simpleName.prettify()))
    }

    open fun <T : RealmModel> displayRealmModel(textView: TextView, value: T) {
        this.displayAny(textView, value)
    }

    open fun displayString(textView: TextView, value: String) {
        val htmlString = Html.fromHtml(value)
        val maxSize = textView.resources.getInteger(R.integer.kolumbus_string_max_size)

        if (htmlString.length > maxSize) {
            val overflow = textView.resources.getString(R.string.kolumbus_string_overflow)

            textView.setOnClickListener {
                AlertDialog.Builder(textView.context)
                        .setMessage(htmlString)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
            }

            textView.text = htmlString.subSequence(0, maxSize - overflow.length)
            textView.append(overflow)
        } else {
            textView.text = htmlString
        }
    }

    open fun displayUrl(textView: TextView, value: String) {
        textView.text = Html.fromHtml(textView.resources.getString(R.string.kolumbus_link, value, value.replaceFirst("([^:/])/.*/".toRegex(), "$1/.../")))
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}
