package academy.bangkit.storyapp.adapter

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.StoryResponse
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.utils.Extension.loadImage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private val stories = ArrayList<StoryResponse>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setStories(storyResponses: ArrayList<StoryResponse>) {
        this.stories.clear()
        this.stories.addAll(storyResponses)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
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

    inner class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storyResponse: StoryResponse) {
            binding.tvStoryName.text = itemView.resources.getString(R.string.name, storyResponse.name)
            binding.imgStoryPhoto.loadImage(storyResponse.photoUrl)
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(storyResponse, binding, itemView)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(storyResponse: StoryResponse, view: StoryItemBinding, itemView: View)
    }
}