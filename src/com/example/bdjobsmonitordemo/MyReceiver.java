package com.example.bdjobsmonitordemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	
	boolean toConnect;
	SharedPreferences sharedPreference;
	//SharedPreferences sharedPreference;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		sharedPreference = context.getSharedPreferences("bdjobs_receiver", 0); /////// 0 for private mode
		this.toConnect = sharedPreference.getBoolean("toConnect", false);
		//this.toConnect = MainActivity.ma.toConnect;
        
		String action = intent.getAction();
		if(action.equals("omi.MY_RECEIVER"))
		{
			if(intent.getExtras().containsKey("toConnect"))
			{
				this.toConnect = intent.getExtras().getBoolean("toConnect");
				
			}
			
		}
		
		else if(action.equals("android.intent.action.BOOT_COMPLETED"))
		{
			if(this.toConnect && haveNetworkConnection(context))
			{
				Intent startIntent = new Intent(context,MyService.class);
				context.startService(startIntent);
				Toast.makeText(context, "monitor started", Toast.LENGTH_SHORT).show();
				
			}
			
		}
		
		else if(action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals("android.net.wifi.STATE_CHANGE"))
		{
			if(this.toConnect)
			{
				if(haveNetworkConnection(context))
				{
					Intent startIntent = new Intent(context,MyService.class);
					context.startService(startIntent);
					//Intent main = new Intent(context,MainActivity.class);
					//main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					//main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//context.startActivity(main);
					Toast.makeText(context, "monitor started because of network coverage", Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					Intent stopIntent = new Intent(context,MyService.class);
					context.stopService(stopIntent);
					//Intent main = new Intent(context,MainActivity.class);
					//main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					//main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//context.startActivity(main);
					Toast.makeText(context, "monitor stopped for unavailability of internet", Toast.LENGTH_SHORT).show();
					
				}
			}
			
			
		}
		
		SharedPreferences.Editor edit = sharedPreference.edit();
    	edit.putBoolean("toConnect", this.toConnect);
    	edit.commit();

	}
	
	
	
	private boolean haveNetworkConnection(Context context) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}

}
