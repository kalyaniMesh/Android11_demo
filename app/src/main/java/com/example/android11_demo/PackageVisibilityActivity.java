package com.example.android11_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class PackageVisibilityActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_visibility);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : list) {
            Log.d(TAG, "zwm, packageName: " + app.packageName);
        }
    }
}