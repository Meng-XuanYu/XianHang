package model;

import java.util.List;

public class GetCommodityCommentResponse {
    private String status;
    private List<Comment> comment;

    public String getStatus() {
        return status;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public static class Comment {
        private String leaveMessageContent;
        private String leaveMessageDate;

        public String getLeaveMessageContent() {
            return leaveMessageContent;
        }

        public String getLeaveMessageDate() {
            return leaveMessageDate;
        }
    }
}
