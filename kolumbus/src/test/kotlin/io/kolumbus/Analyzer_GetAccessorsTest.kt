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
import io.kolumbus.model.Library
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class Analyzer_GetAccessorsTest {
    @Test
    fun getAccessors_NotNullTableAllFields() {
        val table = Library::class.java
        val methods = Analyzer.getAccessors(table, Analyzer.getRealmFields(table))

        assertEquals(4, methods.size)
        assertEquals(table.getMethod("getBooks"), methods["books"])
        assertEquals(table.getMethod("getName"), methods["name"])
        assertEquals(table.getMethod("getOwnerName"), methods["ownerName"])
        assertEquals(table.getMethod("getPhoneNumber"), methods["phoneNumber"])
    }

    @Test
    fun getAccessors_NotNullTableAllFieldsWithBoolean() {
        val table = Book::class.java
        val methods = Analyzer.getAccessors(table, Analyzer.getRealmFields(table))

        assertEquals(5, methods.size) // TODO Should be 4
        assertEquals(table.getMethod("getDescription"), methods["description"])
        assertEquals(table.getMethod("getGenre"), methods["genre"])
        assertEquals(table.getMethod("getName"), methods["name"])
        assertEquals(table.getMethod("isAvailable"), methods["available"])
    }

    @Test
    fun getAccessors_NotNullTableEmptyFields() {
        val methods = Analyzer.getAccessors(Library::class.java, emptyList())

        assertEquals(0, methods.size)
    }

    @Ignore
    @Test
    fun getAccessors_NotNullTableInvalidFields() {
        val table = Library::class.java
        val methods = Analyzer.getAccessors(table, listOf(
                table.getDeclaredField("books"),
                Book::class.java.getDeclaredField("description"),
                table.getDeclaredField("ownerName")
        ))

        assertEquals(2, methods.size)
        assertEquals(table.getMethod("getBooks"), methods["books"])
        assertEquals(table.getMethod("getOwnerName"), methods["ownerName"])
    }

    @Test
    fun getAccessors_NotNullTableMissingFields() {
        val table = Library::class.java
        val methods = Analyzer.getAccessors(table, listOf(
                table.getDeclaredField("books"),
                table.getDeclaredField("ownerName")
        ))

        assertEquals(2, methods.size)
        assertEquals(table.getMethod("getBooks"), methods["books"])
        assertEquals(table.getMethod("getOwnerName"), methods["ownerName"])
    }

    @Test
    fun getAccessors_NullTableEmptyFields() {
        val methods = Analyzer.getAccessors(null, emptyList())

        assertEquals(0, methods.size)
    }
}
