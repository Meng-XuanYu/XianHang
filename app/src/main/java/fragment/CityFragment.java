package fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Arrays;
import java.util.List;

import Search.SearchDetailActivity;
import goodsPage.ItemDetailActivity;

public class CityFragment extends Fragment {

    private ImageView pos_img;
    private LinearLayout item_show1;
    private LinearLayout item_show2;
    private LinearLayout search_layout;

    private List<String> items = Arrays.asList(
            "商品标题1", "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5", "商品标题1",
            "￥1000", "用户1", "4.5", "商品标题1", "￥1000", "用户1", "4.5"
    );
    private List<Integer> img = Arrays.asList(R.drawable.item, R.drawable.item1);
    private int location = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        item_show1 = view.findViewById(R.id.d_item_show1);
        item_show2 = view.findViewById(R.id.d_item_show2);
        search_layout = view.findViewById(R.id.search_layout);
        EditText editText = view.findViewById(R.id.searchEditText1);
        pos_img = view.findViewById(R.id.pos_img);
        if (location == 0) {
            pos_img.setBackgroundResource(R.drawable.xueyuanlu);
        } else if  (location == 1) {
            pos_img.setBackgroundResource(R.drawable.shahe);
        } else {
            pos_img.setBackgroundResource(R.drawable.hangzhou);
        }

        // 跳转到搜索页面
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.clearFocus();
                Intent intent = new Intent(requireActivity(), SearchDetailActivity.class);
                startActivity(intent);
            }
        });

        search_layout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), SearchDetailActivity.class);
            startActivity(intent);
        });

        generateLayout(requireContext(), items);

        return view;
    }

    private void generateLayout(Context context, List<String> items) {
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
                    Intent intent = new Intent(requireActivity(), ItemDetailActivity.class);
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
