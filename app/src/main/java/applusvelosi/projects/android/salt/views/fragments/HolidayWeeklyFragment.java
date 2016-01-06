package applusvelosi.projects.android.salt.views.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.CountryHoliday;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public class HolidayWeeklyFragment extends RootFragment implements OnClickListener, ListAdapterInterface{
	private static HolidayWeeklyFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton;
	private TextView actionbarTitle;
	
	private ArrayList<CountryHoliday> holidays;
	private ListView lv;
	private ListAdapter adapter;

	public static HolidayWeeklyFragment getInstance(){
		if(instance == null)
			instance = new HolidayWeeklyFragment();
		
		return instance;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("Weekly Calendar");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		holidays = new ArrayList<CountryHoliday>();
		View view = inflater.inflate(R.layout.fragment_holidays_weekly, null);
		lv = (ListView)view.findViewById(R.id.list_weeklycalendar);
		adapter = new ListAdapter(this);
		lv.setAdapter(adapter);
		sync();

		return view;
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		lv.setEnabled(true);
	}

	private void sync(){
		activity.startLoading();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Object tempResult;
				try{
					tempResult = app.onlineGateway.getWeeklyHolidays();
				}catch(Exception e){
					tempResult = e.getMessage();
					e.printStackTrace();
				}
				
				final Object result = tempResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if(result instanceof String)
							activity.finishLoading(result.toString());
						else{
							if(result != null){
								activity.finishLoading();
								holidays.clear();
								holidays.addAll((ArrayList<CountryHoliday>)result);
								adapter.notifyDataSetChanged();
							}
						}
					}
				});
			}
		}).start();
		
	}
	
	@Override
	public void onClick(View view) {
		if(view == actionbarMenuButton || view == actionbarTitle){
			activity.toggleSidebar(actionbarMenuButton);
		}else if(view == actionbarRefreshButton){
			sync();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		NHCNodeHolder holder;

		if(view == null){
			holder = new NHCNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_calendar_weekly, null);
			holder.flagIV = (ImageView)view.findViewById(R.id.images_node_calendar_home_flag);
			holder.countryTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_country);
			holder.dateTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_date);
			holder.holidayTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_holiday);
			holder.officesLL = (LinearLayout)view.findViewById(R.id.containers_node_calendar_home_offices);

			holder.dateTV.setTypeface(SaltApplication.myFont(activity));

			view.setTag(holder);
		}

		holder = (NHCNodeHolder)view.getTag();
		CountryHoliday holiday = holidays.get(position);
		holder.countryTV.setText(holiday.getCountry());
		holder.dateTV.setText(holiday.getStringedDate());
		holder.holidayTV.setText(holiday.getName());

		try {
			InputStream ims = activity.getAssets().open("flags/"+holiday.getCountry()+".png");
			Drawable d = Drawable.createFromStream(ims, null);
			holder.flagIV.setImageDrawable(d);
		}
		catch(IOException ex) {
			holder.flagIV.setImageDrawable(null);
		}

		holder.officesLL.removeAllViews();
		for(String office: holiday.getOfficeNames()){
			TextView tv = (TextView)activity.getLayoutInflater().inflate(R.layout.tv_calendar_office, null);
			tv.setText("\u2022 "+office);
			tv.setTypeface(SaltApplication.myFont(activity));
			holder.officesLL.addView(tv);
		}

		return view;	}

	@Override
	public int getCount() {
		return holidays.size();
	}

	private class NHCNodeHolder{ //NHC means National CountryHoliday Calendar
		public ImageView flagIV;
		public TextView countryTV, dateTV, holidayTV;
		public LinearLayout officesLL;
	}

}
