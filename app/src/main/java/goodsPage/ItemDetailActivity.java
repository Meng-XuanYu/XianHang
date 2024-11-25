package goodsPage;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Main.MainActivity;
import Search.SearchDetailActivity;

public class ItemDetailActivity extends AppCompatActivity {
    private ImageView search_back;
    private ImageView search_img;
    private TextView price;
    private TextView item_description;
    private LinearLayout imgs;
    private TextView comment_sum;
    private TextView comment;
    private LinearLayout user_comments;
    private LinearLayout recommends;
    private ImageView child;
    private ScrollView scrollView;
    private LinearLayout target;
    private EditText search;
    private LinearLayout item_show1;
    private LinearLayout item_show2;
    private ArrayList<Integer> imageUrls = new ArrayList<>();
    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);

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
        Context context = this;
        imageUrls.add(R.drawable.item1);
        imageUrls.add(R.drawable.item2);
        imageUrls.add(R.drawable.item3);
        imageUrls.add(R.drawable.item3);
        imageUrls.add(R.drawable.item3);
        imageUrls.add(R.drawable.item3);
        search.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                search.clearFocus();
                Intent intent = new Intent(ItemDetailActivity.this, SearchDetailActivity.class);
                startActivity(intent);

            }
        });
        LayoutInflater inflater = LayoutInflater.from(ItemDetailActivity.this);
        int x = imageUrls.size();
        for (int j = 1; x - j * 2 > 0; j++) {
            View childView = inflater.inflate(R.layout.img_layout, imgs, false);
            imgs.addView(childView);
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            ((LinearLayout) imgs.getChildAt(i / 2)).getChildAt(i % 2).setBackgroundResource(imageUrls.get(i));
        }
        child = findViewById(R.id.child);
        scrollView = findViewById(R.id.scrollview);
        target = findViewById(R.id.target);

        // 返回按钮
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 搜索按钮
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailActivity.this, SearchDetailActivity.class);
                startActivity(intent);
            }
        });

        // 评论按钮
        imgs.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x1 = event.getX(); // 相对于视图的 X 坐标
                float y = event.getY();
                int posx = (int) x1 / dpToPx(context, 186);
                int posy = (int) y / dpToPx(context, 220);
                int pos = posy * 2 + posx;
                Intent intent = new Intent(ItemDetailActivity.this, FullscreenActivity.class);
                intent.putIntegerArrayListExtra("images", imageUrls);
                intent.putExtra("position", pos);
                startActivity(intent);
            }
            return true;
        });
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, target.getTop()));
            }
        });
        generateLayout(this, items);
        generateComments(this, null);

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

    private void generateLayout(Context context, List<String> items) {
        for (String item : items) {
            LinearLayout itemLayout = new LinearLayout(context);
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
            textviewParams2.setMargins(dpToPx(context, 10), 0, 0, dpToPx(context, 5));
            textView2.setLayoutParams(textviewParams2);
            textView2.setText("￥1111");
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
            innerRoundedImageView.setImageResource(R.drawable.item);
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
            textView3.setText("用户名1111");
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
            textView4.setText("卖家信用优秀");
            textView4.setTextSize(10);
            textView4.setBackgroundResource(R.drawable.sell_score);
            textView4.setTextColor(Color.parseColor("#f27000"));
            innerLayout.addView(textView4);
            itemLayout.addView(innerLayout);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
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

    // dp 转 px 工具函数
    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}