package Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {

    private static WebSocket webSocket;
    private static final String BASE_URL = "ws://10.192.171.208:8000/ws/chat/";

    public static void connectWebSocketChat(String chatId, ChatActivity chatActivity) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + chatId + "/")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket connected!");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject data = new JSONObject(text);
                    String command = data.getString("command");

                    if ("messages".equals(command)) {
                        // Handle initial chat history
                        JSONArray messages = data.getJSONArray("messages");
                        renderMessages(messages,chatActivity);
                    }
                } catch (Exception e) {
                    JSONObject data;
                    try {
                        data = new JSONObject(text);
                        renderNewMessage(data, chatActivity);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closed: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("WebSocket error: " + t.getMessage());
            }
        });
    }

    public static void sendMessage(String userId, String content) {
        try {
            JSONObject message = new JSONObject();
            message.put("userId", userId);
            message.put("message_type", "text");
            message.put("content", content);

            webSocket.send(message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing connection");
        }
    }

    private static void renderMessages(JSONArray messages, ChatActivity chatActivity) {
        chatActivity.runOnUiThread(() -> {
            try {
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject message = messages.getJSONObject(i);
                    if (message.getString("senderId").equals(chatActivity.getUserId())) {
                        chatActivity.sendMessage(message.getString("content"), message.getString("timestamp"));
                    } else {
                        chatActivity.receiveMessage(message.getString("content"), message.getString("timestamp"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private static void renderNewMessage(JSONObject message, ChatActivity chatActivity) {
        chatActivity.runOnUiThread(() -> {
            try {
                if (message.getString("senderId").equals(chatActivity.getUserId())) {
                    chatActivity.sendMessage(message.getString("content"), message.getString("timestamp"));
                } else {
                    chatActivity.receiveMessage(message.getString("content"), message.getString("timestamp"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
