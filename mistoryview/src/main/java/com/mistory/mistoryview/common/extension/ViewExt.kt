package com.mistory.mistoryview.common.extension

import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun hideWithFade(vararg view: View) {
    view.iterator().forEach {
        it.animate().alpha(0f).setDuration(200).start()
    }
}

fun showWithFade(vararg view: View) {
    view.iterator().forEach {
        it.animate().alpha(1f).setDuration(200).start()
    }
}