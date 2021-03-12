package com.pigdogbay.lib.utils

import java.lang.StringBuilder

private const val REGEX_ANY_LETTER = "[a-z]"
private const val REGEX_WILD_CHAR = "[a-z]+"
private const val REGEX_SPACE = "[- ]"

fun String.toCrosswordRegex() : String {
    val buff = StringBuilder()
    buff.append("^")
    for (c in this.toCharArray()){
        when (c){
            '.','?' -> buff.append(REGEX_ANY_LETTER)
            ' ','-' -> buff.append(REGEX_SPACE)
            '#','@' -> buff.append(REGEX_WILD_CHAR)
            in '1'..'9' -> for (n in '1'..c) {buff.append(REGEX_ANY_LETTER) }
            in 'a'..'z' -> buff.append(c)
            in 'A'..'Z' -> buff.append(c.toLowerCase())
        }
    }
    buff.append("$")
    return buff.toString()
}