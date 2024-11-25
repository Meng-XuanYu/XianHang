package model;

public class UpdateGenderRequest {
    private String userId;
    private String gender;

    public UpdateGenderRequest(String userId, String gender) {
        this.userId = userId;
        this.gender = gender;
    }

    // Getters 和 Setters（如果需要）
}
