package com.example.android11_demo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.loader.ResourcesLoader;
import android.content.res.loader.ResourcesProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class BioActivity extends AppCompatActivity {

    @BindView(R.id.btnBio)
    AppCompatButton mBtnBio;

    @BindView(R.id.iv_test)
    AppCompatImageView mIvTest;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        funDisplayCutout();
        setContentView(R.layout.activity_bio);
        ButterKnife.bind(this);
        mContext=this;
        //funAppExitReason();
        funBiometric();
      /*  try {
            funResourcesProviderAndLoader();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void funDisplayCutout(){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }

            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
}*/
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void funResourcesProviderAndLoader() throws IOException {
        String sdkroot = getApplicationInfo().dataDir + "/Pictures";
        ResourcesLoader rl = new ResourcesLoader();
        rl.addProvider(ResourcesProvider.loadFromDirectory(sdkroot, null));
        Resources res = getResources();
        res.addLoaders(rl);
        final AssetManager assetManager = res.getAssets();
        //              AssetManager am = AssetManager.class.newInstance();
        InputStream is1 = assetManager.open("demo2.png");
        Bitmap bitmap1 = BitmapFactory.decodeStream(is1);
        InputStream is2 = assetManager.open("demo1.png");
        Bitmap bitmap2 = BitmapFactory.decodeStream(is2);
        if(bitmap1!=null){
            mIvTest.setVisibility(View.VISIBLE);
            mIvTest.setImageBitmap(bitmap1);
        }


    }

    private void funBiometric(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(BioActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                //startActivity(new Intent(BioActivity.this,MainActivity.class));

                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void funAppExitReason(){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ApplicationExitInfo> reasonsList = activityManager.getHistoricalProcessExitReasons(getPackageName(), 0, 0);
        // set pid to 0 for all matches
        // set max to 0 for all

        for (ApplicationExitInfo applicationExitInfo : reasonsList) {

            int reason = applicationExitInfo.getReason();
            String reasonDes = applicationExitInfo.getDescription();
            int reasonImp = applicationExitInfo.getImportance();
            Log.v("App Exit Reasons : ", "reasons: "+reason +",reasons des: "+reasonDes+",reasons Imp: "+reasonImp);
            Log.v("Reasons List: ", String.valueOf(reasonsList));
            // REASON_LOW_MEMORY, REASON_CRASH, REASON_ANR, etc.

        }
    }

    @OnClick({R.id.btnBio,R.id.btnExit,R.id.btnBubble,R.id.btnImage,R.id.btnScopedStorage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBio:
                biometricPrompt.authenticate(promptInfo);
                break;
            case R.id.btnExit:
                 startActivity(new Intent(BioActivity.this,AppExitReasonActivity.class));
                break;
            case R.id.btnBubble:
                 startActivity(new Intent(BioActivity.this,MainActivity.class));
                break;
            case R.id.btnImage:
                startActivity(new Intent(BioActivity.this,ImageDecoderActivity.class));
                break;
            case R.id.btnScopedStorage:
                startActivity(new Intent(BioActivity.this,ScopedStorageActivity.class));
                break;

                case R.id.btnShareToPrint:
                startActivity(new Intent(BioActivity.this, CallScreeningActivity.class));
                break;

            case R.id.btnAuditAccessData:
                startActivity(new Intent(BioActivity.this, AuditAccessToDataActivity.class));
                break;

        }
    }
}