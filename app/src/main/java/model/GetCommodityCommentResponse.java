package model;

import java.util.List;

public class GetCommodityCommentResponse {
    private String status;
    private String message;
    private List<Comment> comment;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public static class Comment {
        private String leaveMessageContent;
        private String leaveMessageDate;
        private String userName;
        private String userImage;
        private String userschool;

        public String getLeaveMessageContent() {
            return leaveMessageContent;
        }

        public String getLeaveMessageDate() {
            return leaveMessageDate;
        }

        public String getUserImage() {
            return userImage;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserSchool() {
            return userschool;
        }
    }
}
