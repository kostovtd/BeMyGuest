package com.kostovtd.bemyguest.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by todor.kostov on 17.9.2016 г..
 *
 * The class provides support for figuring out if there are
 * any missing app permissions. (not accepted permissions)
 *
 * In use for devices with Android API > 19
 */
public class PermissionsChecker {

    private static final String TAG = PermissionsChecker.class.getSimpleName();

    private final Context mContext;


    public PermissionsChecker(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Determine if there are any permissions missing.
     * @param permissions
     * @return TRUE if there are missing permissions, FALSE otherwise
     */
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Determine if there are any permissions miss
     * @param grantResults
     * @return TRUE if everything is granted, FALSE otherwise
     */
    public boolean hasAllPermissionsGranted(int [] grantResults) {
        for(int grantResult : grantResults) {
            if(grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }


    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.mContext, permission) == PackageManager.PERMISSION_DENIED;
    }



}
