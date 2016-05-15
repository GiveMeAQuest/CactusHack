package com.makhrov.android.cactusapplication.WEB.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private final String imageUrl;

    public BitmapWorkerTask(ImageView imageView,String imageUrl, ProgressBar progressBar) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        progressBarWeakReference = new WeakReference<ProgressBar>(progressBar);
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onPreExecute() {
        ProgressBar progressBar = progressBarWeakReference.get();
        if(progressBar !=null)
            progressBar.setVisibility(View.VISIBLE);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        Bitmap myBitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                ProgressBar progressBar = progressBarWeakReference.get();
                if (progressBar !=null)
                    progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}