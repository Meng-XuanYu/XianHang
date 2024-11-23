package model;

public class RegisterRequest {
    private String userId;
    private String password;

    public RegisterRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
