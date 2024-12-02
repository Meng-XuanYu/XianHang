package Message;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.MainActivity;
import Profile.ProfileView;
import RetrofitClient.RetrofitClient;
import goodsPage.ItemDetailActivity;
import model.GetCommodityResponse;
import model.GetProfileResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText editTextChat;
    private ImageButton buttonSend;
    private String chatId;
    private String userId;
    private String commodityId;
    private String myAvatar;
    private String otherAvatar;
    private String otherId;
    private ImageView shangpin;
    private TextView price;
    private TextView pos;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextChat = findViewById(R.id.editTextChat);
        buttonSend = findViewById(R.id.buttonSend);
        shangpin = findViewById(R.id.shangpin);
        price = findViewById(R.id.price);
        pos = findViewById(R.id.pos);
        name = findViewById(R.id.username);

        chatId = getIntent().getStringExtra("chatId");
        commodityId = getIntent().getStringExtra("commodityId");
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        otherId = getIntent().getStringExtra("otherId");
        name.setText(getIntent().getStringExtra("otherName"));
        String profileJson = sharedPreferences.getString("userProfile", null);
        Gson gson = new Gson();
        GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);
        myAvatar = profile.getAvatar();
        otherAvatar = getIntent().getStringExtra("otherAvatar");
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(messageAdapter);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        initCommodity();
        
        initMessage();

        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextChat.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                WebSocketManager.sendMessage(userId, messageContent);
                editTextChat.setText("");
            }
        });

        // Back button
        findViewById(R.id.imageButtonChatBack).setOnClickListener(v -> finish());

        // 进入商家profile
        ImageButton buttonSetting = findViewById(R.id.button_profile);
        buttonSetting.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, ProfileView.class);
            intent.putExtra("userId", otherId);
            startActivity(intent);
        });

        // 点到图片进入商品详情
        findViewById(R.id.shangpin).setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, ItemDetailActivity.class);
            startActivity(intent);
        });
    }

    private void initCommodity() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommodity(commodityId).enqueue(new Callback<GetCommodityResponse>() {
            @Override
            public void onResponse(Call<GetCommodityResponse> call, Response<GetCommodityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {
                        GetCommodityResponse.Commodity commodity = getCommodityResponse.getCommodity();
                        Glide.with(ChatActivity.this).load(commodity.getCommodityImage1()).into(shangpin);
                        price.setText(commodity.getCommodityValue());
                        pos.setText(commodity.getSchool());
                    } else {
                        Toast.makeText(ChatActivity.this, "获取用户信息失败: " + getCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ChatActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommodityResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getDetail", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void initMessage() {
        WebSocketManager.connectWebSocketChat(chatId, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.closeWebSocket();
    }

    public void sendMessage(String messageContent, String time) {
        messageList.add(new Message(messageContent, myAvatar, true, time));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
    }

    public void receiveMessage(String messageContent, String time) {
        messageList.add(new Message(messageContent, otherAvatar, false, time));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
    }

    public String getUserId() {
        return userId;
    }
}