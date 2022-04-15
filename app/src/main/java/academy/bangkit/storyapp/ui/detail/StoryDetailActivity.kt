package academy.bangkit.storyapp.ui.detail

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.Story
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class StoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)

        val detail = intent.getParcelableExtra<Story>(EXTRA_DETAIL)
        Log.d("Detail", "onCreate: $detail")
    }

    companion object{
        const val EXTRA_DETAIL = "academy.bangkit.storyapp.EXTRA_DETAIL"
    }
}