package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.CountryHoliday;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class CalendarListAdapter extends BaseAdapter{
	HomeActivity activity;
	ArrayList<CountryHoliday> holidays;
	
	public CalendarListAdapter(HomeActivity activity, ArrayList<CountryHoliday> holidays){
		this.activity = activity;
		this.holidays = holidays;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		NHCNodeHolder holder;
		
		if(view == null){
			holder = new NHCNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_calendar_weekly, null);
//			holder.flagIV = (ImageView)view.findViewById(R.id.images_node_calendar_home_flag);
			holder.countryTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_country);
			holder.dateTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_date);
			holder.holidayTV = (TextView)view.findViewById(R.id.tviews_node_calendar_home_holiday);
			holder.officesLL = (LinearLayout)view.findViewById(R.id.containers_node_calendar_home_offices);

			holder.dateTV.setTypeface(SaltApplication.myFont(activity));
			
			view.setTag(holder);
		}
		
		holder = (NHCNodeHolder)view.getTag();
		CountryHoliday holiday = holidays.get(pos);
//		holder.flagIV.setImageBitmap(holiday.getBitmap());
		holder.countryTV.setText(holiday.getCountry());
		holder.dateTV.setText(holiday.getStringedDate());
		holder.holidayTV.setText(holiday.getName());
		
		holder.officesLL.removeAllViews();
		for(String office: holiday.getOfficeNames()){
			TextView tv = (TextView)activity.getLayoutInflater().inflate(R.layout.tv_calendar_office, null);			
			tv.setText("\u2022 "+office);
			tv.setTypeface(SaltApplication.myFont(activity));
			holder.officesLL.addView(tv);
		}
		
		return view;
	}

	@Override
	public int getCount() {
		return holidays.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return holidays.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class NHCNodeHolder{ //NHC means National CountryHoliday Calendar
//		public ImageView flagIV;
		public TextView countryTV, dateTV, holidayTV;
		public LinearLayout officesLL;
	}
}
