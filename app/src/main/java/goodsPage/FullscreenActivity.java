package goodsPage;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.login.R;

import java.util.List;

public class FullscreenActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fullscreen);


            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }

            ViewPager2 viewPager = findViewById(R.id.viewPager);

            List<String> imageUrls = getIntent().getStringArrayListExtra("images");
            int startPosition = getIntent().getIntExtra("position", 0);

            FullScreenAdapter adapter = new FullScreenAdapter(this, imageUrls);
            viewPager.setAdapter(adapter);

            // 设置当前显示的图片
            viewPager.setCurrentItem(startPosition, false);

            viewPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
