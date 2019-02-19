package com.meetup.matt.meetup.Helpers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

public class ActivityTransitionHelper {
    public static void displayActivity(Intent intent, boolean isAnimated, Context context) {
        if (intent != null) {
            ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            context.getApplicationContext().startActivity(intent);
        }
    }
}
