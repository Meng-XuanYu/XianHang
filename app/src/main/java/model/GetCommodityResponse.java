package model;

import java.util.ArrayList;
import java.util.Map;

public class GetCommodityResponse {
    private String status;
    private String message;
    private Commodity commodity;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;

        private String commodityDescription;
        private Double commodityValue;
        private String commodityKeywords;
        private String commodityClass;
        private String commodityImage1;
        private Map<String, String> additionalImages;
        private String school;

        private String sellerName;
        private String sellerImage;
        private String currentTradeId;

        public String getCurrentTradeId() {
            return currentTradeId;
        }

        public String getSchool() {
            return school;
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
            return "￥"+commodityValue;
        }

        public String getCommodityKeywords() {
            return commodityKeywords;
        }

        public String getCommodityClass() {
            return commodityClass;
        }

        public String getCommodityImage1() {
            return commodityImage1;
        }

        public ArrayList<String> getAdditionalImages() {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(commodityImage1);
            if(additionalImages!=null){
                tmp.addAll(additionalImages.values());
            }
            return tmp;
        }
    }
}
