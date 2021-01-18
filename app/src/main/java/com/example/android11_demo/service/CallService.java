package com.example.android11_demo.service;

import android.os.Build;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallService extends CallScreeningService {
    @Override
    public void onScreenCall(Call.Details callDetails) {
        CallResponse.Builder response = new CallResponse.Builder();
        Log.e("CallBouncer", "Call screening service triggered");
        respondToCall(callDetails, response.build() );
    }
}