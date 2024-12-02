package Main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.Objects;

import RetrofitClient.RetrofitClient;
import model.AIPublishRequest;
import model.AIPublishResponse;
import network.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class WaitActivity extends AppCompatActivity {
    private RoundedImageView roundedImageView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        String imageUri = getIntent().getStringExtra("imageUri");
        uri = Uri.parse(imageUri);
        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        roundedImageView = findViewById(R.id.img);

        roundedImageView.setImageURI(uri);

        ImageView ball1 = findViewById(R.id.ball1);
        ImageView ball2 = findViewById(R.id.ball2);
        animateBalls(ball1, ball2);

        // 把图片上传到服务器
        uploadImageToServer();
    }

    private void animateBalls(ImageView ball1, ImageView ball2) {
        // 小球1的左右晃动动画
        ObjectAnimator ball1Animator = ObjectAnimator.ofFloat(ball1, "translationX", 0f, 150f, 0f);
        ball1Animator.setDuration(2500);
        ball1Animator.setRepeatCount(ObjectAnimator.INFINITE);
        ball1Animator.setRepeatMode(ObjectAnimator.RESTART);
        ball1Animator.start();

        // 小球2的左右晃动动画
        ObjectAnimator ball2Animator = ObjectAnimator.ofFloat(ball2, "translationX", 0f, -150f, 0f);
        ball2Animator.setDuration(2500);
        ball2Animator.setRepeatCount(ObjectAnimator.INFINITE);
        ball2Animator.setRepeatMode(ObjectAnimator.RESTART);
        ball2Animator.start();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    private void uploadImageToServer() {
        // 上传图片到服务器
        File file = new File(getRealPathFromURI(uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        // 发起请求
        ApiService apiService = RetrofitClient.getApiService();
        apiService.aiPublish(image).enqueue(new Callback<AIPublishResponse>() {
            @Override
            public void onResponse(Call<AIPublishResponse> call, Response<AIPublishResponse> response) {
                if (response.isSuccessful()) {
                    AIPublishResponse aiPublishResponse = response.body();
                    if (aiPublishResponse != null) {
                        Intent intent = new Intent(WaitActivity.this, UnusedActivity.class);
                        intent.putExtra("commodityName", aiPublishResponse.getCommodityName());
                        intent.putExtra("commodityDescription", aiPublishResponse.getCommodityDescription());
                        intent.putExtra("commodityValue", aiPublishResponse.getCommodityValue());
                        intent.putExtra("commodityKeywords", aiPublishResponse.getCommodityKeywords());
                        intent.putExtra("commodityImage", aiPublishResponse.getCommodityImage());
                        intent.putExtra("ai", true);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(WaitActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AIPublishResponse> call, Throwable t) {
                Toast.makeText(WaitActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}