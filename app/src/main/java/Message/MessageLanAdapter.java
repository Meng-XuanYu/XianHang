package Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.avatar.setImageResource(messageLan.getAvatarResId());
        holder.messageText.setText(messageLan.getMessageText());
        holder.commodityImage.setImageResource(messageLan.getMessageImageResId());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(messageLan));
    }

    @Override
    public int getItemCount() {
        return messageLanList.size();
    }

    public static class MessageLanViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView messageText;
        ImageView commodityImage;

        public MessageLanViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            messageText = itemView.findViewById(R.id.message_text);
            commodityImage = itemView.findViewById(R.id.commodity_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MessageLan messageLan);
    }
}