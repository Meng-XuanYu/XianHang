package Profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SetNicknameActivity extends AppCompatActivity {
    private LinearLayout nickname_recommend;
    private EditText nickname ;

    private ImageView back ;
    private Button save;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_nickname);
        name = getIntent().getStringExtra("nickname");
        nickname_recommend = findViewById(R.id.nickname_recommend);
        nickname = findViewById(R.id.nickname);
        nickname.setText(name);
        back = findViewById(R.id.backButton);
        save = findViewById(R.id.save);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SetNicknameActivity.this,EditUserInfoActivity.class);
                intent.putExtra("nickname",name);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nickname.getText().toString();
                nickname.clearFocus();
            }
        });

        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 10, 15, 10);

        FlexboxLayout.LayoutParams params2 = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);

        FlexboxLayout newLinearLayout = new FlexboxLayout(this);
        newLinearLayout.setFlexWrap(FlexWrap.WRAP);
        newLinearLayout.setAlignItems(AlignItems.FLEX_START);
        newLinearLayout.setJustifyContent(JustifyContent.FLEX_START);
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
                    nickname.setText(((TextView)((FlexboxLayout)view).getChildAt(0)).getText());
                    nickname.clearFocus();
                }
            });
        }
        nickname_recommend.addView(newLinearLayout);
    }
}