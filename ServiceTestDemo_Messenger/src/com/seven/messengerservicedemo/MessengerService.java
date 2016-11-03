package com.seven.messengerservicedemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service{
	
	private static final String TAG = "MYTAG";
	private static final int TIME = 1;
	static final int TEST = 0;
	private int i = 0;
	private Timer mTimer = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TEST:
				Log.e(TAG, "Get Message from MainActivity.");
				cMessenger = msg.replyTo;//get the messenger of client
				mTimer.schedule(new MyTimerTask(), 1000,TIME * 1000);
				break;
			default:
				break;
			}
		}
	};
	private Messenger mMessenger = new Messenger(mHandler);//It's the messenger of server
	private Messenger cMessenger = null;//It's the messenger of client
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "MessengerService.onBind()...");
		return mMessenger.getBinder();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "MessengerService.onCreate()...pid: "+Process.myPid());
		mTimer = new Timer();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "MessengerService.onDestroy()...");
		if(mTimer!=null){
			mTimer.cancel();
			mTimer = null;
		}
	}
	
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			if (i == 100) {
				i = 0;
			}
			try {
				//send the message to the client
				Message message = Message.obtain(null, MessengerService.TEST,i, 0);
				cMessenger.send(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}
