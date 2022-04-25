package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.utils.MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `success to call deleteSession`() = runTest {
        mainViewModel.deleteSession()
        Mockito.verify(storyRepository).deleteSession()
    }
}