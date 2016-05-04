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

import android.content.Context
import io.kolumbus.activity.TablesActivity
import io.realm.RealmModel

object Kolumbus {
    internal var architect = Architect()
        private set
    internal val items = mutableListOf<RealmModel>()
    internal val tables = mutableMapOf<String, Class<out RealmModel>>().toSortedMap()

    fun explore(table: Class<out RealmModel>): Kolumbus {
        this.tables.put(table.simpleName, table)

        return this
    }

    fun forget(table: Class<out RealmModel>): Kolumbus {
        this.tables.remove(table.simpleName)

        return this
    }

    fun forgetAll() = this.tables.clear()

    fun navigate(context: Context) = TablesActivity.start(context)

    fun withArchitect(architect: Architect): Kolumbus {
        this.architect = architect

        return this
    }
}
