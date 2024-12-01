package model;

public class GetTradeCommentResponse {
    private String status;
    private String message;
    private CommentData comment;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public CommentData getComment() {
        return comment;
    }

    public static class CommentData {
        private String commentContent;
        private int star;

        public String getCommentContent() {
            return commentContent;
        }

        public int getStar() {
            return star;
        }
    }
}
