package academy.bangkit.storyapp.ui.main.maps

import academy.bangkit.storyapp.data.remote.response.ListStoryResponse
import academy.bangkit.storyapp.utils.MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.utils.DataDummy
import academy.bangkit.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStory = DataDummy.generateDummyStoryResponse()
    private val dummyToken = "randomDummyToken"

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when get all story success, should not null and return Result Success`() {
        val expectedResult: LiveData<Result<ListStoryResponse>> = liveData {
            emit(Result.Success(dummyStory))
        }

        `when`(mapsViewModel.getAllStoryWithLocation(dummyToken)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.getAllStoryWithLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getAllStoryWithLocation(dummyToken)
        assertNotNull(actualResult)
        assertTrue(actualResult is Result.Success)
        assertEquals(dummyStory.stories.size, (actualResult as Result.Success).data.stories.size)
    }

    @Test
    fun `when get all story failed return Result Error`() {
        val expectedResult: LiveData<Result<ListStoryResponse>> = liveData {
            emit(Result.Error(dummyStory.message))
        }

        `when`(mapsViewModel.getAllStoryWithLocation(dummyToken)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.getAllStoryWithLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getAllStoryWithLocation(dummyToken)
        assertTrue(actualResult is Result.Error)
    }
}