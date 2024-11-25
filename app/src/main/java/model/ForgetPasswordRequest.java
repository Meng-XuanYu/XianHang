package model;

public class ForgetPasswordRequest {
    private String identity;
    private String phone;
    private String newPassword;

    public ForgetPasswordRequest(String identity, String phone, String newPassword) {
        this.identity = identity;
        this.phone = phone;
        this.newPassword = newPassword;
    }

    // Getters 和 Setters（如果需要）
}
