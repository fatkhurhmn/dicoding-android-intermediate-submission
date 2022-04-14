package academy.bangkit.storyapp.utils

import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.di.Injection
import academy.bangkit.storyapp.ui.auth.login.LoginViewModel
import academy.bangkit.storyapp.ui.auth.register.RegisterViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class:" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.providerRepository(context))
        }.also { instance = it }
    }
}