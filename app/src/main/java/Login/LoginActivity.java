package Login;

import android.Manifest;
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
import android.widget.Toast;

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
import Profile.CreateUserInfoActivity;
import Register.ChangeActivity;
import Register.RegisterActivity;
import model.GetAttractivenessResponse;
import model.GetMoneyResponse;
import model.GetProfileResponse;
import model.LoginRequest;
import model.LoginResponse;
import network.ApiService;
import retrofit2.Call;
import RetrofitClient.RetrofitClient;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isUserProfileFetched = false;
    private boolean isMoneyFetched = false;
    private boolean isAttractivenessFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 查看是否已经登陆了
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userId", null);
        String password = sharedPreferences.getString("password", null);
        if (profileJson != null && password != null) {
            ((EditText) findViewById(R.id.password)).setText(password);
            ((EditText) findViewById(R.id.username)).setText(profileJson);
            sendLoginRequest();
        }

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
            String password1 = passwordEditText.getText().toString();

            // 检查用户名和密码是否为空
            if (username.isEmpty() || password1.isEmpty()) {
                showAlertDialog("用户名和密码不能为空");
                return;
            }

            // 检查是否勾选服务协议
            if (!checkBoxAgreement.isChecked()) {
                showAlertDialog("请先阅读并同意服务协议");
                return;
            }

            // 发送登录请求
            sendLoginRequest();
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
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("success".equals(loginResponse.getStatus())) {
                        // 登录成功
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "Success: " + loginResponse.getMessage());

                        saveUserIdToPreferences(userId);
                        savePasswordToPreferences(password);

                        // 获取用户的详细信息并存储到 SharedPreferences
                        fetchUserDetails(userId);
                    } else {
                        // 登录失败
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error: " + loginResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Login", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void saveUserIdToPreferences(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId); // 单独存储 userId
        editor.apply();
    }

    private void savePasswordToPreferences(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password); // 单独存储 password
        editor.apply();
    }

    private void fetchUserDetails(String userId) {
        fetchUserProfile(userId);
        fetchUserMoney(userId);
        fetchUserAttractiveness(userId);
    }

    private void fetchUserProfile(String userId) {
        // 使用单例 RetrofitClient 获取 ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 GET 请求获取用户信息
        apiService.getProfile(userId).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetProfileResponse profileResponse = response.body();
                    if ("success".equals(profileResponse.getStatus())) {
                        // 将用户数据存储到 SharedPreferences
                        saveUserToPreferences(profileResponse);
                        isUserProfileFetched = true;
                        checkAllDataFetched();
                    } else {
                        Toast.makeText(LoginActivity.this, "获取用户信息失败: " + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getProfile", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getProfile", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void fetchUserMoney(String userId) {
        // 使用单例 RetrofitClient 获取 ApiService
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getMoney(userId).enqueue(new Callback<GetMoneyResponse>() {
            @Override
            public void onResponse(Call<GetMoneyResponse> call, Response<GetMoneyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetMoneyResponse moneyResponse = response.body();
                    if ("success".equals(moneyResponse.getStatus())) {
                        saveUserMoneyToPreferences(moneyResponse.getMoney());
                        isMoneyFetched = true;
                        checkAllDataFetched();
                    } else {
                        Toast.makeText(LoginActivity.this, "获取余额失败: " + moneyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getMoney", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getMoney", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMoneyResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getMoney", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void fetchUserAttractiveness(String userId) {
        // 使用单例 RetrofitClient 获取 ApiService
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getAttractiveness(userId).enqueue(new Callback<GetAttractivenessResponse>() {
            @Override
            public void onResponse(Call<GetAttractivenessResponse> call, Response<GetAttractivenessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAttractivenessResponse attractivenessResponse = response.body();
                    if ("success".equals(attractivenessResponse.getStatus())) {
                        saveUserAttractivenessToPreferences(attractivenessResponse.getAttractiveness());
                        isAttractivenessFetched = true;
                        checkAllDataFetched();
                    } else {
                        Toast.makeText(LoginActivity.this, "获取航力值失败: " + attractivenessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("getAttractiveness", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAttractivenessResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("getAttractiveness", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void saveUserToPreferences(GetProfileResponse userProfile) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 使用 Gson 将用户对象转为 JSON 字符串
        Gson gson = new Gson();
        String userJson = gson.toJson(userProfile);

        editor.putString("userProfile", userJson);
        editor.apply();

        Log.d("MainActivity", "用户数据已存储: " + userJson);
    }

    private void saveUserMoneyToPreferences(double money) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("userMoney", (float) money);
        editor.apply();
    }

    private void saveUserAttractivenessToPreferences(int attractiveness) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("userAttractiveness", (float) attractiveness);
        editor.apply();
    }

    private void checkAllDataFetched() {
        if (isUserProfileFetched && isMoneyFetched && isAttractivenessFetched) {

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String profileJson = sharedPreferences.getString("userProfile", null);

            if (profileJson != null) {
                Gson gson = new Gson();
                GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

                // 跳转到创建资料页面
                if (profile.getName() == null || profile.getText()== null || profile.getSchool()== null
                        || profile.getIdentity()== null || profile.getPhone()== null || profile.getStudentId() == null
                        || profile.getAvatar()== null || profile.getAvatar().isEmpty() || profile.getName().isEmpty() ||
                        profile.getText().isEmpty() || profile.getSchool().isEmpty() || profile.getIdentity().isEmpty() ||
                        profile.getPhone().isEmpty() || profile.getStudentId().isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, CreateUserInfoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 跳转到主页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(this, "用户信息加载失败", Toast.LENGTH_SHORT).show();
            }

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