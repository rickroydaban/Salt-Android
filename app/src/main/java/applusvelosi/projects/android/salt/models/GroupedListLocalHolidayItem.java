package applusvelosi.projects.android.salt.models;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;

public class GroupedListLocalHolidayItem implements GroupedListItemInterface{
	private LocalHoliday localHoliday;
	
	public GroupedListLocalHolidayItem(LocalHoliday localHoliday){
		this.localHoliday = localHoliday;
	}
	
	@Override
	public View getTextView(Activity a){
		View view = a.getLayoutInflater().inflate(R.layout.cells_holiday_local, null);
		((TextView)view.findViewById(R.id.tviews_cells_localholiday_title)).setText(localHoliday.getName());
		((TextView)view.findViewById(R.id.tviews_cells_localholiday_day)).setText(localHoliday.getDay());
		((TextView)view.findViewById(R.id.tviews_cells_localholiday_date)).setText(localHoliday.getDate());
		
		return view;
	}
	
}
