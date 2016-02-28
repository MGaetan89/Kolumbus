package io.kolumbus.extension

import java.util.regex.Pattern

private val ACCESSOR_PREFIXES = Pattern.compile("^(get|is)").toRegex()
private val CAMEL_CASE = Pattern.compile("([a-z])([A-Z])").toRegex()

fun String.prettify(): String {
    return this.removeAccessorPrefixes().toCamelCase()
}

internal fun String.removeAccessorPrefixes(): String {
    return ACCESSOR_PREFIXES.replaceFirst(this, "")
}

internal fun String.toCamelCase(): String {
    return CAMEL_CASE.replace(this, "$1 $2")
}
