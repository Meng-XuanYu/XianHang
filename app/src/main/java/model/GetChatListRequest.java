package model;

public class GetChatListRequest {
    private String userId;

    public GetChatListRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
