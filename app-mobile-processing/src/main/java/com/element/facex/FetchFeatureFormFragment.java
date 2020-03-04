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

import com.element.camera.ProviderUtil;

public class FetchFeatureFormFragment extends DialogFragment {

    private MainActivity mainActivity;

    private Handler handler;

    private EditText userId;

    private Button fetch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fetch_feature_form, container, false);

        userId = rootView.findViewById(R.id.userId);

        fetch = rootView.findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                onClickEnroll();
            }
        });

        return rootView;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.handler = new Handler();
    }

    private synchronized void onClickEnroll() {
        fetch.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch.setEnabled(true);
            }
        }, 500);

        String userIdStr = userId.getText().toString();

        if (TextUtils.isEmpty(userIdStr)) {
            Toast.makeText(mainActivity, R.string.error_msg_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        ProviderUtil.deleteAllUsers(mainActivity.getBaseContext(), BuildConfig.APPLICATION_ID);

        mainActivity.startFetch(userIdStr);
        dismiss();
    }
}