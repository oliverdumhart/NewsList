package com.oliverdumhart.moap.newslist.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
class NewsItem(
        @PrimaryKey
        var id: String,
        var title: String?,
        var description: String?,
        var link: String?,
        var image: String?,
        var publicationDate: Date?,
        var author: String?,
        var keywords: Set<String>?) : Parcelable