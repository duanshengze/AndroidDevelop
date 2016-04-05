package com.superdan.app.androiddevelop.vogella.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.superdan.app.androiddevelop.R;
import com.superdan.app.androiddevelop.vogella.service.DownloadService;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DownloadActivity extends Activity  {
    private TextView textView;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                String string=bundle.getString(DownloadService.FILEPATH);
                int resultCode=bundle.getInt(DownloadService.RESULT);
                if(resultCode==RESULT_OK){
                    Toast.makeText(DownloadActivity.this,"Download complete. Download URI:"
                    + string,Toast.LENGTH_SHORT).show();
                    textView.setText("download done");
                }else {
                    Toast.makeText(DownloadActivity.this,"Download failed",Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        textView=(TextView)findViewById(R.id.status);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

   public void onClick(View view){
       Intent intent = new Intent(this, DownloadService.class);
       // add infos for the service which file to download and where to store
       intent.putExtra(DownloadService.FileName, "index.html");
       intent.putExtra(DownloadService.URL_PATH,
               "http://www.vogella.com/index.html");
       startService(intent);
       textView.setText("Service started");
   }

}
