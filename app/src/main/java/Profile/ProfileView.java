package Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import Login.LoginActivity;
import RetrofitClient.RetrofitClient;
import Search.SearchDetailActivity;
import goodsPage.ItemDetailActivity;
import model.GetAttractivenessResponse;
import model.GetCommodityCommentResponse;
import model.GetMyPublishResponse;
import model.GetMySellResponse;
import model.GetProfileResponse;
import model.GetSellerTradeCommentResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileView extends AppCompatActivity {
    private LinearLayout item_show1;
    private LinearLayout item_show2;

    private LinearLayout user_comments;

    private TextView textSelling, textReviews;
    private ScrollView scrollView, contentSelling, contentReviews;

    private List<GetMyPublishResponse.Commodity> trades;

    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);

    private EditText editText;
    private ImageView imageView;

    private ImageView avatar;
    private TextView username;
    private TextView credit;
    private TextView jianjie;
    private TextView school;
    private String userId;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        // 初始化视图
        user_comments = findViewById(R.id.user_comments);
        textSelling = findViewById(R.id.text_selling);
        textReviews = findViewById(R.id.text_reviews);
        contentSelling = findViewById(R.id.selling);
        contentReviews = findViewById(R.id.reviews);
        scrollView = findViewById(R.id.scrollView);
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        credit = findViewById(R.id.credit_level);
        jianjie = findViewById(R.id.signature);
        school = findViewById(R.id.address);
        textView = findViewById(R.id.comment_sum);

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            // 加载用户信息
            loadUserProfile();

            // 设置点击事件
            textSelling.setOnClickListener(v -> selectTabSell(textSelling, contentSelling));
            textReviews.setOnClickListener(v -> selectTabReviews(textReviews, contentReviews));

            textSelling.setTextColor(ContextCompat.getColor(this, R.color.blue_bh));  // 设置蓝色字体
            textSelling.setPaintFlags(textSelling.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 加下划线
            // 填充 content_selling 布局
            item_show1 = findViewById(R.id.d_item_show1);
            item_show2 = findViewById(R.id.d_item_show2);

            getOtherSell();
            getComments();

            contentReviews.setVisibility(View.GONE);

            editText = findViewById(R.id.searchEditText);
            imageView = findViewById(R.id.search_img);

            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editText.clearFocus();
                    Intent intent = new Intent(ProfileView.this, SearchDetailActivity.class);
                    startActivity(intent);
                }
            });
            imageView.setOnClickListener(view -> {
                Intent intent = new Intent(ProfileView.this, SearchDetailActivity.class);
                startActivity(intent);
            });

            // 返回按钮
            findViewById(R.id.search_back).setOnClickListener(v -> finish());

            // edit button
            findViewById(R.id.button_setting).setOnClickListener(v -> {
                Intent intent = new Intent(ProfileView.this, EditUserInfoActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            loadOtherUserProfile(userId);
            loadOtherCredit(userId);

            // 设置点击事件
            textSelling.setOnClickListener(v -> selectTabSell(textSelling, contentSelling));
            textReviews.setOnClickListener(v -> selectTabReviews(textReviews, contentReviews));

            textSelling.setTextColor(ContextCompat.getColor(this, R.color.blue_bh));  // 设置蓝色字体
            textSelling.setPaintFlags(textSelling.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 加下划线
            // 填充 content_selling 布局
            item_show1 = findViewById(R.id.d_item_show1);
            item_show2 = findViewById(R.id.d_item_show2);

            getOtherSell();
            getComments();
            contentReviews.setVisibility(View.GONE);

            editText = findViewById(R.id.searchEditText);
            imageView = findViewById(R.id.search_img);

            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editText.clearFocus();
                    Intent intent = new Intent(ProfileView.this, SearchDetailActivity.class);
                    startActivity(intent);
                }
            });
            imageView.setOnClickListener(view -> {
                Intent intent = new Intent(ProfileView.this, SearchDetailActivity.class);
                startActivity(intent);
            });

            // 返回按钮
            findViewById(R.id.search_back).setOnClickListener(v -> finish());

            // edit button
            findViewById(R.id.button_setting).setVisibility(View.GONE);
        }
    }

    private void getComments() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        Gson gson = new Gson();
        GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getSellerTradeComment(userId).enqueue(new Callback<GetSellerTradeCommentResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetSellerTradeCommentResponse> call, Response<GetSellerTradeCommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetSellerTradeCommentResponse getCommodityCommentResponse = response.body();
                    if ("success".equals(getCommodityCommentResponse.getStatus())) {
                        textView.setText((getCommodityCommentResponse.getComments() != null ?
                                getCommodityCommentResponse.getComments().size() : 0) + "条评论");
                        generateComments(ProfileView.this, getCommodityCommentResponse.getComments());
                    } else {
                        Toast.makeText(ProfileView.this, "获取用户信息失败: " + getCommodityCommentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ProfileView.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getComments", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ProfileView.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getComments", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSellerTradeCommentResponse> call, Throwable t) {
                Toast.makeText(ProfileView.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getComments", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void getOtherSell() {
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.getMyPublish(userId).enqueue(new Callback<GetMyPublishResponse>() {
            @Override
            public void onResponse(Call<GetMyPublishResponse> call, Response<GetMyPublishResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetMyPublishResponse getMySellResponse = response.body();
                    if ("success".equals(getMySellResponse.getStatus())) {
                        trades = getMySellResponse.getCommodities();
                        generateLayout(ProfileView.this, trades);
                    } else {
                        // 登录失败
                        Toast.makeText(ProfileView.this, "getMySellResponse.getMessage()", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileView", "Error: " + "getMySellResponse.getMessage()");
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ProfileView.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("ProfileView", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ProfileView.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileView", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMyPublishResponse> call, Throwable t) {
                Toast.makeText(ProfileView.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileView", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void loadOtherCredit(String userId) {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getAttractiveness(userId).enqueue(new Callback<GetAttractivenessResponse>() {
            @Override
            public void onResponse(Call<GetAttractivenessResponse> call, Response<GetAttractivenessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAttractivenessResponse attractivenessResponse = response.body();
                    if ("success".equals(attractivenessResponse.getStatus())) {
                        int creditInt = attractivenessResponse.getAttractiveness();
                        String creditStr = "航力值:" + creditInt;
                        credit.setText(creditStr);
                    } else {
                        Toast.makeText(ProfileView.this, "获取航力值失败: " + attractivenessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ProfileView.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ProfileView.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAttractivenessResponse> call, Throwable t) {
                Toast.makeText(ProfileView.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getAttractiveness", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void loadOtherUserProfile(String userId) {
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 GET 请求获取用户信息
        apiService.getProfile(userId).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetProfileResponse profileResponse = response.body();
                    if ("success".equals(profileResponse.getStatus())) {
                        if (profileResponse.getName() != null) {
                            username.setText(profileResponse.getName());
                        }
                        if (profileResponse.getAvatar() != null) {
                            Glide.with(ProfileView.this)
                                    .load(profileResponse.getAvatar())
                                    .placeholder(R.drawable.xianhang_light_yuan)  // 占位图
                                    .error(R.drawable.xianhang_light_yuan).into(avatar);
                        }
                        if (profileResponse.getText() != null) {
                            jianjie.setText(profileResponse.getText());
                        }
                        if (profileResponse.getSchool() != null) {
                            school.setText(profileResponse.getSchool());
                        }
                    } else {
                        Toast.makeText(ProfileView.this, "获取用户信息失败: " + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ProfileView.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ProfileView.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileView.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getProfile", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        userId = sharedPreferences.getString("userId", null);
        float creditLevel = sharedPreferences.getFloat("userAttractiveness", 0);
        int creditInt = (int) creditLevel;
        String creditStr = "航力值:" + creditInt;
        credit.setText(creditStr);

        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

            if (profile.getName() != null) {
                username.setText(profile.getName());
            }
            if (profile.getAvatar() != null) {
                Glide.with(this)
                        .load(profile.getAvatar())
                        .placeholder(R.drawable.xianhang_light_yuan)  // 占位图
                        .error(R.drawable.xianhang_light_yuan).into(avatar);
            }
            if (profile.getText() != null) {
                jianjie.setText(profile.getText());
            }
            if (profile.getSchool() != null) {
                school.setText(profile.getSchool());
            }
        } else {
            Log.e("SharedPreferences", "用户信息不存在");
        }
    }

    private void selectTabSell(TextView selectedTextView, ScrollView selectedContent) {
        // 重置所有导航项的样式
        resetTabStyles();

        // 设置选中项的样式
        selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.blue_bh));  // 设置蓝色字体
        selectedTextView.setPaintFlags(selectedTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 加下划线

        // 切换滚动区域显示
        contentSelling.setVisibility(View.GONE);
        contentReviews.setVisibility(View.GONE);
        selectedContent.setVisibility(View.VISIBLE);
    }

    private void selectTabReviews(TextView selectedTextView, ScrollView selectedContent) {
        // 重置所有导航项的样式
        resetTabStyles();

        // 设置选中项的样式
        selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.blue_bh));  // 设置蓝色字体
        selectedTextView.setPaintFlags(selectedTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 加下划线

        // 切换滚动区域显示
        contentSelling.setVisibility(View.GONE);
        contentReviews.setVisibility(View.GONE);
        selectedContent.setVisibility(View.VISIBLE);
    }

    private void resetTabStyles() {
        // 重置样式：恢复所有项的默认状态
        textSelling.setTextColor(ContextCompat.getColor(this, R.color.gray));
        textReviews.setTextColor(ContextCompat.getColor(this, R.color.gray));

        textSelling.setPaintFlags(textSelling.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        textReviews.setPaintFlags(textReviews.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

    private void generateLayout(Context context, List<GetMyPublishResponse.Commodity> items) {
        contentReviews.setVisibility(View.GONE);
        if (items == null) {
            return;
        }
        for (GetMyPublishResponse.Commodity item : items) {
            LinearLayout itemLayout = new LinearLayout(context);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setBackgroundResource(R.color.white);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            itemParams.setMargins(0, dpToPx(context, 10), 0, dpToPx(context, 5));
            itemLayout.setLayoutParams(itemParams);
            itemLayout.setBackgroundResource(R.drawable.img);
            RoundedImageView roundedImageView = new RoundedImageView(context);
            LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            roundedImageView.setLayoutParams(imageParams1);
            roundedImageView.setAdjustViewBounds(true);
            roundedImageView.setBackgroundResource(R.drawable.img);
            roundedImageView.setScaleType(ImageView.ScaleType.FIT_START);
            Glide.with(context)
                    .load(item.getCommodityImage())
                    .placeholder(R.drawable.img)  // 占位图
                    .error(R.drawable.img).into(roundedImageView);
            roundedImageView.setCornerRadius(dpToPx(context, 10), dpToPx(context, 10), 0, 0);
            itemLayout.addView(roundedImageView);

            TextView textView1 = new TextView(context);
            LinearLayout.LayoutParams textviewParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textviewParams1.setMargins(dpToPx(context, 10), dpToPx(context, 10), 0, dpToPx(context, 5));
            textView1.setLayoutParams(textviewParams1);
            textView1.setText(item.getCommodityDescription());
            textView1.setSingleLine(true);
            textView1.setTextSize(15);
            textView1.setTextColor(Color.BLACK);
            itemLayout.addView(textView1);

            TextView textView2 = new TextView(context);
            LinearLayout.LayoutParams textviewParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textviewParams2.setMargins(dpToPx(context, 10), 0, 0, dpToPx(context, 15));
            textView2.setLayoutParams(textviewParams2);
            textView2.setText(String.valueOf(item.getCommodityValue()));
            textView2.setTextSize(18);
            textView2.setTypeface(null, Typeface.BOLD);
            textView2.setTextColor(Color.parseColor("#fd424b"));
            itemLayout.addView(textView2);

            final int temp = Integer.parseInt(item.getCommodityId());
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileView.this, ItemDetailActivity.class);
                    intent.putExtra("commodityId", temp);
                    startActivity(intent);
                }
            });

            if (items.indexOf(item) % 2 == 0) {
                item_show1.addView(itemLayout);
            } else {
                item_show2.addView(itemLayout);
            }
        }
    }

    private void generateComments(Context context, List<GetSellerTradeCommentResponse.CommentData> comments) {
        if (comments == null) {
            return;
        }
        for (GetSellerTradeCommentResponse.CommentData comment1 : comments) {
            LinearLayout layout1 = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, dpToPx(context, 20), 0, dpToPx(context, 20));
            layout1.setLayoutParams(params);
            RoundedImageView roundedImageView = new RoundedImageView(context);
            LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
                    dpToPx(context, 34),
                    dpToPx(context, 34)
            );
            roundedImageView.setLayoutParams(imageParams1);
            roundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context)
                    .load(comment1.getBuyerImage())
                    .placeholder(R.drawable.img)  // 占位图
                    .error(R.drawable.img).into(roundedImageView);
            roundedImageView.setCornerRadius(dpToPx(context, 17));
            layout1.addView(roundedImageView);

            LinearLayout layout2 = new LinearLayout(context);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layout2.setLayoutParams(params1);
            layout2.setOrientation(LinearLayout.VERTICAL);
            layout1.addView(layout2);

            LinearLayout layout3 = new LinearLayout(context);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params2.setMargins(dpToPx(context, 10), 0, 0, dpToPx(context, 5));
            layout3.setLayoutParams(params2);
            layout2.addView(layout3);

            TextView textView = new TextView(context);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params3.setMargins(0, dpToPx(context, 1), 0, 0);
            textView.setLayoutParams(params3);
            textView.setTextColor(Color.parseColor("#939393"));
            textView.setText(comment1.getBuyerName());
            textView.setTextSize(16);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            layout3.addView(textView);

            TextView textview1 = new TextView(context);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params4.setMargins(dpToPx(context, 6), 0, 0, 0);
            textview1.setLayoutParams(params4);
            textview1.setTextColor(Color.parseColor("#939393"));
            textview1.setText(comment1.getBuyerSchool());
            textview1.setPadding(dpToPx(context, 4), 0, dpToPx(context, 2), 0);
            textview1.setTextSize(13);
            textview1.setBackgroundResource(R.drawable.user_pos_bg);
            textview1.setTypeface(Typeface.DEFAULT_BOLD);
            layout3.addView(textview1);

            TextView textview2 = new TextView(context);
            LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params5.setMargins(dpToPx(context, 10), dpToPx(context, 2), dpToPx(context, 40), 0);
            textview2.setLayoutParams(params5);
            textview2.setTextColor(Color.BLACK);
            textview2.setText(comment1.getBuyerComment());
            textview2.setTextSize(16);
            layout2.addView(textview2);


            user_comments.addView(layout1);
        }
    }

    // dp 转 px 工具函数
    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}