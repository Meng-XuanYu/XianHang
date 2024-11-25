package Message;

public class Message {
    private String content;
    private String senderAvatarUrl;
    private boolean isSentByUser;

    public Message(String content, String senderAvatarUrl, boolean isSentByUser) {
        this.content = content;
        this.senderAvatarUrl = senderAvatarUrl;
        this.isSentByUser = isSentByUser;
    }

    public String getContent() {
        return content;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
}