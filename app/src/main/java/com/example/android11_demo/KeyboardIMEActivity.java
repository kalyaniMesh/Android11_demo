package com.example.android11_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import android.content.res.Resources;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyboardIMEActivity extends AppCompatActivity {

    @BindView(R.id.edtText)
    AppCompatEditText editText;

    View view;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_i_m_e);
        ButterKnife.bind(this);
        editText = findViewById(R.id.edtText);


        /*Android 11 introduces new APIs to improve the conversion of input methods (IEs), such as on-screen keyboards. These APIs make it easier for you to adjust your app's content to keep up with the ad and disappearance of IME and other elements such as status and navigation bars.

        To display the IME when focusing on any EditText, call view.getInsetsController().show (Type.ime().

                To hide the IME, call view.getInsetsController().hide (Type.ime().).

                Check that the IME is currently visible by calling view.getRootWindowInsets().isVisible (Type.ime)).

                <pre style="margin: 0px; tab-size: 4; white-space: pre-wrap;">view.setOnApplyWindowInsetsListener { view, insets ->
                For example, listen to whether the user input method pops up
        }</pre>*/

                editText.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {

                Insets barsIme = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.ime());
                editText.setPadding(barsIme.top, barsIme.left, barsIme.right, barsIme.bottom);
                view = v;

                // We return the new WindowInsets.CONSUMED to stop the insets being
                // dispatched any further into the view hierarchy. This replaces the
                // deprecated WindowInsets.consumeSystemWindowInsets() and related
                // functions.
                return WindowInsets.CONSUMED;
            }
        });

                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                view.getWindowInsetsController().show(WindowInsets.Type.ime());
                            }
                        }
                        else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                view.getWindowInsetsController().hide(WindowInsets.Type.ime());
                            }
                        }
                    }
                });
    }


}