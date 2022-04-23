package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.remote.response.StoryResponse
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class StoryPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, StoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getAllStories(token = token, page = page, size = params.loadSize)
            Log.d("LOAD_PAGING", "load: $page")
            LoadResult.Page(
                data = responseData.stories,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.stories.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}