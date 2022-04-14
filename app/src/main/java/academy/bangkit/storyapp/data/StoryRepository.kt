package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.local.UserPreferences
import academy.bangkit.storyapp.data.remote.response.LoginResponse
import academy.bangkit.storyapp.data.remote.response.RegisterResponse
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import java.lang.Exception

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

    suspend fun saveAuthToken(token: String) {
        userPreferences.saveAuthToken(token)
    }

    fun getAuthToken(): LiveData<String> = userPreferences.getAuthToken().asLiveData()

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