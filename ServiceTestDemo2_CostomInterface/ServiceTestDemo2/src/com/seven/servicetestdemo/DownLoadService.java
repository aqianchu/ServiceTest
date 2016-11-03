package com.seven.servicetestdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class DownLoadService extends Service implements ICountService{
	private static final String TAG = "MYTAG";
	private static final int TIME = 1;
	private Timer timer = null;
	private int i = 0;
	
	private ServiceBinder serviceBinder = new ServiceBinder();
	
	public class ServiceBinder extends Binder implements ICountService{

		@Override
		public int getCurrentLoad() {
			Log.i(TAG, "ServiceBinder getCurrentLoad()... i=:"+i);
			return i;
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "DownLoadService.onBind()...");
		return serviceBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "DownLoadService.onCreate()...pid: "+Process.myPid());
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
			if(100==i){
				i=0;
			}
			i++;
		}
		
	}

	@Override
	public int getCurrentLoad() {
		return 0;
	}

}
