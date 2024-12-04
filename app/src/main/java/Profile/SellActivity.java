package Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import model.GetMySellResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellActivity extends AppCompatActivity {
    private LinearLayout item_show1;
    private LinearLayout item_show2;

    private List<GetMySellResponse.Trade> trades;
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
        setContentView(R.layout.activity_sell);
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

        getMySell();

    }

    private void getMySell() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.getMySell(userId).enqueue(new Callback<GetMySellResponse>() {
            @Override
            public void onResponse(Call<GetMySellResponse> call, Response<GetMySellResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetMySellResponse getMySellResponse = response.body();
                    if ("success".equals(getMySellResponse.getStatus())) {
                        trades = getMySellResponse.getTrades();
                        generateLayout(SellActivity.this, trades);
                    } else {
                        // 登录失败
                        Toast.makeText(SellActivity.this, "getMySellResponse.getMessage()", Toast.LENGTH_SHORT).show();
                        Log.e("MySell", "Error: " + "getMySellResponse.getMessage()");
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(SellActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("MySell", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(SellActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("MySell", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMySellResponse> call, Throwable t) {
                Toast.makeText(SellActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MySell", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void generateLayout(Context context, List<GetMySellResponse.Trade> items) {
        if (items == null) {
            return;
        }
        for (GetMySellResponse.Trade item : items) {
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

            final int temp = Integer.parseInt(item.getCommodityId());
            itemLayout.setOnClickListener(view -> {
                Intent intent = new Intent(SellActivity.this, ItemDetailActivity.class);
                intent.putExtra("commodityId",temp);
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