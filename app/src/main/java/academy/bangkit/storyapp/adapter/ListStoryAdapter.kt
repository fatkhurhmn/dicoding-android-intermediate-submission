package academy.bangkit.storyapp.adapter

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.StoryResponse
import academy.bangkit.storyapp.databinding.StoryItemBinding
import academy.bangkit.storyapp.utils.Extension.loadImage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ListStoryAdapter : PagingDataAdapter<StoryResponse, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
            override fun areItemsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}