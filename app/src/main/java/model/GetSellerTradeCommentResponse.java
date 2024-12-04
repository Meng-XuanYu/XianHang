package model;

import java.util.List;

public class GetSellerTradeCommentResponse {
    private String status;
    private String message;
    private List<CommentData> comments;

    public String getStatus() {
        return status;
    }

    public List<CommentData> getComments() {
        return comments;
    }

    public String getMessage() {
        return message;
    }

    public static class CommentData {
        private String tradeId;
        private String tradeStatus;
        private String commodityId;
        private String commodityName;
        private String commodityDescription;
        private String commodityValue;
        private String commodityClass;
        private String commodityImage;
        private String buyerId;
        private String buyerComment;
        private String buyerName;
        private String buyerImage;
        private String buyerSchool;

        // Getters

        public String getBuyerImage() {
            return buyerImage;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public String getBuyerSchool() {
            return buyerSchool;
        }

        public String getTradeId() { return tradeId; }
        public String getTradeStatus() { return tradeStatus; }
        public String getCommodityId() { return commodityId; }
        public String getCommodityName() { return commodityName; }
        public String getCommodityDescription() { return commodityDescription; }
        public String getCommodityValue() { return commodityValue; }
        public String getCommodityClass() { return commodityClass; }
        public String getCommodityImage() { return commodityImage; }
        public String getBuyerId() { return buyerId; }
        public String getBuyerComment() { return buyerComment; }
    }
}
