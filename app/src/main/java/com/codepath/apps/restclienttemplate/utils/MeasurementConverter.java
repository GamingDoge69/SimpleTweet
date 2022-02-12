package com.codepath.apps.restclienttemplate.utils;

import android.content.Context;

public class MeasurementConverter {
    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

}
