package com.mwbtech.dealer_register.Dashboard.Support;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prerna Sharma on 09-02-2023
 */
public class NewsArticle {

    @SerializedName("description")
    private String description;

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("pubDate")
    private String pubDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}