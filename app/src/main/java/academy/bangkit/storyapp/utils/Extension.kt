package academy.bangkit.storyapp.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

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
}