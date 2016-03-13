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

import io.kolumbus.model.Book
import io.kolumbus.model.Empty
import io.kolumbus.model.Ignored
import io.kolumbus.model.Library
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class Analyzer_GetRealmFieldsTest {
    @Test
    fun getRealmFields_Empty() {
        val fields = Analyzer.getRealmFields(Empty::class.java)

        assertEquals(0, fields.size)
    }

    @Ignore
    @Test
    fun getRealmFields_IgnoreOnly() {
        val fields = Analyzer.getRealmFields(Ignored::class.java)

        assertEquals(0, fields.size)
    }

    @Ignore
    @Test
    fun getRealmFields_Mixed() {
        val fields = Analyzer.getRealmFields(Book::class.java)

        assertEquals(4, fields.size)
        assertEquals("name", fields[0].name)
        assertEquals("available", fields[1].name)
        assertEquals("description", fields[2].name)
        assertEquals("genre", fields[3].name)
    }

    @Ignore
    @Test
    fun getRealmFields_MultiplePrimaryKeys() {
        val fields = Analyzer.getRealmFields(Library::class.java)

        assertEquals(4, fields.size)
        assertEquals("name", fields[0].name)
        assertEquals("phoneNumber", fields[1].name)
        assertEquals("books", fields[2].name)
        assertEquals("ownerName", fields[3].name)
    }

    @Test
    fun getRealmFields_Null() {
        val fields = Analyzer.getRealmFields(null)

        assertEquals(0, fields.size)
    }
}
