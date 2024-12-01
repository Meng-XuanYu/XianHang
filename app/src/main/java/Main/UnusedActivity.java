package Main;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Method;

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

        String u= getIntent().getStringExtra("uri");
        if(u!=null){
            Uri uri = Uri.parse(u);
            generateImg(uri);
        }

        price_arrow = findViewById(R.id.price_arrow);
        price = findViewById(R.id.price);
        price_arrow.setOnClickListener(v->showKey());
        sort_arrow = findViewById(R.id.sort_arrow);
        sort = findViewById(R.id.sort);

        // 退出
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        imageview = findViewById(R.id.imageView);
        imageview.setOnClickListener(v->openGallery());

        // 选择地点
        sort_arrow.setOnClickListener(v -> showBottomSheetDialog());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 设置类型为图片
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

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
        roundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
        roundedImageView1.setOnClickListener(view -> ((FlexboxLayout)imageview.getParent()).removeView(((FrameLayout)view.getParent())));
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
                // 获取图片的详细信息
                getImageDetails(selectedImageUri);
            }
        }
    }

    // 获取图片的详细信息
    private void getImageDetails(Uri imageUri) {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED
        };

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(imageUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            String imageName = cursor.getString(columnIndex);

            columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            long imageSize = cursor.getLong(columnIndex);

            columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            long imageDate = cursor.getLong(columnIndex);

            // 显示图片信息
            Toast.makeText(this, "Name: " + imageName + ", Size: " + imageSize + " bytes", Toast.LENGTH_LONG).show();
        }

        if (cursor != null) {
            cursor.close();
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
//        et_input.addTextChangedListener(new DecimalTextWatcher(et_input));

//        et_input.setOnClickListener(v->et_input.requestFocus());
        et_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
            if(!et_input.hasFocus() || et_input.getText().toString().isEmpty())return;
            int cursorPosition = et_input.getSelectionStart()-1;
            if(et_input.getSelectionStart() == et_input.getText().toString().length()){
                et_input.setText(et_input.getText().toString().substring(0,cursorPosition));
                et_input.setSelection(cursorPosition);
            }else {
                editableText.delete(cursorPosition,cursorPosition+1);
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