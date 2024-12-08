package Register;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import Login.GongYueActivity;
import RetrofitClient.RetrofitClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.login.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import Login.LoginActivity;
import Main.MainActivity;
import model.RegisterRequest;
import model.RegisterResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // 底部动画
        ImageView bottomImage = findViewById(R.id.bottom_image_register);
        Glide.with(this)
                .asGif()
                .load(R.drawable.register_bottom)
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
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        );

        // 跳转到公约页面的逻辑
        ImageView agreement = findViewById(R.id.gongyue);
        agreement.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, GongYueActivity.class);
            startActivity(intent);
        });

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

        // 跳转到登录页面的逻辑
        TextView loginPrompt = findViewById(R.id.login_prompt);
        loginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            handler.removeCallbacksAndMessages(null);
            finish();
        });

        // 设置复选框文本样式
        CheckBox checkBoxAgreement = findViewById(R.id.checkbox_agreement);
        String text = "您已阅读并同意《闲航社区公约》";
        SpannableString spannableString = new SpannableString(text);

        // 更改“《闲航社区用户注册协议》”的颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_bh, null));
        spannableString.setSpan(colorSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将“《闲航社区用户注册协议》”加粗
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 注册按钮点击事件
        findViewById(R.id.register_button).setOnClickListener(v -> {
            // 检查是否勾选服务协议
            if (!checkBoxAgreement.isChecked()) {
                showAlertDialog("请先阅读并同意服务协议");
                return;
            }

            // 检查用户输入
            String userId = ((EditText) findViewById(R.id.username)).getText().toString();
            String password = passwordEditText.getText().toString();
            if (userId.isEmpty() || password.isEmpty()) {
                showAlertDialog("用户名和密码不能为空");
                return;
            }

            sendRegisterRequest();
        });

        checkBoxAgreement.setText(spannableString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView bottomImage = findViewById(R.id.bottom_image_register);
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
        ImageView bottomImage = findViewById(R.id.bottom_image_register);
        if (bottomImage.getDrawable() instanceof GifDrawable) {
            ((GifDrawable) bottomImage.getDrawable()).stop();
        }
    }

    private void sendRegisterRequest() {
        // 获取用户输入
        String userId = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        // 创建请求对象
        RegisterRequest registerRequest = new RegisterRequest(userId, password);

        // 使用单例 RetrofitClient 获取 ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送注册请求
        apiService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    if ("success".equals(registerResponse.getStatus())) {
                        // 注册成功
                        Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Register", "Success: " + registerResponse.getMessage());
                        // 注册成功后可跳转到登录页面
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // 销毁当前活动
                    } else {
                        // 注册失败
                        Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Register", "Error: " + registerResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Login", "Request Failed: " + t.getMessage());
            }
        });
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