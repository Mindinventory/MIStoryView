package com.mistory.mistoryview.home

import androidx.lifecycle.ViewModel
import com.mistory.mistoryview.data.entity.MiStoryModel
import com.mistory.mistoryview.data.entity.MiUserStoryModel
import com.mistory.mistoryview.util.MiMediaType

class MainViewModel : ViewModel() {
    val mListOfUsers: ArrayList<MiUserStoryModel> = ArrayList()

    init {
        mListOfUsers.add(MiUserStoryModel("1", "Johny Curtis", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                    "Johny Curtis",
                    "07:10 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                    "Johny Curtis",
                    "07:55 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
                    "Johny Curtis",
                    "08:15 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("2", "Adam White", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                    "Adam White",
                    "05:25 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    "Adam White",
                    "05:28 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("3", "Mia Harvey", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                    "Mia Harvey",
                    "12:38 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1008/5616/3744.jpg?hmac=906z84ml4jhqPMsm4ObF9aZhCRC-t2S_Sy0RLvYWZwY",
                    "Mia Harvey",
                    "12:38 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("4", "Kim Gorbachev", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761",
                    "Kim Gorbachev",
                    "07:30 PM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1010/5184/3456.jpg?hmac=7SE0MNAloXpJXDxio2nvoshUx9roGIJ_5pZej6qdxXs",
                    "Kim Gorbachev",
                    "07:30 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("5", "James Salis", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761",
                    "James Salis",
                    "09:55 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1011/5472/3648.jpg?hmac=Koo9845x2akkVzVFX3xxAc9BCkeGYA9VRVfLE4f0Zzk",
                    "James Salis",
                    "09:55 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("6", "Peter Evans", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761",
                    "Peter Evans",
                    "08:42 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1015/6000/4000.jpg?hmac=aHjb0fRa1t14DTIEBcoC12c5rAXOSwnVlaA5ujxPQ0I",
                    "Peter Evans",
                    "08:42 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("7", "Tim Johnson", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    "Tim Johnson",
                    "05:29 PM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1019/5472/3648.jpg?hmac=2mFzeV1mPbDvR0WmuOWSiW61mf9DDEVPDL0RVvg1HPs",
                    "Tim Johnson",
                    "05:29 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("8", "Oliver Backy", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761",
                    "Oliver Backy",
                    "09:44 AM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1020/4288/2848.jpg?hmac=Jo3ofatg0fee3HGOliAIIkcg4KGXC8UOTO1dm5qIIPc",
                    "Oliver Backy",
                    "09:44 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("9", "Charlie Leo", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761",
                    "Charlie Leo",
                    "08:19 PM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1021/2048/1206.jpg?hmac=fqT2NWHx783Pily1V_39ug_GFH1A4GlbmOMu8NWB3Ts",
                    "Charlie Leo",
                    "08:19 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("10", "Don Parker", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/363465031.sd.mp4?s=15b706ccd3c0e1d9dc9290487ccadc7b20fff7f1&profile_id=165&oauth2_token_id=57447761",
                    "Charlie Leo",
                    "08:19 PM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1025/4951/3301.jpg?hmac=_aGh5AtoOChip_iaMo8ZvvytfEojcgqbCH7dzaz-H8Y",
                    "Don Parker",
                    "11:27 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("11", "Brooke Hayes", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://player.vimeo.com/external/422787651.sd.mp4?s=ec96f3190373937071ba56955b2f8481eaa10cce&profile_id=165&oauth2_token_id=57447761",
                    "Brooke Hayes",
                    "03:44 PM",
                    mediaType = MiMediaType.VIDEO
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1028/5184/3456.jpg?hmac=WhttNfn25eTgLTNnhRujSq4IVjx2mMa6wvPG1c6qMVc",
                    "Brooke Hayes",
                    "03:44 PM"
                )
            )
        })
    }

    fun updateListOfUser(mListOfUsers: ArrayList<MiUserStoryModel>) {
        this.mListOfUsers.clear()
        this.mListOfUsers.addAll(mListOfUsers)
    }
}