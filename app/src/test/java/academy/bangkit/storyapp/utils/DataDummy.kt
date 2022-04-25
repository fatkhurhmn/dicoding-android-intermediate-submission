package academy.bangkit.storyapp.utils

import academy.bangkit.storyapp.data.local.entity.Story
import academy.bangkit.storyapp.data.remote.response.LoginResponse
import academy.bangkit.storyapp.data.remote.response.LoginResult
import academy.bangkit.storyapp.data.remote.response.RegisterResponse

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-yj5pc_LARC_AgK61",
            name = "Arif Faizin",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
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

    fun generateDummyStoryResponse(): List<Story> {
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
}