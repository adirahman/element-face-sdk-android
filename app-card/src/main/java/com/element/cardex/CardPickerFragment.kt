package com.element.cardex

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementCardSDK
import java.io.ByteArrayOutputStream

class CardPickerFragment(private val docTypeId: String,
                         private val callback: (cardPhotos: Map<String, ByteArray>) -> Unit) : DialogFragment() {

    private val result = mutableMapOf<String, ByteArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_picker, container, false).apply {

            val box = findViewById<LinearLayout>(R.id.box)

            ElementCardSDK.getCardSides(docTypeId)?.forEachIndexed { index, side ->
                inflater.inflate(R.layout.button, null).apply {
                    box.addView(this)
                    findViewById<Button>(R.id.button).apply {
                        text = getString(R.string.pick_card, side)
                        setOnClickListener {
                            startImageChooser(index)
                        }
                    }
                }
            }

            findViewById<Button>(R.id.done)?.setOnClickListener {
                callback.invoke(result)
                dismiss()
            }
        }
    }

    private fun startImageChooser(requestCode: Int) {
        startActivityForResult(Intent.createChooser(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }, getString(R.string.pick_card_front)), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            data?.data?.run {
                ByteArrayOutputStream().use { stream ->
                    MediaStore.Images.Media.getBitmap(context!!.contentResolver, this)
                            .compress(Bitmap.CompressFormat.JPEG, 75, stream)
                    result[ElementCardSDK.getCardSides(docTypeId)?.getOrNull(requestCode)
                            ?: return@run] = stream.toByteArray()
                }
            }
        }
    }
}