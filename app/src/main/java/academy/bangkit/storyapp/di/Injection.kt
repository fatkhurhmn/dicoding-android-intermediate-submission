package academy.bangkit.storyapp.di

import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.data.local.UserPreferences
import academy.bangkit.storyapp.data.remote.retrofit.ApiConfig
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPreferences")

object Injection {
    fun providerRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        return StoryRepository.getInstance(apiService, userPreferences)
    }
}