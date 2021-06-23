package com.element.facex;

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
import androidx.preference.PreferenceManager;

import com.element.camera.UserInfo;
import com.element.common.text.ComTextUtils;

public class EnrollFormFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enroll_form, container, false);
        Button enroll = rootView.findViewById(R.id.enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                clickEnroll((Button) view);
            }
        });
        String processing = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("processing", "Local");
        enroll.setText("Local".equals(processing) ? R.string.local_enroll : R.string.server_enroll);
        return rootView;
    }

    private synchronized void clickEnroll(Button button) {
        button.setEnabled(false);
        new Handler().postDelayed(() -> button.setEnabled(true), 500);

        String firstName = ((EditText) getView().findViewById(R.id.firstName)).getText().toString();
        String userId = ((EditText) getView().findViewById(R.id.userId)).getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(getActivity(), R.string.error_msg_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfo userInfo;
        if (ComTextUtils.isEmptyOrWhitespace(userId)) {
            userInfo = UserInfo.builder()
                    .setName(firstName)
                    .enroll(getActivity());
        } else {
            userInfo = UserInfo.builder()
                    .setId(userId)
                    .setName(firstName)
                    .enroll(getActivity());
        }

        ((FaceExMainActivity) getActivity()).startEnroll(userInfo.userId);
        dismiss();
    }
}