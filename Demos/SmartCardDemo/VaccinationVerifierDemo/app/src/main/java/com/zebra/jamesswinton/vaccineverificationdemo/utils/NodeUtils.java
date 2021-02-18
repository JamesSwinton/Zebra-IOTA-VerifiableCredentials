package com.zebra.jamesswinton.vaccineverificationdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;

public class NodeUtils {

    // Prefs Holder
    private static final String mNodeJSPreferences = "NODEJS_MOBILE_PREFS";
    private static final String mPrefLastUpdateTime = "NODEJS_MOBILE_APK_LastUpdateTime";

    /**
     * Utility Method to add Node libraries if APK changes
     */

    public static boolean wasApkUpdated(Context context) {
        // Get Last Update time from Shared Prefs
        SharedPreferences prefs = context.getSharedPreferences(mNodeJSPreferences, Context.MODE_PRIVATE);
        long previousLastUpdateTime = prefs.getLong(mPrefLastUpdateTime, 0);
        long lastUpdateTime = 0;

        // Get Last update Time from PackageInfo
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Validate if package has been updated
        return (lastUpdateTime != previousLastUpdateTime);
    }

    public static void recursivelyDeleteLegacyFolders(String nodeInstallationDirectory) {
        File nodeDirReference = new File(nodeInstallationDirectory);
        if (nodeDirReference.exists()) {
            FileUtils.deleteFolderRecursively(new File(nodeInstallationDirectory));
        }
    }

    public static void saveLastUpdateTime(Context context) {
        long lastUpdateTime = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 0);
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = context
                .getSharedPreferences("NODEJS_MOBILE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("NODEJS_MOBILE_APK_LastUpdateTime", lastUpdateTime);
        editor.commit();
    }
}
