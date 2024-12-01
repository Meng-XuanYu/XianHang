package fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONObject;

import Login.LoginActivity;
import Profile.BuyActivity;
import Profile.CollectActivity;
import Profile.EvaluateActivity;
import Profile.HistoryActivity;
import Profile.ProfileView;
import Profile.ReleaseActivity;
import Profile.SellActivity;
import model.ChargeRequest;
import model.ChargeResponse;
import model.GetMoneyResponse;
import model.GetProfileResponse;
import model.LogoutResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import RetrofitClient.RetrofitClient;


public class PersonFragment extends Fragment {
    private ImageView avatar;
    private TextView username;
    private TextView credit;
    private View view;

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_person, container, false);

        // 初始化控件
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.user_name);
        credit = view.findViewById(R.id.credit_level);
        textView = view.findViewById(R.id.my_money);

        // 加载用户信息
        loadUserProfile();

        // 设置按钮
        ImageButton buttonSetting = view.findViewById(R.id.button_setting);
        buttonSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileView.class);
            startActivity(intent);
        });
        avatar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileView.class);
            startActivity(intent);
        }); // 点击头像也可以跳转到个人信息页面
        username.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileView.class);
            startActivity(intent);
        }); // 点击用户名也可以跳转到个人信息页面

        // 登出按钮
        ImageButton buttonLogout = view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(v -> sendLogoutRequest());

        LinearLayout buttonRecharge = view.findViewById(R.id.chongzhi);
        buttonRecharge.setOnClickListener(v -> showRechargeDialog());

        // 我的收藏
        LinearLayout buttonCollection = view.findViewById(R.id.my_star);
        buttonCollection.setOnClickListener(v -> {
            // 跳转到我的收藏页面
            Intent intent = new Intent(requireActivity(), CollectActivity.class);
            startActivity(intent);
        });

        // 我的浏览
        LinearLayout buttonHistory = view.findViewById(R.id.my_history);
        buttonHistory.setOnClickListener(v -> {
            // 跳转到我的浏览页面
            Intent intent = new Intent(requireActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout buttonMyRelease = view.findViewById(R.id.my_release);
        buttonMyRelease.setOnClickListener(v -> {
            // 跳转到我的发布
            Intent intent = new Intent(requireActivity(), ReleaseActivity.class);
            startActivity(intent);
        });

        LinearLayout buttonMySell = view.findViewById(R.id.my_sell);
        buttonMySell.setOnClickListener(v -> {
            // 跳转到我的卖出
            Intent intent = new Intent(requireActivity(), SellActivity.class);
            startActivity(intent);
        });

        LinearLayout buttonMyBuy = view.findViewById(R.id.my_buy);
        buttonMyBuy.setOnClickListener(v -> {
            // 跳转到我的购买
            Intent intent = new Intent(requireActivity(), BuyActivity.class);
            startActivity(intent);
        });

        LinearLayout buttonMyComment = view.findViewById(R.id.my_evaluate);
        buttonMyComment.setOnClickListener(v -> {
            // 跳转到待评论
            Intent intent = new Intent(requireActivity(), EvaluateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void getMoney(TextView textView) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getMoney(userId).enqueue(new Callback<GetMoneyResponse>() {
            @Override
            public void onResponse(Call<GetMoneyResponse> call, Response<GetMoneyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetMoneyResponse chargeResponse = response.body();
                    if ("success".equals(chargeResponse.getStatus())) {
                        textView.setText("余额：" + chargeResponse.getMoney());
                    } else {
                        // 登录失败
                        Toast.makeText(getContext(), chargeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Money", "Error: " + chargeResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Money", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("Money", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMoneyResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Money", "Request Failed: " + t.getMessage());
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        float creditLevel = sharedPreferences.getFloat("userAttractiveness", 0);
        int creditInt = (int) creditLevel;
        String creditStr = "航力值:" + creditInt;
        credit.setText(creditStr);

        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);

            if (profile.getName() != null) {
                username.setText(profile.getName());
            }
            if (profile.getAvatar() != null) {
                Glide.with(this)
                        .load(profile.getAvatar())
                        .placeholder(R.drawable.xianhang_light_yuan)  // 占位图
                        .error(R.drawable.xianhang_light_yuan).into(avatar);
            }
        } else {
            Log.e("SharedPreferences", "用户信息不存在");
        }

        getMoney(textView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showRechargeDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recharge, null);
        builder.setView(dialogView)
                .setTitle("充值")
                .setBackground(getResources().getDrawable(R.drawable.rounded_background, null))
                .setIcon(R.drawable.xianhang_light_fang)
                .setPositiveButton("确认", (dialog, which) -> {
                    EditText editTextAmount = dialogView.findViewById(R.id.edit_text_amount);
                    String amountStr = editTextAmount.getText().toString();
                    if (!amountStr.isEmpty()) {
                        double amount = Double.parseDouble(amountStr);
                        performRecharge(amount);
                    } else {
                        Toast.makeText(getActivity(), "请输入充值金额", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performRecharge(double amount) {
        // 这里可以添加实际的充值逻辑，例如调用API

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        ChargeRequest chargeRequest = new ChargeRequest(userId,amount);
        ApiService apiService = RetrofitClient.getApiService();

        apiService.charge(chargeRequest).enqueue(new Callback<ChargeResponse>() {
            @Override
            public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChargeResponse getMoneyResponse = response.body();
                    if ("success".equals(getMoneyResponse.getStatus())) {
                        getMoney(textView);
                        Toast.makeText(getActivity(), "充值成功: " + amount + "元", Toast.LENGTH_SHORT).show();
                        Log.d("Recharge", "Recharged: " + amount + "元");
                    } else {
                        // 登录失败
                        Toast.makeText(getContext(), getMoneyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Recharge", "Error: " + getMoneyResponse.getMessage());
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Recharge", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("Recharge", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChargeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Recharge", "Request Failed: " + t.getMessage());
            }
        });


    }

    // 发送退出登录请求
    private void sendLogoutRequest() {
        // 初始化 Retrofit ApiService
        ApiService apiService = RetrofitClient.getApiService();

        apiService.logout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LogoutResponse logoutResponse = response.body();
                    if ("success".equals(logoutResponse.getStatus())) {
                        Toast.makeText(requireActivity(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // 清除本地存储的用户信息
                        clearUserData();

                        // 跳转回登录页面
                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        Toast.makeText(requireActivity(), "注销失败: " + logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 从 errorBody 获取错误信息
                        String errorJson = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorJson);
                        String errorMessage = errorObject.optString("message", "未知错误");
                        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Logout", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(requireActivity(), "解析错误消息失败", Toast.LENGTH_SHORT).show();
                        Log.e("Logout", "Error parsing error body: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Logout", "Request Failed: " + t.getMessage());
            }
        });
    }

    // 清除本地存储的用户信息
    private void clearUserData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 清除单独存储的 userId 和 userProfile
        editor.remove("userId");
        editor.remove("userProfile");

        editor.apply();
        Log.d("SharedPreferences", "用户数据已清除: userId 和 userProfile");
    }
}
