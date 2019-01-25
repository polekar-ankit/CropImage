package com.gipl.imagecroping;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gipl.cropper.cropper.CropImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private CropImageView cropImageView;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cropImageView = findViewById(R.id.cropImageView);

        imagePicker = new ImagePicker(this)
                .setDIRECTORY("AppSample")
                .setIMAGE_PATH("AppImages")
                .setStoreInMyPath(true)
                .setiImagePickerResult(new ImagePicker.IImagePickerResult() {
                    @Override
                    public void onImageGet(String sPath, Bitmap bitmap) {
                        if (!sPath.isEmpty()) {
                            cropImageView.setImageUriAsync(Uri.fromFile(new File(sPath)));
                            cropImageView.setScaleType(CropImageView.ScaleType.CENTER_INSIDE);


                        } else
                            cropImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(ImagePicker.CameraErrors cameraErrors) {
                        if (cameraErrors.getErrorType()== ImagePicker.CameraErrors.PERMISSION_ERROR){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setTitle("Camera permission deny!");
                            alertDialog.setMessage("Camera will be available after enabling Camera and Storage permission from setting");
                            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            alertDialog.show();
                        }
                        Toast.makeText(MainActivity.this, cameraErrors.getMessage(), Toast.LENGTH_SHORT).show();
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
                imagePicker.openCamera();
            }
        });

        findViewById(R.id.btn_open_gallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.startGallary();
            }
        });
        findViewById(R.id.btn_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.getCroppedImageAsync();
            }
        });
        findViewById(R.id.btn_crop_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropImageView.isUriProvided())
                    cropImageView.setImageUriAsync((Uri) cropImageView.getOriginalImage());
                else cropImageView.setImageBitmap((Bitmap) cropImageView.getOriginalImage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
