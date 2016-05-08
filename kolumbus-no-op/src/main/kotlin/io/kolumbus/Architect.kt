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

import android.widget.TextView
import io.realm.RealmList
import io.realm.RealmModel

open class Architect {
    open fun displayAny(textView: TextView, value: Any) = Unit

    open fun displayBoolean(textView: TextView, value: Boolean) = Unit

    open fun displayColor(textView: TextView, value: String, color: Int) = Unit

    open fun displayEmpty(textView: TextView) = Unit

    open fun displayFloat(textView: TextView, value: Float) = Unit

    open fun displayInt(textView: TextView, value: Int) = Unit

    open fun displayNull(textView: TextView) = Unit

    open fun <T : RealmModel> displayRealmList(textView: TextView, value: RealmList<T>, type: Class<out RealmModel>) = Unit

    open fun <T : RealmModel> displayRealmModel(textView: TextView, value: T) = Unit

    open fun displayString(textView: TextView, value: String) = Unit

    open fun displayUrl(textView: TextView, value: String) = Unit
}
