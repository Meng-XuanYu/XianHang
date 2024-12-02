package model;

public class DeleteCollectionRequest {
    private String userId;
    private String commodityId;

    public DeleteCollectionRequest(String userId,String commodityId){
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
