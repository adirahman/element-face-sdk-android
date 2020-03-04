package com.element.spex;

import android.app.Application;

import com.element.camera.ElementFaceSDK;

public class SpApplication extends Application {

    @Override
        public void onCreate() {
            super.onCreate();
            ElementFaceSDK.initSDK(SpApplication.this);
            ElementFaceSDK.enableDebugMode(getBaseContext(), true);
    }
}
