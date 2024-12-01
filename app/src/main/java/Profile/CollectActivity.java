package Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import RetrofitClient.RetrofitClient;
import goodsPage.ItemDetailActivity;
import model.GetAttractivenessResponse;
import model.GetCollectionsResponse;
import model.GetProfileResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectActivity extends AppCompatActivity {
    private LinearLayout item_show1;
    private LinearLayout item_show2;

    private List<GetCollectionsResponse.Commodity> commodities;
    private String credit;
    private String name;
    private String avatar;

    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        item_show1 = findViewById(R.id.d_item_show1);
        item_show2 = findViewById(R.id.d_item_show2);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // 返回按钮
        ImageView back = findViewById(R.id.search_back);
        back.setOnClickListener(view -> {
            finish();
        });
        
        getMyCollect();
        generateLayout(this, commodities);
    }

    private void getMyCollect() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

//        if (userId == null) {
//            Toast.makeText(this, "未找到用户 ID，请重新登录", Toast.LENGTH_SHORT).show();
//            return;
//        }

        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.getCollections(userId).enqueue(new Callback<GetCollectionsResponse>() {
            @Override
            public void onResponse(Call<GetCollectionsResponse> call, Response<GetCollectionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCollectionsResponse getMyBuyResponse = response.body();
                    if ("success".equals(getMyBuyResponse.getStatus())) {
                        commodities = getMyBuyResponse.getCollections();
                    } else {
                        // 登录失败
                        Toast.makeText(CollectActivity.this, "getMyBuyResponse.getMessage()", Toast.LENGTH_SHORT).show();
                        Log.e("MyCollect", "Error: " + "getMyBuyResponse.getMessage()");
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CollectActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("MyCollect", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CollectActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("MyCollect", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCollectionsResponse> call, Throwable t) {
                Toast.makeText(CollectActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyCollect", "Request Failed: " + t.getMessage());
            }
        });
    }

    public void getUserNameAndAvatar(String userId) {
        ApiService apiService = RetrofitClient.getApiService();
        // 发送 GET 请求获取用户信息
        apiService.getProfile(userId).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetProfileResponse profileResponse = response.body();
                    if ("success".equals(profileResponse.getStatus())) {
                        // 将用户数据存储到 SharedPreferences
                        name = profileResponse.getName();
                        avatar = profileResponse.getAvatar();
                    } else {
                        Toast.makeText(CollectActivity.this, "获取用户信息失败: " + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CollectActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CollectActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(CollectActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getProfile", "Request Failed: " + t.getMessage());
            }
        });
    }


    public void getAttractiveness(String userId) {
        ApiService apiService = RetrofitClient.getApiService();
        // 发送 GET 请求获取用户信息
        apiService.getAttractiveness(userId).enqueue(new Callback<GetAttractivenessResponse>() {
            @Override
            public void onResponse(Call<GetAttractivenessResponse> call, Response<GetAttractivenessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAttractivenessResponse getAttractivenessResponse = response.body();
                    if ("success".equals(getAttractivenessResponse.getStatus())) {
                        // 将用户数据存储到 SharedPreferences
                        Integer attractiveness = getAttractivenessResponse.getAttractiveness();
                        if (attractiveness >= 500 && attractiveness < 550) {
                            credit="卖家信用一般";
                        } else if (attractiveness >= 550 && attractiveness < 750) {
                            credit="卖家信用良好";
                        } else if (attractiveness >= 750 && attractiveness < 1000) {
                            credit="卖家信用优秀";
                        } else if (attractiveness >= 1000) {
                            credit="卖家信用极好";
                        }
                    } else {
                        Toast.makeText(CollectActivity.this, "获取用户信息失败: " + getAttractivenessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CollectActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CollectActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAttractivenessResponse> call, Throwable t) {
                Toast.makeText(CollectActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getAttractiveness", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void generateLayout(Context context, List<GetCollectionsResponse.Commodity> items) {
        if(items == null){
            return;
        }
        for (GetCollectionsResponse.Commodity item : items) {
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
            textviewParams2.setMargins(dpToPx(context, 10), 0, 0, dpToPx(context, 5));
            textView2.setLayoutParams(textviewParams2);
            textView2.setText(String.valueOf(item.getCommodityValue()));
            textView2.setTextSize(18);
            textView2.setTypeface(null, Typeface.BOLD);
            textView2.setTextColor(Color.parseColor("#fd424b"));
            itemLayout.addView(textView2);

            LinearLayout innerLayout = new LinearLayout(context);
            LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            innerLayout.setLayoutParams(innerParams);
            RoundedImageView innerRoundedImageView = new RoundedImageView(context);
            LinearLayout.LayoutParams innerImageParams1 = new LinearLayout.LayoutParams(
                    dpToPx(context, 25),
                    dpToPx(context, 25)
            );
            innerImageParams1.setMargins(dpToPx(context, 10), dpToPx(context, 5), 0, dpToPx(context, 10));
            innerRoundedImageView.setLayoutParams(innerImageParams1);
            innerRoundedImageView.setBackgroundResource(R.drawable.img);
            innerRoundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            getUserNameAndAvatar(item.getSellerId());
            Glide.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.img)  // 占位图
                    .error(R.drawable.img).into(innerRoundedImageView);
            innerRoundedImageView.setCornerRadius(dpToPx(context, 12.5f));
            innerLayout.addView(innerRoundedImageView);

            TextView textView3 = new TextView(context);
            LinearLayout.LayoutParams textviewParams3 = new LinearLayout.LayoutParams(
                    dpToPx(context, 70),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            textviewParams3.setMargins(dpToPx(context, 7), dpToPx(context, 7), 0, dpToPx(context, 10));
            textView3.setLayoutParams(textviewParams3);
            textView3.setPadding(dpToPx(context, 4), 0, 0, 0);
            textView3.setText(name);
            textView3.setTextSize(14);
            textView3.setMaxLines(1);
            textView3.setEllipsize(TextUtils.TruncateAt.END);
            textView3.setTextColor(Color.parseColor("#979598"));
            innerLayout.addView(textView3);

            TextView textView4 = new TextView(context);
            LinearLayout.LayoutParams textviewParams4 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textviewParams4.setMargins(dpToPx(context, 5), dpToPx(context, 6), 0, dpToPx(context, 10));
            textView4.setLayoutParams(textviewParams4);
            textView4.setPadding(dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2));
            getAttractiveness(item.getSellerId());
            textView4.setText(credit);
            textView4.setTextSize(10);
            textView4.setBackgroundResource(R.drawable.sell_score);
            textView4.setTextColor(Color.parseColor("#f27000"));
            innerLayout.addView(textView4);
            itemLayout.addView(innerLayout);
            itemLayout.setOnClickListener(view -> {
                Intent intent = new Intent(CollectActivity.this, ItemDetailActivity.class);
                startActivity(intent);
            });

            if (items.indexOf(item) % 2 == 0) {
                item_show1.addView(itemLayout);
            } else {
                item_show2.addView(itemLayout);
            }

        }

    }

    // dp 转 px 工具函数
    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}