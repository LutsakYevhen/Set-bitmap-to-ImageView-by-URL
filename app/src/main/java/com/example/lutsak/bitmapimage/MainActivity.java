package com.example.lutsak.bitmapimage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String IMAGE_URL = "https://www.alcopa-auction.fr/assets/img/brand-volkswagen.png";
    private static final int HALF_SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
    private static final int HALF_SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels / 2;

    private ImageView mImageView;
    private DownLoadImageTask mLoadImageTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ">> onCreate");

        mImageView = findViewById(R.id.image_view);

        /*
          Download the logo from online and set it as
          ImageView image programmatically.
         */
        mLoadImageTask = (DownLoadImageTask) getLastCustomNonConfigurationInstance();
        if (mLoadImageTask == null) {
            Log.d(TAG, "create");
            mLoadImageTask = new DownLoadImageTask();
            mLoadImageTask.execute(IMAGE_URL);
        }
        mLoadImageTask.link(this);

        Log.d(TAG, "<< onCreate ");
    }

    public Object onRetainCustomNonConfigurationInstance() {
       mLoadImageTask.unLink();
        return mLoadImageTask;
    }

    /*
      AsyncTask enables proper and easy use of the UI thread. This class
      allows to perform background operations and publish results on the UI
      thread without having to manipulate threads and/or handlers.
     */
    private static class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private MainActivity mActivity;


        void link(MainActivity act) {
            mActivity = act;
        }

        void unLink() {
            mActivity = null;
        }

        private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
            if (maxHeight > 0 && maxWidth > 0) {
                float ratioBitmap = (float) image.getWidth() / (float) image.getHeight();
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > ratioBitmap) {
                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                return image;
            } else {
                return image;
            }
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap logo = null;
            try {
                /*
                  decodeStream(InputStream)
                  Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(new URL(IMAGE_URL).openStream());
                logo = resize(logo, HALF_SCREEN_WIDTH, HALF_SCREEN_HEIGHT);
            } catch (IOException ioe) {  // Catch the download exception
                Log.e(TAG, ioe.toString());
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                mActivity.mImageView.setImageBitmap(result);
            }
            catch (Exception ex){  //handle null image with default one
                mActivity.mImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}


