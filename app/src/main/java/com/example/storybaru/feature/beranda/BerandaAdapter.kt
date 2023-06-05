package com.example.storybaru.feature.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storybaru.databinding.ItemRowBinding
import com.example.storybaru.responses.AllStoriesResponses
import com.example.storybaru.responses.ListStoryItem

class BerandaAdapter:PagingDataAdapter<ListStoryItem,BerandaAdapter.ViewHolder>(DIFF_CALLBACK){
    private lateinit var onItemClick: OnItemClick

    inner class ViewHolder(private val binding :ItemRowBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(stories : ListStoryItem){
            binding.root.setOnClickListener {
                onItemClick.onItemClicked(stories)
            }
            binding.apply {
                Glide.with(itemView).load(stories.photoUrl).into(liststoryphoto)
                liststoryusername.text = stories.name
            }
        }
    }
    interface OnItemClick{
        fun onItemClicked(data : ListStoryItem)
    }

    fun setOnItemClick(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
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