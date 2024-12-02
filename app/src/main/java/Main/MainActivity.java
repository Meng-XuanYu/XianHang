package Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import widgets.NavigateTabBar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG_PAGE_HOME    = "闲航";
    private static final String TAG_PAGE_CITY    = "校区";
    private static final String TAG_PAGE_MORE    = "发布"; //deprecated
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_PERSON  = "我的";

    private NavigateTabBar mNavigateTabBar;
    private ImageView mTabMoreIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        this.mNavigateTabBar = findViewById(R.id.main_tabbar);
        this.mTabMoreIv = findViewById(R.id.tab_more_iv);

        this.mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        this.mNavigateTabBar.addTab(fragment.HomeFragment.class, new NavigateTabBar.TabParam(R.drawable.home, R.drawable.home_filled, TAG_PAGE_HOME));
        this.mNavigateTabBar.addTab(fragment.CityFragment.class, new NavigateTabBar.TabParam(R.drawable.location, R.drawable.location_filled, TAG_PAGE_CITY));
        this.mNavigateTabBar.addTab(null, new NavigateTabBar.TabParam(0, 0, TAG_PAGE_MORE));
        this.mNavigateTabBar.addTab(fragment.MessageFragment.class, new NavigateTabBar.TabParam(R.drawable.message, R.drawable.message_filled, TAG_PAGE_MESSAGE));
        this.mNavigateTabBar.addTab(fragment.PersonFragment.class, new NavigateTabBar.TabParam(R.drawable.profile, R.drawable.profile_filled, TAG_PAGE_PERSON));

        mNavigateTabBar.setRedDotVisibility(TAG_PAGE_MESSAGE, true);
        // 点击发布按钮
        this.mTabMoreIv.setOnClickListener(v -> PopMenuView.getInstance().show(MainActivity.this, mTabMoreIv, this));

        // 返回键监听
        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (PopMenuView.getInstance().isShowing()) {
                    PopMenuView.getInstance().closePopupWindowAction();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Intent intent = new Intent(MainActivity.this, WaitActivity.class);
            if (selectedImage != null) {
                intent.putExtra("imageUri", selectedImage.toString());
            } else {
                Log.e("MainActivity", "onActivityResult: selectedImage is null");
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mNavigateTabBar.onSaveInstanceState(outState);
    }
}