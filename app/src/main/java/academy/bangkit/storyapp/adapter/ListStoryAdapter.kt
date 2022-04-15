package academy.bangkit.storyapp.adapter

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.Story
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.utils.Extension.loadImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private val stories = ArrayList<Story>()

    fun setStories(stories: ArrayList<Story>) {
        this.stories.clear()
        this.stories.addAll(stories)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.tvStoryName.text = itemView.resources.getString(R.string.name, story.name)
            binding.imgStoryPhoto.loadImage(story.photoUrl)
        }
    }
}