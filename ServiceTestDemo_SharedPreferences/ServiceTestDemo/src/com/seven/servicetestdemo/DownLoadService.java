package com.seven.servicetestdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class DownLoadService extends Service{
	private static final String TAG = "MYTAG";
	private static final int TIME = 1;
	private Timer timer = null;
	private int i = 0;
	private SharedPreferences preferences = null;
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "DownLoadService.onBind()...");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "DownLoadService.onCreate()...");
		preferences = getSharedPreferences("CurrentLoading_SharedPs", 0);
		timer = new Timer();
		timer.schedule(new MyTimerTask(), 0, TIME*1000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "DownLoadService.onStartCommand()...");
		Log.e(TAG, "flags: "+flags+" startId: "+startId);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "DownLoadService.onDestroy()...");
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}
	
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			setCurrentLoading();
			if(100==i){
				i=0;
			}
			i++;
		}
		
	}
	
	private void setCurrentLoading() {
		preferences.edit().putInt("CurrentLoading", i).commit();
	}

}
