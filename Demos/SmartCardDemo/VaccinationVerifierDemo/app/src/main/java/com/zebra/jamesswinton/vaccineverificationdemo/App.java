package com.zebra.jamesswinton.vaccineverificationdemo;

import android.app.Application;

import com.zebra.jamesswinton.vaccineverificationdemo.utils.FileUtils;
import com.zebra.jamesswinton.vaccineverificationdemo.utils.NodeUtils;

public class App extends Application {

    // Node Server Status Flag
    public static boolean mNodeServerRunning = false;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("node");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Start Node Server
        mNodeServerRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //The path where we expect the node project to be at runtime.
                String nodeInstallationDirectory = getFilesDir().getAbsolutePath()
                        + "/nodejs-project";

                // If APK has been updated, clear existing Node folders && re-copy from Assets
                if (NodeUtils.wasApkUpdated(getApplicationContext())) {
                    // Delete all existing folders
                    NodeUtils.recursivelyDeleteLegacyFolders(nodeInstallationDirectory);

                    // Re-copy from Assets
                    FileUtils.copyAssetFolder(getAssets(), "nodejs-project",
                            nodeInstallationDirectory);

                    // Update Pref
                    NodeUtils.saveLastUpdateTime(getApplicationContext());
                }

                // Start Node
                startNodeWithArguments(new String[] { "node",
                        nodeInstallationDirectory + "/main.js"
                });
            }
        }).start();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native Integer startNodeWithArguments(String[] arguments);

}
