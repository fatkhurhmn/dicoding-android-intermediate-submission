package academy.bangkit.storyapp.utils

import academy.bangkit.storyapp.data.local.entity.Story
import academy.bangkit.storyapp.data.remote.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user123",
            name = "myName",
            token = "randomToken"
        )
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = loginResult
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateDummyStory(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = "$i",
                name = "user $i",
                description = "desc $i",
                photoUrl = "photo $i",
                createdAt = "date $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoryResponse(): ListStoryResponse {
        val items: MutableList<StoryResponse> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponse(
                id = "$i",
                name = "user $i",
                description = "desc $i",
                photoUrl = "photo $i",
                createdAt = "date $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        return ListStoryResponse(
            error = false,
            message = "Stories fetched successfully",
            stories = items
        )
    }

    fun generateDummyImageMultipart(): MultipartBody.Part {
        val myFile = File("image")
        val requestImageFile = myFile.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(
            "img", myFile.name, requestImageFile
        )
    }

    fun generateDummyRequestBody(string: String): RequestBody {
        return string.toRequestBody("text/plain".toMediaType())
    }

    fun generateDummyFilUploadResponse():FileUploadResponse{
        return FileUploadResponse(
            error = false,
            message = "success"
        )
    }
}