package com.element.facex;

import android.content.Intent;

import com.element.camera.Capture;
import com.element.camera.ElementFaceAuthTask;
import com.element.camera.ElementFaceCaptureActivity;

import java.util.Map;

import androidx.annotation.NonNull;

public class ServerAuthActivity extends ElementFaceCaptureActivity {

    @Override
    public void onImageCaptured(Capture capture) {
        if (capture.success) {
            toastMessage(R.string.processing);

            String url = getString(R.string.face_auth_url);
            String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);

            new ElementFaceAuthTask(url, userId, capture)
                    .post(callback);
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
        ResultFragment fragment = new ResultFragment();
        fragment.setData(message, iconResId);
        fragment.show(getSupportFragmentManager(), null);
    }

    private final ElementFaceAuthTask.Callback callback = new ElementFaceAuthTask.Callback() {
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