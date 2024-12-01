package model;

public class ReturnCommodityRequest {
    private String tradeId;

    public ReturnCommodityRequest(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeId() {
        return tradeId;
    }
}
