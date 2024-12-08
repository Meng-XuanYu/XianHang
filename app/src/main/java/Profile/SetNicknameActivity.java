package Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.login.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import RetrofitClient.RetrofitClient;
import model.GetProfileResponse;
import model.UpdateNameRequest;
import model.UpdateNameResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNicknameActivity extends AppCompatActivity {
    private LinearLayout nickname_recommend;
    private EditText nickname;

    private ImageView back;
    private Button save;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_nickname);
        String create = getIntent().getStringExtra("create");
        String edit = getIntent().getStringExtra("edit");
        nickname_recommend = findViewById(R.id.nickname_recommend);
        nickname = findViewById(R.id.nickname);
        back = findViewById(R.id.backButton);
        save = findViewById(R.id.save);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);

        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse userProfile = gson.fromJson(profileJson, GetProfileResponse.class);
            nickname.setText(userProfile.getName());
        }

        // 换一批
        // 在 onCreate 方法中添加 change 按钮的点击事件
        LinearLayout changeButton = findViewById(R.id.change);
        changeButton.setOnClickListener(view -> updateNicknameRecommendations());


        back.setOnClickListener(view -> {
            Intent intent = new Intent(SetNicknameActivity.this,
                    create != null? CreateUserInfoActivity.class: EditUserInfoActivity.class);
            startActivity(intent);
            finish();
        });

        save.setOnClickListener(view -> {
            name = nickname.getText().toString();
            sendUpdateNameRequest(name);
        });

        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params2 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout = new FlexboxLayout(this);
        newLinearLayout.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout.setJustifyContent(JustifyContent.FLEX_START);
        newLinearLayout.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
        List<String> randomUsernames = generateUsernames(7);
        for (String str : randomUsernames) {
            FlexboxLayout Layout = new FlexboxLayout(this);
            Layout.setFlexWrap(FlexWrap.WRAP);
            Layout.setAlignItems(AlignItems.FLEX_START);
            Layout.setJustifyContent(JustifyContent.FLEX_START);
            Drawable drawable = getResources().getDrawable(R.drawable.search_bg);
            Layout.setBackground(drawable);
            Layout.setPadding(10, 10, 10, 10);
            Layout.setLayoutParams(params1);
            TextView textView = new TextView(this);
            textView.setText(str);
            textView.setTextSize(10);  // 设置字体大小
            textView.setTextColor(Color.BLACK);  // 设置字体颜色
            textView.setPadding(10, 10, 0, 10);  // 设置内边距
            textView.setLayoutParams(params2);
            Layout.addView(textView);
            newLinearLayout.addView(Layout);

            Layout.setOnClickListener(view -> {
                nickname.setText(((TextView) ((FlexboxLayout) view).getChildAt(0)).getText());
                nickname.clearFocus();
            });
        }
        nickname_recommend.addView(newLinearLayout);
    }

    private void updateNicknameRecommendations() {
        nickname_recommend.removeAllViews();

        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params2 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout = new FlexboxLayout(this);
        newLinearLayout.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout.setJustifyContent(JustifyContent.FLEX_START);
        newLinearLayout.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));

        List<String> randomUsernames = generateUsernames(7);
        for (String str : randomUsernames) {
            FlexboxLayout Layout = new FlexboxLayout(this);
            Layout.setFlexWrap(FlexWrap.WRAP);
            Layout.setAlignItems(AlignItems.FLEX_START);
            Layout.setJustifyContent(JustifyContent.FLEX_START);
            Drawable drawable = getResources().getDrawable(R.drawable.search_bg);
            Layout.setBackground(drawable);
            Layout.setPadding(10, 10, 10, 10);
            Layout.setLayoutParams(params1);
            TextView textView = new TextView(this);
            textView.setText(str);
            textView.setTextSize(10);  // 设置字体大小
            textView.setTextColor(Color.BLACK);  // 设置字体颜色
            textView.setPadding(10, 10, 0, 10);  // 设置内边距
            textView.setLayoutParams(params2);
            Layout.addView(textView);
            newLinearLayout.addView(Layout);

            Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nickname.setText(((TextView) ((FlexboxLayout) view).getChildAt(0)).getText());
                    nickname.clearFocus();
                }
            });
        }
        nickname_recommend.addView(newLinearLayout);
    }

    private List<String> generateUsernames(int count) {
        String[] adjectives = {
                "炽热的", "可爱的", "深邃", "迷糊", "闪亮的", "有趣", "忧郁的", "冷酷", "害羞的", "英俊的",
                "迷人的", "慵懒", "神秘的", "活泼的", "虚弱", "机智的", "灿烂", "疲惫", "柔和的", "优雅的",
                "胆小", "不羁的", "沉稳的", "轻浮", "多情的", "调皮", "自信的", "愚蠢", "悲凉", "感性的",
                "坦率", "高冷", "孤独", "纯洁", "深情的", "炫酷", "随和", "绝望的", "安静", "激情",
                "闪烁的", "坚韧", "风趣", "灵动的", "朦胧", "邪恶", "浮躁", "敏捷", "美丽", "灿然",
                "无畏", "幽默", "善良的", "粗犷", "朴素", "慷慨", "平凡", "可靠", "神圣", "呆萌的",
                "疯狂", "优柔", "无聊的", "坚强", "活跃", "稳重", "俏皮的", "诚挚", "真诚的", "忧伤",
                "冷静", "倔强", "滑稽的", "豪放", "轻盈", "热情", "无私的", "甜美", "危险的", "遥远",
                "繁华", "细腻的", "大胆的", "自由的", "聪慧", "稀奇的", "奇怪", "叛逆", "冒险的", "阴沉",
                "温和", "闪耀", "隐秘的", "古怪", "端庄", "自然的", "睿智", "独立", "遥不可及", "破碎的"
        };

        String[] nouns = {
                "星辰", "沙漠", "森林", "微光", "雾气", "梦境", "长风", "湖泊", "日出", "夕阳",
                "深渊", "岛屿", "火焰", "烛光", "极光", "小溪", "星河", "雪原", "流沙", "银河",
                "梧桐", "灯塔", "风帆", "远方", "溪流", "落日", "薄雾", "星图", "珊瑚", "雪山",
                "荒野", "冰川", "翡翠", "桃花", "月亮", "白鸽", "热浪", "寒霜", "青草", "火山",
                "紫罗兰", "蔷薇", "晨曦", "雨林", "夜莺", "松林", "幽谷", "星桥", "云雾", "海浪",
                "萤火虫", "雪花", "长夜", "灯光", "冬阳", "波涛", "露珠", "竹影", "花瓣", "寒夜",
                "枫叶", "孤舟", "白昼", "天空", "溪谷", "日月", "青鸟", "孤星", "琥珀", "雨滴",
                "海洋", "漂流瓶", "清风", "木舟", "山泉", "寒风", "流星", "迷雾", "云海", "寒潭",
                "星辰大海", "浪花", "幽梦", "白雪", "时光", "秋叶", "繁星", "深林", "遗迹", "寂夜",
                "云雀", "幻影", "潮汐", "朝霞", "细雨", "迷途", "雪影", "远山", "孤狼", "岚风"
        };

        Random random = new Random();
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String randomAdjective = adjectives[random.nextInt(adjectives.length)];
            String randomNoun = nouns[random.nextInt(nouns.length)];
            usernames.add(randomAdjective + randomNoun);
        }
        return usernames;
    }

    private void sendUpdateNameRequest(String nickname) {
        // 获取用户输入的信息
        String name = nickname;

        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "未找到用户 ID，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求体
        UpdateNameRequest updateNameRequest = new UpdateNameRequest(userId, name);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updateNameRequest).enqueue(new Callback<UpdateNameResponse>() {
            @Override
            public void onResponse(Call<UpdateNameResponse> call, Response<UpdateNameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateNameResponse updateNameResponse = response.body();
                    if ("success".equals(updateNameResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(SetNicknameActivity.this, updateNameResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateName", "Success: " + updateNameResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        saveUpdatedProfile(name);

                        String create = getIntent().getStringExtra("create");
                        Intent intent = new Intent(SetNicknameActivity.this,
                                create != null? CreateUserInfoActivity.class: EditUserInfoActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    } else {
                        // 更新失败
                        Toast.makeText(SetNicknameActivity.this, "更新失败: " + updateNameResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateName", "Error: " + updateNameResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(SetNicknameActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateName", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(SetNicknameActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateName", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateNameResponse> call, Throwable t) {
                Toast.makeText(SetNicknameActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateName", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void saveUpdatedProfile(String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userProfile", null);

        if (userJson == null) {
            Log.e("UpdateProfile", "用户数据未找到，请重新登录");
            Toast.makeText(this, "用户数据未找到，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将 JSON 转换为用户对象
        Gson gson = new Gson();
        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

        // 更新指定字段
        userProfile.setName(value);
        // 保存更新后的用户对象
        String updatedUserJson = gson.toJson(userProfile);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userProfile", updatedUserJson);
        editor.apply();

        Log.d("UpdateProfile", "用户数据已更新: " + updatedUserJson);
    }
}