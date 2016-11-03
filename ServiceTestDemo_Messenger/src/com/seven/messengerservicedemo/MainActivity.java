package com.seven.messengerservicedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
	private static final String TAG = "MYTAG";
	private boolean mIsBind;
	private Button startBtn = null;
	private Button stopBtn = null;
	private TextView mTextView = null;
	private Messenger rMessenger = null;
	private Messenger mMessenger = null;
	private Intent intent = null;
	private ProgressBar mProgressBar = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MessengerService.TEST:
				Log.e(TAG, "Get Message From MessengerService. i= "+msg.arg1);
				int curLoad = msg.arg1;
				mTextView.setText(curLoad+"%");
				mProgressBar.setProgress(curLoad);
				break;

			default:
				break;
			}
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        intent = new Intent("com.seven.messagerservice.MessengerService");
        mProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        mProgressBar.setMax(100);
        mTextView = (TextView)findViewById(R.id.loading_Tv);
        startBtn = (Button)findViewById(R.id.start_Btn);
        stopBtn = (Button)findViewById(R.id.stop_Btn);
        startBtn.setOnClickListener(new ButtonClickListener());
        stopBtn.setOnClickListener(new ButtonClickListener());
        
    }
	class ButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (startBtn == v) {
				Log.i(TAG, "Start Button Clicked.pid: "+Process.myPid());
				doBindService();
			} else if (stopBtn == v) {
				Log.i(TAG, "Send Button Clicked.");
				doUnBindService();
			}
		}

	}
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}


	private ServiceConnection serConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "onServiceDisconnected()...");
			rMessenger = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "onServiceConnected()...");
				rMessenger = new Messenger(service);//get the object of remote service
				mMessenger = new Messenger(mHandler);//initial the object of local service
				sendMessage();
		}
	};
    
    private void doBindService(){
    	Log.i(TAG, "doBindService()...");
		mIsBind = bindService(intent, serConn, BIND_AUTO_CREATE);//if bind success return true
		Log.e(TAG, "Is bind: "+mIsBind);
    }
    
    private void doUnBindService(){
    	if(mIsBind){
    		Log.i(TAG, "doUnBindService()...");
    		unbindService(serConn);
    		mIsBind = false;
    	}
    }

	private void sendMessage() {
		Message msg = Message.obtain(null, MessengerService.TEST);//MessengerService.TEST=0
		msg.replyTo = mMessenger;
		try {
			rMessenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}