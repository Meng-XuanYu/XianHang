package model;

public class DeleteChatRequest {
    private String chatId;

    public DeleteChatRequest(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}
