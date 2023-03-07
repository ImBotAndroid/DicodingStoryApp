package com.example.submissiondicodingintermediate_1.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissiondicodingintermediate_1.data.local.DetailStory
import com.example.submissiondicodingintermediate_1.data.response.ListStoryItem
import com.example.submissiondicodingintermediate_1.databinding.StoryItemBinding
import com.example.submissiondicodingintermediate_1.ui.detail.DetailStoryActivity
import com.example.submissiondicodingintermediate_1.data.helper.DateFormatter
import java.util.*

class ItemStoryAdapter : PagingDataAdapter<ListStoryItem, ItemStoryAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    class ItemViewHolder (private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            val username = data.name
            val description = data.description
            val photo = data.photoUrl
            val date = DateFormatter.formatDate(data.createdAt, TimeZone.getDefault().id)

            binding.tvItemUsername.text = username
            binding.tvItemDescription.text = description
            binding.tvItemPublishedDate.text = date

            Glide.with(itemView.context)
                .load(photo)
                .into(binding.ivItemName)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                val detailData = DetailStory(username, description, photo)
                intent.putExtra(DetailStoryActivity.EXTRA_DATA, detailData)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}