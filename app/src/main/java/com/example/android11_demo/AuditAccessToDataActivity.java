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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;

import java.util.Arrays;

public class AuditAccessToDataActivity extends AppCompatActivity {
    Context mContext;

    private static final String TAG = AuditAccessToDataActivity.class.getName();
    GPSTracker mGpsTracker;
    double latitude,longitude;
    private Context attributionContext;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_access_to_data);

        //Create attribution（attribute）
        attributionContext = createAttributionContext("visitLocation");

        mContext = this;

        //Listen for events
        AppOpsManager.OnOpNotedCallback appOpsCallback =
                new AppOpsManager.OnOpNotedCallback() {
                    private void logPrivateDataAccess(String opCode,String attributionTag,String trace) {
/*
                        mGpsTracker = new GPSTracker(mContext);
                        if (mGpsTracker.canGetLocation()) {
                            latitude = mGpsTracker.getLatitude();
                            longitude = mGpsTracker.getLongitude();
                        }
*/
                        Log.d(TAG, "zwm, logPrivateDataAccess, opCode: " + opCode);
                        Log.d(TAG, "zwm, logPrivateDataAccess, attributionTag: " + attributionTag);
                        Log.d(TAG, "zwm, logPrivateDataAccess, trace: " + trace);
                    }

                    @Override
                    public void onNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                        /*onNoted - Called when protected data is accessed via a synchronous call. For example, onNoted
                         * would be triggered if an app requested the user's last known location and that function returns
                         * the value synchronously (right away).*/
                        logPrivateDataAccess(syncNotedAppOp.getOp(),syncNotedAppOp.getAttributionTag(),
                                Arrays.toString(new Throwable().getStackTrace()));
                    }

                    @Override
                    public void onSelfNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                        /*onSelfNoted - Called when a developer calls {@link android.app.AppOpsManager#noteOp} or
                         * {@link android.app.AppOpsManager#noteProxyOp} to manually report access to protected data from
                         * it's own {@link android.os.Processl#myUid}. This is the only callback that isn't triggered by
                         * the system. It's a way for apps to to blame themselves when they feel like they are accessing
                         * protected data and want to audit it.*/
                        logPrivateDataAccess(syncNotedAppOp.getOp(),syncNotedAppOp.getAttributionTag(),
                                Arrays.toString(new Throwable().getStackTrace()));
                    }

                    @Override
                    public void onAsyncNoted(@NonNull AsyncNotedAppOp asyncNotedAppOp) {
                        /*>onAsyncNoted - Called when protected data is accessed via an asynchronous callback. For
                         * example, if an app subscribed to location changes, onAsyncNoted would be triggered when the
                         * callback is invoked and returns a new location. A Geofence is another example.*/
                        logPrivateDataAccess(asyncNotedAppOp.getOp(),asyncNotedAppOp.getAttributionTag(),
                                asyncNotedAppOp.getMessage());
                    }
                };

        //Turn on private data monitoring
        AppOpsManager appOpsManager = getSystemService(AppOpsManager.class);
        if (appOpsManager != null) {
            appOpsManager.setOnOpNotedCallback(getMainExecutor(), appOpsCallback);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation();
            }
        }, 5000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        LocationManager locationManager = (LocationManager) attributionContext.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "zwm, getLocation");
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }
}