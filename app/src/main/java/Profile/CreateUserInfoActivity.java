package Profile;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.File;

import Main.MainActivity;
import RetrofitClient.RetrofitClient;
import model.GetProfileResponse;
import model.UpdateIdentityRequest;
import model.UpdateIdentityResponse;
import model.UpdateImageResponse;
import model.UpdatePhoneRequest;
import model.UpdatePhoneResponse;
import model.UpdateSchoolRequest;
import model.UpdateSchoolResponse;
import model.UpdateStudentIdRequest;
import model.UpdateStudentIdResponse;
import network.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CreateUserInfoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private ImageView back;
    private TextView nickname;
    private LinearLayout nickname_arrow;
    private TextView brief;
    private LinearLayout brief_arrow;
    private TextView pos;
    private LinearLayout pos_arrow;
    private TextView tel;
    private LinearLayout tel_arrow;
    private TextView user_id;
    private TextView studentId;
    private TextView hanglizhi;
    private RoundedImageView avatar;
    private LinearLayout avatar_arrow;
    private LinearLayout studentId_arrow;
    private LinearLayout user_id_arrow;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_info);

        // 沉浸式体验
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        );

        nickname = findViewById(R.id.nickname);
        nickname_arrow = findViewById(R.id.nickname_arrow);
        brief = findViewById(R.id.brief);
        brief_arrow = findViewById(R.id.brief_arrow);
        pos = findViewById(R.id.pos);
        pos_arrow = findViewById(R.id.pos_arrow);
        tel = findViewById(R.id.tel);
        tel_arrow = findViewById(R.id.tel_arrow);
        user_id = findViewById(R.id.user_id);
        studentId = findViewById(R.id.studentId);
        avatar = findViewById(R.id.avatar);
        avatar_arrow = findViewById(R.id.avatar_arrow);
        user_id_arrow = findViewById(R.id.user_id_arrow);
        studentId_arrow = findViewById(R.id.studentId_arrow);

        loadUserProfile();

        findViewById(R.id.save).setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String profileJson = sharedPreferences.getString("userProfile", null);

            if (profileJson != null) {
                Gson gson = new Gson();
                GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

                if (profile.getName() != null && !profile.getName().isEmpty() &&
                        profile.getAvatar() != null && !profile.getAvatar().isEmpty() &&
                        profile.getStudentId() != null && !profile.getStudentId().isEmpty() &&
                        profile.getIdentity() != null && !profile.getIdentity().isEmpty() &&
                        profile.getSchool() != null && !profile.getSchool().isEmpty() &&
                        profile.getPhone() != null && !profile.getPhone().isEmpty()) {

                    // All required fields are filled, navigate to MainActivity
                    Intent intent = new Intent(CreateUserInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Some fields are missing, show a toast message
                    Toast.makeText(CreateUserInfoActivity.this, "请填写所有信息", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Profile information is missing, show a toast message
                Toast.makeText(CreateUserInfoActivity.this, "用户信息加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        nickname_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(CreateUserInfoActivity.this, SetNicknameActivity.class);
            intent.putExtra("create","111");
            startActivity(intent);
        });
        avatar_arrow.setOnClickListener(v -> openGallery());
        tel_arrow.setOnClickListener(view -> showRechargeDialog(tel.getText().toString(), "电话号码"));
        user_id_arrow.setOnClickListener(v -> showRechargeDialog(user_id.getText().toString(), "身份证号"));
        studentId_arrow.setOnClickListener(v -> showRechargeDialog(studentId.getText().toString(), "学号"));
        brief_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(CreateUserInfoActivity.this, SetBriefActivity.class);
            intent.putExtra("create","111");
            startActivity(intent);
        });
        pos_arrow.setOnClickListener(v -> showBottomSheetDialog());
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);

        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

            if (profile.getName() != null) {
                nickname.setText(profile.getName());
            }
            if (profile.getAvatar() != null) {
                Glide.with(this)
                        .load(profile.getAvatar())
                        .placeholder(R.drawable.xianhang_light_fang)  // 占位图
                        .error(R.drawable.xianhang_light_fang).into(avatar);
            }
            if (profile.getStudentId() != null) {
                studentId.setText(profile.getStudentId());
            }
            if (profile.getIdentity() != null) {
                user_id.setText(profile.getIdentity());
            }
            if (profile.getSchool() != null) {
                pos.setText(profile.getSchool());
            }
            if (profile.getPhone() != null) {
                tel.setText(profile.getPhone());
            }
            if (profile.getAvatar() != null) {
                Glide.with(this)
                        .load(profile.getAvatar())
                        .placeholder(R.drawable.img)  // 占位图
                        .error(R.drawable.img).into(avatar);
            }
        } else {
            Toast.makeText(this, "用户信息加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBottomSheetDialog() {
        // 创建 BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        // 获取 NumberPicker 和按钮
        NumberPicker numberPicker = bottomSheetDialog.findViewById(R.id.numberPicker);
        numberPicker.setWrapSelectorWheel(false);
        Handler handler = new Handler();
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            handler.postDelayed(() -> {
            }, 200); // 延迟 200ms 更新
        });
        Button btnConfirm = bottomSheetDialog.findViewById(R.id.btnConfirm);
        for (int i = 0; i < numberPicker.getChildCount(); i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                child.setFocusable(false);
                child.setClickable(false);
            }
        }
        // 配置 NumberPicker
        String[] data = {"学院路校区", "沙河校区", "杭州校区"};
        numberPicker.setDisplayedValues(data);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(data.length - 1);
        numberPicker.setWrapSelectorWheel(true); // 滚动循环
        // 按钮点击事件
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                int selectedIndex = numberPicker.getValue();
                pos.setText(numberPicker.getDisplayedValues()[selectedIndex]);
                sendUpdateSchoolRequest(numberPicker.getDisplayedValues()[selectedIndex]);
                bottomSheetDialog.dismiss();
            });
        }
        // 显示 BottomSheetDialog
        bottomSheetDialog.show();
    }

    private void sendUpdateSchoolRequest(String school) {
        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "未找到用户 ID，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求体
        UpdateSchoolRequest updateSchoolRequest = new UpdateSchoolRequest(userId, school);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updateSchoolRequest).enqueue(new Callback<UpdateSchoolResponse>() {
            @Override
            public void onResponse(Call<UpdateSchoolResponse> call, Response<UpdateSchoolResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateSchoolResponse updateSchoolResponse = response.body();
                    if ("success".equals(updateSchoolResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(CreateUserInfoActivity.this, updateSchoolResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateSchool", "Success: " + updateSchoolResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        saveUpdatedSchoolProfile(school);
                    } else {
                        // 更新失败
                        Toast.makeText(CreateUserInfoActivity.this, "更新失败: " + updateSchoolResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error: " + updateSchoolResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CreateUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CreateUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateSchoolResponse> call, Throwable t) {
                Toast.makeText(CreateUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateSchool", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void saveUpdatedSchoolProfile(String value) {
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

        userProfile.setSchool(value);

        // 保存更新后的用户对象
        String updatedUserJson = gson.toJson(userProfile);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userProfile", updatedUserJson);
        editor.apply();

        Log.d("UpdateProfile", "用户数据已更新: " + updatedUserJson);
    }

    // 启动图库选择图片
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 设置类型为图片
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    // 处理选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                avatar.setImageURI(selectedImageUri); // 加载图片到 ImageView
                uploadFile(selectedImageUri); // 上传图片
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    public void uploadFile(Uri uri) {
        File file = new File(getRealPathFromURI(uri)); // 获取文件实际路径
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody userIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), userId);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.uploadFile(userIdBody, body).enqueue(new Callback<UpdateImageResponse>() {
            @Override
            public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateImageResponse updateImageResponse = response.body();
                    if ("success".equals(updateImageResponse.getStatus())) {
                        // 更新成功
                        String uri = updateImageResponse.getMessage();
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("userProfile", null);

                        // 将 JSON 转换为用户对象
                        Gson gson = new Gson();
                        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

                        userProfile.setAvatar(uri);

                        // 保存更新后的用户对象
                        String updatedUserJson = gson.toJson(userProfile);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userProfile", updatedUserJson);
                        editor.apply();

                    } else {
                        // 更新失败
                        Log.e("UpdateImage", "Error: " + updateImageResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Log.e("UpdateImage", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Log.e("UpdateImage", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateImageResponse> call, Throwable t) {
                Log.e("UpdateImage", "Request Failed: " + t.getMessage());
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showRechargeDialog(String s, String title) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CreateUserInfoActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recharge, null);
        builder.setView(dialogView)
                .setTitle("修改" + title)
                .setBackground(getResources().getDrawable(R.drawable.rounded_background, null))
                .setIcon(R.drawable.xianhang_light_fang)
                .setPositiveButton("确认", (dialog, which) -> {
                    EditText editTextAmount = dialogView.findViewById(R.id.edit_text_amount);
                    String str = editTextAmount.getText().toString();
                    if (str.length() != 11 && title.equals("电话号码")) {
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("电话号码")) {
                        tel.setText(str);
                        sendUpdatePhoneRequest(str);
                        dialog.dismiss();
                    }
                    if (str.length() != 8 && title.equals("学号")) {
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的学号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("学号")) {
                        studentId.setText(str);
                        sendUpdateStudentIdRequest(str);
                        dialog.dismiss();
                    }
                    if (str.length() != 18 && title.equals("身份证号")) {
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的身份证号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("身份证号")) {
                        user_id.setText(str);
                        sendUpdateIdentityRequest(str);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
        EditText text = dialogView.findViewById(R.id.edit_text_amount);
        switch (title) {
            case "电话号码":
                text.setHint("请输入电话号码");
                break;
            case "学号":
                text.setHint("请输入学号");
                break;
            case "身份证号":
                text.setHint("请输入身份证号");
                break;
        }
        text.setText(s);
    }

    private void sendUpdateStudentIdRequest(String studentId) {
        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "未找到用户 ID，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求体
        UpdateStudentIdRequest updateStudentIdRequest = new UpdateStudentIdRequest(userId, studentId);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updateStudentIdRequest).enqueue(new Callback<UpdateStudentIdResponse>() {
            @Override
            public void onResponse(Call<UpdateStudentIdResponse> call, Response<UpdateStudentIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateStudentIdResponse updateStudentIdResponse = response.body();
                    if ("success".equals(updateStudentIdResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(CreateUserInfoActivity.this, updateStudentIdResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateStudentId", "Success: " + updateStudentIdResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("userProfile", null);

                        // 将 JSON 转换为用户对象
                        Gson gson = new Gson();
                        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

                        // 更新指定字段
                        userProfile.setStudentId(studentId);
                        // 保存更新后的用户对象
                        String updatedUserJson = gson.toJson(userProfile);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userProfile", updatedUserJson);
                        editor.apply();

                        Log.d("UpdateProfile", "用户数据已更新: " + updatedUserJson);
                    } else {
                        // 更新失败
                        Toast.makeText(CreateUserInfoActivity.this, "更新失败: " + updateStudentIdResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateStudentId", "Error: " + updateStudentIdResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CreateUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateStudentId", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CreateUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateStudentId", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateStudentIdResponse> call, Throwable t) {
                Toast.makeText(CreateUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdatePhone", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void sendUpdatePhoneRequest(String phone) {
        // 获取用户输入的信息

        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // 构建请求体
        UpdatePhoneRequest updatePhoneRequest = new UpdatePhoneRequest(userId, phone);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updatePhoneRequest).enqueue(new Callback<UpdatePhoneResponse>() {
            @Override
            public void onResponse(Call<UpdatePhoneResponse> call, Response<UpdatePhoneResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdatePhoneResponse updatePhoneResponse = response.body();
                    if ("success".equals(updatePhoneResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(CreateUserInfoActivity.this, updatePhoneResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdatePhone", "Success: " + updatePhoneResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("userProfile", null);

                        // 将 JSON 转换为用户对象
                        Gson gson = new Gson();
                        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

                        // 更新指定字段
                        userProfile.setPhone(phone);
                        // 保存更新后的用户对象
                        String updatedUserJson = gson.toJson(userProfile);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userProfile", updatedUserJson);
                        editor.apply();

                        Log.d("UpdateProfile", "用户数据已更新: " + updatedUserJson);
                    } else {
                        // 更新失败
                        Toast.makeText(CreateUserInfoActivity.this, "更新失败: " + updatePhoneResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error: " + updatePhoneResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CreateUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CreateUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePhoneResponse> call, Throwable t) {
                Toast.makeText(CreateUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdatePhone", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void sendUpdateIdentityRequest(String identity) {
        // 从 SharedPreferences 获取 userId
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // 构建请求体
        UpdateIdentityRequest updateIdentityRequest = new UpdateIdentityRequest(userId, identity);

        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // 发送 POST 请求
        apiService.updateProfile(updateIdentityRequest).enqueue(new Callback<UpdateIdentityResponse>() {
            @Override
            public void onResponse(Call<UpdateIdentityResponse> call, Response<UpdateIdentityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateIdentityResponse updateIdentityResponse = response.body();
                    if ("success".equals(updateIdentityResponse.getStatus())) {
                        // 更新成功
                        Toast.makeText(CreateUserInfoActivity.this, updateIdentityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateIdentity", "Success: " + updateIdentityResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("userProfile", null);

                        // 将 JSON 转换为用户对象
                        Gson gson = new Gson();
                        GetProfileResponse userProfile = gson.fromJson(userJson, GetProfileResponse.class);

                        // 更新指定字段
                        userProfile.setIdentity(identity);
                        // 保存更新后的用户对象
                        String updatedUserJson = gson.toJson(userProfile);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userProfile", updatedUserJson);
                        editor.apply();
                    } else {
                        // 更新失败
                        Toast.makeText(CreateUserInfoActivity.this, "更新失败: " + updateIdentityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateIdentity", "Error: " + updateIdentityResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(CreateUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateIdentity", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(CreateUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateIdentity", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateIdentityResponse> call, Throwable t) {
                Toast.makeText(CreateUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateIdentity", "Request Failed: " + t.getMessage());
            }
        });
    }
}