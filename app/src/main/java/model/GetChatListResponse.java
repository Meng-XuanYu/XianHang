package model;

import java.util.List;

public class GetChatListResponse {
    private List<Chat> chatList;

    public List<Chat> getChatList() {
        return chatList;
    }

    public static class Chat {
        private String chatId;
        private String otherName;
        private String latestMessageContent;
        private String latestMessageStatus;
        private String otherAvatar;
        private String commodityId;
        private String commodityName;
        private String commodityImage;
        private String user1_id;
        private String user2_id;

        public String getOtherId(String userId) {
            return user1_id.equals(userId) ? user2_id : user1_id;
        }

        public String getChatId() {
            return chatId;
        }

        public String getOtherName() {
            return otherName;
        }

        public String getLatestMessageContent() {
            return latestMessageContent;
        }

        public String getLatestMessageStatus() {
            return latestMessageStatus;
        }

        public String getOtherAvatar() {
            return otherAvatar;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public String getCommodityImage() {
            return commodityImage;
        }
    }
}
