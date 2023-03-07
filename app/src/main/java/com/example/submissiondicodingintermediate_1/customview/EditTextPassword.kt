package com.example.submissiondicodingintermediate_1.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissiondicodingintermediate_1.R

class EditTextPassword: AppCompatEditText {

    private lateinit var passwordImage: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init(){
        passwordImage = ContextCompat.getDrawable(context, R.drawable.icon_password) as Drawable

        hint = resources.getString(R.string.hint_password)
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16
        setDrawables(passwordImage)

        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    s.toString().isEmpty() -> {
                        this@EditTextPassword.error = this@EditTextPassword.context.getString(R.string.password_empty_alert)
                    }
                    s.toString().length < 6 -> {
                        this@EditTextPassword.error = this@EditTextPassword.context.getString(R.string.password_char_alert)
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