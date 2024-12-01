package model;

public class AddCollectionRequest {
    private String userId;
    private String commodityId;

    public AddCollectionRequest(String userId, String commodityId) {
        this.userId = userId;
        this.commodityId = commodityId;
    }
}
