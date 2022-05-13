package com.mistory.mistoryview.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mistoryview.databinding.ItemUserStoryBinding
import com.mistory.mistoryview.data.entity.MiUserStoryModel

class StoryAdapter(
    private var listOfUseStory: ArrayList<MiUserStoryModel>,
    private val launcherCallBack: () -> ActivityResultLauncher<Intent>,
    private val activityCallBack: () -> AppCompatActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StoryViewHolder(
            ItemUserStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
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
                tvUserName.text = listOfUseStory[holder.adapterPosition].userName
            }
        }
    }

    override fun getItemCount(): Int = listOfUseStory.size

    fun setUserStoryData(mDataList: ArrayList<MiUserStoryModel>) {
        this.listOfUseStory.clear()
        this.listOfUseStory = mDataList
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(itemView: ItemUserStoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val mBinding: ItemUserStoryBinding = itemView
    }
}