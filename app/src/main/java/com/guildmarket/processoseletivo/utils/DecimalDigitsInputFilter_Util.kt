package com.guildmarket.processoseletivo.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern

class DecimalDigitsInputFilterUtil(digitsAfterZero: Int) : InputFilter {
    private var pattern: Pattern = Pattern.compile("^[0-9]*+(\\.[0-9]{0,$digitsAfterZero})?$")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val newString = dest.toString().substring(0, dstart) + source.toString().substring(start, end) + dest.toString().substring(dend)
        val matcher: Matcher = pattern.matcher(newString)
        return if (!matcher.matches()) "" else null
    }
}