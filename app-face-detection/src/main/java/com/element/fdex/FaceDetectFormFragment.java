package com.element.fdex;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FaceDetectFormFragment extends DialogFragment {

    private FdMainActivity activity;

    private Handler handler;

    private EditText userId;

    private Button start;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_face_detect_form, container, false);

        userId = rootView.findViewById(R.id.userId);

        start = rootView.findViewById(R.id.capture);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                onClickEnroll();
            }
        });

        return rootView;
    }

    public void setMainActivity(final FdMainActivity activity) {
        this.activity = activity;
        this.handler = new Handler();
    }

    private synchronized void onClickEnroll() {
        start.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start.setEnabled(true);
            }
        }, 500);

        String userIdStr = userId.getText().toString();

        if (TextUtils.isEmpty(userIdStr)) {
            Toast.makeText(activity, R.string.error_msg_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        activity.start(userIdStr);
        dismiss();
    }
}