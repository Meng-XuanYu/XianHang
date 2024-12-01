package model;

import java.util.List;

public class SearchCommodityResponse {
    private String status;
    private List<Commodity> commodities;

    public String getStatus() {
        return status;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private Double commodityValue;
        private String commodityImage;

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

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
