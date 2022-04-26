package com.dicoding.picodiploma.storyapp1.ui.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.databinding.ItemRowStoryBinding

class ListStoryAdapter(private val listStory: List<StoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]

        Glide.with(holder.binding.imgItemStory.context)
            .load(story.photoUrl)
            .into(holder.binding.imgItemStory)

        holder.binding.tvItemName.text = story.name

        holder.binding.imgItemStory.setOnClickListener { onItemClickCallback.onItemClicked(listStory[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listStory.size

    inner class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryItem)
    }
}