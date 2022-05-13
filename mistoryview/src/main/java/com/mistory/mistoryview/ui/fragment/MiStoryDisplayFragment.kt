package com.mistory.mistoryview.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mistoryview.R
import com.example.mistoryview.databinding.FragmentMiStoryDisplayBinding
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.common.extension.ImageLoadingListener
import com.mistory.mistoryview.common.extension.loadImage
import com.mistory.mistoryview.common.gesturedetector.GestureListener
import com.mistory.mistoryview.common.gesturedetector.MiGestureDetector
import com.mistory.mistoryview.data.entity.MiStoryModel
import com.mistory.mistoryview.ui.MiStoryHorizontalProgressView
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.INDEX_OF_SELECTED_STORY
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_HEIGHT
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME
import com.mistory.mistoryview.ui.activity.MiStoryDisplayViewModel

class MiStoryDisplayFragment(
    private val primaryStoryIndex: Int,
    private val invokeNextStory: ((Int) -> Unit)? = null,
    private val invokePreviousStory: ((Int) -> Unit)? = null
) : Fragment(), MiStoryHorizontalProgressView.MiStoryPlayerListener, GestureListener {

    private lateinit var miGestureDetector: GestureDetector
    private val TAG = javaClass.simpleName
    private lateinit var mBinding: FragmentMiStoryDisplayBinding
    private var isLongPressEventOccurred = false

    /**
     *  Used as callback argument to invoke whole next story from MiStoryDetailActivity
     */
    private var lastStoryPointIndex = INITIAL_STORY_INDEX
    private val miStoryDisplayViewModel: MiStoryDisplayViewModel by lazy {
        ViewModelProvider(requireActivity())[MiStoryDisplayViewModel::class.java]
    }
    private var mStories = arrayListOf<MiStoryModel>()
    private var isResourceReady = false

    companion object {
        fun newInstance(
            primaryStoryIndex: Int,
            invokeNextStory: ((Int) -> Unit)? = null,
            invokePreviousStory: ((Int) -> Unit)? = null
        ): MiStoryDisplayFragment {
            val args = Bundle()
            args.putInt(INDEX_OF_SELECTED_STORY, primaryStoryIndex)

            return MiStoryDisplayFragment(
                primaryStoryIndex, invokeNextStory, invokePreviousStory
            ).apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStories = miStoryDisplayViewModel.listOfUserStory[primaryStoryIndex].userStoryList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMiStoryDisplayBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        miGestureDetector =
            GestureDetector(requireContext(), MiGestureDetector(this@MiStoryDisplayFragment))
        initStoryDisplayProgressView()
        setTouchListener()
    }

    /**
     * Method to check visibility of current
     * fragment and based on that value decide
     * to start progress or pause progress
     * while viewpager transition.
     */
    private fun didVisibilityChange() {
        if (isResumed && isVisible) {
            // Once resumed and at last
            // our Fragment is really visible to user.
            val miStoryModel = mStories.findLast {
                it.isStorySeen
            }
            val indexOfSeenStory = if (miStoryModel != null) {
                mStories.indexOf(miStoryModel)
            } else {
                INITIAL_STORY_INDEX
            }

            lastStoryPointIndex = indexOfSeenStory

            when {
                lastStoryPointIndex == INITIAL_STORY_INDEX && mStories[lastStoryPointIndex].isStorySeen.not() -> {
                    lastStoryPointIndex = INITIAL_STORY_INDEX
                }
                lastStoryPointIndex == INITIAL_STORY_INDEX && mStories[lastStoryPointIndex].isStorySeen && (lastStoryPointIndex + 1) == mStories.count() -> {
                    lastStoryPointIndex = INITIAL_STORY_INDEX
                }
                lastStoryPointIndex >= INITIAL_STORY_INDEX && mStories[lastStoryPointIndex].isStorySeen && (lastStoryPointIndex + 1) < mStories.count() -> {
                    lastStoryPointIndex += 1
                }
                lastStoryPointIndex > INITIAL_STORY_INDEX && mStories[lastStoryPointIndex].isStorySeen && (lastStoryPointIndex + 1) == mStories.count() -> {
                    mBinding.dpvProgress.startOverProgress()
                    lastStoryPointIndex = INITIAL_STORY_INDEX
                }
            }

            // Pre-fill the progress view for
            // story points those are already seen.
            with(mBinding.dpvProgress) {
                prefillProgressView(lastStoryPointIndex - 1)
                startAnimating(lastStoryPointIndex)
            }
        } else {
            mBinding.dpvProgress.pauseProgress()
        }
    }

    private fun initStoryDisplayProgressView() {
        if (!mStories.isNullOrEmpty()) {
            with(mBinding.dpvProgress) {
                setSingleStoryDisplayTime(miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME] as Long)
                setGapBetweenProgressBars(miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR] as Int)
                setProgressBarHeight(miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_PROGRESSBAR_HEIGHT] as Int)
                setProgressBarPrimaryColor(miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR] as Int)
                setProgressBarSecondaryColor(miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR] as Int)

                setMiStoryPlayerListener(this@MiStoryDisplayFragment)
                setMiStoryProgressBarCount(mStories.count())
            }
            loadInitialData()
        }
    }

    /**
     * Load initial data. Image, name of user
     * and timestamp of story.
     */
    private fun loadInitialData() {
        require(mStories.isNotEmpty()) { "Provide list of URLs." }
        loadImage(lastStoryPointIndex)
        mStories[lastStoryPointIndex].let {
            mBinding.tvName.text = it.name
            mBinding.tvTime.text = it.time
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        with(mBinding) {
            ivMiStoryImage.setOnTouchListener { _, event ->
                // Pass current MotionEvent to gesture listener
                miGestureDetector.onTouchEvent(event)

                // Pause progress once user Tap/Touch down,
                // also pause the progress when user swipe
                // forth and back between viewpager items.
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mBinding.dpvProgress.pauseProgress()
                }

                // Once user release the touch event
                // resume the progress again.
                if (event.action == MotionEvent.ACTION_UP) {
                    // Check if long press event is still on, only then resume progress
                    // and make #isLongPressEventOccurred variable false.
                    if (isLongPressEventOccurred) {
                        // Resume progress bar animation when long press event released.
                        isLongPressEventOccurred = false
                        dpvProgress.resumeProgress()
                    }
                }
                true
            }
        }
    }

    /**
     * Callback of progress
     *
     * index: indicates the current index
     *        of progress.
     */
    override fun onStartedPlaying(index: Int) {
        require(mStories.isNotEmpty()) { "Provide list of URLs." }
        lastStoryPointIndex = index
        loadImage(index)
        mStories[index].let {
            mBinding.tvName.text = it.name
            mBinding.tvTime.text = it.time
        }
    }

    /**
     * Move to next story if exists or
     * exit detail view once all story points
     * are visited from particular story.
     *
     * isAtLastIndex: Invoke previous or next story
     *                based on this value.
     *
     * Callback to MiStoryDisplayActivity
     */
    override fun onFinishedPlaying(isAtLastIndex: Boolean) {
        if (isAtLastIndex)
            invokeNextStory?.invoke(lastStoryPointIndex)
        else
            invokePreviousStory?.invoke(lastStoryPointIndex)
    }

    private fun loadImage(index: Int) {
        mBinding.dpvProgress.pauseProgress()

        blockInput()
        mStories.let { stories ->
            mBinding.ivMiStoryImage.loadImage(
                stories[index].imageUrl, object : ImageLoadingListener {
                    override fun onLoadFailed() {
                        startPostponedEnterTransition()
                        showErrorAlert()
                        mBinding.dpvProgress.pauseProgress()
                        unblockInput()
                    }

                    override fun onResourceReady(bitmap: Bitmap) {
                        isResourceReady = true
                        startPostponedEnterTransition()
                        mBinding.dpvProgress.resumeProgress()
                        with(miStoryDisplayViewModel) {
                            if (isVisible && isResumed)
                                updateStoryPoint(index)
                        }
                        unblockInput()
                    }
                })
        }
    }

    /**
     * Pause progress animation when user
     * perform long touch on view.
     */
    override fun onLongPressOccurred(e: MotionEvent?) {
        isLongPressEventOccurred = true
        mBinding.dpvProgress.pauseProgress()
    }

    /**
     * Perform single tap when user touch on story view.
     *
     * Invoke next story and previous story based on
     * the X value of touch event.
     */
    override fun onSingleTapOccurred(e: MotionEvent?) {
        e?.x?.let { x ->
            if (x < (mBinding.ivMiStoryImage.width.toFloat() / 3)) {
                // Invoke previous story point/whole story
                mBinding.dpvProgress.moveToPreviousStoryPoint(lastStoryPointIndex)
                mBinding.dpvProgress.startAnimating(
                    if (lastStoryPointIndex > INITIAL_STORY_INDEX)
                        lastStoryPointIndex - 1
                    else
                        INITIAL_STORY_INDEX
                )
            } else {
                // Invoke next story point/whole story
                mBinding.dpvProgress.moveToNextStoryPoint()
            }
        }
    }

    /**
     * Resume progress once user releases touch
     * of viewpager (in MiStoryDisplayActivity class).
     */
    fun resumeProgress() {
        if (isResourceReady)
            mBinding.dpvProgress.resumeProgress()
    }

    private fun blockInput() {
        mBinding.ivMiStoryImage.isEnabled = false
    }

    private fun unblockInput() {
        mBinding.ivMiStoryImage.isEnabled = true
    }

    private fun showErrorAlert() {
        Toast.makeText(
            requireContext(),
            getString(R.string.txt_download_error),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onResume() {
        super.onResume()
        didVisibilityChange()
    }

    override fun onPause() {
        mBinding.dpvProgress.pauseProgress()
        isResourceReady = false
        super.onPause()
        didVisibilityChange()
    }
}