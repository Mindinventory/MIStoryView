package com.mistory.mistoryview.data.entity

import android.os.Parcelable
import com.mistory.mistoryview.util.MiMediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiStoryModel(
    var mediaUrl: String?,
    var name: String?,
    var time: String?,
    var isStorySeen: Boolean = false,
    var mediaType: MiMediaType = MiMediaType.IMAGE,
    var isMediaTypeVideo: Boolean = (mediaType == MiMediaType.VIDEO)
) : Parcelable