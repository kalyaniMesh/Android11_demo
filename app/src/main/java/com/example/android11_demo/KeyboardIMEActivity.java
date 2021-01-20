package com.example.android11_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

public class KeyboardIMEActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_i_m_e);
/*


        ViewCompat.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {

                Insets barsIme = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.ime());
                mRootView.setPadding(barsIme.left, barsIme.top, barsIme.right, barsIme.bottom);

                // We return the new WindowInsets.CONSUMED to stop the insets being
                // dispatched any further into the view hierarchy. This replaces the
                // deprecated WindowInsets.consumeSystemWindowInsets() and related
                // functions.
                return WindowInsets.CONSUMED;
            }
        });
*/

    }
}