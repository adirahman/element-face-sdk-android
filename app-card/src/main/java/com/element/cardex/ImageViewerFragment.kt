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
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementCardSDK
import com.element.camera.ElementFaceSDK

class ImageViewerFragment(private val userId: String, private val modality: String) :
    DialogFragment() {

    companion object {

        private const val VIEW_CARD = "VIEW_CARD"
        private const val VIEW_SELFIE = "VIEW_SELFIE"

        fun viewCard(activity: CardExMainActivity, token: String) {
            ImageViewerFragment(token, VIEW_CARD).show(activity.supportFragmentManager, null)
        }

        fun viewSelfie(activity: CardExMainActivity, userId: String) {
            ImageViewerFragment(userId, VIEW_SELFIE).show(activity.supportFragmentManager, null)
        }
    }

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
        return inflater.inflate(R.layout.fragment_image_viewer, container, false).apply {
            val box = findViewById<LinearLayout>(R.id.box)
            val list = if (modality == VIEW_CARD) {
                ElementCardSDK.getScannedDocPhotos(userId)
            } else {
                ElementFaceSDK.getUserEnrollSelfies(userId)
            }
            list?.forEach { data ->
                inflater.inflate(R.layout.image, null).let { image ->
                    box.addView(image.findViewById<ImageView>(R.id.image).apply {
                        setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
                    })
                }
            }
            findViewById<Button>(R.id.done).setOnClickListener {
                dismiss()
            }
        }
    }
}