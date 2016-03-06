package io.kolumbus

import io.kolumbus.model.Book
import io.kolumbus.model.Genre
import io.kolumbus.model.Library
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Kolumbus_ForgetTest {
    @Before
    fun before() {
        Kolumbus.explore(Book::class.java)
                .explore(Genre::class.java)
                .explore(Library::class.java)

        assertEquals(3, Kolumbus.tables.size)
    }

    @Test
    fun forgetAll() {
        Kolumbus.forgetAll()

        assertEquals(0, Kolumbus.tables.size)
    }

    @Test
    fun forgetDuplicatedModels() {
        Kolumbus.forget(Genre::class.java)
                .forget(Genre::class.java)

        assertEquals(2, Kolumbus.tables.size)
    }

    @Test
    fun forgetInexistant() {
        Kolumbus.forget(Genre::class.java)

        assertEquals(2, Kolumbus.tables.size)

        Kolumbus.forget(Genre::class.java)

        assertEquals(2, Kolumbus.tables.size)
    }

    @Test
    fun forgetMultipleModels() {
        Kolumbus.forget(Genre::class.java)
                .forget(Book::class.java)

        assertEquals(1, Kolumbus.tables.size)
    }

    @Test
    fun forgetOneModel() {
        Kolumbus.forget(Genre::class.java)

        assertEquals(2, Kolumbus.tables.size)
    }

    @After
    fun after() {
        Kolumbus.forgetAll()

        assertEquals(0, Kolumbus.tables.size)
    }
}
