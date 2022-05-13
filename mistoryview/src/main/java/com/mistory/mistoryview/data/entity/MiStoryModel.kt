package com.mistory.mistoryview.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiStoryModel(
    var imageUrl: String?,
    var name: String?,
    var time: String?,
    var isStorySeen: Boolean = false
) : Parcelable