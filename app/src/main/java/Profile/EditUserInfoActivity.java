package Profile;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.File;

import RetrofitClient.RetrofitClient;
import model.GetProfileResponse;
import model.UpdateImageResponse;
import model.UpdatePhoneRequest;
import model.UpdatePhoneResponse;
import model.UpdateSchoolRequest;
import model.UpdateSchoolResponse;
import network.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserInfoActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_user_info);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }

        back = findViewById(R.id.backButton);
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
        hanglizhi = findViewById(R.id.hanglizhi);
        avatar = findViewById(R.id.avatar);
        avatar_arrow = findViewById(R.id.avatar_arrow);


        loadUserProfile();


        nickname_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(EditUserInfoActivity.this, SetNicknameActivity.class);
            intent.putExtra("nickname", nickname.getText());
            intent.putExtra("edit","111");
            startActivity(intent);
            finish();
        });
        avatar_arrow.setOnClickListener(v -> openGallery());
        tel_arrow.setOnClickListener(view -> showRechargeDialog(tel.getText().toString(), "电话号码"));
        brief_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(EditUserInfoActivity.this, SetBriefActivity.class);
            intent.putExtra("edit","111");
            startActivity(intent);
            finish();
        });
        pos_arrow.setOnClickListener(v -> showBottomSheetDialog());

        // 退出按钮
        back.setOnClickListener(v -> {
            Intent intent = new Intent(EditUserInfoActivity.this, ProfileView.class);
            startActivity(intent);
            finish();
        });
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
                        Toast.makeText(EditUserInfoActivity.this, updateSchoolResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UpdateSchool", "Success: " + updateSchoolResponse.getMessage());

                        // 更新 SharedPreferences 中的用户数据
                        saveUpdatedSchoolProfile(school);
                    } else {
                        // 更新失败
                        Toast.makeText(EditUserInfoActivity.this, "更新失败: " + updateSchoolResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error: " + updateSchoolResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(EditUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(EditUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateSchool", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateSchoolResponse> call, Throwable t) {
                Toast.makeText(EditUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    // 获取图片的详细信息
    private void getImageDetails(Uri imageUri) {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED
        };

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(imageUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            String imageName = cursor.getString(columnIndex);

            columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            long imageSize = cursor.getLong(columnIndex);

            columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            long imageDate = cursor.getLong(columnIndex);

            // 显示图片信息
            Toast.makeText(this, "Name: " + imageName + ", Size: " + imageSize + " bytes", Toast.LENGTH_LONG).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showRechargeDialog(String s, String title) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditUserInfoActivity.this);
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
                        Toast.makeText(EditUserInfoActivity.this, "请输入输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("电话号码")) {
                        tel.setText(str);
                        sendUpdatePhoneRequest(str);
                        dialog.dismiss();
                    }
                    if (str.length() != 8 && title.equals("学号")) {
                        Toast.makeText(EditUserInfoActivity.this, "请输入输入正确的学号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("学号")) {
                        studentId.setText(str);
                        dialog.dismiss();
                    }
                    if (str.length() != 18 && title.equals("身份证号")) {
                        Toast.makeText(EditUserInfoActivity.this, "请输入输入正确的身份证号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (title.equals("身份证号")) {
                        user_id.setText(str);
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
                        Toast.makeText(EditUserInfoActivity.this, updatePhoneResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditUserInfoActivity.this, "更新失败: " + updatePhoneResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error: " + updatePhoneResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(EditUserInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(EditUserInfoActivity.this, "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("UpdatePhone", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePhoneResponse> call, Throwable t) {
                Toast.makeText(EditUserInfoActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdatePhone", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        float attractiveness = sharedPreferences.getFloat("userAttractiveness", 0);
        int userAttractiveness = (int) (attractiveness);
        String userAttractivenessStr = String.valueOf(userAttractiveness);

        hanglizhi.setText(userAttractivenessStr);

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
}