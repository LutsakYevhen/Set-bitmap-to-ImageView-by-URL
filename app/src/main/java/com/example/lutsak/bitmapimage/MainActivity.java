package com.example.lutsak.bitmapimage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements DownloadImageTask.DownLoadImageTaskProtocol {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String IMAGE_URL = "https://www.alcopa-auction.fr/assets/img/brand-volkswagen.png";
    private static final int HALF_SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
    private static final int HALF_SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels / 2;

    private ImageView mImageView;
    private DownloadImageTask mLoadImageTask;
    private Bitmap mBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ">> onCreate");

        mImageView = findViewById(R.id.image_view);

        /*
          Download the logo from online and set it as
          ImageView image programmatically.
         */
        mBitmap = (Bitmap) getLastCustomNonConfigurationInstance();
        Log.d(TAG, "onCreate, mBitmap " + mBitmap);

        if(mBitmap != null){
            mImageView.setImageBitmap(mBitmap);
        } else {
            mLoadImageTask = new DownloadImageTask(IMAGE_URL, HALF_SCREEN_WIDTH, HALF_SCREEN_HEIGHT);
            mLoadImageTask.execute(IMAGE_URL);
            mLoadImageTask.setProtocol(this);
        }
        Log.d(TAG, "<< onCreate ");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ");

        if (mLoadImageTask != null) {
            mLoadImageTask.removeProtocol();
            mLoadImageTask.cancel(false);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
       Log.d(TAG, "onRetainCustomNonConfigurationInstance :");
        return mBitmap;
    }

    @Override
    public void onImageDownloaded(Bitmap bitmap) {
        Log.d(TAG, "onImageDownloaded");
        mBitmap = bitmap;
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onImageDownloadFailed() {
        Log.d(TAG, "onImageDownloadFailed");
        mImageView.setImageResource(R.drawable.ic_launcher_background);
    }
}


