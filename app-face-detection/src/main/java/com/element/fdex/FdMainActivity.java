package com.element.fdex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.element.common.PermissionUtils;

public class FdMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        FaceDetectFormFragment faceDetectFormFragment = new FaceDetectFormFragment();
        faceDetectFormFragment.setMainActivity(FdMainActivity.this);
        faceDetectFormFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionUtils.isGranted(getBaseContext(), android.Manifest.permission.CAMERA)) {
            Toast.makeText(getBaseContext(), "Please grant all permissions in Settings -> Apps", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void start(String userId) {
        Intent intent = new Intent(FdMainActivity.this, FdActivity.class);
        intent.putExtra(FdActivity.EXTRA_ELEMENT_USER_ID, userId);
        startActivity(intent);
        finish();
    }
}