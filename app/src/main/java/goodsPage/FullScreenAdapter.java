package goodsPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.R;

import java.util.List;

public class FullScreenAdapter extends RecyclerView.Adapter<FullScreenAdapter.ViewHolder> {
    private final Context context;
    private final List<Integer> imageUrls;

    public FullScreenAdapter(Context context, List<Integer> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fullscreen_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer imageUrl = imageUrls.get(position);
        Glide.with(context).load(imageUrl).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            if (context instanceof Activity) {
                ((Activity) context).finish(); // 退出全屏
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fullscreenImage);
        }
    }
}

