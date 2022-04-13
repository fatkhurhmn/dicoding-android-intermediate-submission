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

class NameEditText : AppCompatEditText {
    private lateinit var nameIcon: Drawable

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

        hint = resources.getString(R.string.hint_name)
    }

    private fun init() {
        nameIcon = ContextCompat.getDrawable(context, R.drawable.ic_name) as Drawable
        setIcon(nameIcon)

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) error = resources.getString(R.string.error_name)
            }
        })
    }
}