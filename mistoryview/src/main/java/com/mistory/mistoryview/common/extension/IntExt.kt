package com.mistory.mistoryview.common.extension

import android.content.res.Resources
import android.util.TypedValue

fun Int.dpToPx(resource: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        resource.displayMetrics
    ).toInt()
}