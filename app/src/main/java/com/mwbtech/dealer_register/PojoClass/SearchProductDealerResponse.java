package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProductDealerResponse {


    @SerializedName("PageNumber")
    private int pageNumber;

    @SerializedName("PageSize")
    private int pageSize;

    @SerializedName("TotalPages")
    private int totalPages;

    @SerializedName("TotalRecords")
    private int totalRecords;

    @SerializedName("Data")
    private List<SearchProductDealer> data;

    public SearchProductDealerResponse(int pageNumber, int pageSize, int totalPages, int totalRecords, List<SearchProductDealer> data) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<SearchProductDealer> getData() {
        return data;
    }

    public void setData(List<SearchProductDealer> data) {
        this.data = data;
    }
}