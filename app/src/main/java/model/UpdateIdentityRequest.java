package model;

public class UpdateIdentityRequest {
    private String userId;
    private String identity;

    public UpdateIdentityRequest(String userId, String identity) {
        this.userId = userId;
        this.identity = identity;
    }

    // Getters 和 Setters（如果需要）
}
