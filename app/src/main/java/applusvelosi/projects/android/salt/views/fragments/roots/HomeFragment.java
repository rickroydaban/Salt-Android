package applusvelosi.projects.android.salt.views.fragments.roots;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter.StaffLeaveSyncListener;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.NewClaimHeaderActivity;
import applusvelosi.projects.android.salt.views.NewLeaveRequestActivity;
import applusvelosi.projects.android.salt.views.fragments.CalendarWeeklyFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderInputType;
import applusvelosi.projects.android.salt.views.fragments.leaves.LeaveInputType;

public class HomeFragment extends RootFragment implements StaffLeaveSyncListener{
	private static HomeFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefresh, actionbarCalendarButton, actionbarNewLeaveButton, actionbarNewClaimButton;
	
	private TextView tvCurrDateTime, tvStaffName;
	private SimpleDateFormat homeDateTimeFormat;
	private StaffLeaveTypeCounter leaveCounter;

	public static HomeFragment getInstance(){
		if(instance == null)
			instance = new HomeFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	
	@Override
	protected RelativeLayout setupActionbar(){
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_home, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefresh = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarCalendarButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_calendar);
		actionbarNewLeaveButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newleaverequest);
		actionbarNewClaimButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newclaim);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Home");

		actionbarMenuButton.setOnClickListener(this);
		actionbarRefresh.setOnClickListener(this);
		actionbarCalendarButton.setOnClickListener(this);
		actionbarNewLeaveButton.setOnClickListener(this);
		actionbarNewClaimButton.setOnClickListener(this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		actionbarMenuButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		activity.updateMaxSidebarShowWidth(displaymetrics.widthPixels - actionbarMenuButton.getMeasuredWidth() - 100);

		return actionbarLayout;
	}

	@Override
	public View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_home_temp, null);
		
		tvStaffName = (TextView)view.findViewById(R.id.tviews_home_name);
		leaveCounter = app.staffLeaveCounter;
		tvCurrDateTime = (TextView)view.findViewById(R.id.tviews_home_currdatetime);
		homeDateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						tvCurrDateTime.setText(homeDateTimeFormat.format(new Date()));
					}
				});
			}
		}, 0, 1000);


//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try{
//                    String filePath = app.fileManager.getDirForCapturedAttachments()+"saltcapturedattachment_2015_03_25_13_50_28423844781.jpg";
//                    System.out.println("SALT Attempt to get "+filePath);
//                    app.onlineGateway.uploadAttachment(new File(filePath));
//                    System.out.println("SALT Success");
//                }catch(Exception e){
//                    System.out.println("SALT Exception "+e.getMessage());
//                }
//            }
//        }).start();


		return view;
	}

    @Override
    public void disableUserInteractionsOnSidebarShown() {

    }

    @Override
    public void enableUserInteractionsOnSidebarHidden() {

    }

    @Override
	public void onResume() {
		super.onResume();
		refresh();
	}
	
	public void refresh(){
        activity.startLoading();
		leaveCounter.syncToServer(this);
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);	
		}else if(v == actionbarRefresh){
			refresh();
		}else if(v == actionbarNewLeaveButton){
			startActivity(new Intent(activity, NewLeaveRequestActivity.class));
		}else if(v == actionbarNewClaimButton){
			startActivity(new Intent(activity, NewClaimHeaderActivity.class));
		}else if(v == actionbarCalendarButton){
//			activity.changeChildPage(CalendarWeeklyFragment.getInstance());
		}
	}


	@Override
	public void onSyncSuccess() {
        activity.finishLoading();
//		OverviewLeavesFragment.getInstance().refresh(this, leaveCounter);
		tvStaffName.setText(app.getStaff().getLname()+", "+app.getStaff().getFname());
	}

	@Override
	public void onSyncFailed(String errorMessage) {
        activity.finishLoading(errorMessage);
	}
}
