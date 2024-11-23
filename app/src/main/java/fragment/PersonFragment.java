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
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import Login.LoginActivity;
import Profile.ProfileView;
import model.LogoutResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import RetrofitClient.RetrofitClient;


public class PersonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        ImageButton buttonSetting = view.findViewById(R.id.button_setting);
        buttonSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileView.class);
            startActivity(intent);
        });

        // 登出按钮
        ImageButton buttonLogout = view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(v -> sendLogoutRequest());

        LinearLayout buttonRecharge = view.findViewById(R.id.chongzhi);
        buttonRecharge.setOnClickListener(v -> showRechargeDialog());

        return view;
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
        Toast.makeText(getActivity(), "充值成功: " + amount + "元", Toast.LENGTH_SHORT).show();
        Log.d("Recharge", "Recharged: " + amount + "元");
    }

    // 发送退出登录请求
    private void sendLogoutRequest() {
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
                        Toast.makeText(requireActivity(), "登出失败: " + logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "服务器错误: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Logout", "Error: " + response.code());
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
        editor.clear();
        editor.apply();
        Log.d("SharedPreferences", "User data cleared.");
    }
}
