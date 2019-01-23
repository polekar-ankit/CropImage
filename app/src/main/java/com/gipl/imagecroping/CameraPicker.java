package com.gipl.imagecroping;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.gipl.imagecroping.MediaUtility.PROFILE_PHOTO;

/**
 * Creted by User on 17-Jan-19
 */
public class CameraPicker {

    private static final int CAMERA_REQUEST = 12;
    private static final int CAMERA_PERMISSION_REQUEST = 123;
    private boolean fStoreInMyPath = false;
    private String DIRECTORY = "";
    private String IMAGE_PATH = "";
    private Context activity;
    private Fragment fragment;
    private IImagePickerResult iImagePickerResult;
    private String sImgPath = "";

    CameraPicker(Context activity) {
        this.activity = activity;
    }

    CameraPicker setStoreInMyPath(boolean fStoreInMyPath) {
        this.fStoreInMyPath = fStoreInMyPath;
        return this;
    }

    CameraPicker setDIRECTORY(String DIRECTORY) {
        this.DIRECTORY = DIRECTORY;
        return this;
    }

    CameraPicker setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH = IMAGE_PATH;
        return this;
    }

    /**
     * If you pass directory name and image path then your can get String path as return
     * value other wise you will get BITMAP in onActivityResult
     *
     * @return String :  return complete image path if image path data is provided
     */
    void openCamera() {
        try {

            if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    if (fStoreInMyPath) {
                        if (isDirAndPathProvided()) {
                            File photoFile;
                            photoFile = MediaUtility.FILE.createImageFile(DIRECTORY, IMAGE_PATH);
                            sImgPath = photoFile.getAbsolutePath();
                            Uri photoURI;
                            if (Build.VERSION.SDK_INT >= 24) {
                                photoURI = FileProvider.getUriForFile(activity,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        photoFile);
                            } else {
                                photoURI = Uri.fromFile(photoFile);
                            }
                            // Continue only if the File was successfully created
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        } else {
                            iImagePickerResult.onError("Please provide Image Directory and Image path");
                        }
                    }
                    openCamera(takePictureIntent);

                }
            } else {
                startPermissonRequest();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void startGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (fragment != null) {
            fragment.startActivityForResult(intent, PROFILE_PHOTO);
            return;
        }

        ((AppCompatActivity) activity).startActivityForResult(intent, PROFILE_PHOTO);

    }

    private boolean isDirAndPathProvided() {
        return !DIRECTORY.isEmpty() && !IMAGE_PATH.isEmpty();
    }

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    Bitmap photo = null;
                    if (data != null)
                        photo = (Bitmap) data.getExtras().get("data");
                    if (iImagePickerResult != null)
                        iImagePickerResult.onImageGet(sImgPath, photo);

                    return;
                }
                if (requestCode == PROFILE_PHOTO) {
                    String sPath = MediaUtility.getFilePathFromUri(activity, data.getData());

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                    iImagePickerResult.onImageGet(sPath, bitmap);
                }

            } else {
                iImagePickerResult.onError("Unable get image try again");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        openCamera();
    }


    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    private void openCamera(Intent takePictureIntent) {
        if (fragment != null)
            fragment.startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        else
            ((Activity) activity).startActivityForResult(takePictureIntent, CAMERA_REQUEST);
    }

    private void startPermissonRequest() {
        if (fragment != null) {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
            return;
        }
        ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST);
    }

    CameraPicker setiImagePickerResult(IImagePickerResult iImagePickerResult) {
        this.iImagePickerResult = iImagePickerResult;
        return this;
    }

    public interface IImagePickerResult {
        void onImageGet(String sPath, Bitmap bitmap);

        void onError(String sErrorMessage);
    }
}
