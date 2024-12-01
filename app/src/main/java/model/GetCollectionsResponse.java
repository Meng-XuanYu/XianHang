package model;

import java.util.List;

public class GetCollectionsResponse {
    private String status;
    private String message;
    private List<Commodity> collections;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Commodity> getCollections() {
        return collections;
    }

    public static class Commodity {
        private String sellerId;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private Double commodityValue;
        private String commodityClass;
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

        public String getCommodityClass() {
            return commodityClass;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
