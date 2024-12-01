package model;

import java.util.List;

public class GetCommodityListByClassResponse {
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
        private double commodityValue;
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

        public double getCommodityValue() {
            return commodityValue;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
