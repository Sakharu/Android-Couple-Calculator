package com.lescigognes.chatentempsreel.common


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionsHelper
{
    companion object
    {
        fun askForStoragePermission(activity: Activity)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!checkForPermissionEnable(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), CODE_PERMISSIONS_STORAGE)
            }
        }

        fun checkForPermissionEnable(activity: Activity, permission:String) : Boolean = ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED
    }
}
