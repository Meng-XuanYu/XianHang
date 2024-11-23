package Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.login.R;

import Main.MainActivity;
import Register.ChangeActivity;
import Register.RegisterActivity;
import model.LoginRequest;
import model.LoginResponse;
import network.ApiService;
import retrofit2.Call;
import RetrofitClient.RetrofitClient;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import com.bumptech.glide.Glide;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 底部动画
        ImageView bottomImage = findViewById(R.id.bottom_image_login);
        Glide.with(this)
                .asGif()
                .load(R.drawable.login_bottom)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1); // Play once
                        resource.startFromFirstFrame();

                        // Stop the animation at the last frame
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                resource.stop();
                            }
                        });

                        return false;
                    }
                })
                .into(bottomImage);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // 密码可见性切换
        EditText passwordEditText = findViewById(R.id.password);
        ImageButton togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility);
        togglePasswordVisibilityButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility);
            }
            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.length());
        });

        // 跳转到注册页面的逻辑
        TextView registerPrompt = findViewById(R.id.register_prompt);
        registerPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            handler.removeCallbacksAndMessages(null);
            finish();
        });

        // 跳转到忘记密码页面的逻辑
        TextView forgetPassword = findViewById(R.id.change_prompt);
        forgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ChangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            handler.removeCallbacksAndMessages(null);
            finish();
        });

        // 设置复选框文本样式
        CheckBox checkBoxAgreement = findViewById(R.id.checkbox_agreement);
        String text = "您已阅读并同意《闲航社区用户服务协议》";
        SpannableString spannableString = new SpannableString(text);
        // 更改“《闲航社区用户服务协议》”的颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_bh, null));
        spannableString.setSpan(colorSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将“《闲航社区用户服务协议》”加粗
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBoxAgreement.setText(spannableString);

        // 登录按钮逻辑
        EditText usernameEditText = findViewById(R.id.username); // 密码定义在上面
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // 检查用户名和密码是否为空
            if (username.isEmpty() || password.isEmpty()) {
                showAlertDialog("用户名和密码不能为空");
                return;
            }

            // 检查是否勾选服务协议
            if (!checkBoxAgreement.isChecked()) {
                showAlertDialog("请先阅读并同意服务协议");
                return;
            }

            // 发送登录请求
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: close the LoginActivity
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView bottomImage = findViewById(R.id.bottom_image_login);
        Glide.with(this)
                .asGif()
                .load(R.drawable.login_bottom)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1); // Play once
                        resource.startFromFirstFrame();

                        // Stop the animation at the last frame
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                resource.stop();
                            }
                        });

                        return false;
                    }
                })
                .into(bottomImage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImageView bottomImage = findViewById(R.id.bottom_image_login);
        if (bottomImage.getDrawable() instanceof GifDrawable) {
            ((GifDrawable) bottomImage.getDrawable()).stop();
        }
    }

    private void sendLoginRequest() {
        // 获取用户输入
        String userId = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        // 创建请求对象
        LoginRequest loginRequest = new LoginRequest(userId, password);

        // 使用单例 RetrofitClient 获取 ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.login(loginRequest).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("success".equals(loginResponse.getStatus())) {
                        // 登录成功
                        showAlertDialog(loginResponse.getMessage());
                        Log.d("Login", "Success: " + loginResponse.getMessage());
                        // 这里可以跳转页面或保存登录状态
                        saveUserId(userId);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close the LoginActivity
                    } else {
                        // 登录失败
                        showAlertDialog(loginResponse.getMessage());
                        Log.e("Login", "Error: " + loginResponse.getMessage());
                    }
                } else {
                    // 服务器错误
                    showAlertDialog("Server Error: " + response.code());
                    Log.e("Login", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // 网络请求失败
                showAlertDialog("Request Failed: " + t.getMessage());
                Log.e("Login", "Request Failed: " + t.getMessage());
            }
        });
    }

    // 保存用户 ID 到 SharedPreferences
    private void saveUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply(); // 异步保存
        Log.d("SharedPreferences", "User ID saved: " + userId);
    }

    private void showAlertDialog(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("提示")
                .setIcon(R.drawable.xianhang_light_fang)
                .setMessage(message)
                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss())
                .show();
    }
}