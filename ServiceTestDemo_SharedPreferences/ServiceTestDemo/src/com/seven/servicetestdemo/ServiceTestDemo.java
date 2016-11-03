package com.seven.servicetestdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ServiceTestDemo extends Activity {
	 private static final String TAG = "MYTAG";
	 private static final int TIME = 1;
	 private static final int MAX = 100;
    private Button startSerBtn = null;
    private Button stopSerBtn  = null;
    private TextView currentTv = null;
    private Intent intent = null;
    private Timer timer = null;
    private ProgressBar mProgressBar = null;
    private SharedPreferences preferences = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        timer = new Timer();
        mProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        mProgressBar.setMax(MAX);
        preferences = getSharedPreferences("CurrentLoading_SharedPs", 0);
        intent = new Intent();
        intent.setClass(ServiceTestDemo.this, DownLoadService.class);
        currentTv = (TextView)findViewById(R.id.loading_Tv);
        startSerBtn = (Button)findViewById(R.id.start_Btn);
        stopSerBtn  = (Button)findViewById(R.id.stop_Btn);
        startSerBtn.setOnClickListener(new ButtonClickListener());
        stopSerBtn.setOnClickListener(new ButtonClickListener());
    }
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int couLoad = preferences.getInt("CurrentLoading", 0);
			mProgressBar.setProgress(couLoad);
			currentTv.setText(couLoad+"%");
		}
    };
    
    class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			mHandler.sendMessage(mHandler.obtainMessage());
//			mProgressBar.setProgress(preferences.getInt("CurrentLoading", 0));
		}
    	
    }
    
    class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(startSerBtn==v){
				Log.i(TAG, "Start Button Clicked.");
				if(intent!=null){
					startService(intent);
					timer.schedule(new MyTimerTask(), 0, TIME * 1000);
				}
			}else if (stopSerBtn==v) {
				Log.i(TAG, "Stop Button Clicked.");
				if(intent!=null){
					stopService(intent);
					if(timer!=null){
						timer.cancel();
						timer = null;
					}
				}
			}
		}
    	
    }
}