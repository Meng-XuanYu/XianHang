package goodsPage;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.login.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Main.MainActivity;
import Message.ChatActivity;
import Profile.BuyActivity;
import RetrofitClient.RetrofitClient;
import model.AIRecommendRequest;
import model.AIRecommendResponse;
import model.AddCollectionRequest;
import model.AddCollectionResponse;
import model.AddCommodityCommentRequest;
import model.AddCommodityCommentResponse;
import model.AddHistoryRequest;
import model.AddHistoryResponse;
import model.DeleteCollectionRequest;
import model.DeleteCollectionResponse;
import model.GetCommodityCommentResponse;
import model.GetCommodityListByClassResponse;
import model.GetCommodityResponse;
import model.GetMyBuyResponse;
import model.GetOrCreateChatRequest;
import model.GetOrCreateChatResponse;
import model.GetProfileResponse;
import model.IsCollectedRequest;
import model.IsCollectedResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    private ImageView search_back;
    private ImageView search_img;
    private TextView price;
    private TextView item_description;
    private LinearLayout imgs;
    private TextView comment_sum;
    private EditText comment;
    private LinearLayout user_comments;
    private RelativeLayout recommends;
    private ImageView child;
    private ScrollView scrollView;
    private LinearLayout target;
    private EditText search;
    private LinearLayout item_show1;
    private LinearLayout item_show2;
    private RoundedImageView user_img1;
    private TextView user_name1;
    private TextView user_pos1;
    private RoundedImageView user_img2;
    private ImageView send;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private GetCommodityResponse.Commodity commodity;
    private ImageView collect;
    private Button want;

    private List<GetCommodityListByClassResponse.Commodity> commodities;
    private List<AIRecommendResponse.Recommendation> aiCommodities;
    private boolean aiFinished = false;
    private boolean allFinished = false;
    private boolean isCollected;
    private String id;
    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);

    private String credit;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        setContentView(R.layout.activity_item_detail);

        id = String.valueOf(getIntent().getIntExtra("commodityId",0));
        item_show1 = findViewById(R.id.g_item_show1);
        item_show2 = findViewById(R.id.g_item_show2);
        search_back = findViewById(R.id.search_back);
        search_img = findViewById(R.id.search_img);
        price = findViewById(R.id.price);
        item_description = findViewById(R.id.item_description);
        imgs = findViewById(R.id.imgs);
        comment_sum = findViewById(R.id.comment_sum);
        comment = findViewById(R.id.comment);
        user_comments = findViewById(R.id.user_comments);
        recommends = findViewById(R.id.recommends);
        search = findViewById(R.id.searchEditText);
        collect = findViewById(R.id.collect);
        user_img1 = findViewById(R.id.user_img1);
        user_name1 = findViewById(R.id.user_name1);
        user_pos1 = findViewById(R.id.user_pos1);
        send = findViewById(R.id.send);
        user_img2 = findViewById(R.id.user_img2);
        want = findViewById(R.id.want);

        getIsCollected();
        addHistory();
        getDetail();
        getComments();

        Context context = this;
        send.setOnClickListener(view -> addComment());
        want.setOnClickListener(view -> {
            // 创建新的聊天
            createChat();
        });
        search.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                search.clearFocus();
                Intent intent = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
                startActivity(intent);
            }
        });

        child = findViewById(R.id.child);
        scrollView = findViewById(R.id.scrollview);
        target = findViewById(R.id.target);

        // 收藏按钮
        collect.setOnClickListener(view -> {
            // 按钮变色
            if(isCollected){
                collect.setImageResource(R.drawable.collect);
                deleteCollect();
            } else {
                collect.setImageResource(R.drawable.collected);
                addCollect();
            }

        });

        // 返回按钮
        search_back.setOnClickListener(view -> finish());

        // 搜索按钮
        search_img.setOnClickListener(view -> {
            Intent intent = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
            startActivity(intent);
        });

        imgs.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x1 = event.getX(); // 相对于视图的 X 坐标
                float y = event.getY();
                int posx = (int) x1 / dpToPx(context, 186);
                int posy = (int) y / dpToPx(context, 220);
                int pos = posy * 2 + posx;
                if (pos >= imageUrls.size()) {
                    return true;
                }
                Intent intent = new Intent(ItemDetailActivity.this, FullscreenActivity.class);
                intent.putStringArrayListExtra("images", imageUrls);
                intent.putExtra("position", pos);
                startActivity(intent);
            }
            return true;
        });
        child.setOnClickListener(view -> scrollView.post(() -> scrollView.smoothScrollTo(0, target.getTop())));
        generateComments(this, null);
        getAll();
    }

    private void createChat() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String receiverId = commodity.getSellerId();
        String commodityId = commodity.getCommodityId();
        if (userId != null && userId.equals(receiverId)) {
            Toast.makeText(ItemDetailActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
            return;
        }
        GetOrCreateChatRequest request = new GetOrCreateChatRequest(userId, receiverId, commodityId);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOrCreateChat(request).enqueue(new Callback<GetOrCreateChatResponse>() {
            @Override
            public void onResponse(Call<GetOrCreateChatResponse> call, Response<GetOrCreateChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetOrCreateChatResponse getOrCreateChatResponse = response.body();
                    Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                    intent.putExtra("chatId", getOrCreateChatResponse.getChatId());
                    startActivity(intent);
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("CreateChat", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("CreateChat", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetOrCreateChatResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CreateChat", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void getIsCollected() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.isCollected(new IsCollectedRequest(profileJson, id)).enqueue(new Callback<IsCollectedResponse>() {
            @Override
            public void onResponse(Call<IsCollectedResponse> call, Response<IsCollectedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    IsCollectedResponse isCollectedResponse = response.body();
                    if ("success".equals(isCollectedResponse.getStatus())) {
                        isCollected = isCollectedResponse.getIsCollected().equals("True");
                        if(isCollected){
                            collect.setImageResource(R.drawable.collected);
                        } else {
                            collect.setImageResource(R.drawable.collect);
                        }
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + isCollectedResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("IsCollected", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("IsCollected", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsCollectedResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("IsCollected", "Request Failed: " + t.getMessage());
            }
        });
    }

    public void addCollect() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.addCollection(new AddCollectionRequest(profileJson, id)).enqueue(new Callback<AddCollectionResponse>() {
            @Override
            public void onResponse(Call<AddCollectionResponse> call, Response<AddCollectionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddCollectionResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {
                        isCollected = true;
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("AddCollection", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("AddCollection", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddCollectionResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddCollection", "Request Failed: " + t.getMessage());
            }
        });
    }

    public void deleteCollect() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.deleteCollected(new DeleteCollectionRequest(profileJson, id)).enqueue(new Callback<DeleteCollectionResponse>() {
            @Override
            public void onResponse(Call<DeleteCollectionResponse> call, Response<DeleteCollectionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DeleteCollectionResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {
                        isCollected = false;
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("AddCollection", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("AddCollection", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteCollectionResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddCollection", "Request Failed: " + t.getMessage());
            }
        });
    }

    public void addHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.addHistory(new AddHistoryRequest(profileJson, id)).enqueue(new Callback<AddHistoryResponse>() {
            @Override
            public void onResponse(Call<AddHistoryResponse> call, Response<AddHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddHistoryResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {

                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("addHistory", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("addHistory", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddHistoryResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("addHistory", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void getDetail() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommodity(id).enqueue(new Callback<GetCommodityResponse>() {
            @Override
            public void onResponse(Call<GetCommodityResponse> call, Response<GetCommodityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityResponse getCommodityResponse = response.body();
                    if ("success".equals(getCommodityResponse.getStatus())) {
                        commodity = getCommodityResponse.getCommodity();
                        Glide.with(ItemDetailActivity.this)
                                .load(commodity.getSellerImage())
                                .placeholder(R.drawable.img)  // 占位图
                                .error(R.drawable.img).into(user_img1);
                        user_name1.setText(commodity.getSellerName());
                        user_pos1.setText(commodity.getSchool());
                        price.setText(commodity.getCommodityValue());
                        item_description.setText(commodity.getCommodityDescription());
                        imageUrls = commodity.getAdditionalImages();
                        LayoutInflater inflater = LayoutInflater.from(ItemDetailActivity.this);
                        int x = imageUrls.size();
                        for (int j = 1; x - j * 2 > 0; j++) {
                            View childView = inflater.inflate(R.layout.img_layout, imgs, false);
                            imgs.addView(childView);
                        }
                        for (int i = 0; i < imageUrls.size(); i++) {
                            final int index = i;
                            Glide.with(ItemDetailActivity.this)
                                    .load(imageUrls.get(i))
                                    .placeholder(R.drawable.img)  // 占位图
                                    .error(R.drawable.img).into(
                                            ((ImageView)((LinearLayout) imgs.getChildAt(index / 2)).getChildAt(index % 2))
                                            );
                        }
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getDetail", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommodityResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getDetail", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void getComments() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        Gson gson = new Gson();
        GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommodityComment(id).enqueue(new Callback<GetCommodityCommentResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetCommodityCommentResponse> call, Response<GetCommodityCommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityCommentResponse getCommodityCommentResponse = response.body();
                    if ("success".equals(getCommodityCommentResponse.getStatus())) {
                        Glide.with(ItemDetailActivity.this)
                                .load(profile.getAvatar())
                                .placeholder(R.drawable.img)  // 占位图
                                .error(R.drawable.img).into(user_img2);
                        comment_sum.setText((getCommodityCommentResponse.getComment() != null ?
                                getCommodityCommentResponse.getComment().size() : 0) + "条留言");
                        generateComments(ItemDetailActivity.this, getCommodityCommentResponse.getComment());
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityCommentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getComments", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getComments", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommodityCommentResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getComments", "Request Failed: " + t.getMessage());
            }
        });
    }

    public void addComment() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.addCommodityComment(new AddCommodityCommentRequest(id, profileJson, comment.getText().toString())).enqueue(
                new Callback<AddCommodityCommentResponse>() {
                    @Override
                    public void onResponse(Call<AddCommodityCommentResponse> call, Response<AddCommodityCommentResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AddCommodityCommentResponse getCommodityCommentResponse = response.body();
                            if ("success".equals(getCommodityCommentResponse.getStatus())) {
                                comment.setText("");
                                comment.clearFocus();
                                removeLay();
                                getComments();
                            } else {
                                Toast.makeText(ItemDetailActivity.this, "获取用户信息失败: " + getCommodityCommentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                // 从 errorBody 获取错误信息
                                String errorJson = response.errorBody().string();
                                JSONObject errorObject = new JSONObject(errorJson);
                                String errorMessage = errorObject.optString("message", "未知错误");
                                Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                Log.e("AddComment", "Error: " + errorMessage);
                            } catch (Exception e) {
                                Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                                Log.e("AddComment", "Error parsing error body: ", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddCommodityCommentResponse> call, Throwable t) {
                        Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddComment", "Request Failed: " + t.getMessage());
                    }
                });
    }

    private void removeLay() {
        int count = user_comments.getChildCount();
        for (int i = 0; i < count; i++) {
            user_comments.removeView(user_comments.getChildAt(0));
        }
    }

    private void generateComments(Context context, List<GetCommodityCommentResponse.Comment> comments) {
        if (comments == null) {
            return;
        }
        for (GetCommodityCommentResponse.Comment comment1 : comments) {
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
                    .load(comment1.getUserImage())
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
            textView.setText(comment1.getUserName());
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
            textview1.setText(comment1.getUserSchool());
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
            textview2.setText(comment1.getLeaveMessageContent());
            textview2.setTextSize(16);
            layout2.addView(textview2);

            TextView textview3 = new TextView(context);
            LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params6.setMargins(dpToPx(context, 10), dpToPx(context, 5), 0, 0);
            textview3.setLayoutParams(params6);
            textview3.setTextColor(Color.parseColor("#939393"));
            textview3.setText(comment1.getLeaveMessageDate());
            layout2.addView(textview3);
            user_comments.addView(layout1);
        }
    }

    private void getAll() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.getCommodityListByClass("全部").enqueue(new Callback<GetCommodityListByClassResponse>() {
            @Override
            public void onResponse(Call<GetCommodityListByClassResponse> call, Response<GetCommodityListByClassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityListByClassResponse getHistoryResponse = response.body();
                    if ("success".equals(getHistoryResponse.getStatus())) {
                        commodities = getHistoryResponse.getCommodities();
                        allFinished = true;
                        checkAndLay();
                    } else {
                        // 登录失败
                        Toast.makeText(ItemDetailActivity.this, getHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("All", "Error: " + getHistoryResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("All", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("All", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommodityListByClassResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("All", "Request Failed: " + t.getMessage());
            }
        });
        apiService.aiRecommend(new AIRecommendRequest(userId)).enqueue(new Callback<AIRecommendResponse>() {
            @Override
            public void onResponse(Call<AIRecommendResponse> call, Response<AIRecommendResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AIRecommendResponse aIRecommendResponse = response.body();
                    if ("success".equals(aIRecommendResponse.getStatus())) {
                        aiCommodities = aIRecommendResponse.getRecommendations();
                        aiFinished = true;
                        checkAndLay();
                    } else {
                        // 登录失败
                        Toast.makeText(ItemDetailActivity.this, aIRecommendResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AIAll", "Error: " + aIRecommendResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ItemDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("AIAll", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("AIAll", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AIRecommendResponse> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AIAll", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void checkAndLay() {
        if (aiFinished && allFinished) {
            generateLayout(ItemDetailActivity.this, aiCommodities, commodities);
        }
    }


    private void generateLayout(Context context, List<AIRecommendResponse.Recommendation> recommendations, List<GetCommodityListByClassResponse.Commodity> items) {
        if (commodities == null) {
            return;
        }
        ArrayList<Integer> list = new ArrayList<>(); // 用于存储推荐商品的id
        for (AIRecommendResponse.Recommendation item : recommendations) {
            list.add(Integer.parseInt(item.getCommodityId()));
        }
        Iterator<GetCommodityListByClassResponse.Commodity> iterator = items.iterator();
        while (iterator.hasNext()) {
            GetCommodityListByClassResponse.Commodity item = iterator.next();
            if (list.contains(Integer.parseInt(item.getCommodityId()))) {
                iterator.remove();
            } else {
                list.add(Integer.parseInt(item.getCommodityId()));
            }
        }
        int index = 0;
        for (; index < recommendations.size(); index++) {
            AIRecommendResponse.Recommendation item = recommendations.get(index);
            LinearLayout itemLayout = new LinearLayout(context);
            final int temp = Integer.parseInt(item.getCommodityId());
            itemLayout.setOnClickListener(view -> {
                Intent intent = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
                intent.putExtra("commodityId", temp);
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
            Glide.with(context)
                    .load(item.getSellerImage())
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
            textviewParams4.setMargins(dpToPx(context, 5), dpToPx(context, 6), 0, dpToPx(context, 10));
            textView4.setLayoutParams(textviewParams4);
            textView4.setPadding(dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2));
            int attractiveness = Integer.parseInt(item.getSellerAttractiveness());
            if (attractiveness >= 500 && attractiveness < 550) {
                credit = "卖家信用一般";
            } else if (attractiveness >= 550 && attractiveness < 750) {
                credit = "卖家信用良好";
            } else if (attractiveness >= 750 && attractiveness < 1000) {
                credit = "卖家信用优秀";
            } else if (attractiveness >= 1000) {
                credit = "卖家信用极好";
            }
            textView4.setText(credit);
            textView4.setTextSize(10);
            textView4.setBackgroundResource(R.drawable.sell_score);
            textView4.setTextColor(Color.parseColor("#f27000"));
            innerLayout.addView(textView4);
            itemLayout.addView(innerLayout);


            if (index % 2 == 0) {
                item_show1.addView(itemLayout);
            } else {
                item_show2.addView(itemLayout);
            }
        }
        for (; index - recommendations.size() < items.size(); index++) {
            GetCommodityListByClassResponse.Commodity item = items.get(index);
            LinearLayout itemLayout = new LinearLayout(context);
            final int temp = Integer.parseInt(item.getCommodityId());
            itemLayout.setOnClickListener(view -> {
                Intent intent = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
                intent.putExtra("commodityId", temp);
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
            Glide.with(context)
                    .load(item.getSellerImage())
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
            textviewParams4.setMargins(dpToPx(context, 5), dpToPx(context, 6), 0, dpToPx(context, 10));
            textView4.setLayoutParams(textviewParams4);
            textView4.setPadding(dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2), dpToPx(context, 2));
            int attractiveness = Integer.parseInt(item.getSellerAttractiveness());
            if (attractiveness >= 500 && attractiveness < 550) {
                credit = "卖家信用一般";
            } else if (attractiveness >= 550 && attractiveness < 750) {
                credit = "卖家信用良好";
            } else if (attractiveness >= 750 && attractiveness < 1000) {
                credit = "卖家信用优秀";
            } else if (attractiveness >= 1000) {
                credit = "卖家信用极好";
            }
            textView4.setText(credit);
            textView4.setTextSize(10);
            textView4.setBackgroundResource(R.drawable.sell_score);
            textView4.setTextColor(Color.parseColor("#f27000"));
            innerLayout.addView(textView4);
            itemLayout.addView(innerLayout);


            if (index % 2 == 0) {
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