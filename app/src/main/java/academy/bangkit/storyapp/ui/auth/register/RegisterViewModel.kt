package academy.bangkit.storyapp.ui.auth.register

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.ViewModel

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun createAccount(name: String, email: String, password: String) =
        storyRepository.createAccount(name, email, password)
}