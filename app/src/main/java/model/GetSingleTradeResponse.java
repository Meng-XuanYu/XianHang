package model;

public class GetSingleTradeResponse {
    private String status;
    private String message;
    private Trade trade;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Trade getTrade() {
        return trade;
    }

    public static class Trade {
        private String tradeId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private Double commodityValue;
        private String commodityImage;
        private String sellerId;
        private String tradeStatus;

        // Getters
        public String getTradeId() { return tradeId; }
        public String getCommodityId() { return commodityId; }
        public String getCommodityName() { return commodityName; }
        public String getCommodityDescription() { return commodityDescription; }
        public String getCommodityValue() { return commodityValue.toString(); }
        public String getCommodityImage() { return commodityImage; }
        public String getSellerId() { return sellerId; }
        public String getTradeStatus() { return tradeStatus; }
    }
}
