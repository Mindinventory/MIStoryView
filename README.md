# MiStoryView [![](https://jitpack.io/v/Mindinventory/MIStoryView.svg)](https://jitpack.io/#Mindinventory/MIStoryView) ![](https://img.shields.io/github/languages/top/Mindinventory/MIStoryView) ![](https://img.shields.io/github/license/mindinventory/MIStoryView)

MiStoryView is a simple configurable library to integrate stories features into your social media android application.

### Preview
![image](/art/MiStoryView.gif)

# Key features
* Set a list of image/video/GIF URLs in MiStoryView.
* Set fixed duration for particular story of media type Image/GIF only (in milliseconds).
* Duration for media type Video will be set automatically.
* Set any of the predefined animations, while swiping between multiple stories.
* Move to the back and forth story by tapping on the right and left parts of an image.
* Hold story by holding touch on it.
* Move to the whole next story or exit full story view, if a user is at the last item of the story.
* Move to the whole previous story or exit the full story view, if a user is at the first item of the story.
* Story indicator color changes once it is seen.


# Usage
**Dependencies**
- **Step 1: Add the JitPack repository in your project build.gradle file**
```bash
allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
	    }
    }
```

 **or**
    
If Android studio version is Arctic Fox or upper then add it in your settings.gradle:
```bash
 dependencyResolutionManagement {
    		repositories {
        		...
        		maven { url 'https://jitpack.io' }
    		}
	   }
```

- **Step 2: Add the dependency in your app module build.gradle file**
```bash
dependencies {
		    ...
	        implementation 'com.github.Mindinventory:MIStoryView:x.x.x'
	}
```

**Implementation**
* Step 1 : Provide a list of stories. (Note : Use MiUserStoryModel class only to provide list of stories and for json file see in assets folder of demo app.)

        class MainViewModel : ViewModel() {
		    val mListOfUsers: ArrayList<MiUserStoryModel> = ArrayList()

		    fun readAssetsData(context: Context): String {
			val json: String?
			try {
			    val inputStream = context.assets.open("storyData.json")
			    val size = inputStream.available()
			    val buffer = ByteArray(size)
			    inputStream.read(buffer)
			    inputStream.close()
			    json = java.lang.String(buffer, "UTF-8").toString()

			    mListOfUsers.addAll(
				Gson().fromJson(
				    json, object : TypeToken<ArrayList<MiUserStoryModel?>?>() {}.type
				)
			    )
			} catch (ex: IOException) {
			    ex.printStackTrace()
			    return ""
			}
			return json
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

That's it :thumbsup: and you're good to go ????

# Guideline to report an issue/feature request
It would be very helpful for us, if the reporter can share the below things to understand the root cause of the issue.

- Library version.
- Code snippet.
- Logs if applicable.
- Screenshot/video with steps to reproduce the issue.

## LICENSE!

MIStoryView is [MIT-licensed](https://github.com/Mindinventory/MIStoryView/blob/master/LICENSE).


# Let us know!

If you use our open-source libraries in your project, please make sure to credit us and Give a star to www.mindinventory.com

<p><h4>Please feel free to use this component and Let us know if you are interested to building Apps or Designing Products.</h4>
<a href="https://www.mindinventory.com/contact-us.php?utm_source=gthb&utm_medium=repo&utm_campaign=rMIStoryView" target="__blank">
<img src="https://github.com/Sammindinventory/MindInventory/blob/main/hirebutton.png" width="203" height="43"  alt="app development">
</a>
