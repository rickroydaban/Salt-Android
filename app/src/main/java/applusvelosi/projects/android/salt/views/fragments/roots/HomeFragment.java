package applusvelosi.projects.android.salt.views.fragments.roots;

import java.io.File;
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
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter.StaffLeaveSyncListener;
import applusvelosi.projects.android.salt.views.NewClaimHeaderActivity;
import applusvelosi.projects.android.salt.views.NewLeaveRequestActivity;

public class HomeFragment extends RootFragment implements StaffLeaveSyncListener{
	private static HomeFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefresh, actionbarNewLeaveButton, actionbarNewClaimButton;
	
	private TextView tvCurrDateTime, tvStaffName;
	private SimpleDateFormat homeDateTimeFormat;
	private StaffLeaveTypeCounter leaveCounter;

	private TextView tviewLeavesForApproval, tviewClaimsForApproval, tviewRecruitmentsForApproval, tviewCapexForApproval;

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
		actionbarNewLeaveButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newleaverequest);
		actionbarNewClaimButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newclaim);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Home");

		actionbarMenuButton.setOnClickListener(this);
		actionbarRefresh.setOnClickListener(this);
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

        if(app.getStaff().isUser()){
            view.findViewById(R.id.containers_home_approvals).setVisibility(View.GONE);
        }else{
            tviewLeavesForApproval = (TextView)view.findViewById(R.id.tviews_home_leavesforapproval);
            tviewClaimsForApproval = (TextView)view.findViewById(R.id.tviews_home_claimsforapproval);
            tviewRecruitmentsForApproval = (TextView)view.findViewById(R.id.tviews_home_recruitmentsforapproval);
            tviewCapexForApproval = (TextView)view.findViewById(R.id.tviews_home_capexforapproval);

            tviewLeavesForApproval.setOnClickListener(this);
            tviewClaimsForApproval.setOnClickListener(this);
            tviewRecruitmentsForApproval.setOnClickListener(this);
            tviewCapexForApproval.setOnClickListener(this);
        }


		new Thread(new Runnable() {
			@Override
			public void run() {
				Object tempResult;
				try{
					String path = app.fileManager.getDirForCapturedAttachments()+"8mbfile.pdf";
					System.out.println("SALTX "+path);
					tempResult = app.onlineGateway.uploadAttachment(new File(path), new Document());
				}catch(Exception e){
					e.printStackTrace();
					tempResult = e.getMessage();
				}

				final Object result = tempResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						System.out.println("SALTX result "+result);
					}
				});
			}
		}).start();
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
		}else if(v == tviewLeavesForApproval){
            activity.linkToLeavesForApproval(this);
        }else if(v == tviewClaimsForApproval){
            activity.linkToClaimsForApproval(this);
        }else if(v == tviewRecruitmentsForApproval){
            activity.linkToRecruitmentsForApproval(this);
        }else if(v == tviewCapexForApproval){
            activity.linkToCapexForApproval(this);
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
