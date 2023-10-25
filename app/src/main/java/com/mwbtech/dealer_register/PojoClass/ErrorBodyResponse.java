package com.mwbtech.dealer_register.PojoClass;

public class ErrorBodyResponse {
    private String Message;

    private String DisplayMessage = "Server error";

    public String getDisplayMessage() {
        return DisplayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        DisplayMessage = displayMessage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
