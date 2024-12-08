package Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.login.R;

import java.util.List;

import Message.ChatActivity;
import Message.MessageLan;
import Message.MessageLanAdapter;
import Message.WebSocketManager;
import RetrofitClient.RetrofitClient;
import model.GetChatListRequest;
import model.GetChatListResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widgets.NavigateTabBar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG_PAGE_HOME    = "闲航";
    private static final String TAG_PAGE_CITY    = "校区";
    private static final String TAG_PAGE_MORE    = "发布"; //deprecated
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_PERSON  = "我的";

    private NavigateTabBar mNavigateTabBar;
    private ImageView mTabMoreIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        // Register the receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceiver, new IntentFilter("ACTION_REFRESH_FRAGMENT"));

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        this.mNavigateTabBar = findViewById(R.id.main_tabbar);
        this.mTabMoreIv = findViewById(R.id.tab_more_iv);

        this.mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        this.mNavigateTabBar.addTab(fragment.HomeFragment.class, new NavigateTabBar.TabParam(R.drawable.home, R.drawable.home_filled, TAG_PAGE_HOME));
        this.mNavigateTabBar.addTab(fragment.CityFragment.class, new NavigateTabBar.TabParam(R.drawable.location, R.drawable.location_filled, TAG_PAGE_CITY));
        this.mNavigateTabBar.addTab(null, new NavigateTabBar.TabParam(0, 0, TAG_PAGE_MORE));
        this.mNavigateTabBar.addTab(fragment.MessageFragment.class, new NavigateTabBar.TabParam(R.drawable.message, R.drawable.message_filled, TAG_PAGE_MESSAGE));
        this.mNavigateTabBar.addTab(fragment.PersonFragment.class, new NavigateTabBar.TabParam(R.drawable.profile, R.drawable.profile_filled, TAG_PAGE_PERSON));

        mNavigateTabBar.setRedDotVisibility(TAG_PAGE_MESSAGE, false);
        // 点击发布按钮
        this.mTabMoreIv.setOnClickListener(v -> PopMenuView.getInstance().show(MainActivity.this, mTabMoreIv, this));

        // 返回键监听
        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (PopMenuView.getInstance().isShowing()) {
                    PopMenuView.getInstance().closePopupWindowAction();
                } else {
                    finish();
                }
            }
        });

        connect();
    }

    private void connect() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        GetChatListRequest request = new GetChatListRequest(userId);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getChatList(request).enqueue(new Callback<GetChatListResponse>() {
            @Override
            public void onResponse(Call<GetChatListResponse> call, Response<GetChatListResponse> response) {
                if (response.isSuccessful()) {
                    List<GetChatListResponse.Chat> chatList = null;
                    if (response.body() != null) {
                        chatList = response.body().getChatList();
                    }
                    if (chatList != null) {
                        for (GetChatListResponse.Chat chat : chatList) {
                            WebSocketManager.connectWebSocketMain(chat.getChatId(), MainActivity.this);
                        }
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call<GetChatListResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Intent intent = new Intent(MainActivity.this, WaitActivity.class);
            if (selectedImage != null) {
                intent.putExtra("imageUri", selectedImage.toString());
            } else {
                Log.e("MainActivity", "onActivityResult: selectedImage is null");
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mNavigateTabBar.onSaveInstanceState(outState);
    }

    public void setRedDotVisibility(String tag, boolean visible) {
        mNavigateTabBar.setRedDotVisibility(tag, visible);
    }

    public void refreshFragment() {
        mNavigateTabBar.refreshMessageFragment();
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_REFRESH_FRAGMENT".equals(intent.getAction())) {
                refreshFragment();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshReceiver);
    }

    public String getUserId() {
        return getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getString("userId", null);
    }
}