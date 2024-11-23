package Message;

public class Message {
    private int avatarResId;
    private String messageText;
    private int messageImageResId;

    public Message(int avatarResId, String messageText, int messageImageResId) {
        this.avatarResId = avatarResId;
        this.messageText = messageText;
        this.messageImageResId = messageImageResId;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageImageResId() {
        return messageImageResId;
    }
}