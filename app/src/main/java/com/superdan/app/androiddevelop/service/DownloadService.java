package com.superdan.app.androiddevelop.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DownloadService extends Service {

    private static final String TAG=Service.class.getSimpleName();

    private  IBinder binder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.binder=new DownloadServiceBinder();

    }




    public  class DownloadServiceBinder extends Binder{
        public  IBinderView iBinderView;

        public  DownloadService getService(){
            return DownloadService.this;
        }
    }

    public  void startDownload(String imageUrl){
        ( (DownloadServiceBinder)DownloadService.this.binder).iBinderView.downloadStart();;
        new DownloadImageAsyncTask(DownloadService.this).execute(imageUrl);
    }


    public  class DownloadImageAsyncTask extends AsyncTask<String,Integer,String>{
        private Service service;
        private String localFilePath;

        public  DownloadImageAsyncTask(Service service){
            super();
            this.service=service;

        }
        @Override
        protected String doInBackground(String... params) {
            URL fileUrl=null;
            try {
                fileUrl=new URL(params[0]);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            if(fileUrl==null) return  null;

            try{
                HttpURLConnection connection=(HttpURLConnection)fileUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                //计算 文件长度
//                int lengthOfFile=connection.getContentLength();
                /*
                * 不存在SD卡就放在缓存文件夹内
                * */
                File cacheDir= this.service.getCacheDir();

                File downloadFile=new File(cacheDir, UUID.randomUUID().toString()+".jpg");


                if(!downloadFile.exists()){
                    File parent=downloadFile.getParentFile();
                    if(parent!=null)parent.mkdirs();
                }
                FileOutputStream output=new FileOutputStream(downloadFile);
                InputStream inputStream=connection.getInputStream();
                byte[]buttfer=new byte[1024];
                int len;
                long total=0;
                while ((len=inputStream.read(buttfer))>0){
                    total+=len;
//                    publishProgress((int)((total*100)/lengthOfFile));
                    output.write(buttfer,0,len);
                }
                output.close();
                inputStream.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ((DownloadServiceBinder)binder).iBinderView.downloadSuccess(localFilePath);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            ((DownloadServiceBinder)binder).iBinderView.downloadFailure();
        }
    }
}
