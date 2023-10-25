package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("ID")
    int StateID;
    @SerializedName("StateName")
    String StateName;

    private boolean isChecked=false;


    public State(int stateID, String stateName, boolean isChecked) {
        StateID = stateID;
        StateName = stateName;
        this.isChecked = isChecked;
    }


    public State(int stateID) {
        StateID = stateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public State(int stateID, String stateName) {
        StateID = stateID;
        StateName = stateName;
    }

    public int getStateID() {
        return StateID;
    }

    public String getStateName() {
        return StateName;
    }

    @Override
    public String toString() {
        return StateName;
    }

}
