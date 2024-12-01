package model;

public class AddCommodityCommentRequest {
    private String commodityId;
    private String commentContent;

    public AddCommodityCommentRequest(String commodityId, String commentContent) {
        this.commodityId = commodityId;
        this.commentContent = commentContent;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
