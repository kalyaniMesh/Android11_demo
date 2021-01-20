package com.example.android11_demo;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.R)
@Metadata(
        mv = {1, 1, 16},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0015¨\u0006\u0007"},
        d2 = {"Lcom/example/android11_demo/SampActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"}
)

public class PowerManagerActivity extends AppCompatActivity implements PowerManager.OnThermalStatusChangedListener {

    private static final String TAG = PowerManagerActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_manager3);
        PowerManager mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = mPowerManager.isScreenOn();
        Log.d(TAG, "is Screen On : "+isScreenOn);
        if (mPowerManager == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.os.PowerManager");
        } else {
            PowerManager powerManager = (PowerManager)mPowerManager;
            int currentStatus = powerManager.getCurrentThermalStatus();
            Log.d(TAG, "current Status : "+currentStatus);
            powerManager.addThermalStatusListener(this);
        }
    }

    @Override
    public void onThermalStatusChanged(int i) {
        Log.d(TAG, "Thermal status changed : "+ i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }


}

