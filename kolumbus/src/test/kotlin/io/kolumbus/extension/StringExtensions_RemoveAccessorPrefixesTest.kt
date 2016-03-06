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

package io.kolumbus.extension

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class StringExtensions_RemoveAccessorPrefixesTest(val entry: String, val result: String) {
    @Test
    fun removeAccessorPrefixes() {
        assertEquals(this.result, this.entry.removeAccessorPrefixes())
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String>> {
            return listOf(
                    arrayOf("", ""),
                    arrayOf("some text", "some text"),
                    arrayOf("get text", " text"),
                    arrayOf("Get text", "Get text"),
                    arrayOf("getText", "Text"),
                    arrayOf("text getter", "text getter"),
                    arrayOf("is text", " text"),
                    arrayOf("Is text", "Is text"),
                    arrayOf("isText", "Text"),
                    arrayOf("text is", "text is")
            )
        }
    }
}
