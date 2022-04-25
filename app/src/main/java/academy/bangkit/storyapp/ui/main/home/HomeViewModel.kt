package academy.bangkit.storyapp.ui.main.home

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStory(token: String) =
        storyRepository.getAllStory(token).cachedIn(viewModelScope)
}