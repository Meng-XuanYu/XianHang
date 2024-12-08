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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import Login.GongYueActivity;
import RetrofitClient.RetrofitClient;

import Login.LoginActivity;
import model.ForgetPasswordRequest;
import model.ForgetPasswordResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change);

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
            Intent intent = new Intent(ChangeActivity.this, GongYueActivity.class);
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
            Intent intent = new Intent(ChangeActivity.this, LoginActivity.class);
            startActivity(intent);
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

        checkBoxAgreement.setText(spannableString);

        // 修改密码按钮逻辑
        Button changePasswordButton = findViewById(R.id.change_button);
        TextView idTextView = findViewById(R.id.id);
        TextView phoneTextView = findViewById(R.id.phone);
        changePasswordButton.setOnClickListener(v -> {
            String newPassword = passwordEditText.getText().toString();
            String id = idTextView.getText().toString();
            String phone = phoneTextView.getText().toString();

            // 检查复选框是否被选中
            if (!checkBoxAgreement.isChecked()) {
                showAlertDialog("请阅读并同意《闲航社区用户注册协议》");
                return;
            }

            // 检查身份证号是否为空
            if (id.isEmpty()) {
                showAlertDialog("身份证号不能为空");
                return;
            }

            // 检查手机号是否为空
            if (phone.isEmpty()) {
                showAlertDialog("手机号不能为空");
                return;
            }

            // 检查新密码是否为空
            if (newPassword.isEmpty()) {
                showAlertDialog("新密码不能为空");
                return;
            }

            // 发送修改密码请求
            sendForgetPasswordRequest();
        });
    }

    private void sendForgetPasswordRequest() {
        // 获取用户输入的信息
        EditText identityField = findViewById(R.id.id);
        EditText phoneField = findViewById(R.id.phone);
        EditText newPasswordField = findViewById(R.id.password);

        String identity = identityField.getText().toString();
        String phone = phoneField.getText().toString();
        String newPassword = newPasswordField.getText().toString();

        // 构建请求体
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest(identity, phone, newPassword);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.forgetPassword(forgetPasswordRequest).enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ForgetPasswordResponse forgetPasswordResponse = response.body();
                    if ("success".equals(forgetPasswordResponse.getStatus())) {
                        Toast.makeText(ChangeActivity.this, forgetPasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ForgetPassword", "Success: " + forgetPasswordResponse.getMessage());

                        // 跳转回登录页面
                        Intent intent = new Intent(ChangeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // 返回上一个页面
                    } else {
                        Toast.makeText(ChangeActivity.this, "失败: " + forgetPasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ForgetPassword", "Error: " + forgetPasswordResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(ChangeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("ForgetPassword", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(ChangeActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("ForgetPassword", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                Toast.makeText(ChangeActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ForgetPassword", "Request Failed: " + t.getMessage());
            }
        });
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

    private void showAlertDialog(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("提示")
                .setIcon(R.drawable.xianhang_light_fang)
                .setMessage(message)
                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss())
                .show();
    }
}