package applusvelosi.projects.android.salt.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;

public class LoginActivity extends Activity implements OnClickListener{	
	private EditText usernameField, passwordField;
	private Button loginButton;
	private SaltApplication app;
	private AlertDialog dialog;
	private SaltProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SaltApplication)getApplication();
		
		pd = new SaltProgressDialog(this);
		dialog = new AlertDialog.Builder(this).setMessage("Wifi required. Please turn on your Wifi")
				 .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
						
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
						dialog.dismiss();
					}
				})
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						LoginActivity.this.finish();
					}
				}).create();
		
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(app.isNetworkAvailable())
					dialog.dismiss();
				else
					LoginActivity.this.dialog.show();
			}
		});
		
		setContentView(R.layout.activity_login);
		try {
			((TextView)findViewById(R.id.tviews_login_saltversion)).setText("SALT "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			((TextView)findViewById(R.id.tviews_splash_saltversion)).setText("SALT");
		}

		usernameField = (EditText)findViewById(R.id.etexts_login_username);
		passwordField = (EditText)findViewById(R.id.etexts_login_password);
		loginButton = (Button)findViewById(R.id.buttons_login_login);
		loginButton.setOnClickListener(this);
		
		usernameField.setTypeface(SaltApplication.myFont(this));
		passwordField.setTypeface(SaltApplication.myFont(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(!app.isNetworkAvailable() && !dialog.isShowing()){
			dialog.show();
		}				
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("Are you sure you want to exit SALT?")
		 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();											
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}
	
	@Override
	public void onClick(View v) {
		if(v == loginButton){
			if(usernameField.getText().length()>0 && passwordField.getText().length()>0){
				pd.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String temp;
						final String result;
						
						try {
							temp = app.onlineGateway.authentication(usernameField.getText().toString(), passwordField.getText().toString());
						} catch (Exception e) {
							temp = e.getMessage();
							e.printStackTrace();
						}
						result = temp;
						
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run(){
								pd.dismiss();
								final String [] results = result.split("-"); 
								if(results.length > 2){ //successful, no errors to show
									app.setStaff(LoginActivity.this, new Staff(LoginActivity.this, results[0], results[1], results[2]));
									startActivity(new Intent(LoginActivity.this, HomeActivity.class));
									finish();
								}else
									app.showMessageDialog(LoginActivity.this, result);
							}
						});

					}
				}).start();
			}else
				app.showMessageDialog(this, "Username and password are required");
		}
	}
}