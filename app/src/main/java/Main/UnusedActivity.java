package Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.login.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;


import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import model.EditCommodityRequest;
import model.EditCommodityResponse;
import model.GetCommodityResponse;
import model.GetProfileResponse;
import model.PublishCommodityResponse;
import network.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import RetrofitClient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnusedActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private TextView imageview;

    private RelativeLayout btn_0;
    private RelativeLayout btn_1;
    private RelativeLayout btn_2;
    private RelativeLayout btn_3;
    private RelativeLayout btn_4;
    private RelativeLayout btn_5;
    private RelativeLayout btn_6;
    private RelativeLayout btn_7;
    private RelativeLayout btn_8;
    private RelativeLayout btn_9;
    private RelativeLayout btn_point;
    private RelativeLayout btn_hide;
    private LinearLayout btn_delete;
    private LinearLayout btn_confirm;
    private LinearLayout price_arrow;
    private TextView price;
    private EditText et_input;

    private LinearLayout sort_arrow;
    private TextView sort;
    private EditText editText_name;
    private EditText editText;
    private TextView pos;
    private Button button;

    private ArrayList<Uri> imageList = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused);

        // 沉浸式体验
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        price_arrow = findViewById(R.id.price_arrow);
        price = findViewById(R.id.price);
        price_arrow.setOnClickListener(v->showKey());
        sort_arrow = findViewById(R.id.sort_arrow);
        sort = findViewById(R.id.sort);
        editText_name = findViewById(R.id.editText_name);
        pos = findViewById(R.id.pos);
        editText = findViewById(R.id.editText);
        imageList = new ArrayList<>();
        imageview = findViewById(R.id.imageView);

        // 校区
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileJson = sharedPreferences.getString("userProfile", null);
        if (profileJson != null) {
            Gson gson = new Gson();
            GetProfileResponse profile = gson.fromJson(profileJson, GetProfileResponse.class);
            pos.setText("校区：" + profile.getSchool());
        }

        // 退出
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        imageview.setOnClickListener(v->openGallery());

        // 选择分类
        sort_arrow.setOnClickListener(v -> showBottomSheetDialog());

        String ai = String.valueOf(getIntent().getBooleanExtra("ai",false));
        String edit = String.valueOf(getIntent().getBooleanExtra("edit",false));
        if(ai.equals("true")){
            editText_name.setText(getIntent().getStringExtra("commodityName"));
            editText.setText(getIntent().getStringExtra("commodityDescription"));
            price.setText(getIntent().getStringExtra("commodityValue"));
            Uri selectedImageUri = Uri.parse(getIntent().getStringExtra("commodityImage"));
            generateImg(selectedImageUri); // 加载图片到 ImageView
            imageList.add(selectedImageUri);
        } else if (edit.equals("true")) {
            getAllDetail();
        }

        // 发布
        button = findViewById(R.id.save);
        if (edit.equals("true")) {
            button.setText("修改");
            button.setOnClickListener(v -> {
                String commodityId = getIntent().getStringExtra("commodityId");
                String nameText = editText_name.getText().toString();
                String descriptionText = editText.getText().toString();
                String priceText = price.getText().toString();
                String sortText = sort.getText().toString();
                editCommodity(commodityId, nameText, descriptionText, priceText, sortText, imageList);
            });
        } else {
            button.setText("发布");
            button.setOnClickListener(v -> {
                // 获取输入的商品信息，图片就是arraylist
                String nameText = editText_name.getText().toString();
                String descriptionText = editText.getText().toString();
                String priceText = price.getText().toString();
                String sortText = sort.getText().toString();
                // 上传商品信息
                publishCommodity(nameText, descriptionText, priceText, sortText, imageList);
            });
        }
    }

    private void editCommodity(String commodityId, String name, String description, String price, String sort, ArrayList<Uri> imageList) {
        // 商品文本信息
        RequestBody commodityIdBody = RequestBody.create(commodityId, MultipartBody.FORM);
        RequestBody nameBody = RequestBody.create(name, MultipartBody.FORM);
        RequestBody descriptionBody = RequestBody.create(description, MultipartBody.FORM);
        RequestBody priceBody = RequestBody.create(price, MultipartBody.FORM);
        RequestBody sortBody = RequestBody.create(sort, MultipartBody.FORM);

        // 商品图片信息
        ArrayList <MultipartBody.Part> imageBodies = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            File file1 = new File(getRealPathFromURI(imageList.get(i)));
            RequestBody imageFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part imageBody1 = MultipartBody.Part.createFormData("image", file1.getName(), imageFile1);
            imageBodies.add(imageBody1);
        }

        // 发起请求
        ApiService apiService = RetrofitClient.getApiService();
        apiService.editCommodity(commodityIdBody, nameBody, descriptionBody, priceBody, null, sortBody, imageBodies)
                .enqueue(new Callback<EditCommodityResponse>() {
                    @Override
                    public void onResponse(Call<EditCommodityResponse> call, Response<EditCommodityResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            EditCommodityResponse result = response.body();
                            if ("success".equals(result.getStatus())) {
                                Toast.makeText(UnusedActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UnusedActivity.this, "修改失败: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UnusedActivity.this, "修改失败，服务器返回错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditCommodityResponse> call, Throwable t) {
                        Toast.makeText(UnusedActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllDetail() {
        String commodityId = getIntent().getStringExtra("commodityId");
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommodity(commodityId).enqueue(new Callback<GetCommodityResponse>() {
            @Override
            public void onResponse(Call<GetCommodityResponse> call, Response<GetCommodityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetCommodityResponse result = response.body();
                    if ("success".equals(result.getStatus())) {
                        GetCommodityResponse.Commodity commodity = result.getCommodity();
                        editText_name.setText(commodity.getCommodityName());
                        editText.setText(commodity.getCommodityDescription());
                        price.setText(commodity.getCommodityValueRaw());
                        sort.setText(commodity.getCommodityClass());
                        if (commodity.getAdditionalImages() != null) {
                            for (String image : commodity.getAdditionalImages()) {
                                Glide.with(UnusedActivity.this)
                                        .downloadOnly()
                                        .load(image)
                                        .into(new CustomTarget<File>() {
                                            @Override
                                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                                Uri localUri = Uri.fromFile(resource);
                                                generateImg(localUri);
                                                imageList.add(localUri);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                // Handle placeholder if needed
                                            }
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(UnusedActivity.this, "获取商品信息失败: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UnusedActivity.this, "获取商品信息失败，服务器返回错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCommodityResponse> call, Throwable t) {
                Toast.makeText(UnusedActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publishCommodity(String name, String description, String price, String sort, ArrayList<Uri> imageList) {
        //userid部分
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        RequestBody userIdBody = RequestBody.create(userId, MultipartBody.FORM);

        // 商品文本信息
        RequestBody nameBody = RequestBody.create(name, MultipartBody.FORM);
        RequestBody descriptionBody = RequestBody.create(description, MultipartBody.FORM);
        RequestBody priceBody = RequestBody.create(price, MultipartBody.FORM);
        RequestBody sortBody = RequestBody.create(sort, MultipartBody.FORM);

        // 商品图片信息
        // 第一张图片
        File file = new File(getRealPathFromURI(imageList.get(0)));
        RequestBody image1File = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image1Body = MultipartBody.Part.createFormData("image", file.getName(), image1File);
        // 后面的图片
        ArrayList <MultipartBody.Part> imageBodies = new ArrayList<>();
        for (int i = 1; i < imageList.size(); i++) {
            File file1 = new File(getRealPathFromURI(imageList.get(i)));
            RequestBody imageFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part imageBody1 = MultipartBody.Part.createFormData("image", file1.getName(), imageFile1);
            imageBodies.add(imageBody1);
        }

        // 发起请求
        ApiService apiService = RetrofitClient.getApiService();

        // 发起请求
        apiService.publishCommodity(userIdBody, nameBody, descriptionBody, priceBody, null, sortBody, image1Body, imageBodies)
                .enqueue(new Callback<PublishCommodityResponse>() {
                    @Override
                    public void onResponse(Call<PublishCommodityResponse> call, Response<PublishCommodityResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PublishCommodityResponse result = response.body();
                            if ("success".equals(result.getStatus())) {
                                Toast.makeText(UnusedActivity.this, "发布成功！商品ID: " + result.getCommodityId(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UnusedActivity.this, "发布失败: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UnusedActivity.this, "发布失败，服务器返回错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PublishCommodityResponse> call, Throwable t) {
                        Toast.makeText(UnusedActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getScheme().equals("file")) {
            return contentUri.getPath();
        } else {
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
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 设置类型为图片
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void generateImg(Uri uri){
        FrameLayout frameLayout = new FrameLayout(this);
        FlexboxLayout.LayoutParams params1 = new FlexboxLayout.LayoutParams(
                dpToPx(this,100),
                dpToPx(this,100)
        );
        params1.setMargins(0,dpToPx(this,5),dpToPx(this,5),dpToPx(this,5));
        frameLayout.setLayoutParams(params1);
        RoundedImageView roundedImageView = new RoundedImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                dpToPx(this,100),
                dpToPx(this,100)
        );
        roundedImageView.setLayoutParams(params);
        roundedImageView.setImageURI(uri);
        roundedImageView.setCornerRadius(dpToPx(this,10));
        frameLayout.addView(roundedImageView);
        RoundedImageView roundedImageView1 = new RoundedImageView(this);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                dpToPx(this,20),
                dpToPx(this,20)
        );
        params2.gravity = Gravity.TOP|Gravity.END;
        roundedImageView1.setLayoutParams(params2);
        roundedImageView1.setImageResource(R.drawable.baseline_close_25);
        roundedImageView1.setPadding(dpToPx(this,4),dpToPx(this,4),dpToPx(this,4),dpToPx(this,4));
        roundedImageView1.setBackground(getResources().getDrawable(R.drawable.close));
        roundedImageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        roundedImageView1.setOnClickListener(view -> {
            ((FlexboxLayout)imageview.getParent()).removeView(((FrameLayout)view.getParent()));
            imageList.remove(uri);
        });
        frameLayout.addView(roundedImageView1);
        ((FlexboxLayout)imageview.getParent()).addView(frameLayout,0);
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    // 处理选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                generateImg(selectedImageUri); // 加载图片到 ImageView
                imageList.add(selectedImageUri);
            }
        }
    }

    private void showKey(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_price);
        et_input = bottomSheetDialog.findViewById(R.id.et_input);

        StringBuilder price1 = new StringBuilder(et_input.getText());
        if(!price1.toString().isEmpty()){
            price1.delete(1,1);
        }
        et_input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 获取系统的 InputMethodManager 服务
                UnusedActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try {
                    Class<EditText> cls = EditText.class;
                    Method setSoftInputShownOnFocus;
                    setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                    setSoftInputShownOnFocus.setAccessible(true);
                    setSoftInputShownOnFocus.invoke(et_input, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_0 = bottomSheetDialog.findViewById(R.id.btn_0);
        btn_1 = bottomSheetDialog.findViewById(R.id.btn_1);
        btn_2 = bottomSheetDialog.findViewById(R.id.btn_2);
        btn_3 = bottomSheetDialog.findViewById(R.id.btn_3);
        btn_4 = bottomSheetDialog.findViewById(R.id.btn_4);
        btn_5 = bottomSheetDialog.findViewById(R.id.btn_5);
        btn_6 = bottomSheetDialog.findViewById(R.id.btn_6);
        btn_7 = bottomSheetDialog.findViewById(R.id.btn_7);
        btn_8 = bottomSheetDialog.findViewById(R.id.btn_8);
        btn_9 = bottomSheetDialog.findViewById(R.id.btn_9);
        btn_point = bottomSheetDialog.findViewById(R.id.btn_point);
        btn_hide = bottomSheetDialog.findViewById(R.id.btn_key_hide);
        btn_confirm = bottomSheetDialog.findViewById(R.id.btn_confirm);
        btn_delete = bottomSheetDialog.findViewById(R.id.btn_delete);
        Editable editableText = et_input.getText();

        btn_0.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "0");
        });
        btn_1.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "1");
        });
        btn_2.setOnClickListener(v->{if(!et_input.hasFocus())return;

            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "2");
        });
        btn_3.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "3");
        });
        btn_4.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "4");
        });
        btn_5.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "5");
        });
        btn_6.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "6");
        });
        btn_7.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "7");
        });
        btn_8.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "8");
        });
        btn_9.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, "9");
        });
        btn_point.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            editableText.insert(cursorPosition, ".");
        });
        btn_hide.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });
        btn_confirm.setOnClickListener(v->{
            int in = et_input.getText().toString().indexOf('.');
            if(in!=-1){
                if(et_input.getText().toString().
                        indexOf('.',et_input.getText().toString().indexOf('.'))!=-1){
                    Toast.makeText(this,"输入格式不正确", Toast.LENGTH_LONG).show();
                    return;
                } else if(et_input.getText().toString().length()-in >3){
                    Toast.makeText(this,"输入格式不正确", Toast.LENGTH_LONG).show();
                    return;
                } else if(in > 1 && et_input.getText().toString().charAt(0)=='0'){
                    Toast.makeText(this,"输入格式不正确", Toast.LENGTH_LONG).show();
                    return;
                }
            }else if(et_input.getText().toString().charAt(0)=='0'){
                Toast.makeText(this,"输入格式不正确", Toast.LENGTH_LONG).show();
                return;
            }
            price.setText(et_input.getText());
            bottomSheetDialog.dismiss();
        });
        btn_delete.setOnClickListener(v->{
            if(!et_input.hasFocus())return;
            int cursorPosition = et_input.getSelectionStart();
            if (cursorPosition > 0) {
                editableText.delete(cursorPosition - 1, cursorPosition);
            }

        });
        bottomSheetDialog.show();
    }

    private void showBottomSheetDialog() {
        // 创建 BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        // 获取 NumberPicker 和按钮
        NumberPicker numberPicker = bottomSheetDialog.findViewById(R.id.numberPicker);
        numberPicker.setWrapSelectorWheel(false);
        Handler handler = new Handler();
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            handler.postDelayed(() -> {
            }, 200); // 延迟 200ms 更新
        });
        Button btnConfirm = bottomSheetDialog.findViewById(R.id.btnConfirm);
        for (int i = 0; i < numberPicker.getChildCount(); i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                child.setFocusable(false);
                child.setClickable(false);
            }
        }
        // 配置 NumberPicker
        if (numberPicker != null) {
            String[] data = {"书籍", "日常", "衣物", "数码", "食品", "宿舍用品", "游戏", "服务", "鞋子", "其他"};
            numberPicker.setDisplayedValues(data);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(data.length - 1);
            numberPicker.setWrapSelectorWheel(true); // 滚动循环
        }
        // 按钮点击事件
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (numberPicker != null) {
                    int selectedIndex = numberPicker.getValue();
                    sort.setText(numberPicker.getDisplayedValues()[selectedIndex]);
                }
                bottomSheetDialog.dismiss();
            });
        }

        // 显示 BottomSheetDialog
        bottomSheetDialog.show();
    }
}