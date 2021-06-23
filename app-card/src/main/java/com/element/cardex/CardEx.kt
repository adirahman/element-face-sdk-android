package com.element.cardex

import android.view.View
import com.element.camera.ElementCardSDK

const val DOC_SELECT_REQ_CODE = 12200

const val CARD_REQ_CODE = 12500

const val ENROLL_REQ_CODE = 12800

internal fun getDocName(docTypeId: String?): String? {
    ElementCardSDK.getDocs().forEach {
        if (it.first == docTypeId) {
            return it.second
        }
    }
    return null
}

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (System.currentTimeMillis() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = System.currentTimeMillis()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}