package academy.bangkit.storyapp.ui.auth.login

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun loginUser(email: String, password: String) = storyRepository.loginUser(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            storyRepository.saveAuthToken(token)
        }
    }

    fun getAuthToken(): LiveData<String> = storyRepository.getAuthToken()
}