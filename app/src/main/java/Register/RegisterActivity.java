package Register;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.login.R;

import Login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

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

        // 跳转到登录页面的逻辑
        TextView loginPrompt = findViewById(R.id.login_prompt);
        loginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 设置复选框文本样式
        CheckBox checkBoxAgreement = findViewById(R.id.checkbox_agreement);
        String text = "您已阅读并同意《闲航社区用户注册协议》";
        SpannableString spannableString = new SpannableString(text);

        // 更改“《闲航社区用户注册协议》”的颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_bh));
        spannableString.setSpan(colorSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将“《闲航社区用户注册协议》”加粗
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        checkBoxAgreement.setText(spannableString);
    }
}