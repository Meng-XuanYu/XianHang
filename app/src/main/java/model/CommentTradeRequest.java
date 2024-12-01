package model;

public class CommentTradeRequest {
    private String tradeId;
    private int star;
    private String commentContent;

    public CommentTradeRequest(String tradeId, int star, String commentContent) {
        this.tradeId = tradeId;
        this.star = star;
        this.commentContent = commentContent;
    }
}
