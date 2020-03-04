package com.element.spex;

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

import com.element.camera.ProviderUtil;
import com.element.camera.UserInfo;
import com.element.common.text.ComTextUtils;

import java.util.HashMap;

public class ServerEnrollFormFragment extends DialogFragment {

    private SpMainActivity activity;

    private Handler handler;

    private EditText firstName;

    private EditText lastName;

    private EditText userId;

    private Button enroll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_enroll_form, container, false);

        firstName = rootView.findViewById(R.id.firstName);
        lastName = rootView.findViewById(R.id.lastName);
        userId = rootView.findViewById(R.id.userId);

        enroll = rootView.findViewById(R.id.enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                onClickEnroll();
            }
        });

        return rootView;
    }

    public void setMainActivity(SpMainActivity activity) {
        this.activity = activity;
        this.handler = new Handler();
    }

    private synchronized void onClickEnroll() {
        enroll.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enroll.setEnabled(true);
            }
        }, 500);

        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String userIdStr = userId.getText().toString();

        if (TextUtils.isEmpty(firstNameStr)) {
            Toast.makeText(activity, R.string.error_msg_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        ProviderUtil.deleteAllUsers(activity.getBaseContext(), BuildConfig.APPLICATION_ID);

        UserInfo userInfo;
        if (ComTextUtils.isEmptyOrWhitespace(userIdStr)) {
            userInfo = UserInfo.enrollNewUser(
                    activity.getBaseContext(),
                    BuildConfig.APPLICATION_ID,
                    firstNameStr,
                    lastNameStr,
                    new HashMap<String, String>());
        } else {
            userInfo = UserInfo.enrollUser(
                    activity.getBaseContext(),
                    BuildConfig.APPLICATION_ID,
                    userIdStr,
                    firstNameStr,
                    lastNameStr,
                    new HashMap<String, String>());
        }

        activity.startEnroll(userInfo.userId);
        dismiss();
    }
}