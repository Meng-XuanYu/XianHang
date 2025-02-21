package Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.List;

import RetrofitClient.RetrofitClient;
import Search.SearchDetailActivity;
import goodsPage.ItemDetailActivity;
import model.AISearchRequest;
import model.AISearchResponse;
import model.SearchCommodityResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDoneActivity extends AppCompatActivity {

    private LinearLayout search_layout;

    private ImageView search_back;
    private String searchText;
    private String aiSearchText;

    private String name;
    private String credit;
    private String avatar;

    private List<SearchCommodityResponse.Commodity> commodities = null;
    private List<AISearchResponse.Commodity> aiCommodities = null;

    private LinearLayout item_show1;
    private LinearLayout item_show2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_done);
        searchText = getIntent().getStringExtra("text");
        aiSearchText = getIntent().getStringExtra("ai_text");
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        item_show1 = findViewById(R.id.d_item_show1);
        item_show2 = findViewById(R.id.d_item_show2);
        search_back = findViewById(R.id.search_back);

        search_layout = findViewById(R.id.search_layout);
        search_layout.setOnClickListener(view -> {
            Intent intent = new Intent(SearchDoneActivity.this, SearchDetailActivity.class);
            startActivity(intent);
            finish();

        });
        search_back.setOnClickListener(view -> {
            Intent intent = new Intent(SearchDoneActivity.this, SearchDetailActivity.class);
            startActivity(intent);
            finish();
        });
        if(searchText!=null){
            search();
            TextView searchEditText = findViewById(R.id.searchEditText);
            searchEditText.setText(searchText);
        } else {
            aiSearch();
            TextView searchEditText = findViewById(R.id.searchEditText);
            searchEditText.setText(aiSearchText);
        }
    }

    public void search() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.searchCommodity(searchText).enqueue(new Callback<SearchCommodityResponse>() {
            @Override
            public void onResponse(Call<SearchCommodityResponse> call, Response<SearchCommodityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchCommodityResponse searchCommodityResponse = response.body();
                    if ("success".equals(searchCommodityResponse.getStatus())) {
                        commodities = searchCommodityResponse.getCommodities();
                        generateLayout(SearchDoneActivity.this, commodities,aiCommodities);
                    } else {
                        Toast.makeText(SearchDoneActivity.this, "获取用户信息失败: " + searchCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(SearchDoneActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(SearchDoneActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchCommodityResponse> call, Throwable t) {
                Toast.makeText(SearchDoneActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getProfile", "Request Failed: " + t.getMessage());
            }
        });

    }
    public void aiSearch() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.aiSearch(new AISearchRequest(aiSearchText)).enqueue(new Callback<AISearchResponse>() {
            @Override
            public void onResponse(Call<AISearchResponse> call, Response<AISearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AISearchResponse aiSearchResponse = response.body();
                    if ("success".equals(aiSearchResponse.getStatus())) {
                        aiCommodities = aiSearchResponse.getResult();
                        generateLayout(SearchDoneActivity.this, commodities,aiCommodities);
                    } else {
                        Toast.makeText(SearchDoneActivity.this, "获取用户信息失败: " + aiSearchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(SearchDoneActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(SearchDoneActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AISearchResponse> call, Throwable t) {
                Toast.makeText(SearchDoneActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getProfile", "Request Failed: " + t.getMessage());
            }
        });

    }

    private void generateLayout(Context context, List<SearchCommodityResponse.Commodity> items,List<AISearchResponse.Commodity> items1) {
        if(items == null && items1 == null){
            return;
        }
        if (items != null){
            for (SearchCommodityResponse.Commodity item : items) {
                LinearLayout itemLayout = new LinearLayout(context);
                final int temp = Integer.parseInt(item.getCommodityId());
                itemLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(SearchDoneActivity.this, ItemDetailActivity.class);
                    intent.putExtra("commodityId",temp);
                    startActivity(intent);
                });
                itemLayout.setOrientation(LinearLayout.VERTICAL);
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
                roundedImageView.setCornerRadius(dpToPx(context,10),dpToPx(context,10),0,0);
                itemLayout.addView(roundedImageView);

                TextView textView1 = new TextView(context);
                LinearLayout.LayoutParams textviewParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                textviewParams1.setMargins(dpToPx(context,10),dpToPx(context,10),0,dpToPx(context,5));
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
                textviewParams2.setMargins(dpToPx(context,10),0,0,dpToPx(context,5));
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
                        dpToPx(context,25),
                        dpToPx(context,25)
                );
                innerImageParams1.setMargins(dpToPx(context,10),dpToPx(context,5),0,dpToPx(context,10));
                innerRoundedImageView.setLayoutParams(innerImageParams1);
                innerRoundedImageView.setBackgroundResource(R.drawable.img);
                innerRoundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .load(item.getSellerImage())
                        .placeholder(R.drawable.img)  // 占位图
                        .error(R.drawable.img).into(innerRoundedImageView);
                innerRoundedImageView.setCornerRadius(dpToPx(context,12.5f));
                innerLayout.addView(innerRoundedImageView);

                TextView textView3 = new TextView(context);
                LinearLayout.LayoutParams textviewParams3 = new LinearLayout.LayoutParams(
                        dpToPx(context,70),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                textviewParams3.setMargins(dpToPx(context,7),dpToPx(context,7),0,dpToPx(context,10));
                textView3.setLayoutParams(textviewParams3);
                textView3.setPadding(dpToPx(context,4),0,0,0);
                textView3.setText(item.getSellerName());
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
                textviewParams4.setMargins(dpToPx(context,5),dpToPx(context,6),0,dpToPx(context,10));
                textView4.setLayoutParams(textviewParams4);
                textView4.setPadding(dpToPx(context,2),dpToPx(context,2),dpToPx(context,2),dpToPx(context,2));
                int attractiveness = Integer.parseInt(item.getSellerAttractiveness());
                if (attractiveness >= 500 && attractiveness < 550) {
                    credit="卖家信用一般";
                } else if (attractiveness >= 550 && attractiveness < 750) {
                    credit="卖家信用良好";
                } else if (attractiveness >= 750 && attractiveness < 1000) {
                    credit="卖家信用优秀";
                } else if (attractiveness >= 1000) {
                    credit="卖家信用极好";
                }
                textView4.setText(credit);
                textView4.setTextSize(10);
                textView4.setBackgroundResource(R.drawable.sell_score);
                textView4.setTextColor(Color.parseColor("#f27000"));
                innerLayout.addView(textView4);
                itemLayout.addView(innerLayout);


                if(items.indexOf(item) %2 == 0){
                    item_show1.addView(itemLayout);
                } else   {
                    item_show2.addView(itemLayout);
                }

            }
        }else {
            for (AISearchResponse.Commodity item : items1) {
                LinearLayout itemLayout = new LinearLayout(context);
                final int temp = Integer.parseInt(item.getCommodityId());
                itemLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(SearchDoneActivity.this, ItemDetailActivity.class);
                    intent.putExtra("commodityId",temp);
                    startActivity(intent);
                });
                itemLayout.setOrientation(LinearLayout.VERTICAL);
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
                roundedImageView.setCornerRadius(dpToPx(context,10),dpToPx(context,10),0,0);
                itemLayout.addView(roundedImageView);

                TextView textView1 = new TextView(context);
                LinearLayout.LayoutParams textviewParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                textviewParams1.setMargins(dpToPx(context,10),dpToPx(context,10),0,dpToPx(context,5));
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
                textviewParams2.setMargins(dpToPx(context,10),0,0,dpToPx(context,5));
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
                        dpToPx(context,25),
                        dpToPx(context,25)
                );
                innerImageParams1.setMargins(dpToPx(context,10),dpToPx(context,5),0,dpToPx(context,10));
                innerRoundedImageView.setLayoutParams(innerImageParams1);
                innerRoundedImageView.setBackgroundResource(R.drawable.img);
                innerRoundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .load(item.getSellerImage())
                        .placeholder(R.drawable.img)  // 占位图
                        .error(R.drawable.img).into(innerRoundedImageView);
                innerRoundedImageView.setCornerRadius(dpToPx(context,12.5f));
                innerLayout.addView(innerRoundedImageView);

                TextView textView3 = new TextView(context);
                LinearLayout.LayoutParams textviewParams3 = new LinearLayout.LayoutParams(
                        dpToPx(context,70),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                textviewParams3.setMargins(dpToPx(context,7),dpToPx(context,7),0,dpToPx(context,10));
                textView3.setLayoutParams(textviewParams3);
                textView3.setPadding(dpToPx(context,4),0,0,0);
                textView3.setText(item.getSellerName());
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
                textviewParams4.setMargins(dpToPx(context,5),dpToPx(context,6),0,dpToPx(context,10));
                textView4.setLayoutParams(textviewParams4);
                textView4.setPadding(dpToPx(context,2),dpToPx(context,2),dpToPx(context,2),dpToPx(context,2));
                int attractiveness = Integer.parseInt(item.getSellerAttractiveness());
                if (attractiveness >= 500 && attractiveness < 550) {
                    credit="卖家信用一般";
                } else if (attractiveness >= 550 && attractiveness < 750) {
                    credit="卖家信用良好";
                } else if (attractiveness >= 750 && attractiveness < 1000) {
                    credit="卖家信用优秀";
                } else if (attractiveness >= 1000) {
                    credit="卖家信用极好";
                }
                textView4.setText(credit);
                textView4.setTextSize(10);
                textView4.setBackgroundResource(R.drawable.sell_score);
                textView4.setTextColor(Color.parseColor("#f27000"));
                innerLayout.addView(textView4);
                itemLayout.addView(innerLayout);


                if(items1.indexOf(item) %2 == 0){
                    item_show1.addView(itemLayout);
                } else   {
                    item_show2.addView(itemLayout);
                }

            }
        }

    }

    // dp 转 px 工具函数
    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}