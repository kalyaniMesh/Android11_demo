package com.example.android11_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.example.android11_demo.listener.MyUndoListener;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnackbarWithCoordinateActivity extends AppCompatActivity {

    @BindView(R.id.btnSnackbar)
    AppCompatButton btnSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar_with_coordinate);
        ButterKnife.bind(this);
        btnSnackbar = findViewById(R.id.btnSnackbar);

        btnSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Email sent Successfully!",
                        Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("Undo", new MyUndoListener());
                mySnackbar.show();
            }
        });

    }
}