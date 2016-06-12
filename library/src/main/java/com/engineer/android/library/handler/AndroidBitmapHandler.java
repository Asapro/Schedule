package com.engineer.android.library.handler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * Created by L.J on 2016/6/2.
 */
public final class AndroidBitmapHandler {
    static {
        System.loadLibrary("blur");
    }

    private static AndroidBitmapHandler instance = new AndroidBitmapHandler();

    public static AndroidBitmapHandler getInstance(){
        return instance;
    }

    private AndroidBitmapHandler(){

    }

    private native void blur(Bitmap bitmap);

    public void displayImage(Context context,String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public void displayImage(Activity activity,String url,ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public void displayImage(Fragment fragment,String url,ImageView imageView) {
        Glide.with(fragment).load(url).into(imageView);
    }

    public void blurBitmap(Context context,Bitmap bitmap,ImageView imageView){
        new BlurBitmapTask(context, imageView).execute(bitmap);
    }

    public void downloadImage(String url,String destPath,OnDownloadImageListener listener) {
        new HttpDownloadImageTask(url, destPath, listener).execute();
    }

    public void uploadImage(String url,String path,OnUploadImageListener listener){
        new HttpUploadImageTask(url, path, listener).execute();
    }

    public interface OnDownloadImageListener{
        void onStart(String url);
        void onComplete(boolean result,String url,String destPath);
    }

    public interface OnUploadImageListener{
        void start(String url);
        void onComplete(boolean result,String url,String path);
    }

    private class BlurBitmapTask extends AsyncTask<Bitmap,Void,Bitmap>{
        private static final int SCALE = 16;
        private Context context;
        private ImageView imageView;

        public BlurBitmapTask(Context context,ImageView imageView) {
            this.context = context;
            this.imageView = imageView;
        }

        private Bitmap blurWithRenderScript(Bitmap bitmap){
            if(!bitmap.getConfig().equals(Bitmap.Config.ARGB_8888)){
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            }
            Bitmap bin = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth() / SCALE,bitmap.getHeight() / SCALE,true);
            Bitmap bout = Bitmap.createBitmap(bitmap.getWidth() / SCALE,bitmap.getHeight() / SCALE, Bitmap.Config.ARGB_8888);
            final RenderScript renderScript = RenderScript.create(context);
            final Allocation ain = Allocation.createFromBitmap(renderScript,bin);
            final Allocation aout = Allocation.createFromBitmap(renderScript,bout);
            final ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.RGBA_8888(renderScript));
            scriptIntrinsicBlur.setInput(ain);
            scriptIntrinsicBlur.setRadius(25);
            scriptIntrinsicBlur.forEach(aout);
            aout.copyTo(bout);
            scriptIntrinsicBlur.destroy();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                RenderScript.releaseAllContexts();
            }else {
                renderScript.destroy();
            }
            return Bitmap.createScaledBitmap(bout,bitmap.getWidth(),bitmap.getHeight(),true);
        }

        private Bitmap blurWithNative(Bitmap bitmap){
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth() / SCALE,bitmap.getHeight() / SCALE,true);
            blur(scaledBitmap);
            return Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            if(params != null && params.length > 0){
                Bitmap bitmap = params[0];
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                    return blurWithRenderScript(bitmap);
                }else{
                    return blurWithNative(bitmap);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private static class HttpDownloadImageTask extends AsyncTask<Void,Void,Boolean>{
        private String url;
        private String destPath;
        private OnDownloadImageListener listener;

        public HttpDownloadImageTask(String url, String destPath, OnDownloadImageListener listener) {
            this.url = url;
            this.destPath = destPath;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(this.listener != null){
                this.listener.onStart(this.url);
            }
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(this.destPath);
                    if(bitmap.compress(Bitmap.CompressFormat.PNG,75,fileOutputStream)){
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(this.listener != null){
                this.listener.onComplete(aBoolean,this.url,this.destPath);
            }
        }
    }

    private static class HttpUploadImageTask extends AsyncTask<Void,Void,Boolean>{
        private static final String BOUNDARY = "-------7androidBoundary";
        private String url;
        private String path;
        private OnUploadImageListener listener;

        public HttpUploadImageTask(String url, String path, OnUploadImageListener listener) {
            this.url = url;
            this.path = path;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(this.listener != null){
                this.listener.start(this.url);
            }
        }

        private void upload(HttpURLConnection urlConnection) throws IOException {
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bitmap.getAllocationByteCount());
            if(bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream)){
                String filename = this.path.substring(this.path.lastIndexOf(File.separatorChar) + 1);
                dataOutputStream.writeBytes("--" + BOUNDARY + "Content-Disposition:form-data;name=\"image\";filename=\"" + filename + "\"\r\n" +
                        "Content-type:image/jpeg;charset:utf-8\r\n\r\n");
                byteArrayOutputStream.writeTo(dataOutputStream);
                dataOutputStream.writeBytes("--" + BOUNDARY + "--\r\n");
                dataOutputStream.close();
                byteArrayOutputStream.close();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(this.url).openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.addRequestProperty("Content-type","multipart/form-data;boundary=" + BOUNDARY);
                upload(urlConnection);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(this.listener != null){
                this.listener.onComplete(aBoolean,this.url,this.path);
            }
        }
    }
}
