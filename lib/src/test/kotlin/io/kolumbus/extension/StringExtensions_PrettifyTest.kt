package io.kolumbus.extension

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class StringExtensions_PrettifyTest(val entry: String, val result: String) {
    @Test
    fun prettify() {
        assertEquals(this.result, this.entry.prettify())
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String>> {
            return listOf(
                    // StringExtensions_RemoveAccessorPrefixesTest
                    arrayOf("", ""),
                    arrayOf("some text", "some text"),
                    arrayOf("get text", " text"),
                    arrayOf("Get text", "Get text"),
                    arrayOf("getText", "Text"),
                    arrayOf("text getter", "text getter"),
                    arrayOf("is text", " text"),
                    arrayOf("Is text", "Is text"),
                    arrayOf("isText", "Text"),
                    arrayOf("text is", "text is"),

                    // StringExtensions_ToCamelCaseTest
                    arrayOf("helloworld", "helloworld"),
                    arrayOf("helloWorld", "hello World"),
                    arrayOf("Helloworld", "Helloworld"),
                    arrayOf("HelloWorld", "Hello World"),
                    arrayOf("helloWorldMyNameIsJack", "hello World My Name Is Jack"),
                    arrayOf("HelloWorldMyNameIsJack", "Hello World My Name Is Jack"),

                    // Extras
                    arrayOf("gethelloworld", "helloworld"),
                    arrayOf("getHelloworld", "Helloworld"),
                    arrayOf("getHelloWorld", "Hello World"),
                    arrayOf("ishelloworld", "helloworld"),
                    arrayOf("isHelloworld", "Helloworld"),
                    arrayOf("isHelloWorld", "Hello World")
            )
        }
    }
}
