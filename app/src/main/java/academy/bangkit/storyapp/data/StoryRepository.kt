package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.local.UserPreferences
import academy.bangkit.storyapp.data.remote.response.*
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import academy.bangkit.storyapp.utils.Extension.getErrorMessage
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

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
                when (e) {
                    is HttpException -> {
                        val message = e.getErrorMessage()
                        if (message != null) {
                            emit(Result.Error(message))
                        }
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.loginUser(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val message = e.getErrorMessage()
                        if (message != null) {
                            emit(Result.Error(message))
                        }
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(token: String): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
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
                when (e) {
                    is HttpException -> {
                        val message = e.getErrorMessage()
                        if (message != null) {
                            emit(Result.Error(message))
                        }
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
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