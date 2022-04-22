package academy.bangkit.storyapp.utils

import academy.bangkit.storyapp.R
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import retrofit2.HttpException

object Extension {
    fun TextView.setIcon(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
        compoundDrawablePadding = 16
    }

    fun String.showMessage(view: View) {
        Snackbar.make(view, this, Snackbar.LENGTH_SHORT).show()
    }

    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500).placeholder(R.drawable.ic_default_photo))
            .centerInside()
            .into(this)
    }

    fun HttpException.getErrorMessage(): String? {
        val response = this.response()?.errorBody()?.string()
        val jsonObject = response?.let { JSONObject(it) }
        return jsonObject?.getString("message")
    }
}