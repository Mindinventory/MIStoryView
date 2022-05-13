package com.mistory.mistoryview.ui.activity

import androidx.lifecycle.ViewModel
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.data.entity.MiUserStoryModel

class MiStoryDisplayViewModel : ViewModel() {

    var listOfUserStory = ArrayList<MiUserStoryModel>()
    private var mainStoryIndex: Int = INITIAL_STORY_INDEX
    private var horizontalProgressViewAttributes = hashMapOf<String, Any>()

    fun addListOfUserStories(listOfUserStories: ArrayList<MiUserStoryModel>?) {
        listOfUserStory.clear()
        listOfUserStories?.let { listOfUserStory.addAll(it) }
    }

    fun updateStoryPoint(internalStoryIndex: Int) {
        try {
            listOfUserStory[this.mainStoryIndex].lastStoryPointIndex = internalStoryIndex
            listOfUserStory[this.mainStoryIndex].userStoryList[internalStoryIndex].isStorySeen = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMainStoryIndex(mainStoryIndex: Int) {
        this.mainStoryIndex = mainStoryIndex
    }

    fun setHorizontalProgressViewAttributes(horizontalProgressViewAttributes: HashMap<String, Any>) {
        this.horizontalProgressViewAttributes = horizontalProgressViewAttributes
    }

    fun getHorizontalProgressViewAttributes() = horizontalProgressViewAttributes

    override fun onCleared() {
        super.onCleared()
        horizontalProgressViewAttributes = hashMapOf()
    }
}