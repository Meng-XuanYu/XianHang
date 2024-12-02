package model;

import android.hardware.camera2.CameraExtensionSession;

import java.util.List;

public class AIRecommendResponse {
    private String status;
    private String message;
    private List<Recommendation> recommendations;

    public String getStatus() {
        return status;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public static class Recommendation {
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

        public String getSellerId(){
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

    public String getMessage(){
        return message;
    }
}
