package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.local.UserPreferences
import academy.bangkit.storyapp.data.local.room.StoryDatabase
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import academy.bangkit.storyapp.utils.DataDummy
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var database: StoryDatabase

    @Mock
    private lateinit var userPreferences: UserPreferences

    private lateinit var apiService: ApiService
    private lateinit var storyRepository: StoryRepository

    private var dummyName = "myName"
    private var dummyEmail = "myemail@gmail.com"
    private var dummyPassword = "123123"
    private var dummyToken = "randomDummyToken"
    private val dummyImageMultipart = DataDummy.generateDummyImageMultipart()
    private val dummyDescription = DataDummy.generateDummyRequestBody("desc")
    private val dummyLatitude = DataDummy.generateDummyRequestBody("lat")
    private val dummyLongitude = DataDummy.generateDummyRequestBody("lon")

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storyRepository = StoryRepository.getInstance(apiService, userPreferences, database)
    }

    @Test
    fun `success to login`() = runTest {
        val expectedResult = DataDummy.generateDummyLoginResponse()
        val actualResult = apiService.loginUser(dummyEmail, dummyPassword)
        assertNotNull(actualResult.loginResult)
        assertEquals(expectedResult.loginResult, actualResult.loginResult)
    }

    @Test
    fun `success to register`() = runTest {
        val expectedResult = DataDummy.generateDummyRegisterResponse()
        val actualResult = apiService.registerUser(dummyName, dummyEmail, dummyPassword)
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `success to getAllStory`() = runTest {
        val expectedResult = DataDummy.generateDummyStoryResponse()
        val actualResult = apiService.getAllStories(token = dummyToken)
        assertNotNull(actualResult)
        assertEquals(expectedResult.stories.size, actualResult.stories.size)
    }

    @Test
    fun `success to getAllStoryWithLocation`() = runTest {
        val expectedResult = DataDummy.generateDummyStoryResponse()
        val actualResult = apiService.getAllStories(token = dummyToken, location = 1)
        assertNotNull(actualResult)
        assertEquals(expectedResult.stories.size, actualResult.stories.size)
    }

    @Test
    fun `success to uploadStory`() = runTest {
        val expectedResult = DataDummy.generateDummyFilUploadResponse()
        val actualResult = apiService.uploadNewStory(
            token = dummyToken,
            image = dummyImageMultipart,
            description = dummyDescription,
            lat = dummyLatitude,
            lon = dummyLongitude
        )
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `success to saveAuthToken`() = runTest {
        userPreferences.saveAuthToken(dummyToken)
        Mockito.verify(userPreferences).saveAuthToken(dummyToken)
    }

    @Test
    fun `success to getAuthToken`() = runTest {
        val expectedToken = flowOf(dummyToken)
        `when`(userPreferences.getAuthToken()).thenReturn(expectedToken)
        userPreferences.getAuthToken().collect { actualToken ->
            assertTrue(actualToken.isNotEmpty())
        }
        Mockito.verify(userPreferences).getAuthToken()
    }

    @Test
    fun `success to deleteSession`() = runTest {
        userPreferences.deleteSession()
        Mockito.verify(userPreferences).deleteSession()
    }

}