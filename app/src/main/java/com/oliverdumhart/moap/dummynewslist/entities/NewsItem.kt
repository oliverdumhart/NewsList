package com.oliverdumhart.moap.dummynewslist.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class NewsItem(
        var id: String?,
        var title: String?,
        var description: String?,
        var link: String?,
        var image: String?,
        var publicationDate: Date?,
        var author: String?,
        var keywords: Set<String>?) : Parcelable