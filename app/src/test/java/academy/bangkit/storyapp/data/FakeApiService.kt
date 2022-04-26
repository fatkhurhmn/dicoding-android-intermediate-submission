package academy.bangkit.storyapp.data

import academy.bangkit.storyapp.data.remote.response.FileUploadResponse
import academy.bangkit.storyapp.data.remote.response.ListStoryResponse
import academy.bangkit.storyapp.data.remote.response.LoginResponse
import academy.bangkit.storyapp.data.remote.response.RegisterResponse
import academy.bangkit.storyapp.data.remote.retrofit.ApiService
import academy.bangkit.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return DataDummy.generateDummyRegisterResponse()
    }

    override suspend fun loginUser(email: String, password: String): LoginResponse {
        return DataDummy.generateDummyLoginResponse()
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): ListStoryResponse {
        return DataDummy.generateDummyStoryResponse()
    }

    override suspend fun uploadNewStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): FileUploadResponse {
        return DataDummy.generateDummyFilUploadResponse()
    }
}