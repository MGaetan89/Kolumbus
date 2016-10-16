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
import io.realm.RealmConfiguration
import io.realm.RealmModel

object Kolumbus {
    internal var architect = Architect()
        private set
    private val configurations = mutableSetOf<RealmConfiguration>()
    internal val items = mutableListOf<RealmModel>()

    @JvmStatic
    fun build() = this

    fun explore(configuration: RealmConfiguration): Kolumbus {
        this.configurations.add(configuration)

        return this
    }

    fun forget(configuration: RealmConfiguration): Kolumbus {
        this.configurations.remove(configuration)

        return this
    }

    fun forgetAll() = this.configurations.clear()

    fun navigate(context: Context) = TablesActivity.start(context)

    fun withArchitect(architect: Architect): Kolumbus {
        this.architect = architect

        return this
    }

    internal fun getTables(): Set<Class<out RealmModel>> {
        val tables = mutableSetOf<Class<out RealmModel>>()

        this.configurations.forEach {
            tables.addAll(it.realmObjectClasses)
        }

        return tables
    }

    internal fun hasTables(): Boolean {
        this.configurations.forEach {
            if (it.realmObjectClasses.isNotEmpty()) {
                return true
            }
        }

        return false
    }
}
