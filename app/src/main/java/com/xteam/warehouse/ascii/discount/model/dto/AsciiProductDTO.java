package com.xteam.warehouse.ascii.discount.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Data transport object for the Ascii product
 * <p>
 * Created by Wraith on 7/2/2016.
 */

public class AsciiProductDTO  {

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




}
