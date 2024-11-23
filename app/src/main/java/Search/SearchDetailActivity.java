package Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.util.Arrays;
import java.util.List;

import Main.MainActivity;
import Main.SearchDoneActivity;

public class SearchDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView searchImage;
    private LinearLayout searchHistory;
    private LinearLayout searchGuess;
    private EditText editText;
    private int id ;


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

        // searchImage动作
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchDetailActivity.this, SearchDoneActivity.class);
                startActivity(intent);
            }
        });

        editText.requestFocus();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params2 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout = new FlexboxLayout(this);
        newLinearLayout.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout.setJustifyContent(JustifyContent.FLEX_START);
        Context context = this;
        newLinearLayout.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
        List<String> list = Arrays.asList("历史搜索项 1", "历史搜索项 2", "历史搜索项 3", "历史搜索项 4", "历史搜索项 5", "历史搜索项 6", "历史搜索项 7");
        for (String str : list) {
            FlexboxLayout Layout = new FlexboxLayout(this);
            Layout.setFlexWrap(FlexWrap.WRAP);
            Layout.setAlignItems(AlignItems.FLEX_START);
            Layout.setJustifyContent(JustifyContent.FLEX_START);
            Drawable drawable = getResources().getDrawable(R.drawable.search_bg);
            Layout.setBackground(drawable);
            Layout.setPadding(10,10,10,10);
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
                    Log.d("111", view+"");
                    editText.setText(((TextView)((FlexboxLayout)view).getChildAt(0)).getText());
                    editText.setSelection(editText.getText().length());


                }
            });
        }
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
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    // 处理回车键事件
                    Log.d("Keyboard", "Enter key pressed!");
                    // 在这里处理回车键事件，比如提交数据
                    return true;  // 返回 true 表示事件已处理
                }
                return false;
            }
        });
            searchHistory.addView(newLinearLayout);

    }
    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}