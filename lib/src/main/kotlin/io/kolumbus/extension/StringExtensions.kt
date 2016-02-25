package io.kolumbus.extension

fun String.prettify(): String {
    return this.replaceFirst("^(get|is)".toRegex(), "") // Remove accessor prefixes
            .replaceFirst("([a-z])([A-Z])".toRegex(), "$1 $2") // Convert camel case to space
}
