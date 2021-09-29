package com.element.facex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.element.camera.ElementFaceCaptureActivity

class ResultFragment : DialogFragment() {

    private var message: String? = null

    private var iconResId = 0

    companion object {
        fun show(activity: ElementFaceCaptureActivity, message: String, iconResId: Int) {
            val resultFragment = ResultFragment().apply {
                setData(message, iconResId)
            }

            activity.supportFragmentManager.beginTransaction()
                .add(resultFragment, null)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, 0)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_server_auth_result, container, false)
        val confirmText = rootView.findViewById<TextView>(R.id.confirmText)
        val confirmImage = rootView.findViewById<ImageView>(R.id.confirmImage)
        val retryButton = rootView.findViewById<Button>(R.id.retryButton)
        val exitButton = rootView.findViewById<Button>(R.id.exitButton)

        confirmText.text = message
        confirmImage.setImageResource(iconResId)

        retryButton.visibility = if (activity is ServerSideAuthActivity) View.VISIBLE else View.GONE
        retryButton.setOnClickListener {
            (activity as? ServerSideAuthActivity)?.relaunch()
        }

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