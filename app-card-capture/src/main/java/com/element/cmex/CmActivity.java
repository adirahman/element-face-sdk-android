package com.element.cmex;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.element.camera.Capture;
import com.element.camera.ElementCardCaptureActivity;
import com.element.camera.ElementCardMatchTask;

import java.util.Map;

public class CmActivity extends ElementCardCaptureActivity {

    @Override
    protected void onCardPhotoTaken(String userId, Capture capture, int totalCount) {
        if (capture.success) {
            toastMessage(R.string.processing);
            getCaptureButton().setEnabled(false);
            new ElementCardMatchTask(getString(R.string.card_match_url), userId, capture)
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

    private void showResult(final String message, final int iconResId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CmResultFragment fragment = new CmResultFragment();
                fragment.setData(message, iconResId);
                fragment.show(getSupportFragmentManager(), null);
                fragment.setCancelable(false);
            }
        });
    }

    private ElementCardMatchTask.Callback callback = new ElementCardMatchTask.Callback() {
        @Override
        public void onComplete(String message, Map<String, Object> details) {
            showResult(message, R.drawable.icon_check);
        }

        @Override
        public void onError(int code, @NonNull String message, @NonNull Map<String, Object> map) {
            showResult(message, R.drawable.icon_locker_white);
        }
    };
}
