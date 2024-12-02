package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Message.MessageLanAdapter;
import Message.MessageLan;
import Message.ChatActivity;
import RetrofitClient.RetrofitClient;
import model.GetChatListRequest;
import model.GetChatListResponse;
import network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {
    private List<MessageLan> messageLanList = new ArrayList<>();
    private HashMap<MessageLan, String> chatId = new HashMap<>();
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        generateMessageLanList();

        return view;
    }

    private void generateMessageLanList() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        GetChatListRequest request = new GetChatListRequest(userId);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getChatList(request).enqueue(new Callback<GetChatListResponse>() {
            @Override
            public void onResponse(Call<GetChatListResponse> call, Response<GetChatListResponse> response) {
                if (response.isSuccessful()) {
                    List<GetChatListResponse.Chat> chatList = null;
                    if (response.body() != null) {
                        chatList = response.body().getChatList();
                    }
                    if (chatList != null) {
                        for (GetChatListResponse.Chat chat : chatList) {
                            Uri uri = Uri.parse(chat.getOtherAvatar());
                            Uri commodityUri = Uri.parse(chat.getCommodityImage());
                            MessageLan messageLan = new MessageLan(uri, chat.getLatestMessageContent(), commodityUri, chat.getOtherName(), chat.getOtherId(userId), chat.getCommodityId());
                            messageLanList.add(messageLan);
                            chatId.put(messageLan, chat.getChatId());
                        }
                    }
                    MessageLanAdapter adapter = new MessageLanAdapter(messageLanList, messageLan -> {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("chatId", chatId.get(messageLan));
                        intent.putExtra("otherAvatar", messageLan.getAvatar().toString());
                        intent.putExtra("otherId", messageLan.getOtherId());
                        intent.putExtra("commodityId", messageLan.getCommodityId());
                        intent.putExtra("otherName", messageLan.getName());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "获取消息列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetChatListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "获取消息列表失败send", Toast.LENGTH_SHORT).show();
            }
        });
    }
}