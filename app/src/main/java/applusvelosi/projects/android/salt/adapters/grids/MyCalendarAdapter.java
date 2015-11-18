package applusvelosi.projects.android.salt.adapters.grids;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.CalendarEvent;
import applusvelosi.projects.android.salt.models.CalendarItem;
import applusvelosi.projects.android.salt.utils.customviews.SquareGridItemView;
import applusvelosi.projects.android.salt.utils.enums.CalendarItemTypes;
import applusvelosi.projects.android.salt.utils.interfaces.CalendarMonthlyInterface;

public class MyCalendarAdapter  extends BaseAdapter implements OnClickListener{
	//calendar node background classification
	public static final String TYPE_HEADER = "header";
	public static final String TYPE_TODAY = "today"; 
	public static final String TYPE_REGULARDAY = "regularday";
	public static final String TYPE_NONWORKINGDAY = "nonworkingdays";
	public static final String TYPE_OUTMONTH = "outmonth";
	
	private CalendarMonthlyInterface calendarGrid;
	private ArrayList<CalendarItem> calendarItems;
	private View prevClickedRow;
	
	// Days in Current Month
	public MyCalendarAdapter(CalendarMonthlyInterface calendarGrid, ArrayList<CalendarItem> calendarItems) {
		this.calendarGrid = calendarGrid;
		this.calendarItems = calendarItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarItem calendarItem = calendarItems.get(position);
        SquareGridItemView row;
        TextView gridcell;

		if(calendarItem.getItemType() == CalendarItemTypes.TYPE_HEADER){
            row = (SquareGridItemView)calendarGrid.getActivity().getLayoutInflater().inflate(R.layout.calendarcell_monthly_header, parent, false);
            gridcell = (TextView)row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setTextColor(Color.parseColor("#FFFFFF"));
			gridcell.setBackgroundColor(calendarGrid.getActivity().getResources().getColor(R.color.black));
		}else{
			row = (SquareGridItemView)calendarGrid.getActivity().getLayoutInflater().inflate(R.layout.calendarcell_monthly_item, parent, false);
			gridcell = (TextView)row.findViewById(R.id.calendar_day_gridcell);

			if(calendarItem.getEvents()!=null && calendarItem.getEvents().size()>0){
				LinearLayout indicatorContainer = (LinearLayout)row.findViewById(R.id.container_mycalendar_cells_events);
				for(int i=0; i<calendarItem.getEvents().size(); i++){
					CalendarEvent evt = calendarItem.getEvents().get(i);
					View indicator = calendarGrid.getActivity().getLayoutInflater().inflate(R.layout.indicator_nonworkingday, null);
					switch(evt.getDuration()){
						case AM: 
							indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am).setBackgroundColor(calendarItem.getEvents().get(i).getColor()); 
							if(!evt.shouldFill()) indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am_sub).setBackgroundColor(Color.WHITE);
							break;
						case PM: 
							indicator.findViewById(R.id.cells_mycalendar_nonworkingday_pm).setBackgroundColor(calendarItem.getEvents().get(i).getColor()); 
							if(!evt.shouldFill()) indicator.findViewById(R.id.cells_mycalendar_nonworkingday_pm_sub).setBackgroundColor(Color.WHITE);
							break;
						case AllDay: 
//								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am).setBackgroundColor(calendarItem.getEvents().get(i).getColor());
//								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_pm).setBackgroundColor(calendarItem.getEvents().get(i).getColor());
							if(!evt.shouldFill()){ 
								((LinearLayout)indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am).getParent()).setBackgroundColor(evt.getColor());
								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am).setBackgroundColor(Color.WHITE);
								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_pm).setBackgroundColor(Color.WHITE);
							}else{
								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_am).setBackgroundColor(evt.getColor());
								indicator.findViewById(R.id.cells_mycalendar_nonworkingday_pm).setBackgroundColor(evt.getColor());									
							}
							break;
						}
					indicatorContainer.addView(indicator);
				}
			}
			
			if(calendarItem.getItemType() == CalendarItemTypes.TYPE_NOW)
				gridcell.setTextColor(calendarGrid.getActivity().getResources().getColor(R.color.orange_velosi));
			else if(calendarItem.getItemType() == CalendarItemTypes.TYPE_OUTMONTH || calendarItem.getItemType() == CalendarItemTypes.TYPE_NONWORKINGDAY)
				gridcell.setTextColor(Color.parseColor("#ABABAB"));
					
			gridcell.setTag(position);
			gridcell.setOnClickListener(this);
		}
		
		gridcell.setText(calendarItem.getLabel());
		return row;
	}

	@Override
	public int getCount() {
		return calendarItems.size();
	}

	public Object getItem(int position) {
		return calendarItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onClick(View v) {
		ArrayList<String> eventLabels = new ArrayList<String>();
		ArrayList<CalendarEvent> calendarEvents = calendarItems.get(Integer.parseInt(v.getTag().toString())).getEvents();
		if(calendarEvents != null){
			for(CalendarEvent event :calendarEvents)
				eventLabels.add(event.getName());
		}
		
		if(prevClickedRow != null)
			prevClickedRow.setBackgroundResource(R.drawable.bg_input_blur);
		
		v.setBackgroundResource(R.drawable.bg_input_focus);
		prevClickedRow = v;
		
		calendarGrid.reloadEventList(eventLabels);
	}

}