package com.example.lutsak.bitmapimage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RetainFragment extends Fragment{
    private static final String TAG = RetainFragment.class.getSimpleName();

    public LruCache<String,Bitmap> mRetainedCache;

    public static RetainFragment findOrCreateRetainFragment(FragmentManager fragmentManager) {
        Log.d(TAG, "findOrCreateRetainFragment");
        RetainFragment fragment = (RetainFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RetainFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Log.d(TAG, ">> onCreate");

        if (loadState() != null){
            Log.d(TAG, "Load from file " + loadState());
            mRetainedCache = loadState();
        }

        Log.d(TAG, "<< onCreate");

    }

    public static void saveState(Bitmap bitmap) {
        Log.d(TAG, "saveState");
        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/vwCache.PNG");
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private LruCache<String,Bitmap> loadState() {
        Log.d(TAG, "loadState");
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // Stored in kilobytes.
        final int cacheSize = maxMemory / 2; //Use 1/8 of all available memory for cache.
        Bitmap bitmap;
        LruCache<String,Bitmap> lruCache = new LruCache<>(cacheSize);
        try {
            bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/vwCache.PNG");
            lruCache.put(getResources().getString(R.string.image_url), bitmap);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return lruCache;
    }
}
