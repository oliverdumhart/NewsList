package com.oliverdumhart.moap.newslist.database

import androidx.room.TypeConverter
import java.util.*

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(timestamp: Long?): Date? = timestamp?.let { Date(timestamp) }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    @JvmStatic
    fun toCsv(keywords: Set<String>?): String? = keywords?.joinToString(";")

    @TypeConverter
    @JvmStatic
    fun toSet(string: String?): Set<String>? = string?.split(";")?.toSet()

}