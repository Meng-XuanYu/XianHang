package model;

public class AddCommodityCommentRequest {
    private String commodityId;
    private String userId;
    private String commentContent;

    public AddCommodityCommentRequest(String commodityId, String userId,String commentContent) {
        this.commodityId = commodityId;
        this.userId = userId;
        this.commentContent = commentContent;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
