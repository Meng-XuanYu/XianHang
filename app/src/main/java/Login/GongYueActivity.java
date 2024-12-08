package Login;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

public class GongYueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gong_yue);

        // 沉浸式体验
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        );

        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setText(Html.fromHtml(getString(R.string.content), Html.FROM_HTML_MODE_LEGACY));

        // 返回按钮
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }
}