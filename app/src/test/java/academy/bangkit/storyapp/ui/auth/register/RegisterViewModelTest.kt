package academy.bangkit.storyapp.ui.auth.register

import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.data.StoryRepository
import academy.bangkit.storyapp.data.remote.response.RegisterResponse
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private var dummyName = "fatkhu"
    private var dummyEmail = "fatkhu@gmail.com"
    private var dummyPassword = "123123"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `when register success should return Result Success`()= runTest{
        val expectedResult: LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Success(dummyRegisterResponse))
        }

        `when`(registerViewModel.createAccount(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = registerViewModel.createAccount(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).createAccount(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualResult is Result.Success)
    }

    @Test
    fun `when register failed should return Result Error`()= runTest{
        val expectedResult: LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Error(dummyRegisterResponse.message))
        }

        `when`(registerViewModel.createAccount(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = registerViewModel.createAccount(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).createAccount(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualResult is Result.Error)
    }
}