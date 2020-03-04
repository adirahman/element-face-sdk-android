package com.element.cmex;

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

public class CmResultFragment extends DialogFragment {
    private String message;

    private int iconResId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cm_result, container, false);

        TextView confirmText = rootView.findViewById(R.id.confirmText);
        ImageView confirmImage = rootView.findViewById(R.id.confirmImage);

        confirmText.setText(message);
        confirmImage.setImageResource(iconResId);

        Button retryButton = rootView.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof CmActivity) {
                    ((CmActivity) getActivity()).recapture();
                }
            }
        });
        Button exitButton = rootView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return rootView;
    }

    void setData(String message, int iconResId) {
        this.message = message;
        this.iconResId = iconResId;
    }

}
