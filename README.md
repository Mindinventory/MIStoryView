MiStoryView
====

MiStoryView is a simple configurable library to integrate instagram like stories in your android application.

### Preview
![image](/art/artMIStoryView1.gif)

# Key features
* Set list of image url in MiStoryView.
* Set duration for particular story (in millisecond).
* Set any of the predefined animations, while swiping between multiple stories.
* Move to back and forth story by tapping on right and left part of an image.
* Pause progress while keeping touch on an image.
* Move to whole next story or exit full story view, if user is at the last item of story.
* Move to whole previou story or exit full story view, if user is at the first item of story.
* Story indicator color changes once it is seen.

# Usage
**Dependencies**
> Insert gradle dependency here.


**Implementation**
* Step 1 : Provide a list of stories. (Note : Use MiUserStoryModel class only to provide list of stories)

        class MainViewModel : ViewModel() {
                val mListOfUsers: ArrayList<MiUserStoryModel> = ArrayList()

                init {
                        mListOfUsers.add(MiUserStoryModel("1", "Johny Curtis", ArrayList()).also {
                                it.userStoryList.add(
                                        MiStoryModel(
                                        "https://i.picsum.photos/id/0/5616/3744.jpg?hmac=3GAAioiQziMGEtLbfrdbcoenXoWAW-zlyEAMkfEdBzQ",
                                        "Johny Depp",
                                        "10:08 PM"
                                        )
                                )
                                it.userStoryList.add(
                                        MiStoryModel(
                                        "https://i.picsum.photos/id/1/5616/3744.jpg?hmac=kKHwwU8s46oNettHKwJ24qOlIAsWN9d2TtsXDoCWWsQ",
                                        "Johny Depp",
                                        "07:50 AM"
                                        )
                                )
                        })
                }

                fun updateListOfUser(mListOfUsers: ArrayList<MiUserStoryModel>) {
                        this.mListOfUsers.clear()
                        this.mListOfUsers.addAll(mListOfUsers)
                }        
                
        }

* Step 2 : Inflate recyclerview in your layout file.

        <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/rvStory"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:clipToPadding="false"
                android:orientation="vertical"
                android:padding="@dimen/dp_8"
                app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                tools:listitem="@layout/item_user_story" />

* Step 3 : Create a recyclerview row item, which consists MiStoryView class.

        // See row item of sample app.

* Step 4 : Create an adapter object and a resultAPI launcher to launch story detail view from your activity.

        class MainActivity : AppCompatActivity() {
                // This is necessary snippet to listen the changes of seen stories data,
                // when user come back to the origin activity.

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

                // onCreateView here and invoke initView() method in it.

                private fun initView() {
                        // Initialize your adapter here.
                        // Provide launcher and list of stories from viewmodel for example
                        // in constructor of that adapter.

                        with(mBinding.rvStory) {
                            ...
                            storyAdapter = StoryAdapter(mViewModel.mListOfUsers, { launcher }, { this@MainActivity })
                            adapter = storyAdapter
                        }
                }
        }

* Step 5 : Create a recyclerview adapter. Must implement touch event of root view and dispatch that event to MiStoryView to launch Story detail view.

        class StoryAdapter(
                private val launcher: ActivityResultLauncher<Intent>,
                private val launcherCallBack: () -> ActivityResultLauncher<Intent>,
                private val activityCallBack: () -> AppCompatActivity
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                        // inflate your row item layout here
                }

                @SuppressLint("ClickableViewAccessibility")
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        if (holder is StoryViewHolder) {
                                holder.mBinding.apply {
                                        // Dispatch touch event of root view
                                        // to MiStoryView to open full screen
                                        // story preview view.
                                        root.setOnTouchListener { view, motionEvent ->
                                                msvStory.dispatchTouchEvent(motionEvent)
                                                true
                                        }

                                        msvStory.apply {
                                                setActivity(activityCallBack.invoke())
                                                setLauncher(launcherCallBack.invoke())
                                                if (listOfUseStory.isNotEmpty()) {
                                                        setImageUrls(listOfUseStory, holder.adapterPosition)
                                                }
                                        }
                                        tvUserName.text = mDataList[holder.adapterPosition].userName
                                }
                        }
                }

                // Other override methods here
                // Define viewholder class here
                
                fun setUserStoryData(mDataList: ArrayList<MiUserStoryModel>) {
                    this.listOfUseStory.clear()
                    this.listOfUseStory = mDataList
                    notifyDataSetChanged()
                }
        }

# XML Properties

|  Properties          |  Description               |
|----------------------|----------------------------|
|  miPageTransformer   |  Set different animation while switching between stories |
|  miPendingIndicatorColor  | Set color for unseen story  |
|  miStoryImageRadius  | Set size of round image  |
|  miStoryItemIndicatorWidth  |  Set width of progress indicator  |
|  miSpaceBetweenImageAndIndicator  |  Set margin between two progress bar indicator  |
|  miVisitedIndicatorColor  |  Set color for seen story  |
|  miFullScreenProgressBarHeight | Set height of progress in full story view |
|  miFullScreenGapBetweenProgressBar | Set margin between two progress bar indicator in full story view |
| miFullScreenProgressBarPrimaryColor | Set primary color of progress bar in full story view |
| miFullScreenProgressBarSecondaryColor | Set secondary color of progress bar in full story view |
| miFullScreenSingleStoryDisplayTime | Set time for particular story (i.e in milliseconds) |

That's it :thumbsup: and you're good to go 🚀

# Guideline to report an issue/feature request
It would be very helpful for us, if the reporter can share the below things to understand the root cause of the issue.

- Library version.
- Code snippet.
- Logs if applicable.
- Screenshot/video with steps to reproduce the issue.

## LICENSE!

MIStoryView is [MIT-licensed](https://github.com/Mindinventory/MIStoryView/blob/main/LICENSE).


# Let us know!

If you use our open-source libraries in your project, please make sure to credit us and Give a star to www.mindinventory.com

<p><h4>Please feel free to use this component and Let us know if you are interested to building Apps or Designing Products.</h4>
<a href="https://www.mindinventory.com/contact-us.php?utm_source=gthb&utm_medium=repo&utm_campaign=react-native-speed-view" target="__blank">
<img src="https://github.com/Sammindinventory/MindInventory/blob/main/hirebutton.png" width="203" height="43"  alt="app development">
</a>
