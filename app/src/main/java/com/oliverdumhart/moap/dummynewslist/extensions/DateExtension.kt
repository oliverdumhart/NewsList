package com.oliverdumhart.moap.dummynewslist.extensions

import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

@Throws(NullPointerException::class, IllegalArgumentException::class)
public fun Date.toString(format: String) : String = SimpleDateFormat(format).format(this)