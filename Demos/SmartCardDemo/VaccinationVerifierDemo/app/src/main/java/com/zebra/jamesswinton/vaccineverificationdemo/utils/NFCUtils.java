package com.zebra.jamesswinton.vaccineverificationdemo.utils;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;

import com.zebra.jamesswinton.vaccineverificationdemo.utils.dialogs.LoadingDialog;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NFCUtils {

    // Context
    private final Context mCx;

    // Keys
    private String mKeyA;
    private String mKeyB;

    public NFCUtils(Context cx, String keyA, String keyB) {
        this.mCx = cx;
        this.mKeyA = keyA;
        this.mKeyB = keyB;
    }

    public void getBytesFromMifareTag(Tag tag, boolean showDialog,
                                      OnTagReadCallback onTagReadCallback) {
        // Validate TAG Is MifareClassic
        if (isTagMifareClassic(tag)) {
            new ReadMifareAsync(mCx, MifareClassic.get(tag), mKeyA, mKeyB, showDialog,
                    onTagReadCallback)
                    .execute();
        } else {
            onTagReadCallback.onError(new Exception("Tag is not MifareClassic"));
        }
    }

    public boolean isTagMifareClassic(Tag tag) {
        for (String techType : tag.getTechList()) {
            if (techType.contains("android.nfc.tech.MifareClassic")) {
                return true;
            }
        } return false;
    }

    /**
     * Background thread for reading Mifare Data
     *
     * https://developer.android.com/reference/android/nfc/tech/MifareClassic#connect()
     */

    private static class ReadMifareAsync extends AsyncTask<Void, Void, Map<Integer, String>> {

        // WeakRef to Prevent Memory Leaks on Context
        private final WeakReference<Context> mContextWeakRef;

        // Keys
        private final byte[] mKeyA;
        private final byte[] mKeyB;

        // Tag
        private final MifareClassic mMifareClassicTag;

        // UI & Callbacks
        private final boolean mShowDialog;
        private AlertDialog mLoadingDialog;
        private final OnTagReadCallback mOnTagReadCallback;

        // Handler to place callbacks on MainThread
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        public ReadMifareAsync(Context context, MifareClassic mifareClassicTag,
                               String keyA, String keyB, boolean showDialog,
                               OnTagReadCallback onTagReadCallback) {
            super();
            this.mMifareClassicTag = mifareClassicTag;
            this.mOnTagReadCallback = onTagReadCallback;
            this.mKeyA = HexUtils.hexStringToByteArray(keyA);
            this.mKeyB = HexUtils.hexStringToByteArray(keyB);
            this.mShowDialog = showDialog;
            this.mContextWeakRef = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mShowDialog) {
                mLoadingDialog = LoadingDialog.createLoadingDialog(mContextWeakRef.get(),
                        "Reading Mifare Tag: " +
                                HexUtils.byteArrayToHexString(mMifareClassicTag.getTag().getId()));
                mLoadingDialog.show();
            }
        }

        @Override
        protected Map<Integer, String> doInBackground(Void... voids) {
            // Init Block Data Map
            Map<Integer, String> mTagDataMap = new HashMap<>();

            // Perform Read
            try {
                // Connect to tag
                mMifareClassicTag.connect();

                // Verify Connection
                if (mMifareClassicTag.isConnected()){

                    // Loop Each Sector
                    for(int sector = 0; sector < mMifareClassicTag.getSectorCount(); sector++) {

                        // Attempt Authentication, try custom keys first, then fallback to Defaults
                        if (authenticateSectorInTag(mMifareClassicTag, sector, mKeyA, mKeyB)) {
                            // Loop all Blocks in Sector
                            int blocks = mMifareClassicTag.getBlockCountInSector(sector);
                            for (int block = 0; block < blocks; block++) {
                                // Get Block Index
                                int blockIndex =
                                        mMifareClassicTag.sectorToBlock(sector) + block;

                                // Read Block
                                byte[] blockBytes = mMifareClassicTag.readBlock(blockIndex);

                                // Trim Padding from Byte[]
                                byte[] trimmedBlockBytes = HexUtils.trim(blockBytes);

                                // Convert Block to String
                                String blockUtf = new String(trimmedBlockBytes, UTF_8);

                                // Add String to Map
                                mTagDataMap.put(blockIndex, blockUtf);
                            }
                        } else {
                            // Couldn't authenticate with any keys, stop reading & return error
                            mMainThreadHandler.post(() -> mOnTagReadCallback.onError(
                                    new Exception("Authentication Failed!")));
                            return null;
                        }
                    }
                }

                // Close Connection
                mMifareClassicTag.close();

            } catch (IOException e) {
                // Exception occurred, pass back up to calling class
                mMainThreadHandler.post(() -> mOnTagReadCallback.onError(e));
                e.printStackTrace();
                return null;
            }

            // Return Map
            return mTagDataMap;
        }

        @Override
        protected void onPostExecute(Map<Integer, String> tagDataMap) {
            super.onPostExecute(tagDataMap);
            // Remove Dialog
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }

            // Notify Calling Class
            if (tagDataMap != null) {
                mOnTagReadCallback.onTagRead(tagDataMap);
            }
        }
    }

    private static boolean authenticateSectorInTag(MifareClassic tag, int sector, byte[] keyA,
                                                   byte[] keyB) throws IOException {
        if (tag.authenticateSectorWithKeyA(sector, keyA)) {
            return true;
        } else if (tag.authenticateSectorWithKeyB(sector, keyB)) {
            return true;
        } else if (tag.authenticateSectorWithKeyA(sector,
                MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
            return true;
        } else if (tag.authenticateSectorWithKeyA(sector,
                MifareClassic.KEY_DEFAULT)) {
            return true;
        } else return tag.authenticateSectorWithKeyA(sector,
                MifareClassic.KEY_NFC_FORUM);
    }

    /**
     * Interface Callback - Tag reads need to be done asynchronously on a separate thread
     */

    public interface OnTagReadCallback {
        void onTagRead(Map<Integer, String> tagDataMap);
        void onError(Exception e);
    }

}
