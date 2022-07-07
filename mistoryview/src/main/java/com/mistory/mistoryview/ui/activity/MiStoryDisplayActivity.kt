package com.mistory.mistoryview.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mistoryview.databinding.ActivityMiStoryDisplayBinding
import com.mistory.mistoryview.MiStoryView
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.ui.adapter.MiStoryDisplayAdapter
import com.mistory.mistoryview.ui.fragment.MiStoryDisplayFragment
import com.mistory.mistoryview.util.MiPageChangeListener
import com.mistory.mistoryview.util.transformers.*

class MiStoryDisplayActivity : FragmentActivity() {
    companion object {
        const val MI_LIST_OF_STORY = "MiListOfStory"
        const val INDEX_OF_SELECTED_STORY = "IndexOfSelectedStory"
        const val PAGE_TRANSFORMER = "PageTransformer"
        const val FIRST_STORY_ITEM_INDEX = INITIAL_STORY_INDEX
        const val FIRST_STORY_POINT_INDEX = INITIAL_STORY_INDEX

        const val MI_FULLSCREEN_PROGRESSBAR_HEIGHT = "miFullScreenProgressBarHeight"
        const val MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR = "miFullScreenGapBetweenProgressBar"
        const val MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR = "miFullScreenProgressBarPrimaryColor"
        const val MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR =
            "miFullScreenProgressBarSecondaryColor"
        const val MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME = "miFullScreenSingleStoryDisplayTime"
        const val HORIZONTAL_PROGRESS_ATTRIBUTES = "horizontal_progress_attributes"
    }

    private val TAG = javaClass.simpleName
    private lateinit var pagerAdapterMi: MiStoryDisplayAdapter
    private lateinit var mBinding: ActivityMiStoryDisplayBinding
    private var indexOfSelectedStory = -1
    private var pageTransformer: MiStoryView.PageTransformer =
        MiStoryView.PageTransformer.BACKGROUND_TO_FOREGROUND_TRANSFORMER
    private val miStoryDisplayViewModel: MiStoryDisplayViewModel by lazy {
        ViewModelProvider(this)[MiStoryDisplayViewModel::class.java]
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMiStoryDisplayBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent != null) {
            if (intent.hasExtra(MI_LIST_OF_STORY)) {
                miStoryDisplayViewModel.addListOfUserStories(
                    intent.getParcelableArrayListExtra(MI_LIST_OF_STORY)
                )
            }

            if (intent.hasExtra(INDEX_OF_SELECTED_STORY))
                indexOfSelectedStory = intent.getIntExtra(INDEX_OF_SELECTED_STORY, -1)

            if (intent.hasExtra(PAGE_TRANSFORMER))
                pageTransformer =
                    intent.extras?.getParcelable<MiStoryView.PageTransformer>(PAGE_TRANSFORMER) as MiStoryView.PageTransformer

            if (intent.hasExtra(HORIZONTAL_PROGRESS_ATTRIBUTES)) {
                miStoryDisplayViewModel.setHorizontalProgressViewAttributes(
                    intent.getSerializableExtra(HORIZONTAL_PROGRESS_ATTRIBUTES) as HashMap<String, Any>
                )
            }
        }

