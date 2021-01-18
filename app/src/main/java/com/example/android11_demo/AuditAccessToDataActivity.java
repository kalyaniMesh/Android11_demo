package com.example.android11_demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.AsyncNotedAppOp;
import android.app.SyncNotedAppOp;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;

import java.util.Arrays;

public class AuditAccessToDataActivity extends AppCompatActivity {
    Context mContext;

    private static final String TAG = AuditAccessToDataActivity.class.getName();
    GPSTracker mGpsTracker;
    double latitude,longitude;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_access_to_data);
        mContext = this;
        checkRunTimePermission();

        AppOpsManager.OnOpNotedCallback appOpsCallback =
                new AppOpsManager.OnOpNotedCallback() {
                    private void logPrivateDataAccess(String opCode, String trace) {
/*
                        mGpsTracker = new GPSTracker(mContext);
                        if (mGpsTracker.canGetLocation()) {
                            latitude = mGpsTracker.getLatitude();
                            longitude = mGpsTracker.getLongitude();
                        }
*/
                        Log.i(TAG, "Private data accessed. " + "Operation:"+ opCode+ "\n" + trace);
                    }

                    @Override
                    public void onNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                        /*onNoted - Called when protected data is accessed via a synchronous call. For example, onNoted
                         * would be triggered if an app requested the user's last known location and that function returns
                         * the value synchronously (right away).*/
                        logPrivateDataAccess(syncNotedAppOp.getOp(),
                                Arrays.toString(new Throwable().getStackTrace()));
                    }

                    @Override
                    public void onSelfNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                        /*onSelfNoted - Called when a developer calls {@link android.app.AppOpsManager#noteOp} or
                         * {@link android.app.AppOpsManager#noteProxyOp} to manually report access to protected data from
                         * it's own {@link android.os.Processl#myUid}. This is the only callback that isn't triggered by
                         * the system. It's a way for apps to to blame themselves when they feel like they are accessing
                         * protected data and want to audit it.*/
                        logPrivateDataAccess(syncNotedAppOp.getOp(),
                                Arrays.toString(new Throwable().getStackTrace()));
                    }

                    @Override
                    public void onAsyncNoted(@NonNull AsyncNotedAppOp asyncNotedAppOp) {
                        /*>onAsyncNoted - Called when protected data is accessed via an asynchronous callback. For
                         * example, if an app subscribed to location changes, onAsyncNoted would be triggered when the
                         * callback is invoked and returns a new location. A Geofence is another example.*/
                        logPrivateDataAccess(asyncNotedAppOp.getOp(),
                                asyncNotedAppOp.getMessage());
                    }
                };

        AppOpsManager appOpsManager = getSystemService(AppOpsManager.class);
        if (appOpsManager != null) {
            appOpsManager.setOnOpNotedCallback(getMainExecutor(), appOpsCallback);
        }
    }

    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGpsTracker = new GPSTracker(mContext);

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        } else {
            mGpsTracker = new GPSTracker(mContext); //GPSTracker is class that is used for retrieve user current location
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mGpsTracker = new GPSTracker(mContext);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // If User Checked 'Don't Show Again' checkbox for runtime permission, then navigate user to Settings
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("Permission Required");
                    dialog.setCancelable(false);
                    dialog.setMessage("You have to Allow permission to access user location");
                    dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",
                                    mContext.getPackageName(), null));
                            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i, 1001);
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                //code for deny
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (requestCode == 1001) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mGpsTracker = new GPSTracker(mContext);
                    if (mGpsTracker.canGetLocation()) {
                        // latitude = gpsTracker.getLatitude();
                        //longitude = gpsTracker.getLongitude();
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 10);
                }
            }
        }
    }


}