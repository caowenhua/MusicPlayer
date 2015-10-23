package org.yekeqi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class SpUtil {
    public static boolean getIsLoop(Context context){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        return preferences.getBoolean("isLoop", true);
    }

    public static void setIsLoop(Context context, boolean isLoop){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoop", isLoop);
        editor.commit();
    }

    public static boolean getIsNotificationOpen(Context context){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        return preferences.getBoolean("isNotificationOpen", true);
    }

    public static void setIsNotificationOpen(Context context, boolean isNotificationOpen){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isNotificationOpen", isNotificationOpen);
        editor.commit();
    }

    public static int getLastPlatId(Context context){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        return preferences.getInt("lastPlatId", -1);
    }

    public static void setLastPlatId(Context context, int lastPlatId){
        SharedPreferences preferences = context.getSharedPreferences("params", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("lastPlatId", lastPlatId);
        editor.commit();
    }
}
