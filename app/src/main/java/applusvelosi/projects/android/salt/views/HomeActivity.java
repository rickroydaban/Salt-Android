package applusvelosi.projects.android.salt.views;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.lists.GroupedListAdapter;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.GroupedListHeader;
import applusvelosi.projects.android.salt.models.GroupedListSidebarItem;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.HolidaysLocalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.HolidaysMonthlyFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.CalendarMyMonthlyFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.ClaimListFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.ClaimforApprovalListFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.HomeFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveListFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.CapexesForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.MainFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RecruitmentsForApprovalFragment;

public class HomeActivity extends FragmentActivity implements AnimationListener, OnItemClickListener{
	//constants used for sidebar item labels
	private final String SIDEBARITEM_HOME = "Home";
	private final String SIDEBARITEM_LOGOUT = "Logout";
	private final String SIDEBARITEM_MYLEAVES = "My Leaves";
	private final String SIDEBARITEM_MYCLAIMS = "My Claims";
	private final String SIDEBARITEM_HOLIDAYSMONTHLY = "Holiday this Month";
	private final String SIDEBARITEM_HOLIDAYSLOCAL = "Local Holidays";
	private final String SIDEBARITEM_MYCALENDAR = "My Calendar";

	private final String SIDEBARITEM_LEAVESFORAPPROVAL = "Leaves";
	private final String SIDEBARITEM_CLAIMSFORAPPROVAL = "Claims";
	private final String SIDEBARITEM_CAPEXESFORAPPROVAL = "Capex";
	private final String SIDEBARITEM_RECRUITMENTSFORAPPROVAL = "Recruitments";

	//sidebar manageability constants
	private float maxSidebarShownWidth = 0;
	private final int TOGGLE_SPEED = 1000;

    private SaltApplication app;

    //views
	private ListView menuList;
	private ArrayList<GroupedListItemInterface> sidebarItems; //tells the adapter which item is to be displayed as a header or an item in the actionbar items
	private RelativeLayout foreFragment, foreFragmentShadow; //the main display whose display corresponds to the selected item in the sidebar
	private TranslateAnimation animationShowSidebar, animationHideSidebar;
	private MainFragment mainFragment;
	//holders
	private RootFragment toBeShownFragment;
	private GroupedListSidebarItem lastMenuItemClicked;

	//flags for sidebar visibility
	private final String SIDEBAR_HIDDEN = "sidebar_hidden";
	private final String SIDEBAR_SHOWN = "sidebar_shown";

