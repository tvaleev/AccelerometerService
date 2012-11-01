package com.tvaleev.accelerometr.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private AccelerometrService s;
	private boolean mIsBound;

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			s = ((AccelerometrService.AccBinder) binder).getService();
			Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
					.show();
		}

		public void onServiceDisconnected(ComponentName className) {
			s = null;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button btnStart = (Button) findViewById(R.id.btn_start);
		final Button btnStop = (Button) findViewById(R.id.btn_stop);
		final TextView accData = (TextView) findViewById(R.id.acc_text_data);

		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this,
						AccelerometrService.class));
				if (s != null) {
					accData.setText(Float.toString(s.getDataFromAccelerometr()));
				}
			}
		});

		btnStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this,
						AccelerometrService.class));
				doUnbindService();
				accData.setText("Stopped");
			}
		});

		doBindService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	void doBindService() {
		bindService(new Intent(this, AccelerometrService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}
	
	void doUnbindService() {
	    if (mIsBound) {
	        unbindService(mConnection);
	        mIsBound = false;
	    }
	}
}
