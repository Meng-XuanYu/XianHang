package Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Arrays;
import java.util.List;

import Search.SearchDetailActivity;
import goodsPage.ItemDetailActivity;

public class ProfileView extends AppCompatActivity {
    private LinearLayout item_show1;
    private LinearLayout item_show2;

    private LinearLayout user_comments;

    private TextView textSelling, textReviews;
    private ScrollView scrollView, contentSelling, contentReviews;
    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);

    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // 初始化视图
        user_comments = findViewById(R.id.user_comments);
        textSelling = findViewById(R.id.text_selling);
        textReviews = findViewById(R.id.text_reviews);
        contentSelling = findViewById(R.id.selling);
        contentReviews = findViewById(R.id.reviews);
        scrollView = findViewById(R.id.scrollView);


        // 设置点击事件
        textSelling.setOnClickListener(v -> selectTabSell(textSelling, contentSelling));
        textReviews.setOnClickListener(v -> selectTabReviews(textReviews, contentReviews));

        textSelling.setTextColor(ContextCompat.getColor(this, R.color.blue_bh));  // 设置蓝色字体
        textSelling.setPaintFlags(textSelling.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 加下划线
        // 填充 content_selling 布局
        item_show1 = findViewById(R.id.d_item_show1);
        item_show2 = findViewById(R.id.d_item_show2);

        generateLayout(this, items);
        generateComments(this, null);

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

    private void generateLayout(Context context, List<String> items) {
        contentReviews.setVisibility(View.GONE);
        for (String item : items) {
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
            roundedImageView.setImageResource(img.get(items.indexOf(item) % 2));
            roundedImageView.setCornerRadius(dpToPx(context, 10), dpToPx(context, 10), 0, 0);
            itemLayout.addView(roundedImageView);

            TextView textView1 = new TextView(context);
            LinearLayout.LayoutParams textviewParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textviewParams1.setMargins(dpToPx(context, 10), dpToPx(context, 10), 0, dpToPx(context, 5));
            textView1.setLayoutParams(textviewParams1);
            textView1.setText("商品标题 1111111111");
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
            textView2.setText("￥1111");
            textView2.setTextSize(18);
            textView2.setTypeface(null, Typeface.BOLD);
            textView2.setTextColor(Color.parseColor("#fd424b"));
            itemLayout.addView(textView2);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileView.this, ItemDetailActivity.class);
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

    private void generateComments(Context context, List<String> comments) {
        for (int i = 0; i < 5; i++) {
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
            roundedImageView.setImageResource(R.drawable.item1);
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
            textView.setText("用户名");
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
            textview1.setText("校区：北京航空航天大学  学院路校区");
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
            textview2.setText("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
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
            textview3.setText("2024-11-15 16:33:31");
            layout2.addView(textview3);
            user_comments.addView(layout1);
        }
    }

    // dp 转 px 工具函数
    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}