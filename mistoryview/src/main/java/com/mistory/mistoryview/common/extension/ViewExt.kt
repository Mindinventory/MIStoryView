package com.mistory.mistoryview.common.extension

import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hideWithFade() {
    animate().alpha(0f).setDuration(200).start()
}

fun View.showWithFade() {
    animate().alpha(1f).setDuration(200).start()
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

fun View.showViews(vararg view: View) {
    if (view.iterator().hasNext()) {
        view.iterator().next().visibility = View.VISIBLE
    }
}

fun View.hideViews(vararg view: View) {
    if (view.iterator().hasNext()) {
        view.iterator().next().visibility = View.GONE
    }
}