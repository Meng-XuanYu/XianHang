package Main;

import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class WaitActivity extends AppCompatActivity {
    private RoundedImageView roundedImageView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        roundedImageView = findViewById(R.id.img);

        roundedImageView.setImageURI(uri);
        Handler handler = new Handler();

        ImageView ball1 = findViewById(R.id.ball1);
        ImageView ball2 = findViewById(R.id.ball2);
        animateBalls(ball1, ball2);



//        // 创建一个 Runnable，用于执行跳转操作
//        Runnable runnable = () -> {
//            // 延迟 20 秒后跳转到目标页面
//            Intent intent = new Intent(WaitActivity.this, UnusedActivity.class);
//            intent.putExtra("uri",uri.toString());
//            startActivity(intent);
//            finish(); // 可选：结束当前 Activity
//        };
//
//        // 延迟 20 秒（20000 毫秒）后执行跳转
//        handler.postDelayed(runnable, 20000);
    }

    private void animateBalls(ImageView ball1, ImageView ball2) {
        // 小球1的左右晃动动画
        ObjectAnimator ball1Animator = ObjectAnimator.ofFloat(ball1, "translationX", 0f, 150f, 0f);
        ball1Animator.setDuration(2500);
        ball1Animator.setRepeatCount(ObjectAnimator.INFINITE);
        ball1Animator.setRepeatMode(ObjectAnimator.RESTART);
        ball1Animator.start();

        // 小球2的左右晃动动画
        ObjectAnimator ball2Animator = ObjectAnimator.ofFloat(ball2, "translationX", 0f, -150f, 0f);
        ball2Animator.setDuration(2500);
        ball2Animator.setRepeatCount(ObjectAnimator.INFINITE);
        ball2Animator.setRepeatMode(ObjectAnimator.RESTART);
        ball2Animator.start();
    }


}