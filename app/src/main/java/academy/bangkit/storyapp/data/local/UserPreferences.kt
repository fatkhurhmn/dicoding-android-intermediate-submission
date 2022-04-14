package academy.bangkit.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = token
        }
    }

    fun getAuthToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_KEY] ?: ""
        }
    }

    companion object {
        private val AUTH_KEY = stringPreferencesKey("AUTH_TOKEN")

        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return instance ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                this.instance = instance
                instance
            }
        }
    }
}