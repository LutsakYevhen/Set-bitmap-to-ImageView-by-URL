package com.example.lutsak.bitmapimage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

public class RetainFragment extends Fragment {
    private static final String TAG = RetainFragment.class.getSimpleName();
    public LruCache<String, Bitmap> mRetainedCache;

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
    }
}