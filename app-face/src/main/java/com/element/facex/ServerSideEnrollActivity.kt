package com.element.facex

import com.element.camera.*

class ServerSideEnrollActivity : ElementFaceCaptureActivity() {

    private val userId: String? by lazy {
        intent.getStringExtra(EXTRA_ELEMENT_USER_ID)
    }

    private var isUserEnrolled = false

    override fun onImageCaptured(capture: Capture) {
        if (capture.success) {
            toastMessage(R.string.processing)
            ElementFaceEnrollTask(
                    baseContext,
                    getString(R.string.face_enroll_url),
                    ElementUserUtils.getUser(baseContext, userId),
                    capture
            ).post(callback)
        } else {
            showResult(0, getString(R.string.capture_failed), R.drawable.icon_focus)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isUserEnrolled) {
            ElementUserUtils.deleteUser(baseContext, userId)
        }
    }

    private fun showResult(code: Int, message: String, iconResId: Int) {
        ResultFragment.show(this, code, message, iconResId)
    }

    private val callback: ElementFaceEnrollTask.Callback = object : ElementFaceEnrollTask.Callback {
        override fun onComplete(message: String, userInfo: UserInfo, details: Map<String, Any>) {
            showResult(0, message, R.drawable.icon_check)
            isUserEnrolled = true
        }

        override fun onError(code: Int, message: String, map: Map<String, Any>) {
            showResult(code, message, R.drawable.icon_locker_white)
        }
    }
}