package model;

import java.util.Map;

public class GetCommodityResponse {
    private String status;
    private Commodity commodity;

    public String getStatus() {
        return status;
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

        public String getCommodityKeywords() {
            return commodityKeywords;
        }

        public String getCommodityClass() {
            return commodityClass;
        }

        public String getCommodityImage1() {
            return commodityImage1;
        }

        public Map<String, String> getAdditionalImages() {
            return additionalImages;
        }
    }
}
