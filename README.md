MiStoryView
====

MiStoryView is a simple configurable library to integrate instagram like stories in your android application.

![image](/art/MiStoryView Introduction.gif)

# Key features
* Set list of image urls in MiStoryView.
* Set duration for particular story (in millisecond).
* Set different animations while swiping between multiple stories.
* Tap on right side of image to invoke next story.
* Tap on left side of image to invoke previous story.
* Pause progress while keeping touch on an image.
* Move to whole next story if user is at the last item of story or exit the story detail view.
* Move to whole previou story if user is at the first item of story or exit the story detail view.

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

That's it :thumbsup: and you're good to go :rocket:

# Guideline to report an issue/feature request
It would be very helpful for us, if the reporter can share the below things to understand the root cause of the issue.

- Library version.
- Code snippet.
- Logs if applicable.
- Screenshot/video with steps to reproduce the issue.
