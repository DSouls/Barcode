package com.alex.atz.snapshot;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alex.atz.snapshot.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/12.
 */

public class CustomScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {
    @BindView(R.id.btn_switch) Button switchLight;
    @BindView(R.id.btn_hint1) Button hint1Show;
    @BindView(R.id.btn_hint2) Button hint2Show;
    @BindView(R.id.dbv_custom) DecoratedBarcodeView mDBV;

    private CaptureManager mCaptureManager;
    private boolean isLightOn = false;

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistableState) {
        super.onSaveInstanceState(outState, outPersistableState);
        mCaptureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scan_layout);
        ButterKnife.bind(this);

        mDBV.setTorchListener(this);

        if (!hasFlash()) {
            switchLight.setVisibility(View.GONE);
        }

        mCaptureManager = new CaptureManager(this, mDBV);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mCaptureManager.decode();
    }

    @Override
    public void onTorchOn() {
        Toast.makeText(this, "Torch is on.", Toast.LENGTH_SHORT).show();
        isLightOn = true;
    }
    @Override
    public void onTorchOff() {
        Toast.makeText(this, "Torch is off.", Toast.LENGTH_SHORT).show();
        isLightOn = false;
    }
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @OnClick(R.id.btn_switch)
    public void swichLight() {
        if(isLightOn) {
            mDBV.setTorchOff();
        } else {
            mDBV.setTorchOn();
        }
    }
}
