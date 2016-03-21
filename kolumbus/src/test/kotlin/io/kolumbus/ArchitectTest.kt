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
import io.kolumbus.model.Book
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ArchitectTest {
    private lateinit var architect: Architect
    private lateinit var textView: TextView

    @Before
    fun before() {
        this.architect = Architect()
        this.textView = mock(TextView::class.java)
    }

    @Test
    fun displayAny_Float() {
        this.architect.displayAny(this.textView, 4.2f)

        verify(this.textView).text = "4.2"
    }

    @Test
    fun displayAny_Int() {
        this.architect.displayAny(this.textView, 42)

        verify(this.textView).text = "42"
    }

    @Test
    fun displayAny_Long() {
        this.architect.displayAny(this.textView, 42L)

        verify(this.textView).text = "42"
    }

    @Test
    fun displayAny_Object() {
        val book = Book()

        this.architect.displayAny(this.textView, book)

        verify(this.textView).text = book.toString()
    }

    @Test
    fun displayAny_StringEmpty() {
        this.architect.displayAny(this.textView, "")

        verify(this.textView).text = ""
    }

    @Test
    fun displayAny_StringNotEmpty() {
        this.architect.displayAny(this.textView, "Hello, World!")

        verify(this.textView).text = "Hello, World!"
    }

    @Test
    fun displayBoolean_False() {
        this.architect.displayBoolean(this.textView, false)

        verify(this.textView).setText(R.string.kolumbus_no)
    }

    @Test
    fun displayBoolean_True() {
        this.architect.displayBoolean(this.textView, true)

        verify(this.textView).setText(R.string.kolumbus_yes)
    }

    @Test
    fun displayEmpty() {
        this.architect.displayEmpty(this.textView)

        verify(this.textView).setText(R.string.kolumbus_empty)
    }

    @Test
    fun displayFloat_Negative() {
        this.architect.displayFloat(this.textView, -4.2f)

        verify(this.textView).text = "-4.2"
    }

    @Test
    fun displayFloat_Positive() {
        this.architect.displayFloat(this.textView, 4.2f)

        verify(this.textView).text = "4.2"
    }

    @Test
    fun displayFloat_Zero() {
        this.architect.displayFloat(this.textView, 0f)

        verify(this.textView).text = "0.0"
    }

    @Test
    fun displayInt_Negative() {
        this.architect.displayInt(this.textView, -42)

        verify(this.textView).text = "-42"
    }

    @Test
    fun displayInt_Positive() {
        this.architect.displayInt(this.textView, 42)

        verify(this.textView).text = "42"
    }

    @Test
    fun displayInt_Zero() {
        this.architect.displayInt(this.textView, 0)

        verify(this.textView).text = "0"
    }

    @Test
    fun displayNull() {
        this.architect.displayNull(this.textView)

        verify(this.textView).setText(R.string.kolumbus_null)
    }

    @Test
    fun displayRealmObject() {
        val book = Book()

        this.architect.displayRealmObject(this.textView, book)

        verify(this.textView).text = book.toString()
    }
}
