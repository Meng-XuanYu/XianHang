package model;

import java.nio.channels.Pipe;
import java.util.List;

public class AISearchResponse {
    private String status;
    private List<Commodity> result;
    private String message;

    public String getStatus() {
        return status;
    }

    public List<Commodity> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private String commodityValue;
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

        public String getCommodityValue() {
            return "￥" + commodityValue;
        }

        public String getCommodityImage() {
            return commodityImage;
        }

    }
}
