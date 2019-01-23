package com.gipl.imagecroping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gipl.cropper.cropper.CropImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private CropImageView cropImageView;
    private CameraPicker cameraPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cropImageView = findViewById(R.id.cropImageView);

        cameraPicker = new CameraPicker(this)
                .setDIRECTORY("AppSample")
                .setIMAGE_PATH("AppImages")
                .setStoreInMyPath(true)
                .setiImagePickerResult(new CameraPicker.IImagePickerResult() {
                    @Override
                    public void onImageGet(String sPath, Bitmap bitmap) {
                        if (!sPath.isEmpty()) {
                            cropImageView.setImageUriAsync(Uri.fromFile(new File(sPath)));
                            cropImageView.setScaleType(CropImageView.ScaleType.CENTER_INSIDE);


                        } else
                            cropImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(String sErrorMessage) {
                        Toast.makeText(MainActivity.this, sErrorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                if (result.isSuccessful())
                    cropImageView.setImageBitmap(result.getBitmap());
                else
                    cropImageView.setImageBitmap(cropImageView.getCroppedImage());
            }
        });
        findViewById(R.id.btn_open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPicker.openCamera();
            }
        });

        findViewById(R.id.btn_open_gallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPicker.startGallary();
            }
        });
        findViewById(R.id.btn_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.getCroppedImageAsync();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraPicker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraPicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
