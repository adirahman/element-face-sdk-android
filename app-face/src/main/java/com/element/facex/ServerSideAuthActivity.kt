package com.element.facex

import com.element.camera.Capture
import com.element.camera.ElementFaceAuthTask
import com.element.camera.ElementFaceCaptureActivity

open class ServerSideAuthActivity : ElementFaceCaptureActivity() {

    private val userId: String? by lazy {
        intent.getStringExtra(EXTRA_ELEMENT_USER_ID)
    }

    override fun onImageCaptured(capture: Capture) {
        if (capture.success) {
            toastMessage(R.string.processing)
            ElementFaceAuthTask(
                getString(R.string.face_auth_url),
                userId,
                capture
            ).post(callback)
        } else {
            showResult(0, getString(R.string.capture_failed), R.drawable.icon_focus)
        }
    }

    private fun showResult(code: Int, message: String, iconResId: Int) {
        ResultFragment.show(this, message, iconResId)
    }

    private val callback: ElementFaceAuthTask.Callback = object : ElementFaceAuthTask.Callback {
        override fun onComplete(verified: Boolean, message: String, details: Map<String, Any>) {
            showResult(0, message, R.drawable.icon_check)
        }

        override fun onError(code: Int, message: String, map: Map<String, Any>) {
            showResult(code, message, R.drawable.icon_locker_white)
        }
    }
}