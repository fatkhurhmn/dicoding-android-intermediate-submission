package academy.bangkit.storyapp.ui.main.home

import academy.bangkit.storyapp.adapter.ListStoryAdapter
import academy.bangkit.storyapp.data.local.entity.Story
import academy.bangkit.storyapp.utils.DataDummy
import academy.bangkit.storyapp.utils.MainCoroutineRule
import academy.bangkit.storyapp.utils.PagedTestDataSources
import academy.bangkit.storyapp.utils.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken = "asdfdsfdsaf"

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `when Get Quote Should Not Null`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<Story>>()
        story.value = data
        Mockito.`when`(homeViewModel.getAllStory(dummyToken)).thenReturn(story)
        val actualNews = homeViewModel.getAllStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        differ.submitData(actualNews)

        advanceUntilIdle()
        Mockito.verify(homeViewModel).getAllStory(dummyToken)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)

    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
