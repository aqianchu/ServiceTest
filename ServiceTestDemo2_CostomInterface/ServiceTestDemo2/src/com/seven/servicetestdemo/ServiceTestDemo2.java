package com.seven.servicetestdemo;

import java.util.Timer;
import java.util.TimerTask;

import com.seven.servicetestdemo.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ServiceTestDemo2 extends Activity {
	 private static final String TAG = "MYTAG";
	 private static final int TIME = 1;
	 private static final int MAX = 100;
    private Button startSerBtn = null;
    private Button stopSerBtn  = null;
    private TextView currentTv = null;
    private Intent intent = null;
    private Timer timer = null;
    private ProgressBar mProgressBar = null;
    
    private ICountService iCountService = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        timer = new Timer();
        mProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        mProgressBar.setMax(MAX);
        intent = new Intent("com.seven.test");
        currentTv = (TextView)findViewById(R.id.loading_Tv);
        startSerBtn = (Button)findViewById(R.id.start_Btn);
        stopSerBtn  = (Button)findViewById(R.id.stop_Btn);
        startSerBtn.setOnClickListener(new ButtonClickListener());
        stopSerBtn.setOnClickListener(new ButtonClickListener());
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy()...pid: "+Process.myPid());
	}



	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i(TAG, "handleMessage...");
			int curLoad = iCountService.getCurrentLoad();
			mProgressBar.setProgress(curLoad);
			currentTv.setText(curLoad+"%");
		}
    };
    
    class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			mHandler.sendMessage(mHandler.obtainMessage());
		}
    }
    
    private ServiceConnection serConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			iCountService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "onServiceConnected()...");
			iCountService = (ICountService)service;
		}
	};
    
	class ButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (startSerBtn == v) {
				Log.i(TAG, "Start Button Clicked.");
				bindService(intent, serConn, BIND_AUTO_CREATE);
				timer.schedule(new MyTimerTask(), 1000, TIME * 1000);//这里一定要延迟一下再开始获取数据，不然会报空指针异常
			} else if (stopSerBtn == v) {
				Log.i(TAG, "Stop Button Clicked.");
				unbindService(serConn);
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
			}
		}
	}
}