package applusvelosi.projects.android.salt.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter.StaffLeaveSyncListener;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.CustomViewPager;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimInputFragment;
import applusvelosi.projects.android.salt.views.fragments.homepages.OverviewAdvancesFragment;
import applusvelosi.projects.android.salt.views.fragments.homepages.OverviewClaimsFragment;
import applusvelosi.projects.android.salt.views.fragments.homepages.OverviewLeavesFragment;
import applusvelosi.projects.android.salt.views.fragments.leaves.LeaveInputType;

public class HomeFragment_Backup extends ActionbarFragment implements RootFragment, OnPageChangeListener, StaffLeaveSyncListener{
	private static HomeFragment_Backup instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefresh, actionbarCalendarButton, actionbarNewLeaveButton, actionbarNewClaimButton;

	private CustomViewPager pager;
	private TextView pagerHeaderTV, tvCurrDateTime, tvStaffName;
	private SaltProgressDialog pd;

	private String [] headers = {"LEAVES", "CLAIMS", "BUSINESS ADVANCES"};
//	private int previoslySelectedPage = 0;
	private SimpleDateFormat homeDateTimeFormat;
	private StaffLeaveTypeCounter leaveCounter;

	public static HomeFragment_Backup getInstance(){
		if(instance == null)
			instance = new HomeFragment_Backup();

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
//		TODO
		actionbarNewClaimButton.setVisibility(View.GONE);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		actionbarMenuButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		activity.updateMaxSidebarShowWidth(displaymetrics.widthPixels - actionbarMenuButton.getMeasuredWidth() - 100);

		return actionbarLayout;
	}

	@Override
	public View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_home_temp, null);

		pd = new SaltProgressDialog(activity);
		tvStaffName = (TextView)view.findViewById(R.id.tviews_home_name);
		leaveCounter = app.staffLeaveCounter;
		pagerHeaderTV = (TextView)view.findViewById(R.id.tviews_home_pagertitle);
//		indicatorContainer = (LinearLayout)view.findViewById(R.id.containers_home_pageindicator);

		pager = (CustomViewPager)view.findViewById(R.id.home_pager);
		pager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(1);
		pager.setCurrentItem(0);

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
	public void onResume() {
		super.onResume();
//		refresh();
	}
	
	public void refresh(){
		pd.show();
		leaveCounter.syncToServer(this);
	}

	
	
	private class HomePagerAdapter extends FragmentPagerAdapter{

		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
			case 0:
				return OverviewLeavesFragment.getInstance();
				
			case 1:
				return OverviewClaimsFragment.getInstance();
				
			case 2:
				return OverviewAdvancesFragment.getInstance();
			}
			
			return null;
		}
		
		@Override
		public int getCount() {
			return 1;
		}	
		
	}
	
	//important to avoid illegalStateException when dealing with viewpager on fragment
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int page) {
//		pagerHeaderTV.setText(headers[page]);
//
//		if(previoslySelectedPage >= 0)
//			((ImageView)indicatorContainer.getChildAt(previoslySelectedPage)).setImageResource(R.drawable.bg_pageindicator_blur);
//
//		((ImageView)indicatorContainer.getChildAt(page)).setImageResource(R.drawable.bg_pageindicator_focus);
//		previoslySelectedPage = page;
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);	
		}else if(v == actionbarRefresh){
			refresh();
		}else if(v == actionbarNewLeaveButton){
			activity.changeChildPage(new LeaveInputType());
//			activity.changeChildPage(new LeaveInputFragment());
		}else if(v == actionbarNewClaimButton){
			activity.changeChildPage(new ClaimInputFragment());
		}else if(v == actionbarCalendarButton){
			activity.changeChildPage(CalendarWeeklyFragment.getInstance());
		}
	}

	@Override
	public void enableListButtonOnSidebarAnimationFinished() {
		actionbarMenuButton.setEnabled(true);
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		pager.setPagingEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		if(pager!=null) //enable if and only if usable
			pager.setPagingEnabled(true);
	}

	@Override
	public void onSyncSuccess() {
		pd.dismiss();
		tvStaffName.setText(app.getStaff().getLname()+", "+app.getStaff().getFname());
	}

	@Override
	public void onSyncFailed(String errorMessage) {
		pd.dismiss();
		app.showMessageDialog(activity, errorMessage);
	}		
}
