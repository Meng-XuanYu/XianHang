package model;

public class BuyCommodityRequest {
    private String userId;
    private String commodityId;

    public BuyCommodityRequest(String userId, String commodityId) {
        this.userId = userId;
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCommodityId() {
        return commodityId;
    }
}
