package io.kolumbus.extension

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class StringExtensions_ToCamelCaseTest(val entry: String, val result: String) {
    @Test
    fun toCamelCase() {
        assertEquals(this.result, this.entry.toCamelCase())
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String>> {
            return listOf(
                    arrayOf("", ""),
                    arrayOf("helloworld", "helloworld"),
                    arrayOf("helloWorld", "hello World"),
                    arrayOf("Helloworld", "Helloworld"),
                    arrayOf("HelloWorld", "Hello World"),
                    arrayOf("helloWorldMyNameIsJack", "hello World My Name Is Jack"),
                    arrayOf("HelloWorldMyNameIsJack", "Hello World My Name Is Jack")
            )
        }
    }
}
