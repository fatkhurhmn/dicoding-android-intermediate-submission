package academy.bangkit.storyapp.views

import academy.bangkit.storyapp.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class PasswordEditText : AppCompatEditText {

    private lateinit var passwordIcon: Drawable

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        transformationMethod = PasswordTransformationMethod.getInstance()
        hint = "Password"
    }

    private fun init() {
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
        setIcon(passwordIcon)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && s.toString().length < 6) error =
                    "Password cannot be less than 6 characters"
            }
        })
    }

    private fun setIcon(
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
}