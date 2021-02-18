package com.zebra.jamesswinton.vaccineverificationdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.zebra.jamesswinton.vaccineverificationdemo.data.VerifiableCredential;
import com.zebra.jamesswinton.vaccineverificationdemo.data.Verification;

import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerifyCredentialsAsync extends AsyncTask<Void, Void, String> {

    // Weak Ref Context
    private WeakReference<Context> mContextWeakRef;

    // Credential to Verify
    private VerifiableCredential mVerifiableCredential;

    // Callback
    private OnCredentialsVerifiedCallback mOnCredentialsVerifiedCallback;

    // Handler to place callbacks on MainThread
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public VerifyCredentialsAsync(Context context, VerifiableCredential verifiableCredential,
                                  OnCredentialsVerifiedCallback onCredentialsVerifiedCallback) {
        this.mContextWeakRef = new WeakReference<>(context);
        this.mVerifiableCredential = verifiableCredential;
        this.mOnCredentialsVerifiedCallback = onCredentialsVerifiedCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Build Request
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(mVerifiableCredential));
        Request request = new Request.Builder()
                .url("http://localhost:3000/")
                .post(body)
                .build();

        // Send request && await response
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch(Exception e) {
            e.printStackTrace();
            mMainThreadHandler.post(() -> mOnCredentialsVerifiedCallback.onVerificationError(e));
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            mOnCredentialsVerifiedCallback.onVerificationComplete(
                    new Gson().fromJson(response, Verification.class));
        }
    }

    public interface OnCredentialsVerifiedCallback {
        void onVerificationComplete(Verification verification);
        void onVerificationError(Exception e);
    }
}
