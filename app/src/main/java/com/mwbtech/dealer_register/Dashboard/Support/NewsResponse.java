package com.mwbtech.dealer_register.Dashboard.Support;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prerna Sharma on 10-02-2023
 */
public class NewsResponse {
    @SerializedName("item")
    private NewsArticle newsArticle;

    public NewsArticle getNewsArticle() {
        return newsArticle;
    }

    public void setNewsArticle(NewsArticle newsArticle) {
        this.newsArticle = newsArticle;
    }
}
