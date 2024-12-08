package Message;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.MainActivity;
import Profile.ProfileView;
import RetrofitClient.RetrofitClient;
import goodsPage.ItemDetailActivity;
import model.BuyCommodityRequest;
import model.BuyCommodityResponse;
import model.CancelTradeRequest;
import model.CancelTradeResponse;
import model.CommentTradeRequest;
import model.CommentTradeResponse;
import model.ConfirmReceiveRequest;
import model.ConfirmReceiveResponse;
import model.ConfirmSendRequest;
import model.ConfirmSendResponse;
import model.GetCommodityResponse;
import model.GetProfileResponse;
import model.GetSingleTradeResponse;
import model.SetReadRequest;
import model.SetReadResponse;
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
    private String tradeId;
    private String sellerId;
    private Button gongneng;
    private String status;
    private int sta = 0;
    private boolean needToBeSetRead = false;

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
        gongneng = findViewById(R.id.gongneng);

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
        needToBeSetRead = getIntent().getStringExtra("needToBeSetRead") != null;

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        if (needToBeSetRead) {
            setRead();
        }

        initCommodity();

        initMessage();



        gongneng.setOnClickListener(view -> {
            ApiService apiService = RetrofitClient.getApiService();
            if (sta==0){
                apiService.buyCommodity(new BuyCommodityRequest(userId,commodityId)).enqueue(new Callback<BuyCommodityResponse>() {
                    @Override
                    public void onResponse(Call<BuyCommodityResponse> call, Response<BuyCommodityResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            BuyCommodityResponse getSingleTradeResponse = response.body();
                            if ("success".equals(getSingleTradeResponse.getStatus())) {
                                initCommodity();
                            } else {
                                Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<BuyCommodityResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Request Failed: " + t.getMessage());
                    }
                });

            } else if(sta == 1){
                apiService.confirmSend(new ConfirmSendRequest(tradeId)).enqueue(new Callback<ConfirmSendResponse>() {
                    @Override
                    public void onResponse(Call<ConfirmSendResponse> call, Response<ConfirmSendResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ConfirmSendResponse getSingleTradeResponse = response.body();
                            if ("success".equals(getSingleTradeResponse.getStatus())) {
                                initCommodity();
                            } else {
                                Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<ConfirmSendResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Request Failed: " + t.getMessage());
                    }
                });
            } else if(sta == 2){
                apiService.cancelTrade(new CancelTradeRequest(tradeId)).enqueue(new Callback<CancelTradeResponse>() {
                    @Override
                    public void onResponse(Call<CancelTradeResponse> call, Response<CancelTradeResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CancelTradeResponse getSingleTradeResponse = response.body();
                            if ("success".equals(getSingleTradeResponse.getStatus())) {
                                initCommodity();

                            } else {
                                Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<CancelTradeResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Request Failed: " + t.getMessage());
                    }
                });
            } else if(sta == 3){

                apiService.confirmReceive(new ConfirmReceiveRequest(tradeId)).enqueue(new Callback<ConfirmReceiveResponse>() {
                    @Override
                    public void onResponse(Call<ConfirmReceiveResponse> call, Response<ConfirmReceiveResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ConfirmReceiveResponse getSingleTradeResponse = response.body();
                            if ("success".equals(getSingleTradeResponse.getStatus())) {
                                initCommodity();
                            } else {
                                Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<ConfirmReceiveResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Request Failed: " + t.getMessage());
                    }
                });
            } else if(sta == 4){
                showRechargeDialog(apiService);
            }
        });

        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextChat.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                WebSocketManager.sendMessage(userId, messageContent);
                editTextChat.setText("");
            }
        });

        // Back button
        findViewById(R.id.imageButtonChatBack).setOnClickListener(v -> {
            // Send broadcast to refresh the fragment
            Intent intent = new Intent("ACTION_REFRESH_FRAGMENT");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            finish();
        });

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
            int commodityId = Integer.parseInt(this.commodityId);
            intent.putExtra("commodityId", commodityId);
            startActivity(intent);
        });
    }

    private void setRead() {
        ApiService apiService = RetrofitClient.getApiService();
        SetReadRequest request = new SetReadRequest(chatId);
        apiService.setRead(request).enqueue(new Callback<SetReadResponse>() {
            @Override
            public void onResponse(Call<SetReadResponse> call, Response<SetReadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SetReadResponse getSingleTradeResponse = response.body();
                    if ("success".equals(getSingleTradeResponse.getStatus())) {
                        Log.d("setRead", "Set read success");
                    } else {
                        Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("setRead", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ChatActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("setRead", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SetReadResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("setRead", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void getStatus() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getSingleTrade(tradeId).enqueue(new Callback<GetSingleTradeResponse>() {
            @Override
            public void onResponse(Call<GetSingleTradeResponse> call, Response<GetSingleTradeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetSingleTradeResponse getSingleTradeResponse = response.body();
                    if ("success".equals(getSingleTradeResponse.getStatus())) {
                        status = getSingleTradeResponse.getTrade().getTradeStatus();
                        if (!(status.equals("买家已支付")||status.equals("买家已评价")) && sellerId.equals(userId)) {
                            gongneng.setVisibility(View.INVISIBLE);
                        } else {
                            gongneng.setVisibility(View.VISIBLE);
                        }

                        if (status.equals("买家已支付") && sellerId.equals(userId)) {
                            gongneng.setText("发货");
                            sta = 1;
                        } else if (status.equals("买家已支付") && !sellerId.equals(userId)) {
                            gongneng.setText("取消订单");
                            sta = 2;
                        } else if (status.equals("卖家已发货") && !sellerId.equals(userId)) {
                            gongneng.setText("确认收货");
                            sta = 3;
                        }else if (status.equals("买家已收货") && !sellerId.equals(userId)) {
                            gongneng.setText("待评价");
                            sta = 4;
                        } else if (status.equals("买家已评价") ) {
                            gongneng.setText("买家已评价");
                            sta = 5;
                        }
                    } else {
                        Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<GetSingleTradeResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getDetail", "Request Failed: " + t.getMessage());
            }
        });
    }
    private void showRechargeDialog(ApiService apiService) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ChatActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.comment, null);
        builder.setView(dialogView)
                .setTitle("评价")
                .setBackground(getResources().getDrawable(R.drawable.rounded_background, null))
                .setIcon(R.drawable.xianhang_light_fang)
                .setPositiveButton("确认", (dialog, which) -> {
                    EditText editTextAmount = dialogView.findViewById(R.id.edit_text_amount);
                    EditText editText = dialogView.findViewById(R.id.comment);
                    if(Integer.parseInt(editTextAmount.getText().toString())<0&&Integer.parseInt(editTextAmount.getText().toString())>5){
                        Toast.makeText(ChatActivity.this, "请输入保证输入的星级在0~5", Toast.LENGTH_SHORT).show();
                    }
                    String amountStr = editTextAmount.getText().toString();
                    if (!amountStr.isEmpty()) {
                        apiService.commentTrade(new CommentTradeRequest(tradeId,Integer.parseInt(editTextAmount.getText().toString()),editText.getText().toString())).enqueue(new Callback<CommentTradeResponse>() {
                            @Override
                            public void onResponse(Call<CommentTradeResponse> call, Response<CommentTradeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    CommentTradeResponse getSingleTradeResponse = response.body();
                                    if ("success".equals(getSingleTradeResponse.getStatus())) {
                                        initCommodity();
                                    } else {
                                        Toast.makeText(ChatActivity.this, "获取用户信息失败: ", Toast.LENGTH_SHORT).show();
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
                            public void onFailure(Call<CommentTradeResponse> call, Throwable t) {
                                Toast.makeText(ChatActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("getDetail", "Request Failed: " + t.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(ChatActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void initCommodity() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommodity(commodityId).enqueue(new Callback<GetCommodityResponse>() {
            @Override
            public void onResponse(Call<GetCommodityResponse> call, Response<GetCommodityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {
                        if(sta == 2){
                            sta =0;
                        }
                        GetCommodityResponse.Commodity commodity = getCommodityResponse.getCommodity();
                        tradeId = commodity.getCurrentTradeId();
                        sellerId = commodity.getSellerId();
                        Glide.with(ChatActivity.this).load(commodity.getCommodityImage1()).into(shangpin);
                        price.setText(commodity.getCommodityValue());
                        pos.setText(commodity.getSchool());
                        if(tradeId!=null){
                            getStatus();
                        } else{
                            if (sellerId.equals(userId)) {
                                gongneng.setVisibility(View.INVISIBLE);
                            } else {
                                gongneng.setText("立即下单");
                            }
                        }
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