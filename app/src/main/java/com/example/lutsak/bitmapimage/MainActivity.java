package com.example.lutsak.bitmapimage;

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

    public static final String IMAGE_URL = "https://www.google.com/images/srpr/logo11w.png";

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
        mLoadImageTask = new DownLoadImageTask(mImageView);
        mLoadImageTask.execute(IMAGE_URL);

        Log.d(TAG, "<< onCreate ");
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoadImageTask.cancel(true);
    }

    /*
      AsyncTask enables proper and easy use of the UI thread. This class
      allows to perform background operations and publish results on the UI
      thread without having to manipulate threads and/or handlers.
     */
    private static class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
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
            } catch (IOException ioe) {  // Catch the download exception
                Log.e(TAG, ioe.toString());
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                imageView.setImageBitmap(result);
            }
            catch (Exception ex){  //handle null image with default one
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}


