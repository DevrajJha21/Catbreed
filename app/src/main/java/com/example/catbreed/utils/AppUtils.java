package com.example.catbreed.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.catbreed.R;

public class AppUtils {
    private static Dialog dialog;

    public static void hideLoader() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
    public static void showLoader(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_loader, null, false);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        final LottieAnimationView groupcreate = (LottieAnimationView) view.findViewById(R.id.loader);
        groupcreate.setAnimation("loader.json");
        groupcreate.playAnimation();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
