package com.xteam.warehouse.ascii.discount.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data transport object for the Ascii product
 * <p>
 * Created by Wraith on 7/2/2016.
 */

public class AsciiProductDTO implements Parcelable {

    @SerializedName("type")
    public String mType;

    @SerializedName("id")
    public String mID;

    @SerializedName("size")
    public long mSize;

    @SerializedName("price")
    public double mPrice;

    @SerializedName("face")
    public String mFace;

    @SerializedName("stock")
    public long mStock;

    @SerializedName("tags")
    public String[] mTags;


    public static final Creator<AsciiProductDTO> CREATOR = new Creator<AsciiProductDTO>() {
        @Override
        public AsciiProductDTO createFromParcel(Parcel in) {
            return new AsciiProductDTO(in);
        }

        @Override
        public AsciiProductDTO[] newArray(int size) {
            return new AsciiProductDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public AsciiProductDTO(Parcel in){
        mType = in.readString();
        mID = in.readString();
        mSize = in.readLong();
        mPrice = in.readDouble();
        mFace = in.readString();
        mStock = in.readLong();
        mTags = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mType);
        parcel.writeString(mID);
        parcel.writeLong(mSize);
        parcel.writeDouble(mPrice);
        parcel.writeString(mFace);
        parcel.writeLong(mStock);
        parcel.writeStringArray(mTags);
    }


}
