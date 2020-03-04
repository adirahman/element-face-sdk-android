package com.element.cmex;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.element.common.PermissionUtils;

public class CmMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        CardCaptureFormFragment cardCaptureFormFragment = new CardCaptureFormFragment();
        cardCaptureFormFragment.setMainActivity(CmMainActivity.this);
        cardCaptureFormFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PermissionUtils.verifyPermissions(
                this,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public void start(String userId) {
        Intent intent = new Intent(CmMainActivity.this, CmActivity.class);
        intent.putExtra(CmActivity.EXTRA_ELEMENT_USER_ID, userId);
        startActivity(intent);
        finish();
    }
}