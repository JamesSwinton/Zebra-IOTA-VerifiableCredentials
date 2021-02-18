package com.zebra.jamesswinton.vaccineverificationdemo.utils.dialogs;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zebra.jamesswinton.R;

public class LoadingDialog {

  public static AlertDialog createLoadingDialog(Context cx, String message) {
    // Inflate View
    View customDialogView = LayoutInflater.from(cx).inflate(R.layout.layout_loading_dialog,
            null);

    // Get View Components
    TextView messageView = customDialogView.findViewById(R.id.progress_message);

    // Set Component Values
    messageView.setText(Html.fromHtml(message));

    // Create Dialog
    return new MaterialAlertDialogBuilder(cx)
            .setView(customDialogView)
            .setCancelable(false)
            .setCancelable(false)
            .create();
  }

}
