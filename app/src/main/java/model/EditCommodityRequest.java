package model;

import java.util.List;

import okhttp3.MultipartBody;

public class EditCommodityRequest {

    private String commodityId;
    private String commodityName;
    private String commodityDescription;
    private String commodityValue;
    private String commodityKeywords;
    private String commodityClass;
    private List<MultipartBody.Part> images;

    public EditCommodityRequest(String commodityId, String commodityName, String commodityDescription, String commodityValue, String commodityKeywords, String commodityClass, List<MultipartBody.Part> images) {
        this.commodityId = commodityId;
        this.commodityName = commodityName;
        this.commodityDescription = commodityDescription;
        this.commodityValue = commodityValue;
        this.commodityKeywords = commodityKeywords;
        this.commodityClass = commodityClass;
        this.images = images;
    }

    // Getters and setters
    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public String getCommodityValue() {
        return commodityValue;
    }

    public void setCommodityValue(String commodityValue) {
        this.commodityValue = commodityValue;
    }

    public String getCommodityKeywords() {
        return commodityKeywords;
    }

    public void setCommodityKeywords(String commodityKeywords) {
        this.commodityKeywords = commodityKeywords;
    }

    public String getCommodityClass() {
        return commodityClass;
    }

    public void setCommodityClass(String commodityClass) {
        this.commodityClass = commodityClass;
    }

    public List<MultipartBody.Part> getImages() {
        return images;
    }

    public void setImages(List<MultipartBody.Part> images) {
        this.images = images;
    }
}
