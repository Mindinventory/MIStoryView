package com.mistory.mistoryview.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.data.entity.MiUserStoryModel

class MiStoryDisplayViewModel : ViewModel() {

    var listOfUserStory = ArrayList<MiUserStoryModel>()
    private var mainStoryIndex: Int = INITIAL_STORY_INDEX
    private var horizontalProgressViewAttributes = hashMapOf<String, Any>()

    private var _startOverStoryLiveData = MutableLiveData<Boolean>()
    val startOverStoryLiveData: LiveData<Boolean> = _startOverStoryLiveData

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
        startOverStory()
    }

    fun setHorizontalProgressViewAttributes(horizontalProgressViewAttributes: HashMap<String, Any>) {
        this.horizontalProgressViewAttributes = horizontalProgressViewAttributes
    }

    fun getHorizontalProgressViewAttributes() = horizontalProgressViewAttributes

    private fun startOverStory() {
        _startOverStoryLiveData.postValue(true)
    }

    override fun onCleared() {
        super.onCleared()
        horizontalProgressViewAttributes = hashMapOf()
    }
}