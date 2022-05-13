package com.mistory.mistoryview.home

import androidx.lifecycle.ViewModel
import com.mistory.mistoryview.data.entity.MiStoryModel
import com.mistory.mistoryview.data.entity.MiUserStoryModel

class MainViewModel : ViewModel() {
    val mListOfUsers: ArrayList<MiUserStoryModel> = ArrayList()

    init {
        mListOfUsers.add(MiUserStoryModel("1", "Johny Curtis", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/0/5616/3744.jpg?hmac=3GAAioiQziMGEtLbfrdbcoenXoWAW-zlyEAMkfEdBzQ",
                    "Johny Curtis",
                    "10:08 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1/5616/3744.jpg?hmac=kKHwwU8s46oNettHKwJ24qOlIAsWN9d2TtsXDoCWWsQ",
                    "Johny Curtis",
                    "07:50 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/10/2500/1667.jpg?hmac=J04WWC_ebchx3WwzbM-Z4_KC_LeLBWr5LZMaAkWkF68",
                    "Johny Curtis",
                    "02:36 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/100/2500/1656.jpg?hmac=gWyN-7ZB32rkAjMhKXQgdHOIBRHyTSgzuOK6U0vXb1w",
                    "Johny Curtis",
                    "11:01 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1000/5626/3635.jpg?hmac=qWh065Fr_M8Oa3sNsdDL8ngWXv2Jb-EE49ZIn6c0P-g",
                    "Johny Curtis",
                    "08:54 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1001/5616/3744.jpg?hmac=38lkvX7tHXmlNbI0HzZbtkJ6_wpWyqvkX4Ty6vYElZE",
                    "Johny Curtis",
                    "03:38 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1002/4312/2868.jpg?hmac=5LlLE-NY9oMnmIQp7ms6IfdvSUQOzP_O3DPMWmyNxwo",
                    "Johny Curtis",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1003/1181/1772.jpg?hmac=oN9fHMXiqe9Zq2RM6XT-RVZkojgPnECWwyEF1RvvTZk",
                    "Johny Curtis",
                    "04:00 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("2", "Adam White", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1004/5616/3744.jpg?hmac=Or7EJnz-ky5bsKa9_frdDcDCR9VhCP8kMnbZV6-WOrY",
                    "Adam White",
                    "05:25 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1005/5760/3840.jpg?hmac=2acSJCOwz9q_dKtDZdSB-OIK1HUcwBeXco_RMMTUgfY",
                    "Adam White",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1006/3000/2000.jpg?hmac=x83pQQ7LW1UTo8HxBcIWuRIVeN_uCg0cG6keXvNvM8g",
                    "Adam White",
                    "10:10 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("3", "Mia Harvey", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1008/5616/3744.jpg?hmac=906z84ml4jhqPMsm4ObF9aZhCRC-t2S_Sy0RLvYWZwY",
                    "Mia Harvey",
                    "12:38 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/101/2621/1747.jpg?hmac=cu15YGotS0gIYdBbR1he5NtBLZAAY6aIY5AbORRAngs",
                    "Mia Harvey",
                    "11:17 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("4", "Kim Gorbachev", ArrayList()).also {
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
                    "https://i.picsum.photos/id/1011/5472/3648.jpg?hmac=Koo9845x2akkVzVFX3xxAc9BCkeGYA9VRVfLE4f0Zzk",
                    "James Salis",
                    "09:55 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1012/3973/2639.jpg?hmac=s2eybz51lnKy2ZHkE2wsgc6S81fVD1W2NKYOSh8bzDc",
                    "James Salis",
                    "11:12 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1013/4256/2832.jpg?hmac=UmYgZfqY_sNtHdug0Gd73bHFyf1VvzFWzPXSr5VTnDA",
                    "James Salis",
                    "00:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1014/6016/4000.jpg?hmac=yMXsznFliL_Y2E2M-qZEsOZE1micNu8TwgNlHj7kzs8",
                    "James Salis",
                    "10:56 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("6", "Peter Evans", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1015/6000/4000.jpg?hmac=aHjb0fRa1t14DTIEBcoC12c5rAXOSwnVlaA5ujxPQ0I",
                    "Peter Evans",
                    "08:42 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1016/3844/2563.jpg?hmac=WEryKFRvTdeae2aUrY-DHscSmZuyYI9jd_-p94stBvc",
                    "Peter Evans",
                    "03:00 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1018/3914/2935.jpg?hmac=3N43cQcvTE8NItexePvXvYBrAoGbRssNMpuvuWlwMKg",
                    "Peter Evans",
                    "04:40 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("7", "Tim Johnson", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1019/5472/3648.jpg?hmac=2mFzeV1mPbDvR0WmuOWSiW61mf9DDEVPDL0RVvg1HPs",
                    "Tim Johnson",
                    "05:29 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/102/4320/3240.jpg?hmac=ico2KysoswVG8E8r550V_afIWN963F6ygTVrqHeHeRc",
                    "Tim Johnson",
                    "08:20 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("8", "Oliver Backy", ArrayList()).also {
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
                    "https://i.picsum.photos/id/1021/2048/1206.jpg?hmac=fqT2NWHx783Pily1V_39ug_GFH1A4GlbmOMu8NWB3Ts",
                    "Charlie Leo",
                    "08:19 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1022/6000/3376.jpg?hmac=FBA9Qbec8NfDlxj8xLhV9k3DQEKEc-3zxkQM-hmfcy0",
                    "Charlie Leo",
                    "01:02 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1023/3955/2094.jpg?hmac=AW_7mARdoPWuI7sr6SG8t-2fScyyewuNscwMWtQRawU",
                    "Charlie Leo",
                    "05:25 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1024/1920/1280.jpg?hmac=-PIpG7j_fRwN8Qtfnsc3M8-kC3yb0XYOBfVzlPSuVII",
                    "Charlie Leo",
                    "06:30 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("10", "Don Parker", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1025/4951/3301.jpg?hmac=_aGh5AtoOChip_iaMo8ZvvytfEojcgqbCH7dzaz-H8Y",
                    "Don Parker",
                    "11:27 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1026/4621/3070.jpg?hmac=OJ880cIneqAKIwHbYgkRZxQcuMgFZ4IZKJasZ5c5Wcw",
                    "Don Parker",
                    "01:32 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1027/2848/4272.jpg?hmac=EAR-f6uEqI1iZJjB6-NzoZTnmaX0oI0th3z8Y78UpKM",
                    "Don Parker",
                    "04:19 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("11", "Brooke Hayes", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1028/5184/3456.jpg?hmac=WhttNfn25eTgLTNnhRujSq4IVjx2mMa6wvPG1c6qMVc",
                    "Brooke Hayes",
                    "03:44 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1029/4887/2759.jpg?hmac=uMSExsgG8_PWwP9he9Y0LQ4bFDLlij7voa9lU9KMXDE",
                    "Brooke Hayes",
                    "08:55 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/103/2592/1936.jpg?hmac=aC1FT3vX9bCVMIT-KXjHLhP6vImAcsyGCH49vVkAjPQ",
                    "Brooke Hayes",
                    "11:27 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1031/5446/3063.jpg?hmac=Zg0Vd3Bb7byzpvy-vv-fCffBW9EDp1coIbBFdEjeQWE",
                    "Brooke Hayes",
                    "01:32 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("12", "Mark Blair", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1032/2880/1800.jpg?hmac=5SLBdyPZBMyr5IBkIRfffZV10bP87Y7RrxVZX1vCefA",
                    "Mark Blair",
                    "10:08 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1033/2048/1365.jpg?hmac=zEuPfX7t6U866nzXjWF41bf-uxkKOnf1dDrHXmhcK-Q",
                    "Mark Blair",
                    "07:50 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1041/5184/2916.jpg?hmac=TW_9o6HeD7H7I7NVo-S1Fa1iAvzQ10uvmJqsXvNoi0M",
                    "Mark Blair",
                    "02:36 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1035/5854/3903.jpg?hmac=DV0AS2MyjW6ddofvSIU9TVjj1kewfh7J3WEOvflY8TM",
                    "Mark Blair",
                    "11:01 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1036/4608/3072.jpg?hmac=Tn9CS_V7lFSMMgAI5k1M38Mdj-YEJR9dPJCyeXNpnZc",
                    "Mark Blair",
                    "08:54 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("13", "Krish Mathews", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1037/5760/3840.jpg?hmac=fZe213BcO2KPQEJKChsdHnVYg-6kAtQMTZV24f1fS94",
                    "Krish Mathews",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1038/3914/5863.jpg?hmac=SGtXryWDkn_eVQdA1NjYrikcsrIfcfS4SFYHo4lCpkQ",
                    "Krish Mathews",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1039/6945/4635.jpg?hmac=tdgHDygif2JPCTFMM7KcuOAbwEU11aucKJ8eWcGD9_Q",
                    "Krish Mathews",
                    "03:38 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/104/3840/2160.jpg?hmac=Rv0qxBiYb65Htow4mdeDlyT5kLM23Z2cDlN53YYldZU",
                    "Krish Mathews",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1040/4496/3000.jpg?hmac=kvZONlBpTcZ16PuE_g2RWxlicQ5JKVq2lqqZndfafBY",
                    "Krish Mathews",
                    "04:00 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("14", "Rowan Baxter", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1042/3456/5184.jpg?hmac=5xr8Veg2D_kEQQO6rvGj_Yk8s_N4iq3-eZ9_KclSXNQ",
                    "Rowan Baxter",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1043/5184/3456.jpg?hmac=wsz2e0aFKEI0ij7mauIr2nFz2pzC8xNlgDHWHYi9qbc",
                    "Rowan Baxter",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1044/4032/2268.jpg?hmac=BXmoMkaurlzpTLYQupXLipcmI1sFbgT5sIz98Ob5VZE",
                    "Rowan Baxter",
                    "03:38 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1045/3936/2624.jpg?hmac=PMfAbC94Asle_jgeRYsj7zQHFabfTfsIwL247Ewetwc",
                    "Rowan Baxter",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1047/3264/2448.jpg?hmac=ksy0K4uGgm79hAV7-KvsfHY2ZuPA0Oq1Kii9hqkOCfU",
                    "Rowan Baxter",
                    "04:00 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("15", "Crispin Walker", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1048/5616/3744.jpg?hmac=N5TZKe4gtmf4hU8xRs-zbS4diYiO009Jni7n50609zk",
                    "Crispin Walker",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1049/3900/3120.jpg?hmac=Ox2snaSyRuEofh721sagxPbwVzLtung57FNHefB8Kdw",
                    "Crispin Walker",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1050/6000/4000.jpg?hmac=HhCXFcOrIrNguK7GqP6VhICXPa5FmcLZdug505qiEZM",
                    "Crispin Walker",
                    "03:38 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1051/4928/3264.jpg?hmac=-O25O5Q9z7LI8gDrUkTUmDJir4F9cp1RadCwShIDGms",
                    "Crispin Walker",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1052/4000/2667.jpg?hmac=VG0SPn0166Vw0hWeiJL4uVFbjGRcHnddwL4u0wpqL8Y",
                    "Crispin Walker",
                    "04:00 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1053/3596/2393.jpg?hmac=shru06Q5TiVeHArvSrQdBwclhorTJwYGnprqLqaH0hk",
                    "Crispin Walker",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1054/3079/1733.jpg?hmac=Rk5_Xt3oLlDLJHH3ZDyHCqua0s45mhNjXmID277ZOMI",
                    "Crispin Walker",
                    "04:00 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1055/5472/3648.jpg?hmac=1c293cGVlNouNQsjxT8y3nsYO-7qLCaOBEGvih0ibEU",
                    "Crispin Walker",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1056/3988/2720.jpg?hmac=qX6hO_75zxeYI7C-1TOspJ0_bRDbYInBwYeoy_z_h08",
                    "Crispin Walker",
                    "04:00 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("16", "Nicholas Graves", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1057/6016/4016.jpg?hmac=RjPyzbGq_MxSbghhfa1iVykXTskk9IISuzavny11_lI",
                    "Nicholas Graves",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1058/4608/3072.jpg?hmac=kfHIsJ4T3b-ily0CcdGESnuC4wwOPtnOQpcICheyvFQ",
                    "Nicholas Graves",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1059/7360/4912.jpg?hmac=vVWk1qyiXN_VgPhpNqFm3yl2HUPW6fHqYOjTHYO2bHQ",
                    "Nicholas Graves",
                    "03:38 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/106/2592/1728.jpg?hmac=E1-3Hac5ffuCVwYwexdHImxbMFRsv83exZ2EhlYxkgY",
                    "Nicholas Graves",
                    "05:10 AM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1060/5598/3732.jpg?hmac=31kU0jp5ejnPTdEt-8tAXU5sE-buU-y1W1qk_BsiUC8",
                    "Nicholas Graves",
                    "04:00 AM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("17", "Isaiah Curtis", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1061/3264/2448.jpg?hmac=HoVLqUN6k0SMxCPSdgoc9_6Mhu3nGYm_lL3NgrDbwFU",
                    "Isaiah Curtis",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1062/5092/3395.jpg?hmac=o9m7qeU51uOLfXvepXcTrk2ZPiSBJEkiiOp-Qvxja-k",
                    "Isaiah Curtis",
                    "10:10 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("18", "Rowan Felps", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1063/4867/3215.jpg?hmac=-ksdmOruOUma2z6ENQo9Yqp9T7lsnokLo8SFfAt-UNU",
                    "Rowan Felps",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1064/4236/2819.jpg?hmac=YygzDG22SIIGfbbuoV45bKoBIUguEtto0Jw_YdPDGyY",
                    "Rowan Felps",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1065/3744/5616.jpg?hmac=V64psST3xnjnVwmIogHI8krnL3edsh_sy4HNc3dJ_xY",
                    "Rowan Felps",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1066/2144/1424.jpg?hmac=J3AeMzlHPnXjRZdkB3pj5QrRPY8kPPqGjfRjgGyP4NI",
                    "Rowan Felps",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1067/5760/3840.jpg?hmac=gO_V7rUFdM8YddyLysCQet4CS0CzSvUcfAtHI1ismLM",
                    "Rowan Felps",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1068/7117/4090.jpg?hmac=Y6xHXrzHsNlbRTbhzZ53Yk-Ux9lUECBLbbP4wb5a1qY",
                    "Rowan Felps",
                    "10:10 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("19", "Wendy Goodwin", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1069/3500/2333.jpg?hmac=VBJ1vR2opkcKLS9NKGDl5uPxF02u6dSqbwc1x1b4oJc",
                    "Wendy Goodwin",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/107/5760/3840.jpg?hmac=pOr5LeKE8rivWSYuheeVgYtDjCNGouPkeFjx2TsbAoY",
                    "Wendy Goodwin",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1070/5472/3648.jpg?hmac=oFxAwLeGJmas45_yf5NdpeQzexAF-tMVL6q9JwvSuo0",
                    "Wendy Goodwin",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1071/3000/1996.jpg?hmac=rPo94Qr1Ffb657k6R7c9Zmfgs4wc4c1mNFz7ND23KnQ",
                    "Wendy Goodwin",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1072/3872/2592.jpg?hmac=I5d8vixhn6Ne9Ao1YQdtHfxS2YKNyx6_Bu8N_V1-ovk",
                    "Wendy Goodwin",
                    "09:25 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("20", "Bartholomew Blair", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1073/5472/3648.jpg?hmac=xCDetU9pLnLGZopbvHOQOkQRhTiYwyrzWc0YyHPzp5Y",
                    "Bartholomew Blair",
                    "09:25 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("21", "Sebastian Tate", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1075/4858/3239.jpg?hmac=YSCuWp376_Eij3ipRSoYhDKZ5nzGLKeQneQhDgY1qK4",
                    "Sebastian Tate",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1074/5472/3648.jpg?hmac=w-Fbv9bl0KpEUgZugbsiGk3Y2-LGAuiLZOYsRk0zo4A",
                    "Sebastian Tate",
                    "10:10 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1076/4835/3223.jpg?hmac=sw_W2oBUV1Pp_QwMbkODYHgWlX-SOkVRatS_UErh64Q",
                    "Sebastian Tate",
                    "09:25 PM"
                )
            )
        })
        mListOfUsers.add(MiUserStoryModel("22", "Rhea Harmon", ArrayList()).also {
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1079/4496/3000.jpg?hmac=G-dJcpU08vEMqjUz2rb3IxjOG99rcePqW9BF1IsPLf0",
                    "Rhea Harmon",
                    "09:25 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1077/3000/1995.jpg?hmac=gx_KF_cdQtv7ilOSaN8YEsNKkhcwRQ507UB7qZ4RutI",
                    "Rhea Harmon",
                    "08:47 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/12/2500/1667.jpg?hmac=Pe3284luVre9ZqNzv1jMFpLihFI6lwq7TPgMSsNXw2w",
                    "Rhea Harmon",
                    "09:48 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/119/3264/2176.jpg?hmac=PYRYBOGQhlUm6wS94EkpN8dTIC7-2GniC3pqOt6CpNU",
                    "Rhea Harmon",
                    "09:50 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/118/1500/1000.jpg?hmac=jumRsHTbfg0frrVd5Xw1187nwNcZCqJbqrdlXpPAIjw",
                    "Rhea Harmon",
                    "09:55 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/117/1544/1024.jpg?hmac=xFWtVUv1xkFVVidifC3drKerU_k_za4w28fv5etvxRc",
                    "Rhea Harmon",
                    "09:58 PM"
                )
            )
            it.userStoryList.add(
                MiStoryModel(
                    "https://i.picsum.photos/id/1078/3000/2000.jpg?hmac=kI-4ittyvRAG5-z9urHPPBQ4kDNJ4ItiEw6-NagOy10",
                    "Rhea Harmon",
                    "10:10 PM"
                )
            )
        })
    }

    fun updateListOfUser(mListOfUsers: ArrayList<MiUserStoryModel>) {
        this.mListOfUsers.clear()
        this.mListOfUsers.addAll(mListOfUsers)
    }
}