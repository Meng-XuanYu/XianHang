package Main;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.login.R;

import Search.SearchDetailActivity;

public class PopMenuView {

    private MainActivity mainActivity;

    public static PopMenuView getInstance() {
        return PopupMenuViewHolder.INSTANCE;
    }

    private static class PopupMenuViewHolder {
        public static PopMenuView INSTANCE = new PopMenuView();
    }

    private View        mRootVew;
    private PopupWindow mPopupWindow;

    private RelativeLayout mCloseLayout;
    private ImageView      mCloseIv, slogan;
    private ImageButton button,button_AI;

    /**
     * 动画执行的 属性值数组
     */
    private float mAnimatorProperty[] = null;

    /**
     * 第一排图 距离屏幕底部的距离
     */
    private int mTop = 0;

    /**
     * 第二排图 距离屏幕底部的距离
     */
    private int mBottom = 0;

    /**
     * 创建PopupWindow
     *
     */
    @SuppressLint("ResourceType")
    private void createView(final Context context) {
        this.mRootVew = LayoutInflater.from(context).inflate(R.layout.view_pop_menu, null);
        this.mPopupWindow = new PopupWindow(this.mRootVew, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.mPopupWindow.setFocusable(false); // 设置为失去焦点 方便监听返回键的监听
        mPopupWindow.setClippingEnabled(false); // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        this.mPopupWindow.setOutsideTouchable(false);

        if (this.mAnimatorProperty == null) {
            this.mTop = dip2px(context, 310);
            this.mBottom = dip2px(context, 210);
            this.mAnimatorProperty = new float[] { this.mBottom, 60, -30, -20 - 10, 0 };
        }

        this.initLayout(context);

        // 发闲置按钮
        button.setOnClickListener(view -> {
            Intent intent = new Intent(context, UnusedActivity.class);
            context.startActivity(intent);
        });
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     *
     * @return 转换之后的px值
     */
    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(Context context) {
        this.mCloseLayout = (RelativeLayout) this.mRootVew.findViewById(R.id.close_layout);
        this.mCloseIv = (ImageView) this.mRootVew.findViewById(R.id.close_iv);
        this.slogan = (ImageView) this.mRootVew.findViewById(R.id.slogan);
        this.button = (ImageButton) this.mRootVew.findViewById(R.id.button_post);
        this.button_AI = (ImageButton) this.mRootVew.findViewById(R.id.button_AI);

        this.mCloseLayout.setOnClickListener(new ItemClick(0, context));

    }

    /**
     * Item点击事件
     */
    private class ItemClick implements View.OnClickListener {

        private int     index;
        private Context context;

        public ItemClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {   // 关闭按钮
                closePopupWindowAction();
            }
            else {
                Toast.makeText(context, "index：" + index, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 打开popupWindow执行的动画
     */
    private void openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseIv, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        startAnimation(this.slogan, 300, this.mAnimatorProperty, true);
        startAnimation(this.button, 400, this.mAnimatorProperty, false);
        startAnimation(this.button_AI, 500, this.mAnimatorProperty, false);
    }


    /**
     * 关闭popupWindow执行的动画
     */
    public void closePopupWindowAction() {
        if (this.mCloseIv != null && this.mCloseLayout != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.mCloseIv, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            closeAnimation(this.slogan, 300, this.mTop, true);
            closeAnimation(this.button, 400, this.mTop, false);
            closeAnimation(this.button_AI, 300, this.mTop, false);

            this.mCloseLayout.postDelayed(this::close, 300);
        }
    }

    /**
     * 显示PopupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void show(Context context, View parent) {
        createView(context);
        if (this.mPopupWindow != null && !this.mPopupWindow.isShowing()) {
            this.mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            openPopupWindowAction();
        }
    }

    /**
     * 关闭popupWindow
     */

    public void close() {
        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
            this.mPopupWindow = null;
        }
    }

    /**
     * PopupWindow是否显示了
     *
     * @return
     */
    public boolean isShowing() {
        return this.mPopupWindow != null && this.mPopupWindow.isShowing();
    }

    /**
     * 启动PopupWindow动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void startAnimation(View view, int duration, float[] distance, boolean isSlogan) {
        ObjectAnimator anim;
        if (!isSlogan) {
            anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        } else {
            anim = ObjectAnimator.ofFloat(view, "translationY", -distance[0], -distance[distance.length - 1]);
        }
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 关闭PopupWindow动画
     *
     * @param view     view
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void closeAnimation(View view, int duration, int next, boolean isSlogan) {
        ObjectAnimator anim;
        if (!isSlogan) {
            anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        } else {
            anim = ObjectAnimator.ofFloat(view, "translationY", 0f, -next);
        }
        anim.setDuration(duration);
        anim.start();

    }
}
