package com.seven.broadcastservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BroadCastService extends Activity {
    private static final String TAG = "MYTAG";
    private static final String MYACTION = "com.seven.broadcast";
    private static final int ERROR = 999;
    private Button startBtn = null;
    private Button stopBtn = null;
    private TextView mTextView = null;
    private ProgressBar mProgressBar = null;
    private Intent mIntent = null;
    private int curLoad = 0;
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i(TAG, "current loading: "+curLoad);
			if(curLoad<0||curLoad>100){
				Log.e(TAG, "ERROR: "+curLoad);
				return;
			}
			mProgressBar.setProgress(curLoad);
			mTextView.setText(curLoad+"%");
		}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mIntent = new Intent();
        startBtn = (Button)findViewById(R.id.start_Btn);
        stopBtn = (Button)findViewById(R.id.stop_Btn);
        startBtn.setOnClickListener(new ButtonClickListener());
        stopBtn.setOnClickListener(new ButtonClickListener());
        mTextView = (TextView)findViewById(R.id.loading_Tv);
        mProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        mProgressBar.setMax(100);
    }
    
    class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v == startBtn){
				Log.i(TAG, "start button clicked...pid: "+Process.myPid());
				mIntent.setClass(BroadCastService.this, DownLoadService.class);
				startService(mIntent);
			}else if (v == stopBtn) {
				stopService(mIntent);
			}
		}
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "register the broadcast receiver...");
		IntentFilter filter = new IntentFilter();
		filter.addAction(MYACTION);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "unregister the broadcast receiver...");
		unregisterReceiver(receiver);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(MYACTION.equals(intent.getAction())){
				Log.i(TAG, "get the broadcast from DownLoadService...");
				curLoad = intent.getIntExtra("CurrentLoading", ERROR);
				mHandler.sendMessage(mHandler.obtainMessage());
			}
		}
	};
}