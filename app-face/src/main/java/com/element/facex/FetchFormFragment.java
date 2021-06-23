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

public class FetchFormFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fetch_feature_form, container, false);
        Button fetch = rootView.findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                clickFetch(fetch);
            }
        });
        return rootView;
    }

    private synchronized void clickFetch(Button button) {
        if (getActivity() == null || getView() == null) {
            return;
        }

        button.setEnabled(false);
        new Handler().postDelayed(() -> button.setEnabled(true), 500);

        String userId = ((EditText) getView().findViewById(R.id.userId)).getText().toString();

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(getActivity(), R.string.error_msg_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        ((FaceExMainActivity) getActivity()).startFetch(userId);
        dismiss();
    }
}