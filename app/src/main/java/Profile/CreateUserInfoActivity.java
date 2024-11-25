package Profile;



import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

public class CreateUserInfoActivity extends AppCompatActivity {
    private ImageView back ;
    private TextView nickname ;
    private LinearLayout nickname_arrow;
    private TextView brief;
    private LinearLayout brief_arrow;
    private TextView pos;
    private LinearLayout pos_arrow;
    private TextView tel;
    private LinearLayout tel_arrow;
    private TextView user_id;
    private TextView studentId;
    private TextView hanglizhi;
    private RoundedImageView avatar;
    private LinearLayout avatar_arrow;
    private LinearLayout studentId_arrow;
    private LinearLayout user_id_arrow;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_info);
        String name = getIntent().getStringExtra("nickname");

        back = findViewById(R.id.backButton);
        nickname = findViewById(R.id.nickname);
        if(name!=null){
            nickname.setText(name);
        }else{
            //初次进入该页面，需要从服务器请求数据
        }
        nickname_arrow = findViewById(R.id.nickname_arrow);
        brief= findViewById(R.id.brief);
        brief_arrow= findViewById(R.id.brief_arrow);
        pos= findViewById(R.id.pos);
        pos_arrow= findViewById(R.id.pos_arrow);
        tel= findViewById(R.id.tel);
        tel_arrow= findViewById(R.id.tel_arrow);
        user_id= findViewById(R.id.user_id);
        studentId= findViewById(R.id.studentId);
        avatar = findViewById(R.id.avatar);
        avatar_arrow = findViewById(R.id.avatar_arrow);
        user_id_arrow = findViewById(R.id.user_id_arrow);
        studentId_arrow  = findViewById(R.id.studentId_arrow);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(EditUserInfoActivity.this);
//                    startActivity(intent);
//            }
//        });
//        nickname.setText();
//        pos.setText();
//        tel.setText();
//        user_id.setText();
//        studentId.setText();
//        hanglizhi.setText();
//        avatar.setBackgroundResource();
        nickname_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateUserInfoActivity.this,SetNicknameActivity.class);
                intent.putExtra("nickname",nickname.getText());
                startActivity(intent);
            }
        });
        pos_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        avatar_arrow.setOnClickListener(v -> openGallery());
        tel_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EditUserInfoActivity.this,);
//                startActivity(intent);
                showRechargeDialog(tel.getText().toString(),"电话号码");
            }
        });
        user_id_arrow.setOnClickListener(v->showRechargeDialog(user_id.getText().toString(),"身份证号"));
        studentId_arrow.setOnClickListener(v->showRechargeDialog(studentId.getText().toString(),"学号"));
        brief_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateUserInfoActivity.this,SetBriefActivity.class);
                startActivity(intent);
            }
        });
        pos_arrow.setOnClickListener(v -> showBottomSheetDialog());
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
            String[] data = {"北京航空航天大学 学院路校区", "北京航空航天大学 沙河校区", "北京航空航天大学 杭州校区", "未知"};
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
                    pos.setText(numberPicker.getDisplayedValues()[selectedIndex]);
                }
                bottomSheetDialog.dismiss();
            });
        }

        // 显示 BottomSheetDialog
        bottomSheetDialog.show();

    }

    // 启动图库选择图片
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 设置类型为图片
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    // 处理选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                avatar.setImageURI(selectedImageUri); // 加载图片到 ImageView
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showRechargeDialog(String s,String title) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CreateUserInfoActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recharge, null);
        builder.setView(dialogView)
                .setTitle("修改"+title)
                .setBackground(getResources().getDrawable(R.drawable.rounded_background, null))
                .setIcon(R.drawable.xianhang_light_fang)
                .setPositiveButton("确认", (dialog, which) -> {
                    EditText editTextAmount = dialogView.findViewById(R.id.edit_text_amount);
                    String str = editTextAmount.getText().toString();
                    if(str.length()!=11 &&title.equals("电话号码")){
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(title.equals("电话号码")){
                        tel.setText(str);
                        dialog.dismiss();
                    }
                    if(str.length()!=8 &&title.equals("学号")){
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的学号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(title.equals("学号")){
                        studentId.setText(str);
                        dialog.dismiss();
                    }
                    if(str.length()!=18 &&title.equals("身份证号")){
                        Toast.makeText(CreateUserInfoActivity.this, "请输入输入正确的身份证号", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(title.equals("身份证号")){
                        user_id.setText(str);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
        EditText text = dialogView.findViewById(R.id.edit_text_amount);
        text.setText(s);
    }
}