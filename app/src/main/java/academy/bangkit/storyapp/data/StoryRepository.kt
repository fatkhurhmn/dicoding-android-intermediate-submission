package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.local.UserPreferences
import academy.bangkit.storyapp.data.remote.response.FileUploadResponse
import academy.bangkit.storyapp.data.remote.response.ListStoryResponse
import academy.bangkit.storyapp.data.remote.response.LoginResponse
import academy.bangkit.storyapp.data.remote.response.RegisterResponse
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    fun createAccount(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.registerUser(name, email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.loginUser(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getAllStories(token: String): LiveData<Result<ListStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getAllStories(token)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadNewStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<FileUploadResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.uploadNewStory(token, image, description)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun saveAuthToken(token: String) {
        userPreferences.saveAuthToken(token)
    }

    fun getAuthToken(): LiveData<String> = userPreferences.getAuthToken().asLiveData()

    suspend fun deleteSession() = userPreferences.deleteSession()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, userPreferences)
        }.also { instance = it }
    }
}