package applusvelosi.projects.android.salt.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Currency;
//import applusvelosi.projects.android.salt.SaltApplication.TrackerName;
//
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Logger.LogLevel;
//import com.google.android.gms.analytics.Tracker;

public class SplashActivity extends Activity {
	public static final String KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN = "pendingrootfragtoopen"; //value for this map should be int
	SaltApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SaltApplication)getApplication();
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void onResume() {
		super.onResume();

		String versionName;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			((TextView)findViewById(R.id.tviews_splash_saltversion)).setText("SALT " + versionName);

			String latestVersion = ParseQuery.getQuery("System").getFirst().getString("latestVersion");
			if(!versionName.equals(latestVersion)){
				new AlertDialog.Builder(SplashActivity.this).setTitle("Update required").
						setMessage("New version available. Please update with the latest version")
						.setPositiveButton("Update", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=applusvelosi.projects.android.salt")));
							}
						})
						.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).create().show();
			}else{
				startApp();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			((TextView)findViewById(R.id.tviews_splash_saltversion)).setText("SALT ");
			startApp();
		} catch (ParseException e) {
			e.printStackTrace();
			startApp();
		}
	}

	private void startApp(){
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
				app.initializeApp();

				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						Intent intent;
						if(((SaltApplication)getApplication()).hasSavedData()){
							intent = new Intent(SplashActivity.this, HomeActivity.class);
							if(getIntent().hasExtra(KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN))
								intent.putExtra(HomeActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN, getIntent().getExtras().getInt(KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN));
						}else
							intent = new Intent(SplashActivity.this, LoginActivity.class);

						startActivity(intent);
						finish();
					}
				});
			}
		}).start();
	}
}
