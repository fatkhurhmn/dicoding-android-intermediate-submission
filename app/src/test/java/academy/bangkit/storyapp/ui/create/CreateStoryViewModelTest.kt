package academy.bangkit.storyapp.ui.create

import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.data.remote.response.FileUploadResponse
import academy.bangkit.storyapp.utils.DataDummy
import academy.bangkit.storyapp.utils.MainCoroutineRule
import academy.bangkit.storyapp.utils.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var createStoryViewModel: CreateStoryViewModel
    private val dummyToken = "dummyRandomToken"
    private val dummyImageMultipart = DataDummy.generateDummyImageMultipart()
    private val dummyDescription = DataDummy.generateDummyRequestBody("desc")
    private val dummyLatitude = DataDummy.generateDummyRequestBody("lat")
    private val dummyLongitude = DataDummy.generateDummyRequestBody("lon")
    private val dummyFileUploadResponse = DataDummy.generateDummyFilUploadResponse()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(storyRepository)
    }

    @Test
    fun `when upload new story success, should return Result Success`() = runTest{
        val expectedResult: LiveData<Result<FileUploadResponse>> = liveData {
            emit(Result.Success(dummyFileUploadResponse))
        }


        `when`(
            createStoryViewModel.uploadNewStory(
                token = dummyToken,
                image = dummyImageMultipart,
                description = dummyDescription,
                lat = dummyLatitude,
                lon = dummyLongitude
            )
        ).thenReturn(expectedResult)

        val actualResult = createStoryViewModel.uploadNewStory(
            token = dummyToken,
            image = dummyImageMultipart,
            description = dummyDescription,
            lat = dummyLatitude,
            lon = dummyLongitude
        ).getOrAwaitValue()

        verify(storyRepository).uploadNewStory(
            token = "Bearer $dummyToken",
            image = dummyImageMultipart,
            description = dummyDescription,
            lat = dummyLatitude,
            lon = dummyLongitude
        )
        assertTrue(actualResult is Result.Success)
    }

    @Test
    fun `when upload new story failed, should return Result Error`() = runTest{
        val expectedResult: LiveData<Result<FileUploadResponse>> = liveData {
            emit(Result.Error(dummyFileUploadResponse.message))
        }


        `when`(
            createStoryViewModel.uploadNewStory(
                token = dummyToken,
                image = dummyImageMultipart,
                description = dummyDescription,
                lat = dummyLatitude,
                lon = dummyLongitude
            )
        ).thenReturn(expectedResult)

        val actualResult = createStoryViewModel.uploadNewStory(
            token = dummyToken,
            image = dummyImageMultipart,
            description = dummyDescription,
            lat = dummyLatitude,
            lon = dummyLongitude
        ).getOrAwaitValue()

        verify(storyRepository).uploadNewStory(
            token = "Bearer $dummyToken",
            image = dummyImageMultipart,
            description = dummyDescription,
            lat = dummyLatitude,
            lon = dummyLongitude
        )
        assertTrue(actualResult is Result.Error)
    }
}