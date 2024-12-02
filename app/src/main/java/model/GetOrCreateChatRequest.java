package model;

public class GetOrCreateChatRequest {
    private String userId;
    private String receiverId;
    private String commodityId;

    public GetOrCreateChatRequest(String userId, String receiverId, String commodityId) {
        this.userId = userId;
        this.receiverId = receiverId;
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getCommodityId() {
        return commodityId;
    }
}
