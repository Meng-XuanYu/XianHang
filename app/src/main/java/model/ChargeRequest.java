package model;

public class ChargeRequest {
    private String userId;
    private Double money;

    public ChargeRequest(String userId, Double money) {
        this.userId = userId;
        this.money = money;
    }
}
