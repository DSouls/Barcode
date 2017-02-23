package com.alex.atz.snapshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RC = 0;
    @BindView(R.id.content) EditText editText;
    @BindView(R.id.generate_btn) Button generateButton;
    @BindView(R.id.scan_btn) Button scanButton;
    @BindView(R.id.img_view) ImageView imageView;

    public void customScan(){
        new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(CustomScanActivity.class).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode,resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        generateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                String str = editText.getText().toString();
                EncodeAsBitmap encodeAsBitmap = new EncodeAsBitmap();
                Bitmap bmp = encodeAsBitmap.encodeAsBitmap(str);
                imageView.setImageBitmap(bmp);
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i = new Intent(MainActivity.this, CustomScanActivity.class);
                startActivityForResult(i, RC);
            }
        });
    }

}
