package com.element.spex;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.element.camera.Capture;
import com.element.camera.ElementFaceAuthTask;
import com.element.camera.ElementFaceCaptureActivity;

import java.util.Map;

public class SpAuthActivity extends ElementFaceCaptureActivity {

    @Override
    public void onImageCaptured(Capture capture) {
        if (capture.success) {
            toastMessage(R.string.processing);

            String url = getString(R.string.face_auth_url);
            String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);

            new ElementFaceAuthTask(url, userId, capture)
                    .exec(callback);
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
        SpResultFragment fragment = new SpResultFragment();
        fragment.setData(message, iconResId);
        fragment.show(getSupportFragmentManager(), null);
    }

    private ElementFaceAuthTask.Callback callback = new ElementFaceAuthTask.Callback() {
        @Override
        public void onComplete(boolean verified, String message, Map<String, Object> details) {
            showResult(message, R.drawable.icon_check);
        }

        @Override
        public void onError(int code, @NonNull String message, @NonNull Map<String, Object> map) {
            showResult(message, R.drawable.icon_locker_white);
        }
    };
}