package model;

import java.util.List;

public class GetMyPublishResponse {
    private String status;
    private List<Commodity> commodities;

    public String getStatus() {
        return status;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public static class Commodity {
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private int commodityValue;
        private String commodityClass;
        private String commodityImage;
        private String commodityStatus;

        // Getters
        public String getCommodityId() { return commodityId; }
        public String getCommodityName() { return commodityName; }
        public String getCommodityDescription() { return commodityDescription; }
        public int getCommodityValue() { return commodityValue; }
        public String getCommodityClass() { return commodityClass; }
        public String getCommodityImage() { return commodityImage; }
        public String getCommodityStatus() { return commodityStatus; }
    }
}
