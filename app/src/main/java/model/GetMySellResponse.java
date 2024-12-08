package model;

import java.util.List;

public class GetMySellResponse {
    private String status;
    private String message;
    private List<Trade> trades;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public static class Trade {
        private String tradeId;
        private String tradeStatus;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private Double commodityValue;
        private String commodityClass;
        private String commodityImage;
        private String buyerId;

        // Getters
        public String getTradeId() { return tradeId; }
        public String getTradeStatus() { return tradeStatus; }
        public String getCommodityId() { return commodityId; }
        public String getCommodityName() { return commodityName; }
        public String getCommodityDescription() { return commodityDescription; }
        public String getCommodityValue() { return "￥" + commodityValue; }
        public String getCommodityClass() { return commodityClass; }
        public String getCommodityImage() { return commodityImage; }
        public String getBuyerId() { return buyerId; }
    }
}
