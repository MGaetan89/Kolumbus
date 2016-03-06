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
        Kolumbus.explore(Book::class.java)
                .explore(Book::class.java)
                .explore(Library::class.java)

        assertEquals(2, Kolumbus.tables.size)
    }

    @Test
    fun exploreMultipleModels() {
        Kolumbus.explore(Book::class.java)
                .explore(Genre::class.java)
                .explore(Library::class.java)

        assertEquals(3, Kolumbus.tables.size)
    }

    @Test
    fun exploreOneModel() {
        Kolumbus.explore(Book::class.java)

        assertEquals(1, Kolumbus.tables.size)
    }
}
