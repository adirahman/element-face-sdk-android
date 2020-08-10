package com.element.facex;

import androidx.annotation.NonNull;

import com.element.camera.Capture;
import com.element.camera.ElementFaceCaptureActivity;
import com.element.camera.ElementFaceEnrollTask;
import com.element.camera.ElementUserUtils;
import com.element.camera.UserInfo;

import java.util.Map;

public class ServerEnrollActivity extends ElementFaceCaptureActivity {

    @Override
    public void onImageCaptured(Capture capture) {
        if (capture.success) {
            toastMessage(R.string.processing);

            String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);
            UserInfo userInfo = ElementUserUtils.getUser(getBaseContext(), userId);

            ElementFaceEnrollTask task = new ElementFaceEnrollTask(getBaseContext(), getString(R.string.face_enroll_url), userInfo, capture);
            task.post(callback);
        } else {
            showResult(getString(R.string.capture_failed), R.drawable.icon_focus);
        }
    }

    private void showResult(String message, int iconResId) {
        ResultFragment fragment = new ResultFragment();
        fragment.setData(message, iconResId);
        fragment.show(getSupportFragmentManager(), null);
    }

    private ElementFaceEnrollTask.Callback callback = new ElementFaceEnrollTask.Callback() {
        @Override
        public void onComplete(String message, UserInfo userInfo, Map<String, Object> details) {
            showResult(message, R.drawable.icon_check);
        }

        @Override
        public void onError(int code, @NonNull String message, @NonNull Map<String, Object> map) {
            showResult(message, R.drawable.icon_locker_white);
        }
    };
}