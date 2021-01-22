package com.element.cardex

import android.content.Context
import com.element.camera.ElementCardSDK
import com.element.camera.ElementUserUtils

const val CARD_REQ_CODE = 12500

const val ENROLL_REQ_CODE = 12800

const val AUTH_REQ_CODE = 12801

internal fun getDocName(docTypeId: String?): String? {
    ElementCardSDK.getDocs().forEach {
        if (it.first == docTypeId) {
            return it.second
        }
    }
    return null
}

internal fun String.getUserName(context: Context): String? {
    return ElementUserUtils.getUser(context, this)?.name
}

internal fun getUserScannedDocs(context: Context, userId: String?): List<Pair<String, String>> {
    return ElementCardSDK.getDocs().map {
        if (ElementCardSDK.getUserToken(context, userId, it.first) != null) {
            it
        } else {
            null
        }
    }.toList().filterNotNull()
}