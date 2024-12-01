package model;

public class AIRecommendRequest {
    private String userId;

    public AIRecommendRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
