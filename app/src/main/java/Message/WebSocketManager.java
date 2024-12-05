package Message;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Main.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {

    private static WebSocket webSocket;
    private static ArrayList<WebSocket> webSockets = new ArrayList<>();
    private static final String BASE_URL = "ws://10.192.171.208:8000/ws/chat/";

    public static void connectWebSocketMain(String chatId, MainActivity mainActivity) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + chatId + "/")
                .build();

        webSockets.add(client.newWebSocket(request, new WebSocketListener() {
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
                    } else if ("new_message".equals(command)) {
                        // Handle new message
                        JSONObject message = data.getJSONObject("message");
                        renderNewMessage(message, mainActivity);
                    }
                } catch (Exception e) {
                    JSONObject data;
                    try {
                        data = new JSONObject(text);
                        renderNewMessage(data, mainActivity);
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
        }));
    }

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

    public static void connectWebSocketMain(String chatId, ChatActivity chatActivity) {
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
                        renderMessages(messages, chatActivity);
                    } else if ("new_message".equals(command)) {
                        // Handle new message
                        JSONObject message = data.getJSONObject("message");
                        renderNewMessage(message, chatActivity);
                    }
                } catch (Exception e) {
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

    private static void renderNewMessage(JSONObject message, MainActivity mainActivity) {
        mainActivity.runOnUiThread(() -> {
            try {
                String content = message.getString("content");
                String senderId = message.getString("senderId");

                if (senderId.equals(mainActivity.getUserId())) {

                } else {
                    sendNotification(mainActivity, content, senderId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private static void renderNewMessage(JSONObject message, ChatActivity chatActivity) {
        chatActivity.runOnUiThread(() -> {
            try {
                String content = message.getString("content");
                String timestamp = message.getString("timestamp");
                String senderId = message.getString("senderId");

                if (senderId.equals(chatActivity.getUserId())) {
                    chatActivity.sendMessage(content, timestamp);
                } else {
                    chatActivity.receiveMessage(content, timestamp);
                    sendNotification(chatActivity, content,senderId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private static void sendNotification(Context context, String messageContent, String senderId) {
        String channelId = "new_message_channel";
        String channelName = "New Message Notifications";
        int notificationId = 1;

        // Create the notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.xianhang_light_fang) // Replace with your app's notification icon
                .setContentTitle(senderId)
                .setContentText(messageContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
