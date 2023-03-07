package com.example.submissiondicodingintermediate_1.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissiondicodingintermediate_1.R

class EditTextEmail: AppCompatEditText {
    private lateinit var emailImage: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        emailImage = ContextCompat.getDrawable(context, R.drawable.icon_email) as Drawable

        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        compoundDrawablePadding = 16
        hint = resources.getString(R.string.hint_email)
        setDrawables(emailImage)

        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    s.toString().isEmpty() -> {
                        this@EditTextEmail.error = this@EditTextEmail.context.getString(R.string.email_empty_alert)
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(s).matches() -> {
                        this@EditTextEmail.error = this@EditTextEmail.context.getString(R.string.email_config_alert)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}