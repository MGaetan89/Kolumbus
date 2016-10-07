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

package io.kolumbus.activity

import android.content.Context
import io.realm.RealmModel

class TableActivity {
    companion object {
        fun start(context: Context, table: Class<out RealmModel>?) = Unit

        fun <T : RealmModel> start(context: Context, table: Class<out T>?, items: Array<out T>?) = Unit
    }
}