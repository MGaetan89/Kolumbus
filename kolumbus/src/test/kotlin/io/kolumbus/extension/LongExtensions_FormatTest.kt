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

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Locale
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class LongExtensions_FormatTest(val long: Long, val formatted: String) {
	@Before
	fun before() {
		Locale.setDefault(Locale.US)
	}

	@Test
	fun format() {
		assertEquals(this.formatted, this.long.format())
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			return listOf(
					arrayOf(-1000000L, "-1,000,000"),
					arrayOf(-100000L, "-100,000"),
					arrayOf(-10000L, "-10,000"),
					arrayOf(-1000L, "-1,000"),
					arrayOf(-100L, "-100"),
					arrayOf(-10L, "-10"),
					arrayOf(-1L, "-1"),
					arrayOf(0L, "0"),
					arrayOf(1L, "1"),
					arrayOf(10L, "10"),
					arrayOf(100L, "100"),
					arrayOf(1000L, "1,000"),
					arrayOf(10000L, "10,000"),
					arrayOf(100000L, "100,000"),
					arrayOf(1000000L, "1,000,000")
			)
		}
	}
}
