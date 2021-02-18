package com.zebra.jamesswinton.vaccineverificationdemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zebra.jamesswinton.R;
import com.zebra.jamesswinton.databinding.ActivityReadTagBinding;
import com.zebra.jamesswinton.vaccineverificationdemo.VerifyCredentialsAsync;
import com.zebra.jamesswinton.vaccineverificationdemo.data.VerifiableCredential;
import com.zebra.jamesswinton.vaccineverificationdemo.data.Verification;
import com.zebra.jamesswinton.vaccineverificationdemo.utils.NFCUtils;
import com.zebra.jamesswinton.vaccineverificationdemo.utils.TagDataMapParser;
import com.zebra.jamesswinton.vaccineverificationdemo.utils.dialogs.CustomDialog;

import java.util.Map;

public class ReadTagActivity extends AppCompatActivity  implements NFCUtils.OnTagReadCallback,
        VerifyCredentialsAsync.OnCredentialsVerifiedCallback {

    // Keys
    private final String mKeyA = "C914D8AC25FA";
    private final String mKeyB = "7E47E741DF0F";

    // NFCUtils
    private NFCUtils mNFCUtils = null;

    // UI
    private ActivityReadTagBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_tag);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for Tag on Launch
        Intent intent = getIntent();
        if (intent != null && intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null) {
            // Init Tag
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            // Reset UI
            resetUI();

            // Parse Tag Async
            mNFCUtils = new NFCUtils(this, mKeyA, mKeyB);
            mNFCUtils.getBytesFromMifareTag(tag, false, this);
        }
    }

    /**
     * OnTagReadCallbacks
     *
     * Tag data will be returned in onTagRead()
     *
     */

    @Override
    public void onTagRead(Map<Integer, String> sectorBytesMap) {
        // Parse Map to Verifiable Credential
        VerifiableCredential verifiableCredential = TagDataMapParser.parseMap(sectorBytesMap);

        // Update UI
        populateUIWithCredentialInfo(verifiableCredential);

        // Verify JSON with Tangle
        new VerifyCredentialsAsync(this, verifiableCredential,
                this).execute();
    }

    @Override
    public void onError(Exception e) {
        CustomDialog.showCustomDialog(this, CustomDialog.DialogType.ERROR,
                "Tag Read Failed!", "Failed to read tag:\n\n" + e.getMessage());
    }

    /**
     * OnTagVerifiedCallbacks
     */

    @Override
    public void onVerificationComplete(Verification verification) {
        // Update UI
        updateVaccinationResultUI(verification.verified);
    }

    @Override
    public void onVerificationError(Exception e) {
        Log.e(this.getClass().getName(), "Exception: " + e.getMessage());
        CustomDialog.showCustomDialog(this, CustomDialog.DialogType.ERROR,
                "Error!", "An exception occurred:" + e.getMessage());
    }

    /**
     * Utility Methods
     */

    private void resetUI() {
        // User Deets
        mDataBinding.vaccineCardLayout.credentialName.setText("parsing...");
        mDataBinding.vaccineCardLayout.credentialDob.setText("parsing...");
        mDataBinding.vaccineCardLayout.credentialLocation.setText("parsing...");

        // Vaccine Deets
        mDataBinding.vaccineCardLayout.vaccineType.setText("parsing...");
        mDataBinding.vaccineCardLayout.vaccineDateOne.setText("parsing...");
        mDataBinding.vaccineCardLayout.vaccineDateTwo.setText("parsing...");

        // Data binding
        mDataBinding.vaccineStatusImageView.setImageDrawable(getDrawable(R.drawable.ic_loading));
        mDataBinding.vaccineStatusTextView.setText("Reading and verifying credentials. Please wait...");
    }

    private void populateUIWithCredentialInfo(VerifiableCredential verifiableCredential) {
        // User Deets
        mDataBinding.vaccineCardLayout.credentialName.setText(
                verifiableCredential.credentialSubject.name);
        mDataBinding.vaccineCardLayout.credentialDob.setText(
                verifiableCredential.credentialSubject.dob);
        mDataBinding.vaccineCardLayout.credentialLocation.setText(
                verifiableCredential.credentialSubject.location);

        // Vaccine Deets
        mDataBinding.vaccineCardLayout.vaccineType.setText(
                verifiableCredential.credentialSubject.vaccinationType + " / "
                        + verifiableCredential.credentialSubject.vaccinationTypeSub);
        mDataBinding.vaccineCardLayout.vaccineDateOne.setText(
                verifiableCredential.credentialSubject.dateDoseOne);
        mDataBinding.vaccineCardLayout.vaccineDateTwo.setText(
                verifiableCredential.credentialSubject.dateDoseTwo);
    }

    private void updateVaccinationResultUI(boolean verified) {
        if (verified) {
            mDataBinding.vaccineStatusImageView.setImageDrawable(getDrawable(R.drawable.ic_vaccine_verified));
            mDataBinding.vaccineStatusTextView.setText("Vaccination credentials are verified");
        } else {
            mDataBinding.vaccineStatusImageView.setImageDrawable(getDrawable(R.drawable.ic_vaccine_invalid));
            mDataBinding.vaccineStatusTextView.setText("Vaccination credentials are invalid");
        }
    }
}