package Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import model.GetProfileResponse;
import model.UpdateTextRequest;
import model.UpdateTextResponse;
import network.ApiService;
import RetrofitClient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetBriefActivity extends AppCompatActivity {
    private ImageView back ;
    private Button save ;
    private EditText editText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_brief);
        String create = getIntent().getStringExtra("create");
        String edit = getIntent().getStringExtra("edit");
        back = findViewById(R.id.backButton);
        save = findViewById(R.id.save);
        editText = findViewById(R.id.brief);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        back.setOnClickListener(view -> {
            Intent intent=new Intent(SetBriefActivity.this,
                    create != null? CreateUserInfoActivity.class: EditUserInfoActivity.class);
            startActivity(intent);
            finish();
        });

        save.setOnClickListener(view -> {
            editText.getText();
            sendUpdateTextRequest();
            editText.clearFocus();
            Intent intent=new Intent(SetBriefActivity.this,
                    create != null? CreateUserInfoActivity.class: EditUserInfoActivity.class);
            startActivity(intent);
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);

        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse userProfile = gson.fromJson(profileJson, GetProfileResponse.class);
            editText.setText(userProfile.getText());
        }
    }

    private void sendUpdateTextRequest() {
        // 获取用户输入的信息
        String text = editText.getText().toString();

        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "未找到用户 ID，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求体
        UpdateTextRequest updateTextRequest = new UpdateTextRequest(userId, text);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updateTextRequest).enqueue(new Callback<UpdateTextResponse>() {
            @Override
            public void onResponse(Call<UpdateTextResponse> call, Response<UpdateTextResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateTextResponse updateTextResponse = response.body();
                    if ("success".equals(updateTextResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(SetBriefActivity.this, updateTextResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateText", "Success: " + updateTextResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        saveUpdatedProfile("text", text);

                        // 返回上一页或提示用户
                        finish();
                    } else {
                        // 更新失败
                        Toast.makeText(SetBriefActivity.this, "更新失败: " + updateTextResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateText", "Error: " + updateTextResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(SetBriefActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateText", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(SetBriefActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateText", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateTextResponse> call, Throwable t) {
                Toast.makeText(SetBriefActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateText", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void saveUpdatedProfile(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userProfile", null);

        if (userJson == null) {
            Log.e("UpdateProfile", "用户数据未找到，请重新登录");
            Toast.makeText(this, "用户数据未找到，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将 JSON 转换为用户对象
        Gson gson = new Gson();
        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

        // 更新指定字段
        userProfile.setText(value);

        // 保存更新后的用户对象
        String updatedUserJson = gson.toJson(userProfile);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userProfile", updatedUserJson);
        editor.apply();

        Log.d("UpdateProfile", "用户数据已更新: " + updatedUserJson);
    }
}