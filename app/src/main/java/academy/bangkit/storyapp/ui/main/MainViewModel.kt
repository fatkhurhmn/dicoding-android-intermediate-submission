package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun deleteSession() {
        viewModelScope.launch {
            storyRepository.deleteSession()
        }
    }
}