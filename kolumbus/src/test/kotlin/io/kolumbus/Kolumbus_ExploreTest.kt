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
import io.kolumbus.model.Genre
import io.kolumbus.model.Library
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Kolumbus_ExploreTest {
    @Before
    fun before() {
        Kolumbus.forgetAll()

        assertEquals(0, Kolumbus.tables.size)
    }

    @Test
    fun exploreDuplicatedModels() {
        Kolumbus.explore(Library::class.java)
                .explore(Book::class.java)
                .explore(Library::class.java)

        assertEquals(2, Kolumbus.tables.size)
        assertEquals(Book::class.java, Kolumbus.tables["Book"])
        assertEquals(Library::class.java, Kolumbus.tables["Library"])
    }

    @Test
    fun exploreMultipleModels() {
        Kolumbus.explore(Genre::class.java)
                .explore(Book::class.java)
                .explore(Library::class.java)

        assertEquals(3, Kolumbus.tables.size)
        assertEquals(Book::class.java, Kolumbus.tables["Book"])
        assertEquals(Genre::class.java, Kolumbus.tables["Genre"])
        assertEquals(Library::class.java, Kolumbus.tables["Library"])
    }

    @Test
    fun exploreOneModel() {
        Kolumbus.explore(Book::class.java)

        assertEquals(1, Kolumbus.tables.size)
        assertEquals(Book::class.java, Kolumbus.tables["Book"])
    }
}
