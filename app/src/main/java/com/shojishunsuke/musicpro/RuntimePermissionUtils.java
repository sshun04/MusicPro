package com.shojishunsuke.musicpro;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

public class RuntimePermissionUtils {
    private RuntimePermissionUtils(){

    }

    public static boolean hasSelfPermissions(@NonNull Context context, @NonNull String... permissions ){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        for (String permission : permissions){
            if (context.checkSelfPermission(permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public static boolean checkGrantResults(@NonNull int... grantResults){

        if (grantResults.length == 0)throw new IllegalArgumentException("grantResults is empty");

        for (int result: grantResults){
            if (result != PackageManager.PERMISSION_GRANTED ){
                return false;
            }
        }

        return true;
    }

}
