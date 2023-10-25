package com.mwbtech.dealer_register.PojoClass;

public class VideoItems {

    private String VideoURL;
    private String VideoName;
    private String pdfurl;

    public VideoItems(String videoName,String videoURL) {
        VideoURL = videoURL;
        VideoName = videoName;
    }

    public VideoItems( String videoName, String videoURL,String pdfurl) {
        VideoURL = videoURL;
        VideoName = videoName;
        this.pdfurl = pdfurl;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}
