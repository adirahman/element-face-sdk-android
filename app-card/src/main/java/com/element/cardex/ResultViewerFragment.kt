package com.element.cardex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementOcrResultField
import com.element.card.CardConstants.KEY_OCR_RESULTS
import com.element.card.CardConstants.OCR_RESULT_ERROR
import com.element.card.CardConstants.OCR_RESULT_OK
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ResultViewerFragment(
    private val resultCode: Int,
    private val result: Bundle
) : DialogFragment() {

    private val revision = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false).apply {
            when (resultCode) {
                OCR_RESULT_ERROR -> findViewById<TextView>(R.id.info).apply {
                    text = result.toString()
                    visibility = VISIBLE
                }
                OCR_RESULT_OK -> {
                    val box = findViewById<LinearLayout>(R.id.box)
                    result.keySet().filterNot { it == KEY_OCR_RESULTS }.forEach {
                        box.addView(inflateDataForm(inflater, it, result.get(it).toString()))
                    }
                    result.getParcelableArrayList<ElementOcrResultField>(KEY_OCR_RESULTS)
                        ?.let { ocrResult ->
                            box.addView(inflateResultForm(inflater, ocrResult))
                        }
                }
            }
            findViewById<Button>(R.id.revision).apply {
                text = getString(R.string.button_done)
                setOnClickListener {
                    (activity as CardExMainActivity?)!!.refreshUi()
                    dismiss()
                }
            }
        }
    }

    private fun inflateResultForm(
        inflater: LayoutInflater,
        data: List<ElementOcrResultField>,
        @StringRes title: Int = R.string.ocr_result_title
    ) =
        inflater.inflate(R.layout.result_form, null).apply {
            findViewById<TextView>(R.id.resultTitle).setText(title)
            val resultBox = findViewById<LinearLayout>(R.id.ocrResult)
            data.forEach {
                resultBox.addView(inflateDataForm(inflater, it.displayName, it.value.toString()))
            }
        }

    private fun inflateDataForm(inflater: LayoutInflater, title: String, message: String) =
        inflater.inflate(R.layout.data_form, null).apply {
            findViewById<TextInputLayout>(R.id.textLayout).run {
                this.hint = title
            }
            findViewById<TextInputEditText>(R.id.input).run {
                isEnabled = false
                setText(message)
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        revision[title] = s.toString()
                    }
                })
            }
        }
}