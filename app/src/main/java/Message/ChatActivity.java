package Message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Profile.ProfileView;
import goodsPage.ItemDetailActivity;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText editTextChat;
    private ImageButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextChat = findViewById(R.id.editTextChat);
        buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(messageAdapter);

        // Immersive experience
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextChat.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
                editTextChat.setText("");
            }
        });

        receiveMessage("Hello! How can I help you today?");

        // Back button
        findViewById(R.id.imageButtonChatBack).setOnClickListener(v -> finish());

        // 进入商家profile
        ImageButton buttonSetting = findViewById(R.id.button_profile);
        buttonSetting.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, ProfileView.class);
            startActivity(intent);
        });

        // 点到图片进入商品详情
        findViewById(R.id.shangpin).setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, ItemDetailActivity.class);
            startActivity(intent);
        });
    }

    private void sendMessage(String messageContent) {
        String avatarUrl = "drawable/xianhang_light_fang";
        messageList.add(new Message(messageContent, avatarUrl, true));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
    }

    public void receiveMessage(String messageContent) {
        String avatarUrl = "drawable/xianhang_light_yuan";
        messageList.add(new Message(messageContent, avatarUrl, false));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
    }
}