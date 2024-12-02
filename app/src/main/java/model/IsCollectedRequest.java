package model;

public class IsCollectedRequest {
    private String userId;
    private String commodityId;

    public IsCollectedRequest(String userId,String commodityId){
        this.commodityId = commodityId;
        this.userId = userId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getUserId() {
        return userId;
    }
}
