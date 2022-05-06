package academy.bangkit.storyapp.ui.create

import academy.bangkit.storyapp.data.StoryRepository
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadNewStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = storyRepository.uploadNewStory("Bearer $token", image, description, lat, lon)
}