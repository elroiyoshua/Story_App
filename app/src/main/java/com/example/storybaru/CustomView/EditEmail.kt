package com.example.storybaru.CustomView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import com.example.storybaru.R

class EditEmail : AppCompatEditText {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if(p0 != null && !Patterns.EMAIL_ADDRESS.matcher(p0).matches()){
                    this@EditEmail.error = this@EditEmail.context.getString(R.string.erroremail)


                }
            }

        })
    }
}