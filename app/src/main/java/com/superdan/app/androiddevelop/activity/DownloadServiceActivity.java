package com.superdan.app.androiddevelop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superdan.app.androiddevelop.R;
import com.superdan.app.androiddevelop.service.DownloadService;
import com.superdan.app.androiddevelop.service.IBinderView;
import com.superdan.app.androiddevelop.utils.ImageUtil;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DownloadServiceActivity extends AppCompatActivity implements View.OnClickListener,IBinderView {
    private static final String OBJECT_IMAGE_URL = "http://img.blog.csdn.net/20150913233900119";

    private Button startBT;
    private ImageView imageIV;
    private DownloadService mService;

    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_service);
        initView();
        initData();
        initListeners();


    }

    private void  initView(){
        TextView imageTV=(TextView)findViewById(R.id.image_tv);
        imageTV.setText(OBJECT_IMAGE_URL);
        startBT=(Button)findViewById(R.id.start_service_bt);
        imageIV=(ImageView)findViewById(R.id.image_iv);

    }


    private void initData(){
        connection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadServiceBinder binder=(DownloadService.DownloadServiceBinder)service;
                binder.iBinderView=DownloadServiceActivity.this;
                mService=binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService=null;
            }
        };
        bindService(new Intent(DownloadServiceActivity.this,DownloadService.class)
                ,connection, Context.BIND_AUTO_CREATE
        );

    }

    private void  initListeners(){
        startBT.setOnClickListener(this);
    }

    @Override
    public void downloadStart() {
        this.startBT.setEnabled(false);
    }

    @Override
    public void downloadSuccess(String imageFilePath) {

        this.startBT.setEnabled(true);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int screenHeight=metrics.heightPixels;

        Bitmap bitmap= ImageUtil.decodeScaleImage(imageFilePath,screenWidth,screenHeight);
        imageIV.setImageBitmap(bitmap);
    }

    @Override
    public void downloadFailure() {
        this.startBT.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.start_service_bt:
                mService.startDownload(OBJECT_IMAGE_URL);
                break;
        }
    }
}
