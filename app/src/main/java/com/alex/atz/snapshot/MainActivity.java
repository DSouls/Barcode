package com.alex.atz.snapshot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RC = 0;
    @BindView(R.id.content) EditText editText;
    @BindView(R.id.generate_btn) Button generateButton;
    @BindView(R.id.save_pic) Button saveButton;
    @BindView(R.id.scan_btn) Button scanButton;
    @BindView(R.id.img_view) ImageView imageView;
    @BindView(R.id.result_text_view) TextView resultTextView;

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dataDirectory", Environment.getExternalStorageState());
                String str = editText.getText().toString();
                EncodeAsBitmap encodeAsBitmap = new EncodeAsBitmap();
                Bitmap bmp = encodeAsBitmap.encodeAsBitmap(str);
                try {

                    saveBitmap(bmp, "Bar2");
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    private void saveBitmap(Bitmap bmp, String bitName) throws IOException {

        File dir = Environment.getExternalStorageDirectory();
        Log.d("dataDirectory", "External Storage Dir is " + dir.toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, bitName);
        Log.d("dataDirectory", "File path is " + file.toString());
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
            Log.d("dataDirectory", file.toString() + "已保存");
               out.flush();
               out.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("dataDirectory", "File not found exception");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dataDirectory", "IO exception");
        }


        try {
            MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), file.getAbsolutePath(), bitName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        Log.d("dataDirectory", "Broadcast sent.");
    }

}
