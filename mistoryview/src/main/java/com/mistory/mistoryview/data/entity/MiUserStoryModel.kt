package com.mistory.mistoryview.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiUserStoryModel(
    val id: String?,
    val userName: String?,
    val userStoryList: ArrayList<MiStoryModel>,
    var lastStoryPointIndex: Int = 0
) : Parcelable