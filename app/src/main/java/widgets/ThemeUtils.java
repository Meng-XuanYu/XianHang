package widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import com.example.login.R;


public class ThemeUtils {

    private static final int[] APPCOMPAT_CHECK_ATTRS = { R.attr.navigateTabSelectedTextColor };

    public static void checkAppCompatTheme(Context context) {
        @SuppressLint("ResourceType") TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        boolean failed = !a.hasValue(0);
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme " + "(or descendant) with the design library.");
        }
    }
}
