package Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

public class SetBriefActivity extends AppCompatActivity {
    private ImageView back ;
    private Button save ;
    private EditText editText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_brief);
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
            Intent intent=new Intent(SetBriefActivity.this,EditUserInfoActivity.class);
            startActivity(intent);
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText();
                Intent intent=new Intent(SetBriefActivity.this,EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}