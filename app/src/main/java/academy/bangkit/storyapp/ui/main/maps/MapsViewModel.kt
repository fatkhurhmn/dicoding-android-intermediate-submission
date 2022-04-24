package academy.bangkit.storyapp.ui.main.maps

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.ViewModel

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStoryWithLocation(token: String) = storyRepository.getAllStoryWithLocation(token)
}