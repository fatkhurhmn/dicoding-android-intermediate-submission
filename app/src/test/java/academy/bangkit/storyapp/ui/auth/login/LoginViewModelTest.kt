package academy.bangkit.storyapp.ui.auth.login

import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.data.remote.response.LoginResponse
import academy.bangkit.storyapp.utils.DataDummy
import academy.bangkit.storyapp.utils.MainCoroutineRule
import academy.bangkit.storyapp.utils.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
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
class LoginViewModelTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private var dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private var dummyEmail = "myemail@gmail.com"
    private var dummyPassword = "123123"
    private var dummyToken = "randomDummyToken"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `when login success should return Result Success`() = runTest {
        val expectedResult: LiveData<Result<LoginResponse>> = liveData {
            emit(Result.Success(dummyLoginResponse))
        }

        `when`(loginViewModel.loginUser(dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = loginViewModel.loginUser(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(dummyEmail, dummyPassword)
        assertTrue(actualResult is Result.Success)
    }

    @Test
    fun `when login failed should return Result Error`() = runTest {
        val expectedResult: LiveData<Result<LoginResponse>> = liveData {
            emit(Result.Error(dummyLoginResponse.message))
        }

        `when`(loginViewModel.loginUser(dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = loginViewModel.loginUser(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(dummyEmail, dummyPassword)
        assertTrue(actualResult is Result.Error)
    }

    @Test
    fun `success to call saveAuthToken`() = runTest {
        loginViewModel.saveAuthToken(dummyToken)
        Mockito.verify(storyRepository).saveAuthToken(dummyToken)
    }

    @Test
    fun `success get token from datastore`(){
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken

        `when`(loginViewModel.getAuthToken()).thenReturn(expectedToken)

        val actualToken = loginViewModel.getAuthToken().getOrAwaitValue()
        Mockito.verify(storyRepository).getAuthToken()
        assertTrue(actualToken.isNotEmpty())
    }
}