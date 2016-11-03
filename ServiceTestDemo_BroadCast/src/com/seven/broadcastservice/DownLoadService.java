package com.seven.broadcastservice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class DownLoadService extends Service{
	private static final String TAG = "MYTAG";
	private static final int TIME = 1;
	private Timer mTimer = null;
	private Intent intent =null;
	private int i = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "DownLoadService.onCreate()...pid: "+Process.myPid());
		intent = new Intent("com.seven.broadcast");
		mTimer = new Timer();
		mTimer.schedule(new MyTimerTask(), 0 , TIME * 1000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "DownLoadService.onStartCommand()...");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "DownLoadService.onDestroy()...");
		if(mTimer!=null){
			mTimer.cancel();
			mTimer = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			if(i==100){
				i=0;
			}
			intent.putExtra("CurrentLoading", i);
			sendBroadcast(intent);
			i++;
			Log.e(TAG, "i= "+i);
		}
	}

}
