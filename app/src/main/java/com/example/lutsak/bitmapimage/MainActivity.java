package com.example.lutsak.bitmapimage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements DownloadImageTask.DownLoadImageTaskProtocol {
    
    private static String Hi = "Де там можна буде припаркуватися?, і напиши тут адрес ресторану бо ми шось запутались.";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String IMAGE_URL = "https://www.alcopa-auction.fr/assets/img/brand-volkswagen.png";
    private static final int HALF_SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
    private static final int HALF_SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels / 2;

    private final int mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // Stored in kilobytes.
    private final int mCacheSize = mMaxMemory / 2; //Use 1/8 of all available memory for cache.
    private LruCache<String,Bitmap> mMemoryCache;

    private ImageView mImageView;
    private DownloadImageTask mLoadImageTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ">> onCreate");

        mImageView = findViewById(R.id.image_view);

        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
        mMemoryCache = retainFragment.mRetainedCache;
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<>(mCacheSize);
            retainFragment.mRetainedCache = mMemoryCache;
        }

        loadImage(IMAGE_URL, mImageView);

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
    public void onImageDownloaded(Bitmap bitmap) {
        Log.d(TAG, "onImageDownloaded");
        addBitmapToMemoryCache(IMAGE_URL, bitmap);
    }

    @Override
    public void onImageDownloadFailed() {
        Log.d(TAG, "onImageDownloadFailed");
        mImageView.setImageResource(R.drawable.ic_launcher_background);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        Log.d(TAG, "addBitmapToMemoryCache");
        if (getBitmapFromMemCache(key) == null) {
            Log.d(TAG, "put bitmap");
            mMemoryCache.put(key, bitmap);
        }
        RetainFragment.saveState(bitmap);
        loadImage(key, mImageView);
    }

    private Bitmap getBitmapFromMemCache(String key) {
        Log.d(TAG, "getBitmapFromMemCache");
        return mMemoryCache.get(key);
    }

    private void loadImage (String key, ImageView imageView){
        Log.d(TAG, "loadImage");
        final Bitmap bitmap = getBitmapFromMemCache(key);
        Log.d(TAG, "bitmap getBitmapFromMemCache : " + bitmap);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        } else {
            mLoadImageTask = new DownloadImageTask(key, HALF_SCREEN_WIDTH, HALF_SCREEN_HEIGHT);
            mLoadImageTask.execute(key);
            mLoadImageTask.setProtocol(this);
        }
    }
}


