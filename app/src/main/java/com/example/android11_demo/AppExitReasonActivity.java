package com.example.android11_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class AppExitReasonActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;

    Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_exit_reason);
        ButterKnife.bind(this);
        mContext = this;

        //creating fake crash
        mTvTitle.setText("kalynijij");

    }
}