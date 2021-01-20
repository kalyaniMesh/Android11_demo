package com.example.android11_demo

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import androidx.annotation.RequiresApi

class SampActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samp)

        val powerManager = getSystemService(Context.POWER_SERVICE)
                as PowerManager

        val currentStatus = powerManager.currentThermalStatus

        powerManager.addThermalStatusListener {
            // do something with status
        }
    }
}