package com.example.android11_demo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    AppCompatTextView mTextView;

    @BindView(R.id.btnBubble)
    AppCompatButton mBtnBubble;

    @BindView(R.id.btnBubble2)
    AppCompatButton mBtnBubble2;

    NotificationManager mNotificationManager;
    Notification.Builder mBuilder;
    NotificationChannel mChannel;
    Context mContext;
    GPSTracker mGpsTracker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence name = "My Channel";
        String description = "xyz";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        mChannel = new NotificationChannel("1", name, importance);
        mChannel.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mChannel.setAllowBubbles(true);
        }

        checkRunTimePermission();
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case 1001:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mGpsTracker = new GPSTracker(mContext);
                        if (mGpsTracker.canGetLocation()) {
                                // latitude = gpsTracker.getLatitude();
                            // longitude = gpsTracker.getLongitude();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 10);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @OnClick({R.id.btnBubble, R.id.btnBubble2})
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnBubble:
                Intent target = new Intent(MainActivity.this, BubbleActivity.class);
                PendingIntent bubbleIntent =
                        PendingIntent.getActivity(MainActivity.this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT /* flags */);

                // Create bubble metadata
                Notification.BubbleMetadata bubbleData =
                        new Notification.BubbleMetadata.Builder()
                                .setDesiredHeight(600)
                                .setIcon(Icon.createWithResource(MainActivity.this, R.mipmap.ic_launcher))
                                .setIntent(bubbleIntent)
                                .setAutoExpandBubble(true)
                                .setSuppressNotification(true)
                                .build();

                // Create notification
                Person chatPartner = new Person.Builder()
                        .setName("Chat partner")
                        .setImportant(true)
                        .build();

                Notification.Builder builder =
                        new Notification.Builder(MainActivity.this, mChannel.getId())
                                .setContentIntent(bubbleIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setBubbleMetadata(bubbleData)
                                .addPerson(String.valueOf(chatPartner));

                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(1, builder.build());
                break;

            case R.id.btnBubble2:
                target = new Intent(MainActivity.this, BubbleActivity.class);
                target.putExtra("key","This is the second bubble");
                bubbleIntent =
                        PendingIntent.getActivity(MainActivity.this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create bubble metadata
                bubbleData = new Notification.BubbleMetadata.Builder()
                        .setDesiredHeight(600)
                        .setIcon(Icon.createWithResource(MainActivity.this, R.mipmap.ic_launcher))
                        .setIntent(bubbleIntent)
                        .build();


                builder = new Notification.Builder(MainActivity.this, mChannel.getId())
                        .setContentTitle("Second Bubble")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setBubbleMetadata(bubbleData);


                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(2, builder.build());

                break;
        }

    }
}