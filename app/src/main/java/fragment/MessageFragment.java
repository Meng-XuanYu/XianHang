package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

import Message.MessageLanAdapter;
import Message.MessageLan;
import Message.ChatActivity;

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<MessageLan> messageLanList = new ArrayList<>();

        // 添加数据
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Hello!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "How are you?", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "I'm fine, thank you!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Goodbye!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Hello!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "How are you?", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "I'm fine, thank you!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Goodbye!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Hello!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "How are you?", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_yuan, "I'm fine, thank you!", R.drawable.old_main_building));
        messageLanList.add(new MessageLan(R.drawable.xianhang_light_fang, "Goodbye!", R.drawable.old_main_building));

        MessageLanAdapter adapter = new MessageLanAdapter(messageLanList, messageLan -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            // 传递必要的数据到ChatActivity
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}