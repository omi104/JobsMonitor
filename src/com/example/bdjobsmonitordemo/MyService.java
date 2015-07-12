package com.example.bdjobsmonitordemo;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service{
	Timer timer;;
    TimerTask doAsynchronousTask;
    int notification_id = 0;
    
    
    
    
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("destroy service", "in myservice");
		
		
	    timer.cancel();
	    timer = null;
	    doAsynchronousTask.cancel();
	    doAsynchronousTask = null;
	}
	
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		Log.d("stop service", "in myservice");
		
		
		    timer.cancel();
		    timer = null;
		    doAsynchronousTask.cancel();
		    doAsynchronousTask = null;
		    
		
		return super.stopService(name);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		callAsynchronousTask();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public void callAsynchronousTask() {
		
	    //final Handler handler = new Handler();
	    timer = new Timer();
	    doAsynchronousTask = new TimerTask() { 
	    	
	    	String primary = "";
	        @Override
	        public void run() {
	        	
	        	Document doc = null;
	        	try {
					doc = Jsoup.connect("http://joblist.bdjobs.com/jobsearch.asp?fcatId=8&icatId=").header("Accept-Encoding", "gzip, deflate")
						    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
						    .maxBodySize(0)
						    .timeout(600000)
						    .get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	Log.d("doc string", doc.toString());
	        	Log.d("doc html", doc.html());
	        	if(doc.toString().equals(doc.html()))
	        	{
	        		Log.d("doc_html","equal");
	        		
	        	}
	        	else
	        	{
	        		Log.d("doc_html","not equal");
	        		
	        	}
	        	Element first_job_elem = doc.getElementsByClass("norm_jobs_wrapper").first();
	        	Element comp_name_elem = first_job_elem.getElementsByClass("comp_name_text").first();
	        	String comp_name = comp_name_elem.text();
	        	Log.d("comp_name", comp_name);
	        	//String post = first_job_elem.select("a").first().text();
	        	//String post = comp_name_elem.getElementsByTag("a").first().text();
	        	//Log.d("post", post);
	        	//Element e = doc.select("div.entry > p")
	        	
	        	//String post = first_job_elem.getElementsByClass("job_title_text").first().getElementsByTag("a").first().data();
	        	//Log.d("post", post);
	        	
	        	Elements pElems = doc.select("div.job_title_text > a");
	        	
	        	if(pElems == null)
	        	{
	        		Log.d("elements", "null");
	        	}
	        	else
	        	{
	        		Log.d("elements", "not null");
	        		Log.d("omiomi", pElems.toString());
	        		//Log.d("elements data",pElems.first().data() );
	        		//Log.d("elements text", pElems.first().text());
	        		
	        	}

	        	for (Element pElem : pElems) {
	        	   //myArrayList.add(pElem.data());
	        		//String post_data = pElem.data();
	        		String post = pElem.text();// it works !!! :)
	        		String href = pElem.attr("href");
	        		//Log.d("post_data", post_data);
	        		Log.d("post_text", post);  // it works !!! :)
	        		Log.d("href", href);
	        		
	        		if(href.equals(primary))
	        		{
	        			
	        			Log.d("job and primary are ","equal");
	        			Log.d("href",href);
	        			Log.d("primary",primary);
	        			
	        			
	        			
	        		}
	        		else
	        		{
	        			Log.d("href and primary are ","not equal");
	        			Log.d("primary", primary);
	        			Log.d("href",href);
	        			primary = href;
	        			
	        			showNotification(href,comp_name,post);
	        			
	        			
	        		}
	        		
	        		break;
	        	}
	        	
	        	
	        	
	            
	                     
	                    
	       }
	           
	        
	    };
	    timer.schedule(doAsynchronousTask, 0, 900000); //execute in every 50000 ms
	}
	
	void showNotification(String href,String comp,String post)
	{
		
		Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
		intent.putExtra("data", href);
		//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);/// this must be done for sending data///
		//PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		Notification mNotification = new Notification.Builder(MyService.this)
		            .setContentTitle("New Job available !!!")
		            .setContentText("company name:"+comp+"post:"+post)
		            .setSmallIcon(R.drawable.ic_launcher)
		            .setContentIntent(pIntent)
		            .setSound(soundUri)
		            .build();
		
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notification_id, mNotification);
		notification_id++;
		
		
		
		
	}

}
