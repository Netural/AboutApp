package com.netural.aboutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by stephan.schober on 01.12.14.
 */
public class AboutAppOverlay {

    private static final String TAG = AboutAppOverlay.class.getSimpleName();

    /**
     * Add a new inflated layout into a GestureOverlayView and return the new view
     *
     * @param context context
     * @param layout  the layout to inflate
     * @param rawId   the resource identifier of the gesture file in your raw folder
     * @return the new contentView wrapped in a GestureOverlayView
     */
    @Deprecated
    public static View getOverlayContentView(Context context, int layout, int rawId) {
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        return getOverlayContentView(contentView, null, rawId);
    }

    /**
     * Add a new inflated layout into a GestureOverlayView and return the new view
     *
     * @param context     context
     * @param layout      the layout to inflate
     * @param packageName the package name of the app who uses this library
     * @param rawId       the resource identifier of the gesture file in your raw folder
     * @return the new contentView wrapped in a GestureOverlayView
     */
    public static View getOverlayContentView(Context context, int layout, String packageName,
                                             int rawId) {
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        return getOverlayContentView(contentView, packageName, rawId);
    }

    /**
     * Add the content view of the activity into a GestureOverlayView and return the new view
     *
     * @param contentView the original content view of the activity
     * @param packageName the package name of the app who uses this library
     * @param rawId       the resource identifier of the gesture file in your raw folder
     * @return the new contentView wrapped in a GestureOverlayView
     */
    public static View getOverlayContentView(final View contentView, final String packageName,
                                             int rawId) {
        //load the gestures
        final GestureLibrary gestureLib = GestureLibraries
                .fromRawResource(contentView.getContext(), rawId);
        if (!gestureLib.load()) {
            Log.w(TAG, "could not load gestures");
            return contentView;
        }

        GestureOverlayView gestureOverlayView = new GestureOverlayView(contentView.getContext());
        gestureOverlayView.setGestureColor(Color.TRANSPARENT);
        gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
        gestureOverlayView.addView(contentView);
        gestureOverlayView
                .addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
                    @Override
                    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                        //load all gestures
                        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
                        //the only received prediction should be "netural"
                        for (Prediction prediction : predictions) {
                            if (prediction.score > 1.0) {
                                showDialog(contentView.getContext(), packageName);
                            }
                        }
                    }
                });
        return gestureOverlayView;
    }

    /**
     * Building the dialog with the buildconfig info
     *
     * @param context     the context of the view submitted to getOverlayContentView
     * @param packageName the package name of the app who uses this library
     */
    public static void showDialog(Context context, String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String message = "";

        String applicationId = (String) getBuildConfigValue(context, packageName,
                "APPLICATION_ID"); //BuildConfig.APPLICATION_ID
        String versionCode = String.valueOf((Integer) getBuildConfigValue(context, packageName,
                "VERSION_CODE")); //BuildConfig.VERSION_CODE
        String versionName = (String) getBuildConfigValue(context, packageName,
                "VERSION_NAME"); //BuildConfig.VERSION_NAME
        String buildType = (String) getBuildConfigValue(context, packageName,
                "BUILD_TYPE"); //BuildConfig.BUILD_TYPE
        String flavor = (String) getBuildConfigValue(context, packageName,
                "FLAVOR"); //BuildConfig.FLAVOR
        String gitSha = (String) getBuildConfigValue(context, packageName,
                "GIT_SHA"); //BuildConfig.GIT_SHA
        String buildTime = (String) getBuildConfigValue(context, packageName,
                "BUILD_TIME"); //BuildConfig.BUILD_TIME

        if (applicationId != null) {
            message += context.getResources().getString(R.string.application_id) + ": "
                    + applicationId + "\n";
        }
        if (versionCode != null) {
            message += context.getResources().getString(R.string.version_code) + ": "
                    + versionCode + "\n";
        }
        if (versionName != null) {
            message += context.getResources().getString(R.string.version_name) + ": "
                    + versionName + "\n";
        }
        if (buildType != null) {
            message += context.getResources().getString(R.string.build_type) + ": "
                    + buildType + "\n";
        }
        if (flavor != null && flavor.length() > 0) {
            message += context.getResources().getString(R.string.product_flavor) + ": "
                    + flavor + "\n";
        }
        if (gitSha != null && gitSha.length() > 0) {
            message += context.getResources().getString(R.string.git_sha) + ": "
                    + gitSha + "\n";
        }
        if (buildTime != null && buildTime.length() > 0) {
            message += context.getResources().getString(R.string.build_time) + ": "
                    + buildTime + "\n";
        }

        builder.setMessage(message)
               .setTitle(R.string.app_name);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors are
     * used at the project level to set custom fields.
     *
     * @param context     Used to find the correct file
     * @param packageName the package name of the app who uses this library
     * @param fieldName   The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static Object getBuildConfigValue(Context context, String packageName,
                                             String fieldName) {
        if (packageName == null) {
            packageName = context.getPackageName();
        }
        try {
            Class<?> clazz = Class.forName(packageName + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "error reading BuildConfig value", e);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "error reading BuildConfig value", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "error reading BuildConfig value", e);
        }
        return null;
    }
}
