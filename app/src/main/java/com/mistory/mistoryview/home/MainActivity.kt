package com.mistory.mistoryview.home

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mistoryview.R
import com.example.mistoryview.databinding.ActivityMainBinding
import com.mistory.mistoryview.data.entity.MiUserStoryModel
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity
import com.mistory.mistoryview.utils.DividerItemDecorator

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()
    private lateinit var storyAdapter: StoryAdapter

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                if (it.resultCode == Activity.RESULT_OK) {
                    val list = arrayListOf<MiUserStoryModel>()

                    it.data?.hasExtra(MiStoryDisplayActivity.MI_LIST_OF_STORY)
                        ?.let { hasMiStoryList ->
                            if (hasMiStoryList) {
                                it.data?.getParcelableArrayListExtra<MiUserStoryModel>(
                                    MiStoryDisplayActivity.MI_LIST_OF_STORY
                                )?.let { listOfUserStories ->
                                    list.addAll(listOfUserStories)
                                }
                            }
                        }

                    if (!mViewModel.mListOfUsers.containsAll(list)) {
                        storyAdapter.setUserStoryData(list)
                        mViewModel.updateListOfUser(list)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel.readAssetsData(this)
        initView()
    }

    private fun initView() {
        with(mBinding.rvStory) {
            setHasFixedSize(true)
            ContextCompat.getDrawable(this@MainActivity, R.drawable.divider)
                ?.let { DividerItemDecorator(it) }?.let {
                    addItemDecoration(it)
                }
            storyAdapter =
                StoryAdapter(mViewModel.mListOfUsers, { launcher }, { this@MainActivity })
            adapter = storyAdapter
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                startPostponedEnterTransition()
            }
        }

        mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(1))
    }
}