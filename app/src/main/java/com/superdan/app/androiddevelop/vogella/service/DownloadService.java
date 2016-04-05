package com.superdan.app.androiddevelop.vogella.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DownloadService extends IntentService {
    private int result= Activity.RESULT_CANCELED;

    public  static final  String URL_PATH="URL_PATH";
    public static final  String FileName="filename";
    public  static final String FILEPATH="filepath";
    public static final String RESULT="result";
    public static final String NOTIFICATION="com.superdan.app.androiddevelop.receiver";
    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String PATH=intent.getStringExtra(URL_PATH);
        String filename=intent.getStringExtra(FileName);
        File output=new File(Environment.getExternalStorageDirectory(),filename);
        if (output.exists()){
            output.delete();
        }
        InputStream stream=null;
        FileOutputStream fos=null;

        try {
            URL url=new URL(PATH);
            stream=url.openConnection().getInputStream();
            InputStreamReader reader=new InputStreamReader(stream);
            fos=new FileOutputStream(output.getPath());
            int next=-1;
            while ((next=reader.read())!=-1){
                fos.write(next);
            }
            result=Activity.RESULT_OK;
        }catch ( MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
        publishResults(output.getPath(),result);

    }


    private  void publishResults(String outputPath,int result){
        Intent intent=new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH,outputPath);
        intent.putExtra(RESULT,result);
        sendBroadcast(intent);
    }
}
