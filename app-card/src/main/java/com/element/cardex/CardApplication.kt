package com.element.cardex

import android.app.Application
import com.element.camera.ElementCardSDK

open class CardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ElementCardSDK.initSDK(this)
    }
}