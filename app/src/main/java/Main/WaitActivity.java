package Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URI;

public class WaitActivity extends AppCompatActivity {
    private ImageView back;
    private RoundedImageView roundedImageView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        uri = Uri.parse(getIntent().getStringExtra("url"));

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        back = findViewById(R.id.search_back);
        roundedImageView = findViewById(R.id.img);
        back.setOnClickListener(view -> {
            finish();
        });

        roundedImageView.setImageURI(uri);
        Handler handler = new Handler();

        // 创建一个 Runnable，用于执行跳转操作
        Runnable runnable = () -> {
            // 延迟 20 秒后跳转到目标页面
            Intent intent = new Intent(WaitActivity.this, UnusedActivity.class);
            intent.putExtra("uri",uri.toString());
            startActivity(intent);
            finish(); // 可选：结束当前 Activity
        };

        // 延迟 20 秒（20000 毫秒）后执行跳转
        handler.postDelayed(runnable, 20000);
    }
}