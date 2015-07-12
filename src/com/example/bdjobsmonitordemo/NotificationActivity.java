package com.example.bdjobsmonitordemo;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class NotificationActivity extends Activity {
	//TextView t;
	WebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("omi", "in onCreate");
		
		wv = new WebView(this);
		//setContentView(R.layout.activity_web_view);
		setContentView(wv);
		//wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new MyWebViewClient());
		onNewIntent(getIntent());
	}
	
	
	@Override
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		Log.d("omi", "in onNewIntent");
	    
		        String msg = intent.getStringExtra("data");
		        if(msg == null)
		        {
		        	Log.d("message", "is null");
		        }
		        else if(msg == "")
		        {
		        	Log.d("message", "is empty");
		        }
		        else
		        {
		        	Log.d("message", msg);
		        	
		        }
		        
	        	//t = new TextView(this);
	        	//t.setText(msg);
	            //setContentView(t);
		        String url = "http://joblist.bdjobs.com/"+msg;
	           
		        wv.loadUrl(url);
	            
	        
	    }
	
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	view.loadUrl(url); // you are using siteView here instead of view
	        return true;
	        }
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	       // startActivity(intent);
	        //return true;
	    }


	}


