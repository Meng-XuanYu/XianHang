package Profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private LinearLayout user_comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        user_comments = findViewById(R.id.user_comments);
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

    private int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}