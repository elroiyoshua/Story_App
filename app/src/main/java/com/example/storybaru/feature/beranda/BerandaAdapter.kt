package com.example.storybaru.feature.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storybaru.databinding.ItemRowBinding
import com.example.storybaru.responses.AllStoriesResponses
import com.example.storybaru.responses.ListStoryItem

class BerandaAdapter(private val listStories : List<ListStoryItem>):RecyclerView.Adapter<BerandaAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding :ItemRowBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(stories : ListStoryItem){
            binding.apply {
                Glide.with(itemView).load(stories.photoUrl).into(liststoryphoto)
                liststoryusername.text = stories.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = listStories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

}