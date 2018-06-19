package com.example.lutsak.bitmapimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = DownloadImageTask.class.getSimpleName();

    private final String mLinkToImage;

    private final int mDesiredImageWidth;
    private final int mDesiredImageHeight;

    interface DownLoadImageTaskProtocol {
        void onImageDownloaded(Bitmap bitmap);
        void onImageDownloadFailed();
    }

    private DownLoadImageTaskProtocol mProtocol;

    public DownloadImageTask(String linkToImage, int desiredImageWidth, int desiredImageHeight){
        mLinkToImage = linkToImage;
        mDesiredImageWidth = desiredImageWidth;
        mDesiredImageHeight = desiredImageHeight;
    }

    void setProtocol(DownLoadImageTaskProtocol protocol) {
        mProtocol = protocol;
    }

    void removeProtocol() {
        mProtocol = null;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        Log.d(TAG, ">> resize");
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
        Log.d(TAG, ">> doInBackground, this [" + this + "]");

        Bitmap logo = null;
        try {

            Log.d(TAG, "doInBackground, > sleep");

            Thread.sleep(10000);

            Log.d(TAG, "doInBackground, < sleep");

                /*
                  decodeStream(InputStream)
                  Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(new URL(mLinkToImage).openStream());
            logo = resize(logo, mDesiredImageWidth, mDesiredImageHeight);
        } catch (IOException ioe) {  // Catch the download exception
            Log.e(TAG, ioe.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return logo;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d(TAG, ">> onPostExecute, this [" + this + "], result " + result);
        if(result == null){
            mProtocol.onImageDownloadFailed();
            return;
        }
        mProtocol.onImageDownloaded(result);
    }
}
