package widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

public class NavigateTabBar extends LinearLayout implements View.OnClickListener {

    private static final String KEY_CURRENT_TAG = "NavigateTabBar";

    private List<ViewHolder>      mViewHolderList;
    private OnTabSelectedListener mTabSelectListener;
    private FragmentActivity mFragmentActivity;
    private String                mCurrentTag;
    private String                mRestoreTag;

    /**
     * 主内容显示区域View的id
     */
    private int mMainContentLayoutId;

    /**
     * 选中的Tab文字颜色
     */
    private ColorStateList mSelectedTextColor;

    /**
     * 正常的Tab文字颜色
     */
    private ColorStateList mNormalTextColor;

    /**
     * Tab文字的颜色
     */
    private float mTabTextSize;

    /**
     * 默认选中的tab index
     */
    private int mDefaultSelectedTab = 0;

    /**
     * 当前选中的tab
     */
    private int mCurrentSelectedTab;

    public NavigateTabBar(Context context) {
        this(context, null);
    }

    public NavigateTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public NavigateTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NavigateTabBar, 0, 0);
        ColorStateList tabTextColor = typedArray.getColorStateList(R.styleable.NavigateTabBar_navigateTabTextColor);
        ColorStateList selectedTabTextColor = typedArray.getColorStateList(R.styleable.NavigateTabBar_navigateTabSelectedTextColor);

        this.mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.NavigateTabBar_navigateTabTextSize, 0);
        this.mMainContentLayoutId = typedArray.getResourceId(R.styleable.NavigateTabBar_containerId, 0);
        this.mNormalTextColor = (tabTextColor != null ? tabTextColor : context.getResources().getColorStateList(R.color.navigate_tabbar_text_normal));

        if (selectedTabTextColor != null) {
            this.mSelectedTextColor = selectedTabTextColor;
        }
        else {
            ThemeUtils.checkAppCompatTheme(context);
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.navigateTabSelectedTextColor, typedValue, true);
            this.mSelectedTextColor = context.getResources().getColorStateList(typedValue.resourceId);
        }

        this.mViewHolderList = new ArrayList<>();
    }

    public void setRedDotVisibility(String tag, boolean visible) {
        for (ViewHolder holder : this.mViewHolderList) {
            if (TextUtils.equals(tag, holder.tag)) {
                holder.redDot.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            }
        }
    }

    public void refreshMessageFragment() {
        setCurrentSelectedTab(2);
    }

    public void addTab(Class frameLayoutClass, TabParam tabParam) {
        int defaultLayout = R.layout.view_navigate_tabbar;

        View view = LayoutInflater.from(getContext()).inflate(defaultLayout, null);
        view.setFocusable(true);

        ViewHolder holder = new ViewHolder();
        holder.tabIndex = this.mViewHolderList.size();
        holder.fragmentClass = frameLayoutClass;
        holder.tag = tabParam.title;
        holder.pageParam = tabParam;
        holder.tabIcon = view.findViewById(R.id.tab_icon);
        holder.tabTitle = view.findViewById(R.id.tab_title);
        holder.redDot = view.findViewById(R.id.red_dot); // 初始化红点视图

        if (TextUtils.isEmpty(tabParam.title)) {
            holder.tabTitle.setVisibility(View.INVISIBLE);
        } else {
            holder.tabTitle.setText(tabParam.title);
        }

        if (this.mTabTextSize != 0) {
            holder.tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mTabTextSize);
        }
        if (this.mNormalTextColor != null) {
            holder.tabTitle.setTextColor(this.mNormalTextColor);
        }

        if (tabParam.backgroundColor > 0) {
            view.setBackgroundResource(tabParam.backgroundColor);
        }

        if (tabParam.iconResId > 0) {
            holder.tabIcon.setImageResource(tabParam.iconResId);
        } else {
            holder.tabIcon.setVisibility(View.INVISIBLE);
        }

        if (tabParam.iconResId > 0 && tabParam.iconSelectedResId > 0) {
            view.setTag(holder);
            view.setOnClickListener(this);
            this.mViewHolderList.add(holder);
        }

        super.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mMainContentLayoutId == 0) {
            throw new RuntimeException("mFrameLayoutId Cannot be 0");
        }
        if (this.mViewHolderList.size() == 0) {
            throw new RuntimeException("mViewHolderList.size Cannot be 0, Please call addTab()");
        }
        if (!(getContext() instanceof FragmentActivity)) {
            throw new RuntimeException("parent activity must is extends FragmentActivity");
        }
        this.mFragmentActivity = (FragmentActivity) getContext();

        ViewHolder defaultHolder = null;

        hideAllFragment();
        if (!TextUtils.isEmpty(this.mRestoreTag)) {
            for (ViewHolder holder : this.mViewHolderList) {
                if (TextUtils.equals(this.mRestoreTag, holder.tag)) {
                    defaultHolder = holder;
                    this.mRestoreTag = null;
                    break;
                }
            }
        }
        else {
            defaultHolder = this.mViewHolderList.get(this.mDefaultSelectedTab);
        }

        this.showFragment(defaultHolder);
    }

    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object != null && object instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) v.getTag();
            showFragment(holder);
            if (this.mTabSelectListener != null) {
                this.mTabSelectListener.onTabSelected(holder);
            }
        }
    }

    private void showFragment(ViewHolder holder) {
        FragmentTransaction transaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
        setCurrSelectedTabByTag(holder.tag);

        // 始终创建Fragment的新实例
        Fragment fragment = getFragmentInstance(holder.tag);
        transaction.replace(this.mMainContentLayoutId, fragment, holder.tag);
        transaction.commitAllowingStateLoss(); // 使用commitAllowingStateLoss()代替commit()
        this.mCurrentSelectedTab = holder.tabIndex;
    }

    private void setCurrSelectedTabByTag(String tag) {
        if (TextUtils.equals(this.mCurrentTag, tag)) {
            return;
        }
        for (ViewHolder holder : this.mViewHolderList) {
            if (TextUtils.equals(this.mCurrentTag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconResId);
                holder.tabTitle.setTextColor(this.mNormalTextColor);
            }
            else if (TextUtils.equals(tag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconSelectedResId);
                holder.tabTitle.setTextColor(this.mSelectedTextColor);
            }
        }
        this.mCurrentTag = tag;
    }

    private Fragment getFragmentInstance(String tag) {
        Fragment fragment = null;
        for (ViewHolder holder : this.mViewHolderList) {
            if (TextUtils.equals(tag, holder.tag)) {
                try {
                    fragment = (Fragment) Class.forName(holder.fragmentClass.getName()).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return fragment;
    }

    /**
     * 隐藏所有的fragment
     */
    private void hideAllFragment() {
        if (this.mViewHolderList == null || this.mViewHolderList.size() == 0) {
            return;
        }
        FragmentTransaction transaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
        for (ViewHolder holder : this.mViewHolderList) {
            Fragment fragment = this.mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    /**
     * 设置选中的tab文字颜色
     *
     * @param selectedTextColor
     */
    public void setSelectedTabTextColor(ColorStateList selectedTextColor) {
        this.mSelectedTextColor = selectedTextColor;
    }

    /**
     * 设置选中的tab文字颜色
     *
     * @param color
     */
    public void setSelectedTabTextColor(int color) {
        this.mSelectedTextColor = ColorStateList.valueOf(color);
    }

    /**
     * 设置tab文字颜色
     *
     * @param color
     */
    public void setTabTextColor(ColorStateList color) {
        this.mNormalTextColor = color;
    }

    /**
     * 设置tab文字颜色
     *
     * @param color
     */
    public void setTabTextColor(int color) {
        this.mNormalTextColor = ColorStateList.valueOf(color);
    }

    /**
     * 设置fragment布局文件
     *
     * @param frameLayoutId
     */
    public void setFrameLayoutId(int frameLayoutId) {
        this.mMainContentLayoutId = frameLayoutId;
    }

    /**
     * 恢复状态
     *
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mRestoreTag = savedInstanceState.getString(KEY_CURRENT_TAG);
        }
    }

    /**
     * 保存状态
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_TAG, this.mCurrentTag);
    }

    public Fragment getFragmentByTag(String tag) {
        return this.mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        public String tag;
        public TabParam pageParam;
        public ImageView tabIcon;
        public TextView tabTitle;
        public View redDot; // 添加红点视图
        public Class fragmentClass;
        public int tabIndex;
    }

    /**
     * tab参数类
     */
    public static class TabParam {
        public int backgroundColor = android.R.color.white;
        public int    iconResId;
        public int    iconSelectedResId;
        public int    titleStringRes;
        //        public int tabViewResId;
        public String title;

        public TabParam(int iconResId, int iconSelectedResId, String title) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }

        public TabParam(int iconResId, int iconSelectedResId, int titleStringRes) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        /**
         * 构造方法
         *
         * @param backgroundColor
         * @param iconResId
         * @param iconSelectedResId
         * @param titleStringRes
         */
        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, int titleStringRes) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        /**
         * 构造方法
         *
         * @param backgroundColor
         * @param iconResId
         * @param iconSelectedResId
         * @param title
         */
        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, String title) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }
    }

    /**
     * tab选中监听器
     */
    public interface OnTabSelectedListener {
        void onTabSelected(ViewHolder holder);
    }

    /**
     * 设置tab选中监听器
     *
     */
    public void setTabSelectListener(OnTabSelectedListener tabSelectListener) {
        this.mTabSelectListener = tabSelectListener;
    }

    /**
     * 设置默认选中的tab
     *
     */
    public void setDefaultSelectedTab(int index) {
        if (index >= 0 && index < this.mViewHolderList.size()) {
            this.mDefaultSelectedTab = index;
        }
    }

    /**
     * 设置当前选中的tab
     *
     */
    public void setCurrentSelectedTab(int index) {
        if (index >= 0 && index < this.mViewHolderList.size()) {
            ViewHolder holder = this.mViewHolderList.get(index);
            this.showFragment(holder);
        }
    }

    /**
     * 获取当前选中的tab
     *
     */
    public int getCurrentSelectedTab() {
        return this.mCurrentSelectedTab;
    }
}
