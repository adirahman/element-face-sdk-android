package com.element.facex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultFragment extends DialogFragment {

    private String message;

    private int iconResId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_auth_result, container, false);

        TextView confirmText = rootView.findViewById(R.id.confirmText);
        ImageView confirmImage = rootView.findViewById(R.id.confirmImage);

        confirmText.setText(message);
        confirmImage.setImageResource(iconResId);

        Button retryButton = rootView.findViewById(R.id.retryButton);
        retryButton.setVisibility(getActivity() instanceof ServerAuthActivity ? View.VISIBLE : View.GONE);
        retryButton.setOnClickListener(view -> {
            if (getActivity() instanceof ServerAuthActivity) {
                ((ServerAuthActivity) getActivity()).recapture();
            }
        });

        Button exitButton = rootView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return rootView;
    }

    void setData(String message, int iconResId) {
        this.message = message;
        this.iconResId = iconResId;
    }
}