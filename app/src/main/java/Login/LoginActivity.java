package Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

import Main.MainActivity;
import Register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

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
        });

        // 设置复选框文本样式
        CheckBox checkBoxAgreement = findViewById(R.id.checkbox_agreement);
        String text = "您已阅读并同意《闲航社区用户服务协议》";
        SpannableString spannableString = new SpannableString(text);
        // 更改“《闲航社区用户服务协议》”的颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_bh));
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

            if (isLoginValid(username, password)) {
                // If login is successful, navigate to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: close the LoginActivity
            } else {
                // Show an error message
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isLoginValid(String username, String password) {
        // Replace this with your actual login validation logic
        return "admin".equals(username) && "1".equals(password);
    }
}