        setupViewPager()
    }

    /**
     * Set up viewpager to generate
     * list of story views and move to
     * particular index to preview selected
     * story.
     **/
    private fun setupViewPager() {
        pagerAdapterMi = MiStoryDisplayAdapter(
            this,
            miStoryDisplayViewModel.listOfUserStory, ::invokeNextStory, ::invokePreviousStory
        )

        mBinding.vpMiStoryViewPager.apply {
            adapter = pagerAdapterMi
            offscreenPageLimit = 1
            setPageTransformer(getPageTransformer())
            registerOnPageChangeCallback(onPageChangeCallback)

            // Move to particular index of story.
            if (indexOfSelectedStory > 0) {
                setCurrentItem(indexOfSelectedStory, false)
            }
            miStoryDisplayViewModel.setMainStoryIndex(currentItem)
        }
    }

    private fun fetchCurrentVPItem(): Int {
        return mBinding.vpMiStoryViewPager.currentItem
    }

    /**
     * Move to next story
     * i.e set next story as current viewpager item
     * if exists else exit.
     */
    private fun invokeNextStory(currentStoryIndex: Int) {
        if (miStoryDisplayViewModel.listOfUserStory[fetchCurrentVPItem()].userStoryList.size == (currentStoryIndex + 1)) {
            if ((miStoryDisplayViewModel.listOfUserStory.count() - 1) > fetchCurrentVPItem()) {
                mBinding.vpMiStoryViewPager.currentItem += 1 // Move to next story once all story points are visited from particular story.
            } else {
                setResult() // Finish detail activity once all story points are visited
            }
        }
    }

    /**
     * Move to previous story
     * i.e set previous story as current viewpager item
     * if exists else exit.
     */
    private fun invokePreviousStory(currentStoryIndex: Int) {
        if (fetchCurrentVPItem() == FIRST_STORY_ITEM_INDEX
            && currentStoryIndex == FIRST_STORY_POINT_INDEX
        ) {
            setResult()
        } else {
            mBinding.vpMiStoryViewPager.currentItem -= 1
        }
    }

    /**
     * When viewpager page transition stopped/canceled,
     * start progress again else update data of
     * particular story according to index of viewpager.
     */
    private val onPageChangeCallback = object : MiPageChangeListener() {
        override fun onPageScrollCanceled() {
            val fragment = getFragmentByTag()
            if (fragment is MiStoryDisplayFragment) {
                fragment.resumeProgress()
            }
        }

        override fun onPageSelected(position: Int) {
            setCurrentPageIndex(position)
            miStoryDisplayViewModel.setMainStoryIndex(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            val fragment = getFragmentByTag()
            if (fragment is MiStoryDisplayFragment) {
                fragment.pauseExoPlayer(state)
            }
        }
    }

    private fun getFragmentByTag(): Fragment? {
        return supportFragmentManager.findFragmentByTag("f" + fetchCurrentVPItem())
    }

    private fun getPageTransformer(): ViewPager2.PageTransformer {
        return when (pageTransformer) {
            MiStoryView.PageTransformer.BACKGROUND_TO_FOREGROUND_TRANSFORMER -> BackgroundToForegroundPageTransformer()
            MiStoryView.PageTransformer.FOREGROUND_TO_BACKGROUND_TRANSFORMER -> ForegroundToBackgroundPageTransformer()
            MiStoryView.PageTransformer.CUBE_OUT_TRANSFORMER -> CubeOutTransformer()
            MiStoryView.PageTransformer.ZOOM_OUT_PAGE_TRANSFORMER -> ZoomOutPageTransformer()

            MiStoryView.PageTransformer.CUBE_IN_TRANSFORMER -> CubeInPageTransformer()
            MiStoryView.PageTransformer.ROTATE_DOWN_PAGE_TRANSFORMER -> RotateDownPageTransformer()
            MiStoryView.PageTransformer.ROTATE_UP_PAGE_TRANSFORMER -> RotateUpPageTransformer()
            MiStoryView.PageTransformer.ZOOM_IN_TRANSFORMER -> ZoomInTransformer()
        }
    }

    private fun setResult() {
        createIntent()
        finish()
    }

    override fun onBackPressed() {
        createIntent()
        super.onBackPressed()
    }

    private fun createIntent() {
        val intent = Intent()
        intent.putParcelableArrayListExtra(
            MI_LIST_OF_STORY, miStoryDisplayViewModel.listOfUserStory
        )
        intent.putExtra(INDEX_OF_SELECTED_STORY, fetchCurrentVPItem())
        setResult(RESULT_OK, intent)
    }
}