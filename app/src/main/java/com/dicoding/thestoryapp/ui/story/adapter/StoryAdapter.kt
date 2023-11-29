package com.dicoding.thestoryapp.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.thestoryapp.R
import com.dicoding.thestoryapp.databinding.ItemStoryBinding
import com.dicoding.thestoryapp.model.Story
import com.dicoding.thestoryapp.util.changeFormatDate

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var Storylist: List<Story>? = null
    private lateinit var onItemClickListener: OnItemClickListener

    fun setData(listStory: List<Story>) {
        this.Storylist = listStory
    }

    fun setOnItemClicked(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bindData(Storylist?.get(position) ?: Story())
    }

    override fun getItemCount(): Int = if (Storylist != null)  Storylist!!.size else 0

    inner class StoryViewHolder(private val view: ItemStoryBinding): RecyclerView.ViewHolder(view.root){
        fun bindData(story: Story) {
            with(view) {
                date.text = story.createdAt?.let { changeFormatDate(it) }
                description.text = story.description
                createdBy.text = story.name

                Glide.with(this.root)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.image_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(img)

                itemStory.setOnClickListener {
                    onItemClickListener.onItemClicked(story.id as String)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

}