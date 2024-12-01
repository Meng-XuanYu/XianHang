package model;

import java.util.List;

public class AISearchResponse {
    private String status;
    private List<Commodity> result;

    public String getStatus() {
        return status;
    }

    public List<Commodity> getResult() {
        return result;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private int commodityValue;
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

        public int getCommodityValue() {
            return commodityValue;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
