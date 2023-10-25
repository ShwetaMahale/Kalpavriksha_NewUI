package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class AdvertisementTypeModel {

    @SerializedName("ID")
    private int adTypeId;

    @SerializedName("Type")
    private String AdvertisementType;

    @SerializedName("Description")
    private String Description;

    @SerializedName("Selected")
    private String selectedImage;

    @SerializedName("UnSelected")
    private String unselectedImage;

    public AdvertisementTypeModel(int adTypeId, String advertisementType) {
        this.adTypeId = adTypeId;
        AdvertisementType = advertisementType;
    }

    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getAdTypeId() {
        return adTypeId;
    }

    public void setAdTypeId(int adTypeId) {
        this.adTypeId = adTypeId;
    }

    public String getAdvertisementType() {
        return AdvertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        AdvertisementType = advertisementType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
}