	private View menuButton;
    private int loaderCnt = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_home);

        app = (SaltApplication)getApplication();
		foreFragment = (RelativeLayout)findViewById(R.id.containers_activities_home_fore);
		foreFragmentShadow = (RelativeLayout)findViewById(R.id.containers_activities_home_foreshadow);
		mainFragment = new MainFragment();
		setupSidebar();
		getSupportFragmentManager().beginTransaction().replace(foreFragment.getId(), mainFragment).commit();
		initAnimations();

        if(app.getCurrencies() == null) { //get currencies from background if it is null
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object tempResult;
                    try {
                        tempResult = app.onlineGateway.getCurrencies();
                    } catch (Exception e) {
                        e.printStackTrace();
                        tempResult = e.getMessage();
                    }

                    final Object result = tempResult;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(result instanceof String)
                                Toast.makeText(HomeActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                            else
                                app.offlineGateway.serializeCurrencies((ArrayList<Currency>)result);
                        }
                    });
                }
            }).start();
        }
	}
	
	//sidebar
	private void setupSidebar(){
		menuList = (ListView)findViewById(R.id.lists_sidebar);
		sidebarItems = new ArrayList<GroupedListItemInterface>();
		sidebarItems.add(new GroupedListHeader("My Account"));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_HOME, getResources().getDrawable(R.drawable.icon_home), getResources().getDrawable(R.drawable.icon_home_sel)));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_MYLEAVES, getResources().getDrawable(R.drawable.icon_leaves), getResources().getDrawable(R.drawable.icon_leaves_sel)));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_MYCLAIMS, getResources().getDrawable(R.drawable.icon_myclaims), getResources().getDrawable(R.drawable.icon_myclaims_sel)));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_MYCALENDAR, getResources().getDrawable(R.drawable.icon_mycalendar), getResources().getDrawable(R.drawable.icon_mycalendar_sel)));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_LOGOUT, getResources().getDrawable(R.drawable.icon_logout),getResources().getDrawable(R.drawable.icon_logout_sel)));

		Staff staff = ((SaltApplication)getApplication()).getStaff();
		if(staff.isAdmin() || staff.isCM() || staff.isAM()){
			sidebarItems.add(new GroupedListHeader("FOR APPROVAL"));
			sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_LEAVESFORAPPROVAL, getResources().getDrawable(R.drawable.icon_leavesforapproval),getResources().getDrawable(R.drawable.icon_leavesforapproval_sel)));
			sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_CLAIMSFORAPPROVAL, getResources().getDrawable(R.drawable.icon_claimforapproval),getResources().getDrawable(R.drawable.icon_claimforapproval_sel)));
			sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_CAPEXESFORAPPROVAL, getResources().getDrawable(R.drawable.icon_capexforapproval), getResources().getDrawable(R.drawable.icon_capexforapproval_sel)));
			sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_RECRUITMENTSFORAPPROVAL, getResources().getDrawable(R.drawable.icon_recruitmentforapproval),getResources().getDrawable(R.drawable.icon_recruitmentforapproval_sel)));
		}

		sidebarItems.add(new GroupedListHeader("HOLIDAYS"));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_HOLIDAYSMONTHLY, getResources().getDrawable(R.drawable.icon_monthlycalendar), getResources().getDrawable(R.drawable.icon_monthlycalendar_sel)));
		sidebarItems.add(new GroupedListSidebarItem(SIDEBARITEM_HOLIDAYSLOCAL, getResources().getDrawable(R.drawable.icon_localholidays), getResources().getDrawable(R.drawable.icon_localholidays_sel)));

		menuList.setAdapter(new GroupedListAdapter(this, sidebarItems));
        menuList.post(new Runnable() {
            @Override
            public void run() {
                selectMenu(1);
            }
        });
	}

	//needs to call this so that we can have proper position of our forefragment's shadow when sidebar list is shown
	public void updateMaxSidebarShowWidth(float width){
		if(width>maxSidebarShownWidth)
			maxSidebarShownWidth = width;
	}


	private void hideSidebar(){
		foreFragment.startAnimation(animationHideSidebar);
		mainFragment.getCurrRootFragment().disableUserInteractionsOnSidebarShown();
		menuList.setOnItemClickListener(null);
	}
	
	public void toggleSidebar(View menuButton){
		this.menuButton = menuButton;
		if(foreFragment.getTag().toString().equals(SIDEBAR_SHOWN)){
			hideSidebar();
		}else{
			foreFragment.startAnimation(animationShowSidebar);
			mainFragment.getCurrRootFragment().disableUserInteractionsOnSidebarShown();
			menuList.setOnItemClickListener(this);
		}
	}

	public void changeChildPage(RootFragment fragment){
		mainFragment.changePage(fragment);
	}

	public void setupActionbar(RelativeLayout actionbar){
		mainFragment.setupActionbar(actionbar);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		foreFragment.setTag((animation == animationShowSidebar) ? SIDEBAR_SHOWN : SIDEBAR_HIDDEN);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		foreFragment.setX((animation == animationShowSidebar) ? maxSidebarShownWidth : 0);
		foreFragmentShadow.setX(maxSidebarShownWidth - 10);

		mainFragment.getCurrRootFragment().enableUserInteractionsOnSidebarHidden();
		
		if(animation == animationHideSidebar)
			mainFragment.changePage(toBeShownFragment);
		
		menuButton.setEnabled(true);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		selectMenu(pos);

        if(foreFragment.getTag().equals(SIDEBAR_SHOWN))
    		hideSidebar();
	}	
	
	private void selectMenu(int pos){
		GroupedListItemInterface sidebarItem = sidebarItems.get(pos);
		if(sidebarItem instanceof GroupedListSidebarItem){ //prevents actions when clicking sidebar headers
			if(lastMenuItemClicked != null)
				lastMenuItemClicked.displayAsNormalItem();
			
			((GroupedListSidebarItem) sidebarItem).displayAsSelectedItem();
			lastMenuItemClicked = (GroupedListSidebarItem)sidebarItem;
			
			if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_HOME)) toBeShownFragment = HomeFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_LOGOUT)) logout();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_MYLEAVES)) toBeShownFragment = LeaveListFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_MYCLAIMS)) toBeShownFragment = ClaimListFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_HOLIDAYSMONTHLY)) toBeShownFragment = HolidaysMonthlyFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_HOLIDAYSLOCAL)) toBeShownFragment = HolidaysLocalFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_MYCALENDAR)) toBeShownFragment = CalendarMyMonthlyFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_LEAVESFORAPPROVAL)) toBeShownFragment = LeaveForApprovalFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_CLAIMSFORAPPROVAL)) toBeShownFragment = ClaimforApprovalListFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_CAPEXESFORAPPROVAL)) toBeShownFragment = CapexesForApprovalFragment.getInstance();
			else if(((GroupedListSidebarItem) sidebarItem).getLabel().toString().equals(SIDEBARITEM_RECRUITMENTSFORAPPROVAL)) toBeShownFragment = RecruitmentsForApprovalFragment.getInstance();
		}
	}

	private void logout(){

		HomeFragment.removeInstance();
		LeaveListFragment.removeInstance();
		HolidaysLocalFragment.removeInstance();
		CalendarMyMonthlyFragment.removeInstance();
		ClaimListFragment.removeInstance();
		ClaimforApprovalListFragment.removeInstance();

		((SaltApplication)getApplication()).offlineGateway.logout();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	public void linkToLeavesForApproval(HomeFragment key){
		selectMenu(3);
		changeChildPage(LeaveForApprovalFragment.getInstance());
	}

    private void initAnimations(){
		animationShowSidebar = new TranslateAnimation(0, maxSidebarShownWidth, 0, 0);
		animationShowSidebar.setDuration(TOGGLE_SPEED);
		animationShowSidebar.setFillEnabled(true);
		animationShowSidebar.setAnimationListener(this);
		animationHideSidebar = new TranslateAnimation(0, -maxSidebarShownWidth, 0, 0);
		animationHideSidebar.setDuration(TOGGLE_SPEED);
		animationHideSidebar.setFillEnabled(true);
		animationHideSidebar.setAnimationListener(this);
		foreFragment.setTag(SIDEBAR_HIDDEN);
	}

    public void startLoading(){
        if(loaderCnt == 0 ){
            mainFragment.containersLoader.setVisibility(View.VISIBLE);
            mainFragment.containersLoader.startAnimation(app.animationShow);
            mainFragment.ivLoader.setVisibility(View.VISIBLE);
            mainFragment.tviewsLoader.setText("Loading");
            mainFragment.tviewsLoader.setTextColor(getResources().getColor(R.color.black));
        }

        loaderCnt++;
    }

    public void finishLoading(){
        loaderCnt--;
        if(loaderCnt == 0){
            mainFragment.containersLoader.setVisibility(View.GONE);
            mainFragment.containersLoader.startAnimation(app.animationHide);
        }
	}

    public void finishLoading(String error){
        loaderCnt--;
        if(loaderCnt == 0) {
            mainFragment.ivLoader.setVisibility(View.GONE);
            if (error.contains("No address associated with hostname"))
                error = "Server Connection Failed";
            mainFragment.tviewsLoader.setText(error);
            mainFragment.tviewsLoader.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void onBackPressed() {
        if(mainFragment.containersLoader.getVisibility() == View.GONE)
            new AlertDialog.Builder(this).setMessage("Are you sure you want to exit SALT?")
                .setPositiveButton("Yes", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HomeActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .create().show();
    }

}
