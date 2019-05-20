package top.jewishcheck.radiocar;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class Utils {
    /**
     * @param context
     * @param key
     * @param defaultVal
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getStringMetadata(Context context, String key, String defaultVal) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = applicationInfo.metaData;

        return bundle.getString(key, defaultVal);
    }

    /**
     * @param context
     * @param key
     * @param defaultVal
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static int getIntMetadata(Context context, String key, int defaultVal) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = applicationInfo.metaData;

        return bundle.getInt(key, defaultVal);
    }

    /**
     * @param context
     * @param text
     */
    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * @param context
     * @param text
     */
    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
