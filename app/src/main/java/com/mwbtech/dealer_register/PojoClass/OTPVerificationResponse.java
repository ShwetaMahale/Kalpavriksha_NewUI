package com.mwbtech.dealer_register.PojoClass;

public class OTPVerificationResponse {
        private String OTPStatus;

        public boolean isOTPVerified() {
                return OTPStatus.equalsIgnoreCase("OTP Verified");
        }

        public String getOTPStatus() {
                return OTPStatus;
        }

        public void setOTPStatus(String OTPStatus) {
                this.OTPStatus = OTPStatus;
        }
}

