package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class ProfessionalReqModel {

    @SerializedName("ID")
    private int ProfessionalRequirementID;

    @SerializedName("RequirementName")
    private String RequirementName;
    @SerializedName("Selected")
    private String selectedImage = "";
    @SerializedName("UnSelected")
    private String unSelectedImage = "";
    boolean isChecked = false;

    public ProfessionalReqModel(String requirementName,int professionalRequirementID) {
        ProfessionalRequirementID = professionalRequirementID;
        RequirementName = requirementName;
    }

    public int getProfessionalRequirementID() {
        return ProfessionalRequirementID;
    }

    public void setProfessionalRequirementID(int professionalRequirementID) {
        ProfessionalRequirementID = professionalRequirementID;
    }

    public String getRequirementName() {
        if(RequirementName==null)
            return "";
        return RequirementName;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setRequirementName(String requirementName) {
        RequirementName = requirementName;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public String getUnSelectedImage() {
        return unSelectedImage;
    }
}
