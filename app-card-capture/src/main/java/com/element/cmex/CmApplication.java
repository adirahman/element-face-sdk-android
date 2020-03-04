package com.element.cmex;

import android.app.Application;

import com.element.camera.ElementFaceSDK;

public class CmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ElementFaceSDK.initSDK(CmApplication.this);
        ElementFaceSDK.enableDebugMode(getBaseContext(), true);
    }
}
