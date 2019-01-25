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
    private int colorCode;
    private boolean fIsDialogCancelable;
    private int nBackGroundColor;

    public PickerConfiguration() {
        colorCode = Color.BLACK;
        nBackGroundColor = Color.WHITE;
        fIsSetCustomDialog = false;
        fIsDialogCancelable = true;
    }

    protected PickerConfiguration(Parcel in) {
        fIsSetCustomDialog = in.readByte() != 0;
        colorCode = in.readInt();
        fIsDialogCancelable = in.readByte() != 0;
        nBackGroundColor = in.readInt();
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

    int getColorCode() {
        return colorCode;
    }

    public PickerConfiguration setColorCode(int colorCode) {
        this.colorCode = colorCode;
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
        parcel.writeInt(colorCode);
        parcel.writeByte((byte) (fIsDialogCancelable ? 1 : 0));
        parcel.writeInt(nBackGroundColor);
    }
}
