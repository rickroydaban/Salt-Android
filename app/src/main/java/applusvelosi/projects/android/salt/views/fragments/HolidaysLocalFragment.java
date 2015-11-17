package applusvelosi.projects.android.salt.views.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.GroupedListAdapter;
import applusvelosi.projects.android.salt.models.GroupedListHeader;
import applusvelosi.projects.android.salt.models.GroupedListLocalHolidayItem;
import applusvelosi.projects.android.salt.models.LocalHoliday;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;

public class HolidaysLocalFragment extends HomeActionbarFragment implements RootFragment{
	private static HolidaysLocalFragment instance;
	//action bar buttons
	private RelativeLayout actionbarLayout, actionbarMenuButton, actionbarRefreshButton;

//	private ArrayList<LocalHoliday> localHolidays; //for saving local holidays in shared preferences
	private ArrayList<GroupedListItemInterface> items;
	private SaltProgressDialog pd;
	private ListView lv;
	private GroupedListAdapter adapter;
	private String currMonthName;

	public static HolidaysLocalFragment getInstance(){
		if(instance == null)
			instance = new HolidaysLocalFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	
	@Override
	protected RelativeLayout setupActionbar() {
		actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Local Holidays");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		SimpleDateFormat formatMonthName = new SimpleDateFormat("LLLL", Locale.UK);
		currMonthName = formatMonthName.format(new Date());

		View view = inflater.inflate(R.layout.fragment_listview, null);
		lv = (ListView)view.findViewById(R.id.lists_lv);

		items = new ArrayList<GroupedListItemInterface>();
		adapter = new GroupedListAdapter(activity, items);
		
		lv.setAdapter(adapter);
		
		if(!app.hasLoadedLocalHolidays())
			reloadLocalHolidays();	
		else
			updateView();
		
		return view;
	}
	
	private void reloadLocalHolidays(){
		if(pd == null)
			pd = new SaltProgressDialog(activity);
		
		pd.show();
		new Thread(new Runnable() {
			Object tempResult;
			
			@Override
			public void run() {
				try{
					tempResult = app.onlineGateway.getLocalHolidayArrayListOrErrorMessage();						
				}catch(Exception e){
					tempResult = e.getMessage();
				}
				final Object result = tempResult;
				
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {							
						if(result instanceof String)
							app.showMessageDialog(activity, result.toString());
						else{
							app.updateLocalHolidays((ArrayList<LocalHoliday>)result);
							app.setLocalHolidaysLoaded(HolidaysLocalFragment.this);
						}
						
						updateView();
						pd.dismiss();							
					}
				});
			}
		}).start();
	}
	
	private void updateView(){
		String header = "";
		this.items.clear();
		int scrollablePos = 0;
		int addedItemCount = 0;
		
		for(LocalHoliday localHoliday :app.getLocalHolidays()){
			if(!localHoliday.getMonth().equals(header)){
				header = localHoliday.getMonth();

				items.add(new GroupedListHeader(localHoliday.getMonth()));
				items.add(new GroupedListLocalHolidayItem(localHoliday));
				addedItemCount=addedItemCount+2;

				if(header.equals(currMonthName))
					scrollablePos = addedItemCount;

			}else{
				items.add(new GroupedListLocalHolidayItem(localHoliday));
				addedItemCount++;
			}
		}
		
		adapter.notifyDataSetChanged();
		lv.smoothScrollToPosition(scrollablePos);
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarRefreshButton){
			reloadLocalHolidays();
		}
	}

	@Override
	public void enableListButtonOnSidebarAnimationFinished() {
		actionbarMenuButton.setEnabled(true);
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		try{
			lv.setEnabled(true);			
		}catch(NullPointerException e){
			System.out.println("Null pointer exception at CalendarMyCalendarFragment enableUserInteractionOnSidebarHidden()");
		}
	}

}
