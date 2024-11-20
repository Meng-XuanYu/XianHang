package Main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

import widgets.NavigateTabBar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG_PAGE_HOME    = "首页";
    private static final String TAG_PAGE_CITY    = "同城";
    private static final String TAG_PAGE_MORE    = "发布";
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

        this.mNavigateTabBar.addTab(com.pzj.navigatetabbar.fragment.HomeFragment.class, new NavigateTabBar.TabParam(R.drawable.home, R.drawable.home_filled, TAG_PAGE_HOME));
        this.mNavigateTabBar.addTab(com.pzj.navigatetabbar.fragment.CityFragment.class, new NavigateTabBar.TabParam(R.drawable.location, R.drawable.location_filled, TAG_PAGE_CITY));
        this.mNavigateTabBar.addTab(null, new NavigateTabBar.TabParam(0, 0, TAG_PAGE_MORE));
        this.mNavigateTabBar.addTab(com.pzj.navigatetabbar.fragment.MessageFragment.class, new NavigateTabBar.TabParam(R.drawable.message, R.drawable.message_filled, TAG_PAGE_MESSAGE));
        this.mNavigateTabBar.addTab(com.pzj.navigatetabbar.fragment.PersonFragment.class, new NavigateTabBar.TabParam(R.drawable.profile, R.drawable.profile_filled, TAG_PAGE_PERSON));

        this.mTabMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMenuView.getInstance().show(MainActivity.this, mTabMoreIv);
            }
        });

        // Handle back press
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mNavigateTabBar.onSaveInstanceState(outState);
    }
}