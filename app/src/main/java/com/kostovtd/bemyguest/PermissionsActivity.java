package com.kostovtd.bemyguest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kostovtd.bemyguest.util.PermissionsChecker;


/**
 * Created by todor.kostov on 21.9.2016 г..
 *
 * An Activity class for handling the process of granting permissions
 */

public class PermissionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = PermissionsActivity.class.getSimpleName();

    public static final int PERMISSION_REQUEST_CODE = 0;
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;
    private static final String EXTRA_PERMISSIONS = "extra_permissions";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionsChecker permissionsChecker;
    private boolean requiresCheck;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate() hit");
        super.onCreate(savedInstanceState);

        if(getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }

        setContentView(R.layout.activity_permissions);

        permissionsChecker = new PermissionsChecker(this);
        requiresCheck = true;
    }


    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume() hit");
        super.onResume();
        if(requiresCheck) {
            String[] permissions = getPermissions();

            if(permissionsChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        } else {
            requiresCheck = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE &&
                permissionsChecker.hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;
            showMissingPermissionDialog();
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }


    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }


    /**
     * A simple utility method, which other methods should use to
     * start PermissionsActivity with a given set of required permissions.
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }


    /**
     * Pass control to the OS to request the permissions which
     * are required.
     * @param permissions
     */
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }



    private void showMissingPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.application_help);
        dialogBuilder.setMessage(R.string.application_help_text);
        dialogBuilder.setNegativeButton(R.string.application_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.application_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }


    /**
     * Start the Settings screen for the current application
     */
    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
  }
