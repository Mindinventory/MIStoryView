package com.mistory.mistoryview.util.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.mistoryview.R
import java.util.*

class MiStoryProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val progressViewMis: MutableList<MiProgressView> = ArrayList()
    private var storiesListener: StoriesListener? = null
    private var storiesCount = -1
    private var current = -1
    private var isSkipStart = false
    private var isReverseStart = false
    private var position = -1
    private var isComplete = false

    init {
        orientation = HORIZONTAL
        val typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.StoriesProgressView
        )
        storiesCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0)
        typedArray.recycle()
        bindViews()
    }

    private fun bindViews() {
        progressViewMis.clear()
        removeAllViews()
        for (i in 0 until storiesCount) {
            val p = createProgressBar()
            p.tag = "p($position) c($i)" // debug
            progressViewMis.add(p)
            addView(p)
            if (i + 1 < storiesCount) {
                addView(createSpace())
            }
        }
    }

    private fun createProgressBar(): MiProgressView {
        return MiProgressView(context).apply {
            layoutParams =
                PROGRESS_BAR_LAYOUT_PARAM
        }
    }

    private fun createSpace(): View {
        return View(context).apply {
            layoutParams =
                SPACE_LAYOUT_PARAM
        }
    }

    private fun callback(index: Int): MiProgressView.Callback {
        return object : MiProgressView.Callback {
            override fun onStartProgress() {
                current = index
            }

            override fun onFinishProgress() {
                if (isReverseStart) {
                    if (storiesListener != null) storiesListener!!.onPrev()
                    if (0 <= current - 1) {
                        val p = progressViewMis[current - 1]
                        p.setMinWithoutCallback()
                        progressViewMis[--current].startProgress()
                    } else {
                        progressViewMis[current].startProgress()
                    }
                    isReverseStart = false
                    return
                }
                val next = current + 1
                if (next <= progressViewMis.size - 1) {
                    if (storiesListener != null) storiesListener!!.onNext()
                    progressViewMis[next].startProgress()
                    ++current
                } else {
                    isComplete = true
                    if (storiesListener != null) storiesListener!!.onComplete()
                }
                isSkipStart = false
            }
        }
    }

    fun setStoriesCountDebug(storiesCount: Int, position: Int) {
        this.storiesCount = storiesCount
        this.position = position
        bindViews()
    }

    fun setStoriesListener(storiesListener: StoriesListener?) {
        this.storiesListener = storiesListener
    }

    fun skip() {
        if (isSkipStart || isReverseStart) return
        if (isComplete) return
        if (current < 0) return
        val p = progressViewMis[current]
        isSkipStart = true
        p.setMax()
    }

    fun reverse() {
        if (isSkipStart || isReverseStart) return
        if (isComplete) return
        if (current < 0) return
        val p = progressViewMis[current]
        isReverseStart = true
        p.setMin()
    }

    fun setAllStoryDuration(duration: Long) {
        for (i in progressViewMis.indices) {
            progressViewMis[i].setDuration(duration)
            progressViewMis[i].setCallback(callback(i))
        }
    }

    fun startStories() {
        if (progressViewMis.size > 0) {
            progressViewMis[0].startProgress()
        }
    }

    fun startStories(from: Int) {
        for (i in progressViewMis.indices) {
            progressViewMis[i].clear()
        }
        for (i in 0 until from) {
            if (progressViewMis.size > i) {
                progressViewMis[i].setMaxWithoutCallback()
            }
        }
        if (progressViewMis.size > from) {
            progressViewMis[from].startProgress()
        }
    }

    fun destroy() {
        for (p in progressViewMis) {
            p.clear()
        }
    }

    fun abandon() {
        if (progressViewMis.size > current && current >= 0) {
            progressViewMis[current].setMinWithoutCallback()
        }
    }

    fun pause() {
        if (current < 0) return
        progressViewMis[current].pauseProgress()
    }

    fun resume() {
        if (current < 0 && progressViewMis.size > 0) {
            progressViewMis[0].startProgress()
            return
        }
        progressViewMis[current].resumeProgress()
    }

    fun getProgressWithIndex(index: Int): MiProgressView {
        return progressViewMis[index]
    }

    companion object {
        private val PROGRESS_BAR_LAYOUT_PARAM = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1F)
        private val SPACE_LAYOUT_PARAM = LayoutParams(5, LayoutParams.WRAP_CONTENT)
    }

    interface StoriesListener {
        fun onNext()
        fun onPrev()
        fun onComplete()
    }
}