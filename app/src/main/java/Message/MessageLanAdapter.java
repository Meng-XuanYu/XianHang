package Message;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.R;

import java.util.List;

public class MessageLanAdapter extends RecyclerView.Adapter<MessageLanAdapter.MessageLanViewHolder> {

    private List<MessageLan> messageLanList;
    private OnItemClickListener onItemClickListener;

    public MessageLanAdapter(List<MessageLan> messageLanList, OnItemClickListener onItemClickListener) {
        this.messageLanList = messageLanList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MessageLanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageLanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageLanViewHolder holder, int position) {
        MessageLan messageLan = messageLanList.get(position);
        Glide.with(holder.itemView)
                .load(messageLan.getAvatar())
                .placeholder(R.drawable.xianhang_light_yuan)  // 占位图
                .error(R.drawable.xianhang_light_yuan).into(holder.avatar);
        holder.messageText.setText(messageLan.getMessageText());
        Glide.with(holder.itemView)
                .load(messageLan.getCommodityImage())
                .placeholder(R.drawable.xianhang_light_yuan)  // 占位图
                .error(R.drawable.xianhang_light_yuan).into(holder.commodityImage);
        holder.name.setText(messageLan.getName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(messageLan));
        if (messageLan.isRedDotVisible()) {
            holder.redDot.setVisibility(View.VISIBLE);
        } else {
            holder.redDot.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messageLanList.size();
    }

    public static class MessageLanViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView messageText;
        ImageView commodityImage;
        TextView name;
        ImageView redDot;

        public MessageLanViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            messageText = itemView.findViewById(R.id.message_text);
            commodityImage = itemView.findViewById(R.id.commodity_image);
            name = itemView.findViewById(R.id.name);
            redDot = itemView.findViewById(R.id.red_dot);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MessageLan messageLan);
    }
}