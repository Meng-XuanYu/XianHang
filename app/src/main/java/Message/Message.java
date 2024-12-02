package Message;

public class Message {
    private String content;
    private String senderAvatarUrl;
    private boolean isSentByUser;
    private String time;


    public Message(String content, String senderAvatarUrl, boolean isSentByUser, String time) {
        this.content = content;
        this.senderAvatarUrl = senderAvatarUrl;
        this.isSentByUser = isSentByUser;
        this.time = time;
    }

    public String getTime() {
        return time;
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