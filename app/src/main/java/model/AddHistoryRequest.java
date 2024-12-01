package model;

public class AddHistoryRequest {
    private String userId;
    private String commodityId;

    public AddHistoryRequest(String userId, String commodityId) {
        this.userId = userId;
        this.commodityId = commodityId;
    }
}
