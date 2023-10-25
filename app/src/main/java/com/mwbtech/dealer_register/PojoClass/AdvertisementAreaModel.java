package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class AdvertisementAreaModel {

    @SerializedName("ID")
    private int AdvertisementAreaID;

    @SerializedName("Name")
    private String AdvertisementAreaName;

    @SerializedName("Selected")
    private String selectedImage;

    @SerializedName("UnSelected")
    private String unselectedImage;

    @SerializedName("AdAreaMatrix")
    private double AdAreaMatrix;

    private boolean isChecked=false;

    public AdvertisementAreaModel(int advertisementAreaID, String advertisementAreaName) {
        AdvertisementAreaID = advertisementAreaID;
        AdvertisementAreaName = advertisementAreaName;
    }

    public AdvertisementAreaModel(int advertisementAreaID, String advertisementAreaName, String selectedImage, String unselectedImage, double adAreaMatrix) {
        AdvertisementAreaID = advertisementAreaID;
        AdvertisementAreaName = advertisementAreaName;
        this.selectedImage = selectedImage;
        this.unselectedImage = unselectedImage;
        AdAreaMatrix = adAreaMatrix;
    }

    public AdvertisementAreaModel(int advertisementAreaID, String advertisementAreaName, double adAreaMatrix) {
        AdvertisementAreaID = advertisementAreaID;
        AdvertisementAreaName = advertisementAreaName;
        AdAreaMatrix = adAreaMatrix;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getAdvertisementAreaID() {
        return AdvertisementAreaID;
    }

    public void setAdvertisementAreaID(int advertisementAreaID) {
        AdvertisementAreaID = advertisementAreaID;
    }

    public String getAdvertisementAreaName() {
        return AdvertisementAreaName;
    }

    public void setAdvertisementAreaName(String advertisementAreaName) {
        AdvertisementAreaName = advertisementAreaName;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getUnselectedImage() {
        return unselectedImage;
    }

    public void setUnselectedImage(String unselectedImage) {
        this.unselectedImage = unselectedImage;
    }

    public double getAdAreaMatrix() {
        return AdAreaMatrix;
    }

    public void setAdAreaMatrix(double adAreaMatrix) {
        AdAreaMatrix = adAreaMatrix;
    }
}
