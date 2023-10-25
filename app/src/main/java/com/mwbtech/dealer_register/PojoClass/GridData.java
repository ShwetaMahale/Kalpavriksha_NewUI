package com.mwbtech.dealer_register.PojoClass;


public class GridData {


    int gridId;
    String gridName;
    int imageView;

    public GridData(int gridId, String gridName) {
        this.gridId = gridId;
        this.gridName = gridName;
    }

    public GridData(int gridId, String gridName, int imageView) {
        this.gridId = gridId;
        this.gridName = gridName;
        this.imageView = imageView;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }


    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    @Override
    public String toString() {
        return "GridData{" +
                "gridId=" + gridId +
                ", gridName='" + gridName + '\'' +
                '}';
    }
}
