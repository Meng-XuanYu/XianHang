package model;

import java.util.List;

public class GetHistoryResponse {
    private String status;
    private String message;
    private List<Commodity> history;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Commodity> getHistory() {
        return history;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private Double commodityValue;
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

        public String getSellerId() {
            return sellerId;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public String getCommodityDescription() {
            return commodityDescription;
        }

        public Double getCommodityValue() {
            return commodityValue;
        }

        public String getCommodityClass() {
            return commodityClass;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
