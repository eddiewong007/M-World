package com.mworld.support.utils;

import android.app.Activity;
import android.content.res.TypedArray;

public class ThemeUtility {
	
	public static int getColor(int attr) {
        return getColor(GlobalContext.getInstance().getActivity(), attr);
    }

    public static int getColor(Activity activity, int attr) {
        int[] attrs = new int[]{attr};
        TypedArray ta = activity.obtainStyledAttributes(attrs);
        int color = ta.getColor(0, 430);
        ta.recycle();
        return color;
    }
}
