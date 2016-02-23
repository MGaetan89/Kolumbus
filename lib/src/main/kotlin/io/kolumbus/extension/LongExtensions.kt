package io.kolumbus.extension

import java.text.NumberFormat

fun Long.format(): String {
    return NumberFormat.getIntegerInstance().format(this)
}
