package com.mistory.mistoryview.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import com.example.mistoryview.R
import com.example.mistoryview.databinding.FragmentMiStoryDisplayBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.common.extension.*
import com.mistory.mistoryview.common.gesturedetector.GestureListener
import com.mistory.mistoryview.common.gesturedetector.MiGestureDetector
import com.mistory.mistoryview.data.entity.MiStoryModel
import com.mistory.mistoryview.ui.MiStoryHorizontalProgressView
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.INDEX_OF_SELECTED_STORY
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME
import com.mistory.mistoryview.ui.activity.MiStoryDisplayViewModel

class MiStoryDisplayFragment(
    private val primaryStoryIndex: Int,
    private val invokeNextStory: ((Int) -> Unit)? = null,
    private val invokePreviousStory: ((Int) -> Unit)? = null
) : Fragment(), MiStoryHorizontalProgressView.MiStoryPlayerListener, GestureListener {

    private val TAG = javaClass.simpleName
    private lateinit var miGestureDetector: GestureDetector
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
    private var exoPlayer: ExoPlayer? = null
    private var storyDuration = 0L
    private var isCurrentStoryFinished = true

    /**
     * Exoplayer listener
     */
    private val playerListener = object : Player.Listener {
        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            Log.e("TAG", "**** isLoading :: $isLoading ****")
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    Log.e("TAG", "**** STATE_BUFFERING ****")
                    pausePlayer()
                }

                Player.STATE_READY -> {
                    Log.e("TAG", "**** Duration :: ${exoPlayer?.duration} ****")
                    resumePlayer()
                    if (!isCurrentStoryFinished) {
                        Log.e("TAG", "**** Story running ****")
                        mBinding.dpvProgress.resume()
                    } else {
                        Log.e("TAG", "**** Story start over ****")
                        isCurrentStoryFinished = false
                        if (isResumed && isVisible) {
                            mBinding.dpvProgress.apply {
                                setSingleStoryDisplayTime(exoPlayer?.duration)
                                startAnimating(lastStoryPointIndex)
                            }
                        }
                    }
                }

                Player.STATE_ENDED -> {
                    Log.e("TAG", "**** STATE_ENDED ****")
                }

                Player.STATE_IDLE -> {
                    Log.e("TAG", "**** STATE_IDLE ****")
                }
            }
        }
    }

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
        // Fetch story duration of image file type here initially.
        storyDuration =
            miStoryDisplayViewModel.getHorizontalProgressViewAttributes()[MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME] as Long
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

            manageInitialStoryIndex()

            // Pre-fill the progress view for
            // story points those are already seen.
            with(mBinding.dpvProgress) {
                prefillProgressView(lastStoryPointIndex - 1)
                if (mStories[lastStoryPointIndex].isMediaTypeVideo.not()) {
                    mBinding.dpvProgress.setSingleStoryDisplayTime(storyDuration)
                    startAnimating(lastStoryPointIndex) // Initial entry point for progress animation.
                }
            }
        } else {
            pausePlayer()
        }
    }

    private fun manageInitialStoryIndex() {
        initMediaPlayer()

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

        if (mStories[lastStoryPointIndex].isMediaTypeVideo)
            prepareMedia(mStories[lastStoryPointIndex])
    }

    private fun initStoryDisplayProgressView() {
        if (!mStories.isNullOrEmpty()) {
            with(mBinding.dpvProgress) {
                consumeAttrib(
                    miStoryDisplayViewModel.getHorizontalProgressViewAttributes(),
                    mStories.count()
                )
                setMiStoryPlayerListener(this@MiStoryDisplayFragment)
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
        managePlayerVisibility(mStories[INITIAL_STORY_INDEX])
        mStories[INITIAL_STORY_INDEX].let {
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
                    mBinding.dpvProgress.pause()
                }

                // Once user release the touch event
                // resume the progress again.
                if (event.action == MotionEvent.ACTION_UP) {
                    // Check if long press event is still on, only then resume progress
                    // and make #isLongPressEventOccurred variable false.
                    if (isLongPressEventOccurred) {
                        // Resume progress bar animation when long press event released.
                        isLongPressEventOccurred = false
                        showWithFade(mBinding.dpvProgress, mBinding.tvName, mBinding.tvTime)
                        mBinding.dpvProgress.resume()
                        if (mStories[lastStoryPointIndex].isMediaTypeVideo)
                            resumePlayer()
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
        managePlayerVisibility(mStories[index])
        mStories[index].let {
            mBinding.tvName.text = it.name
            mBinding.tvTime.text = it.time
        }
    }

    override fun onStoryFinished(index: Int) {
        Log.e("TAG", "**** onStoryFinished invoked index :: $index****")
        if (isResumed && isVisible) {
            isCurrentStoryFinished = true

            val miStoryModel = if (index == mStories.count() - 1) {
                mStories[index]
            } else {
                mStories[index + 1]
            }

            if (miStoryModel.isMediaTypeVideo) {
                Log.e("TAG", "**** URL :: ${miStoryModel.mediaUrl} ****")
                prepareMedia(miStoryModel)
            }
        }
    }

    override fun getCurrentTime(elapsedTime: Long, totalDuration: Long) {
        val remainingTime = totalDuration - elapsedTime
        val hours = (remainingTime / (1000 * 60 * 60)).toInt()
        val minutes = (remainingTime % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (remainingTime % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        mBinding.tvElapsedTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
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
        Log.e("TAG", "All stories seen...")
        if (isAtLastIndex)
            invokeNextStory?.invoke(lastStoryPointIndex)
        else
            invokePreviousStory?.invoke(lastStoryPointIndex)
    }

    private fun loadImage(index: Int) {
        mBinding.dpvProgress.pause()

        blockInput()
        mStories.let { stories ->
            mBinding.ivMiStoryImage.loadImage(
                stories[index].mediaUrl, object : ImageLoadingListener {
                    override fun onLoadFailed() {
                        startPostponedEnterTransition()
                        showErrorAlert()
                        mBinding.dpvProgress.pause()
                        unblockInput()
                    }

                    override fun onResourceReady(bitmap: Bitmap) {
                        isResourceReady = true
                        startPostponedEnterTransition()
                        showWithFade(mBinding.dpvProgress, mBinding.tvName, mBinding.tvTime)
                        mBinding.dpvProgress.resume()
                        if (isVisible && isResumed)
                            miStoryDisplayViewModel.updateStoryPoint(index)
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
        pausePlayer()
        hideWithFade(mBinding.dpvProgress, mBinding.tvTime, mBinding.tvName)
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
        if (isResourceReady) {
            if (isResumed && isVisible) {
                showWithFade(mBinding.dpvProgress, mBinding.tvName, mBinding.tvTime)
                mBinding.dpvProgress.resume()
                if (mStories[lastStoryPointIndex].isMediaTypeVideo) {
                    resumePlayer()
                }
            }
        }
    }

    /**
     * Pause video player when user starts dragging.
     * Resume video player when user stops dragging.
     *
     * draggingState: indicates the current state of
     *                viewpager state i.e dragging,
     *                settling or idle.
     */
    fun pauseExoPlayer(draggingState: Int) {
        if (draggingState == SCROLL_STATE_DRAGGING) {
            exoPlayer?.playWhenReady = false
        }
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

    private fun initMediaPlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(requireContext())
                .setPauseAtEndOfMediaItems(true)
                .build()
        } else {
            exoPlayer?.release()
            exoPlayer = null
            exoPlayer = ExoPlayer.Builder(requireContext())
                .setPauseAtEndOfMediaItems(true)
                .build()
        }

        mBinding.videoPlayerContainer.player = exoPlayer
        exoPlayer?.addListener(playerListener)
    }

    private fun managePlayerVisibility(miStoryModel: MiStoryModel) {
        if (miStoryModel.isMediaTypeVideo) {
            mBinding.videoPlayerContainer.show()
        } else {
            if (exoPlayer != null && exoPlayer?.isPlaying == true) {
                exoPlayer?.playWhenReady = false
            }
            mBinding.dpvProgress.setSingleStoryDisplayTime(storyDuration)
            loadImage(lastStoryPointIndex)
            mBinding.videoPlayerContainer.hide()
        }
    }

    private fun prepareMedia(miStoryModel: MiStoryModel) {
        val videoUri = Uri.parse(miStoryModel.mediaUrl)
        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer?.addMediaItem(mediaItem)
        exoPlayer?.prepare()
    }

    private fun releasePlayer() {
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun pausePlayer() {
        mBinding.dpvProgress.pause()
        exoPlayer?.playWhenReady = false
    }

    private fun resumePlayer() {
        if (isResumed)
            exoPlayer?.playWhenReady = true
    }

    override fun onResume() {
        super.onResume()
        didVisibilityChange()
    }

    override fun onPause() {
        isResourceReady = false
        super.onPause()
        didVisibilityChange()
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
    }
}