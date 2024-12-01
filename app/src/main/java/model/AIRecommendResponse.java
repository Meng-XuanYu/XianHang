package model;

import java.util.List;

public class AIRecommendResponse {
    private String status;
    private List<Recommendation> recommendations;

    public String getStatus() {
        return status;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public static class Recommendation {
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private String commodityValue;
        private String commodityImage;

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
            return commodityValue;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
