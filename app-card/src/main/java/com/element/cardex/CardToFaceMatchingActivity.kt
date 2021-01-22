package com.element.cardex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementCardToFaceMatchingActivity
import com.element.camera.ElementServiceResultReceiver
import com.element.card.CardConstants

class CardToFaceMatchingActivity : ElementCardToFaceMatchingActivity() {

    override fun onCardToFaceMatchingServiceStarted() {
        runOnUiThread {
            Toast.makeText(baseContext, R.string.processing, Toast.LENGTH_LONG).show()
        }
    }

    override fun onFaceCaptureFailed(reason: String) {
        showResult(reason, R.drawable.icon_focus)
    }

    override fun getElementServiceResultReceiver(): ElementServiceResultReceiver {
        return ElementServiceResultReceiver { resultCode, _ ->
            when (resultCode) {
                CardConstants.OCR_RESULT_OK -> {
                    showResult("Success!", R.drawable.icon_check)
                }
                CardConstants.OCR_RESULT_ERROR -> {
                    showResult("Failed!", R.drawable.icon_focus)
                }
            }
        }
    }

    private fun showResult(message: String, iconResId: Int) {
        val fragment = ResultFragment()
        fragment.setData(message, iconResId)
        fragment.show(supportFragmentManager, null)
    }
}

class ResultFragment : DialogFragment() {

    private var message: String? = null
    private var iconResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, 0)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_server_auth_result, container, false)
        val confirmText = rootView.findViewById<TextView>(R.id.confirmText)
        val confirmImage = rootView.findViewById<ImageView>(R.id.confirmImage)
        confirmText.text = message
        confirmImage.setImageResource(iconResId)

        val exitButton = rootView.findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            activity?.finish()
        }
        return rootView
    }

    fun setData(message: String?, iconResId: Int) {
        this.message = message
        this.iconResId = iconResId
    }
}