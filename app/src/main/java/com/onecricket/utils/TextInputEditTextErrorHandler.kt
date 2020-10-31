package com.onecricket.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object TextInputEditTextErrorHandler {

    fun resetTextInputErrorsOnTextChanged(vararg textInputLayouts: TextInputLayout) {
        for (inputLayout in textInputLayouts) {
            val editText = inputLayout.editText as TextInputEditText?
            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    if (inputLayout.error != null) inputLayout.error = null
                }
            })
        }
    }
}
