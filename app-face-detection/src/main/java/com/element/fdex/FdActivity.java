package com.element.fdex;

import android.content.Intent;

import com.element.camera.Capture;
import com.element.camera.ElementFaceDetectActivity;

public class FdActivity extends ElementFaceDetectActivity {

    @Override
    public void onImageCaptured(Capture capture) {
        if (capture.success) {
            showResult(getString(R.string.captured, getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID)), R.drawable.icon_check);
        } else {
            showResult(getString(R.string.capture_failed), R.drawable.icon_focus);
        }
    }

    void recapture() {
        finish();

        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.setClass(getBaseContext(), getClass());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void showResult(String message, int iconResId) {
        FdResultFragment fragment = new FdResultFragment();
        fragment.setData(message, iconResId);
        fragment.show(getSupportFragmentManager(), null);
    }
}