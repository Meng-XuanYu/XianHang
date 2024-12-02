package model;

import java.util.List;

public class GetMyBuyResponse {
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
        private String sellerId;
        private String tradeId;
        private String tradeStatus;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private int commodityValue;
        private String commodityClass;
        private String commodityImage;

        private String sellerName;
        private String sellerImage;
        private String sellerAttractiveness;

        public String getSellerAttractiveness() {
            return sellerAttractiveness;
        }

        public String getSellerImage() {
            return sellerImage;
        }

        public String getSellerName() {
            return sellerName;
        }

        // Getters
        public String getSellerId() { return sellerId; }
        public String getTradeId() { return tradeId; }
        public String getTradeStatus() { return tradeStatus; }
        public String getCommodityId() { return commodityId; }
        public String getCommodityName() { return commodityName; }
        public String getCommodityDescription() { return commodityDescription; }
        public String getCommodityValue() { return "￥" + commodityValue; }
        public String getCommodityClass() { return commodityClass; }
        public String getCommodityImage() { return commodityImage; }
    }
}
