package academy.bangkit.storyapp.ui.detail

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.remote.response.Story
import academy.bangkit.storyapp.databinding.ActivityStoryDetailBinding
import academy.bangkit.storyapp.utils.Extension.loadImage
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupContent()
        closeDetail()
    }

    private fun closeDetail() {
        binding.btnCloseDetail.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupContent() {
        val detail = intent.getParcelableExtra<Story>(EXTRA_DETAIL)

        with(binding) {
            if (detail != null) {
                imgDetailPhoto.loadImage(detail.photoUrl)
                tvDetailName.text = getString(R.string.name, detail.name)
                tvDetailDate.text = dateConverter(detail.createdAt)
                tvDetailDesc.text = detail.description
            }
        }
    }

    private fun dateConverter(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMMM yyyy | HH:mm", Locale.getDefault())
        return formatter.format(parser.parse(date) as Date)
    }

    companion object {
        const val EXTRA_DETAIL = "academy.bangkit.storyapp.EXTRA_DETAIL"
    }
}