package Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Main.SearchDoneActivity;
import RetrofitClient.RetrofitClient;
import model.AISearchRequest;
import model.GetCommodityResponse;
import model.GetCommodityResponse;
import model.SearchCommodityResponse;
import model.SearchCommodityResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView searchImage;
    private ImageView aiSearchImage;
    private LinearLayout searchHistory;
    private LinearLayout searchGuess;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_detail);
        imageView = findViewById(R.id.search_back);
        searchHistory = findViewById(R.id.search_history);
        searchGuess = findViewById(R.id.search_guess);
        editText = findViewById(R.id.searchEditText2);
        searchImage = findViewById(R.id.search_img);
        aiSearchImage = findViewById(R.id.ai_search);


        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        // searchImage动作
        searchImage.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(SearchDetailActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(SearchDetailActivity.this, SearchDoneActivity.class);
            intent.putExtra("text", editText.getText().toString());
            SharedPreferences sharedPreferences = getSharedPreferences("search_history", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            List<String> searchHistoryList = new ArrayList<>();

            String json = sharedPreferences.getString("search_history", null);
            if (json != null) {
                Type type = new TypeToken<List<String>>() {}.getType();
                searchHistoryList = gson.fromJson(json, type);
            }

            searchHistoryList.add(editText.getText().toString());

            String newJson = gson.toJson(searchHistoryList);
            editor.putString("search_history", newJson);
            editor.apply();
            startActivity(intent);
            finish();
        });
        aiSearchImage.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(SearchDetailActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(SearchDetailActivity.this, SearchDoneActivity.class);
            intent.putExtra("ai_text", editText.getText().toString());
            startActivity(intent);
            finish();
        });

        editText.requestFocus();
        imageView.setOnClickListener(view -> finish());
        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params2 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout = new FlexboxLayout(this);
        newLinearLayout.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout.setJustifyContent(JustifyContent.FLEX_START);
        newLinearLayout.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
        SharedPreferences sharedPreferences = getSharedPreferences("search_history", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        List<String> searchHistoryList = new ArrayList<>();

        String json = sharedPreferences.getString("search_history", null);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            searchHistoryList = gson.fromJson(json, type);
        }

        for (int i = 0; i < searchHistoryList.size() && i < 7; i++) {
            String str = searchHistoryList.get(i);
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
                Log.d("111", view + "");
                editText.setText(((TextView) ((FlexboxLayout) view).getChildAt(0)).getText());
                editText.setSelection(editText.getText().length());
            });
        }
        searchHistory.addView(newLinearLayout);

        FlexboxLayout.LayoutParams params11 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params11.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params21 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout1 = new FlexboxLayout(this);
        newLinearLayout1.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout1.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout1.setJustifyContent(JustifyContent.FLEX_START);
        newLinearLayout1.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
        List<String> list = Arrays.asList("手机", "电脑", "耳机", "鼠标", "键盘", "显示器", "电视", "冰箱", "洗衣机", "空调", "电饭煲", "电磁炉");

        Collections.shuffle(list);

        List<String> randomItems = list.subList(0, 3);
        for (String str : randomItems) {
            FlexboxLayout Layout = new FlexboxLayout(this);
            Layout.setFlexWrap(FlexWrap.WRAP);
            Layout.setAlignItems(AlignItems.FLEX_START);
            Layout.setJustifyContent(JustifyContent.FLEX_START);
            Drawable drawable = getResources().getDrawable(R.drawable.search_bg);
            Layout.setBackground(drawable);
            Layout.setPadding(10, 10, 10, 10);
            Layout.setLayoutParams(params11);
            TextView textView = new TextView(this);
            textView.setText(str);
            textView.setTextSize(10);  // 设置字体大小
            textView.setTextColor(Color.BLACK);  // 设置字体颜色
            textView.setPadding(10, 10, 0, 10);  // 设置内边距
            textView.setLayoutParams(params21);
            Layout.addView(textView);
            newLinearLayout1.addView(Layout);

            Layout.setOnClickListener(view -> {
                editText.setText(((TextView) ((FlexboxLayout) view).getChildAt(0)).getText());
                editText.setSelection(editText.getText().length());
            });
        }
        searchGuess.addView(newLinearLayout1);
        View rootView = findViewById(android.R.id.content);


        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 判断点击的位置是否在 EditText 外部
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View focusedView = getCurrentFocus();
                    if (focusedView instanceof EditText) {
                        Rect outRect = new Rect();
                        focusedView.getGlobalVisibleRect(outRect); // 获取视图的矩形区域
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            // 如果点击的位置不在 EditText 内部，则失去焦点
                            focusedView.clearFocus();
                            // 可选：隐藏软键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

    }
}