package com.example.android11_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;

public class ImageDecoderActivity extends AppCompatActivity {

    @BindView(R.id.iv_LoadImage)
    AppCompatImageView mIVLoadImage;

    @SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_decoder);
        ButterKnife.bind(this);
        Resources resources = this.getResources();

        ImageDecoder.Source source = ImageDecoder.createSource(this.getResources(), R.drawable.ic_launcher_background);
        ImageDecoder.OnHeaderDecodedListener listener = new ImageDecoder.OnHeaderDecodedListener() {
            public void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
                decoder.setTargetSampleSize(2);
            }
        };
       // Drawable drawable = ImageDecoder.decodeDrawable(source, listener);
        Bitmap bitmap = null;
        try {
            bitmap = ImageDecoder.decodeBitmap(source,listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap!=null){ mIVLoadImage.setImageBitmap(bitmap);
        }


    }
    
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void funAniDecode() throws IOException {

        ImageDecoder.Source source = ImageDecoder.createSource(this.getResources(), R.drawable.ic_launcher_background);
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            drawable = ImageDecoder.decodeDrawable(source);
        }
        if (drawable instanceof AnimatedImageDrawable) {
            ((AnimatedImageDrawable) drawable).start();
        }

        /*Drawable drawable = ImageDecoder.decodeDrawable(source, (decoder, info, src) -> {
            decoder.setOnPartialImageListener((ImageDecoder.DecodeException e) -> {
                // Returning true indicates to create a Drawable or Bitmap even
                // if the whole image could not be decoded. Any remaining lines
                // will be blank.
                return true;
            });
        });*/

    }
}