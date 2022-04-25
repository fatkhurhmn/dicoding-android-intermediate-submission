package academy.bangkit.storyapp.utils

import academy.bangkit.storyapp.data.local.entity.Story
import academy.bangkit.storyapp.data.remote.response.*

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
}