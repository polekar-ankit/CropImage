package com.gipl.imagepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Creted by User on 25-Jan-19
 */
public class ImagePickerDialog extends DialogFragment {
    private Context context;
    private ImagePicker imagePicker;
    private ImagePicker.IImagePickerResult iImagePickerResult;
    private LinearLayout llOpenCamera, llOpenGallery;
    private TextView tvCancel;
    private PickerConfiguration pickerConfiguration;

    public static ImagePickerDialog display(FragmentManager fragmentManager,
                               PickerConfiguration pickerConfiguration) {
        ImagePickerDialog newFragment = new ImagePickerDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("pickerConfig", pickerConfiguration);
        newFragment.setArguments(bundle);
        newFragment.show(fragmentManager, "");
        return newFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof ImagePicker.IImagePickerResult) {
            iImagePickerResult = (ImagePicker.IImagePickerResult) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePicker(context)
                .setDIRECTORY("AppSample")
                .setIMAGE_PATH("AppImages")
                .setStoreInMyPath(true);
        imagePicker.setFragment(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pickerConfiguration = getArguments().getParcelable("pickerConfig");

        imagePicker.setiImagePickerResult(iImagePickerResult);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        if (pickerConfiguration.isIsSetCustomDialog()) {
            View view = inflater.inflate(R.layout.layout_custom_image_picker, null);
            builder.setView(view);
            setCustomView(view);
            setViewConfig(view);
        } else
            builder.setTitle("Image Picker")
                    .setItems(R.array.dialog_menus_image_picker, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            switch (position) {
                                case 0:
                                    imagePicker.openCamera();
                                    break;
                                case 1:
                                    imagePicker.startGallary();
                                    break;
                                case 2:
                                    dismiss();
                                    break;
                            }
                        }
                    });
        builder.setCancelable(pickerConfiguration.isfIsDialogCancelable());
        return builder.create();
    }

    private void setViewConfig(View view) {

        DrawableCompat.setTint(((ImageView) view.findViewById(R.id.iv_camera)).getDrawable(), pickerConfiguration.getColorCode());
        ((TextView) view.findViewById(R.id.tv_camera)).setTextColor(pickerConfiguration.getColorCode());

        DrawableCompat.setTint(((ImageView) view.findViewById(R.id.iv_galray)).getDrawable(), pickerConfiguration.getColorCode());
        ((TextView) view.findViewById(R.id.tv_gallery)).setTextColor(pickerConfiguration.getColorCode());

        ((TextView) view.findViewById(R.id.tv_cancel)).setTextColor(pickerConfiguration.getColorCode());

        view.setBackgroundColor(pickerConfiguration.getBackGroundColor());
    }

    private void setCustomView(View view) {
        llOpenCamera = view.findViewById(R.id.ll_camera);
        llOpenGallery = view.findViewById(R.id.ll_gallery);
        tvCancel = view.findViewById(R.id.tv_cancel);

        llOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.startGallary();
                dismiss();
            }
        });
        llOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.openCamera();
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
