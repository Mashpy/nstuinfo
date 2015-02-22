package com.mashpy.nstuinfo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.Activity;

public class NstuInfoDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nstu_info_details);
AdView adView = (AdView)this.findViewById(R.id.adView);

AdRequest adRequest = new AdRequest.Builder().build();

adView.loadAd(adRequest);
		String newtest= getIntent().getStringExtra("New_Topic");
		WebView wv = (WebView) findViewById(R.id.webView1); 
		wv.getSettings().setJavaScriptEnabled(true);     
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wv.setBackgroundColor(0);
        wv.loadDataWithBaseURL("", newtest, mimeType, encoding, "");
        
	}
}
