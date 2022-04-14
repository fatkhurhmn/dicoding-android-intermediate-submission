package academy.bangkit.storyapp.adapter

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.Story
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.utils.Extension.loadImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ListStoryAdapter : ListAdapter<Story, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvStoryName.text = itemView.resources.getString(R.string.name, story.name)
            binding.imgStoryPhoto.loadImage(story.photoUrl)
        }

    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Story> = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == oldItem
            }

        }
    }
}