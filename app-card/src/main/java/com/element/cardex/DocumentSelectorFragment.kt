package com.element.cardex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementCardSDK

class DocumentSelectorFragment(
        private val docs: List<Pair<String, String>> = ElementCardSDK.getDocs(),
        private val callback: (selectedDoc: Pair<String, String>) -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doc_selector, container, false).apply {
            val box = findViewById<LinearLayout>(R.id.box)

            docs.forEach { pair ->
                inflater.inflate(R.layout.button, null).apply {
                    box.addView(this)
                    findViewById<Button>(R.id.button).apply {
                        text = pair.second
                        setOnClickListener {
                            callback.invoke(pair)
                            dismiss()
                        }
                    }
                }
            }
        }
    }
}