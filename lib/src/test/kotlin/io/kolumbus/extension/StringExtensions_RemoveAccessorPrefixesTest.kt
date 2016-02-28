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
