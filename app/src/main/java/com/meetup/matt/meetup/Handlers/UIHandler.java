package com.meetup.matt.meetup.Handlers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class UIHandler extends Activity {

    public static void handleToast(Context context, final String msg) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
