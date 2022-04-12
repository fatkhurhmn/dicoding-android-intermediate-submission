package academy.bangkit.storyapp.views

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.utils.Extension.setIcon
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class EmailEditText : AppCompatEditText {

    private lateinit var emailIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        hint = resources.getString(R.string.hint_email)
    }

    private fun init() {
        emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        setIcon(emailIcon)

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(s)
                        .matches()
                ) error = resources.getString(R.string.error_email)
            }
        })
    }
}