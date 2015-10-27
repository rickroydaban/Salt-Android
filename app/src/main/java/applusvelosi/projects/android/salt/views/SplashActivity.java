package applusvelosi.projects.android.salt.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
//import applusvelosi.projects.android.salt.SaltApplication.TrackerName;
//
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Logger.LogLevel;
//import com.google.android.gms.analytics.Tracker;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);	
		try {
			((TextView)findViewById(R.id.tviews_splash_saltversion)).setText("SALT "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			((TextView)findViewById(R.id.tviews_splash_saltversion)).setText("SALT ");
		}

		Tracker t = ((SaltApplication)getApplication()).getTracker(SaltApplication.TrackerName.APP_TRACKER);
		    GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		    // Set screen name.
		    // Where path is a String representing the screen name.
		    t.setScreenName(getString(R.string.app_name));

		    // Send a screen view.
		    t.send(new HitBuilders.ScreenViewBuilder().build());
		    System.out.println("Sent request!");

	    new Thread(new Runnable() {

			@Override
			public void run() {
				((SaltApplication)getApplication()).initializeApp(SplashActivity.this);
				
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						startActivity(new Intent(SplashActivity.this, (((SaltApplication)getApplication()).hasSavedData())?HomeActivity.class:LoginActivity.class));
						finish();
					}
				});
			}
		}).start();
	}	
}