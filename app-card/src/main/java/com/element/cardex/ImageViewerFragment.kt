package com.element.cardex

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementCardSDK

class ImageViewerFragment(private val token: String) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false).apply {
            if (token.isEmpty()) {
                dismiss()
            } else {
                val box = findViewById<LinearLayout>(R.id.box)
                val nullDataStringBuilder = StringBuilder()
                val docTypeId = ElementCardSDK.getDocTypeId(token)
                ElementCardSDK.getCardSides(docTypeId)?.forEach {
                    val data = ElementCardSDK.getPhotosFromToken(token, it)
                    if (data != null) {
                        inflater.inflate(R.layout.image, null).let { image ->
                            box.addView(image.findViewById<ImageView>(R.id.image).apply {
                                setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
                            })
                        }
                    } else {
                        nullDataStringBuilder.append(getString(R.string.no_display) + "\n")
                    }
                }
                if (nullDataStringBuilder.isNotEmpty()) {
                    findViewById<TextView>(R.id.info).run {
                        visibility = View.VISIBLE
                        text = nullDataStringBuilder.toString()
                    }
                }
            }
            findViewById<Button>(R.id.done).setOnClickListener {
                dismiss()
            }
        }
    }
}