package com.gipl.imagepicker;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creted by User on 25-Jan-19
 */
public class PickerConfiguration implements Parcelable {
    public static final Creator<PickerConfiguration> CREATOR = new Creator<PickerConfiguration>() {
        @Override
        public PickerConfiguration createFromParcel(Parcel in) {
            return new PickerConfiguration(in);
        }

        @Override
        public PickerConfiguration[] newArray(int size) {
            return new PickerConfiguration[size];
        }
    };
    private boolean fIsSetCustomDialog;
    private int colorCodeText;
    private int colorCodeIcon;
    private boolean fIsDialogCancelable;
    private int nBackGroundColor;
    private int cameraImageId;
    private int galleryImageId;

    public String getsGalleryTitle() {
        return sGalleryTitle;
    }

    private String sCameraTitle;
    private String sGalleryTitle;

    public PickerConfiguration() {
        colorCodeText = Color.BLACK;
        nBackGroundColor = Color.WHITE;
        fIsSetCustomDialog = false;
        fIsDialogCancelable = true;
        cameraImageId = -1;
        galleryImageId = -1;
        sCameraTitle ="";
        sGalleryTitle ="";

    }

    protected PickerConfiguration(Parcel in) {
        fIsSetCustomDialog = in.readByte() != 0;
        colorCodeText = in.readInt();
        fIsDialogCancelable = in.readByte() != 0;
        nBackGroundColor = in.readInt();
        colorCodeIcon = in.readInt();
        cameraImageId = in.readInt();
        galleryImageId = in.readInt();
        sCameraTitle = in.readString();
        sGalleryTitle = in.readString();
    }

    public static PickerConfiguration build() {
        return new PickerConfiguration();
    }

    public int getBackGroundColor() {
        return nBackGroundColor;
    }

    public PickerConfiguration setBackGroundColor(int nBackGroundColor) {
        this.nBackGroundColor = nBackGroundColor;
        return this;
    }

    boolean isIsSetCustomDialog() {
        return fIsSetCustomDialog;
    }

    public PickerConfiguration setSetCustomDialog(boolean fIsSetCustomDialog) {
        this.fIsSetCustomDialog = fIsSetCustomDialog;
        return this;
    }

    int getTextColor() {
        return colorCodeText;
    }

    public PickerConfiguration setTextColor(int colorCode) {
        this.colorCodeText = colorCode;
        return this;
    }

    boolean isfIsDialogCancelable() {
        return fIsDialogCancelable;
    }

    public PickerConfiguration setIsDialogCancelable(boolean fIsDialogCancelable) {
        this.fIsDialogCancelable = fIsDialogCancelable;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (fIsSetCustomDialog ? 1 : 0));
        parcel.writeInt(colorCodeText);
        parcel.writeByte((byte) (fIsDialogCancelable ? 1 : 0));
        parcel.writeInt(nBackGroundColor);
        parcel.writeInt(colorCodeIcon);
        parcel.writeInt(cameraImageId);
        parcel.writeInt(galleryImageId);
        parcel.writeString(sCameraTitle);
        parcel.writeString(sGalleryTitle);
    }

    public int getIconColor() {
        return colorCodeIcon;
    }

    public PickerConfiguration setIconColor(int colorCodeIcon) {
        this.colorCodeIcon = colorCodeIcon;
        return this;
    }

    public int getCameraImageId() {
        return cameraImageId;
    }

    public PickerConfiguration setCameraImageId(int cameraImageId) {
        this.cameraImageId = cameraImageId;
        return this;
    }

    public int getGalleryImageId() {
        return galleryImageId;
    }

    public PickerConfiguration setGalleryImageId(int galleryImageId) {
        this.galleryImageId = galleryImageId;
        return this;
    }

    public String getCameraTitle() {
        return sCameraTitle;
    }

    public PickerConfiguration setCameraTitle(String sCameraTitle) {
        this.sCameraTitle = sCameraTitle;
        return this;
    }

    public String getGalleryTitle() {
        return sGalleryTitle;
    }

    public PickerConfiguration setGalleryTitle(String sGalleryTitle) {
        this.sGalleryTitle = sGalleryTitle;
        return this;
    }
}
