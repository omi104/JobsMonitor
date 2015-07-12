package com.example.bdjobsmonitordemo;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
	Button start,stop,manual;;
	boolean toConnect;
	SharedPreferences sharedPreference;
	public static MainActivity ma = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ma = this;
		
		sharedPreference = getSharedPreferences("bdjobs_receiver", 0); /////// 0 for private mode
		this.toConnect = sharedPreference.getBoolean("toConnect", false);
		//Intent intent = new Intent(this,MyService.class);
		//startService(intent);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		manual = (Button) findViewById(R.id.manual);
		
		/*
		if(isMyServiceRunning(MyService.class))
		{
			start.setVisibility(View.INVISIBLE);///invisible
			//btn.setVisibility(View.GONE); /// gone...delete
			
		}
		else
		{
			stop.setVisibility(View.INVISIBLE);
		}
		
		*/
		
		if(toConnect)
		{
			start.setVisibility(View.INVISIBLE);///invisible
			//btn.setVisibility(View.GONE); /// gone...delete
			
		}
		else
		{
			stop.setVisibility(View.INVISIBLE);
		}
		
		manual.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,About.class));
				
			}
		});
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Intent intent_receiver = new Intent("omi.MY_RECEIVER");
				//intent_receiver.putExtra("toConnect", true);
				//MainActivity.this.sendBroadcast(intent_receiver);
				MainActivity.this.toConnect = true;
				writePreference();
				start.setVisibility(View.INVISIBLE);
				stop.setVisibility(View.VISIBLE);
				
				
				if(isConnected())
				{
					
					
					Intent intent = new Intent(MainActivity.this,MyService.class);
					startService(intent);
					
					Toast.makeText(getApplicationContext(),"monitoring started", Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					
					Toast.makeText(getApplicationContext(),"no internet available", Toast.LENGTH_SHORT).show();
					
					
				}
				
				
			}
		});
		
		
		stop.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				//Intent intent_receiver = new Intent("omi.MY_RECEIVER");
				//intent_receiver.putExtra("toConnect", false);
				//MainActivity.this.sendBroadcast(intent_receiver);
				MainActivity.this.toConnect = false;
				writePreference();
				
				
				Intent intent = new Intent(MainActivity.this,MyService.class);
				stopService(intent);
				stop.setVisibility(View.INVISIBLE);
				start.setVisibility(View.VISIBLE);
				
				
				
				
				Toast.makeText(getApplicationContext(),"monitoring stopped", Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("stop service", "in main activity");
		writePreference();
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Intent intent = new Intent(this,MyService.class);
		//stopService(intent);
	}
	
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public boolean isConnected() 
	{
	 ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	 NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	 return (networkInfo != null && networkInfo.isConnected());
	}  
	
	private void writePreference()
	{
		SharedPreferences.Editor edit = sharedPreference.edit();
    	edit.putBoolean("toConnect", this.toConnect);
    	edit.commit();
		
	}

	
}
