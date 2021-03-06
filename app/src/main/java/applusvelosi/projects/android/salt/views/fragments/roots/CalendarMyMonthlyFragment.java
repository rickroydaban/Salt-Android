package applusvelosi.projects.android.salt.views.fragments.roots;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.grids.MyCalendarAdapter;
import applusvelosi.projects.android.salt.adapters.lists.SimpleAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.CalendarEvent;
import applusvelosi.projects.android.salt.models.CalendarEvent.CalendarEventDuration;
import applusvelosi.projects.android.salt.models.CalendarItem;
import applusvelosi.projects.android.salt.models.CountryHoliday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.enums.CalendarItemTypes;
import applusvelosi.projects.android.salt.utils.interfaces.CalendarMonthlyInterface;

public class CalendarMyMonthlyFragment extends RootFragment implements OnItemSelectedListener, CalendarMonthlyInterface{
	private static CalendarMyMonthlyFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton;
	private TextView actionbarNowButton, actionbarTitle;
	
	private ImageView propButtonPrev, propButtonNext;
	private Spinner propSpinnerMonth, propSpinnerYear;
	private GridView calendarView;
	private ListView lvHolidayNames;

	private SimpleAdapter adapterEvents;
	private Calendar currDateCalendar, headerSelectionCalendar, minCalendar, maxCalendar;

	private MyCalendarAdapter adapterCalendarGridItem;
	private HashMap<String, ArrayList<CalendarEvent>> propMapDayEvents;
	private ArrayList<CalendarItem> gridCalendarItems;
	private ArrayList<String> eventListItems;
	
	private GestureDetector gestureDetector;
	public static CalendarMyMonthlyFragment getInstance(){
		if(instance == null)
			instance = new CalendarMyMonthlyFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	

	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_staffcalendar, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarNowButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_now);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("My Calendar");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarNowButton.setOnClickListener(this);
		return actionbarLayout;
	}
	
	@Override
	public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar_mycalendar, null);
		propButtonPrev = (ImageView) view.findViewById(R.id.buttons_mycalendar_prevmonth);
		propSpinnerMonth = (Spinner)view.findViewById(R.id.choices_mycalendar_month);
		propSpinnerYear = (Spinner)view.findViewById(R.id.choices_mycalendar_year);
		propButtonNext = (ImageView) view.findViewById(R.id.buttons_mycalendar_nextmonth);
		calendarView = (GridView) view.findViewById(R.id.calendar);
		lvHolidayNames = (ListView)view.findViewById(R.id.lists_mycalendar);

		if(gridCalendarItems == null){
			gridCalendarItems = new ArrayList<CalendarItem>();
			eventListItems = new ArrayList<String>();

			currDateCalendar = Calendar.getInstance();
			headerSelectionCalendar = Calendar.getInstance();
			
			propMapDayEvents = new HashMap<String, ArrayList<CalendarEvent>>();
			adapterCalendarGridItem = new MyCalendarAdapter(this, gridCalendarItems);
			adapterEvents = new SimpleAdapter(activity, eventListItems);
						
			reloadAppHolidays();
			
		}
		
		propSpinnerMonth.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownMonths, NodeSize.SIZE_NORMAL));
		propSpinnerYear.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownYears, NodeSize.SIZE_NORMAL));
		propSpinnerMonth.setSelection(headerSelectionCalendar.get(Calendar.MONTH));
		propSpinnerYear.setSelection(app.dropDownYears.indexOf(String.valueOf(headerSelectionCalendar.get(Calendar.YEAR))));
		calendarView.setAdapter(adapterCalendarGridItem);
		lvHolidayNames.setAdapter(adapterEvents);
		
		propButtonPrev.setOnClickListener(this);
		propButtonNext.setOnClickListener(this);		
		propSpinnerMonth.setOnItemSelectedListener(this);
		propSpinnerYear.setOnItemSelectedListener(this);
		
		return view;
	}
	
	public void reloadEventList(ArrayList<String> eventNames){
		eventListItems.clear();
		eventListItems.addAll(eventNames);
		adapterEvents.notifyDataSetChanged();
	}
	
	private void reloadClassHolidays(){
		eventListItems.clear();
		gridCalendarItems.clear();
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Sun", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Mon", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Tue", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Wed", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Thu", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Fri", null));
		gridCalendarItems.add(new CalendarItem(CalendarItemTypes.TYPE_HEADER, "Sat", null));
	
		Calendar beginningOfThisMonth = Calendar.getInstance();
		beginningOfThisMonth.set(Calendar.YEAR, headerSelectionCalendar.get(Calendar.YEAR));
		beginningOfThisMonth.set(Calendar.MONTH, headerSelectionCalendar.get(Calendar.MONTH));

		int dayofweek = beginningOfThisMonth.get(Calendar.DAY_OF_WEEK);
		dayofweek = (dayofweek == 1)?7:dayofweek-1; //important to include months that start on saturdays
		int offset = (beginningOfThisMonth.get(Calendar.DAY_OF_MONTH)%7) - dayofweek;
		if(offset > 1)
			offset-=7;
		beginningOfThisMonth.set(Calendar.DAY_OF_MONTH, offset); 
			
		boolean trailingDaysAreDaysOfThisMonth = false;
	
		for(int i=0; i<42; i++){
			int dayOfMonth = beginningOfThisMonth.get(Calendar.DAY_OF_MONTH);
			if(dayOfMonth == 1)
				trailingDaysAreDaysOfThisMonth = !trailingDaysAreDaysOfThisMonth;				
		
			CalendarItemTypes itemType;
			ArrayList<CalendarEvent> events;
			if(trailingDaysAreDaysOfThisMonth){
				if(beginningOfThisMonth.get(Calendar.DAY_OF_MONTH)==currDateCalendar.get(Calendar.DAY_OF_MONTH) && beginningOfThisMonth.get(Calendar.MONTH)==currDateCalendar.get(Calendar.MONTH) && beginningOfThisMonth.get(Calendar.YEAR)==currDateCalendar.get(Calendar.YEAR)){
					itemType = CalendarItemTypes.TYPE_NOW;
					events = propMapDayEvents.get(app.dateFormatDefault.format(beginningOfThisMonth.getTime()));
				}else{
					itemType = CalendarItemTypes.TYPE_DAYOFMONTH;
					events = propMapDayEvents.get(app.dateFormatDefault.format(beginningOfThisMonth.getTime()));

					if(!app.getStaff().hasSunday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasMonday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasTuesday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasWednesday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasThursday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasFriday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;
					if(!app.getStaff().hasSaturday() && beginningOfThisMonth.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) itemType = CalendarItemTypes.TYPE_NONWORKINGDAY;				
				}
			}else{
				itemType = CalendarItemTypes.TYPE_OUTMONTH;
				events = null;
			}
	
			gridCalendarItems.add(new CalendarItem(itemType, String.valueOf(dayOfMonth), events));
			beginningOfThisMonth.set(Calendar.DAY_OF_MONTH, beginningOfThisMonth.get(Calendar.DAY_OF_MONTH)+1);
		}
		
		adapterEvents.notifyDataSetChanged();
		adapterCalendarGridItem.notifyDataSetChanged();
	}
	
	void reloadAppHolidays(){
		activity.startLoading();
		new Thread(new Runnable() {
			Object tempHolidayResult, tempLeaveResult;
			
			@Override
			public void run() {
				try{
					tempHolidayResult = app.onlineGateway.getOfficeHolidaysOrErrorMessage(app.getStaff().getOfficeID());					
					tempLeaveResult = app.onlineGateway.getMyLeaves();
				}catch(Exception e){
					tempHolidayResult = e.getMessage();
				}
				
				
				final Object holidayResult = tempHolidayResult;
				final Object leaveResult = tempLeaveResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if(holidayResult instanceof String && !holidayResult.toString().contains(SaltApplication.CONNECTION_ERROR))
							activity.finishLoading(holidayResult.toString());
						else if(leaveResult instanceof String && !holidayResult.toString().contains(SaltApplication.CONNECTION_ERROR))
							activity.finishLoading(leaveResult.toString());
						else{
							ArrayList<CountryHoliday> holidays = new ArrayList<CountryHoliday>();
							ArrayList<Leave> leaves = new ArrayList<Leave>();
							System.out.println("SALTX leaveresult "+leaveResult);
							if(leaveResult==null || leaveResult.toString().contains(SaltApplication.CONNECTION_ERROR) || holidayResult.toString().contains(SaltApplication.CONNECTION_ERROR)){
								activity.finishLoadingAndShowOutdatedData();
								if(holidayResult.toString().contains(SaltApplication.CONNECTION_ERROR))
									holidays.addAll(app.offlineGateway.deserializeMyCalendarHolidays());
								if(leaveResult==null || leaveResult.toString().contains(SaltApplication.CONNECTION_ERROR)) {
                                    ArrayList<Leave> savedLeaves = app.offlineGateway.deserializeMyLeaves();
                                    System.out.println("SALTX added leaves :"+savedLeaves.size());
                                    leaves.addAll(savedLeaves);
                                }
							}else{
								ArrayList<Leave> tempLeaves = (ArrayList<Leave>)leaveResult;
								ArrayList<CountryHoliday> tempHolidays = (ArrayList<CountryHoliday>)holidayResult;
								holidays.addAll(tempHolidays);
								leaves.addAll(tempLeaves);
								app.offlineGateway.serializeMyLeaves(tempLeaves);
								app.offlineGateway.serializeMyCalendarHolidays(tempHolidays);
							}

							activity.finishLoading();
							propMapDayEvents.clear();
							for(CountryHoliday holiday :holidays){
								ArrayList<CalendarEvent> currEvents = (propMapDayEvents.containsKey(holiday.getStringedDate()))?propMapDayEvents.get(holiday.getStringedDate()):new ArrayList<CalendarEvent>();
								currEvents.add(new CalendarEvent(holiday.getName(), activity.getResources().getColor(R.color.holiday), CalendarEventDuration.AllDay, true));
								propMapDayEvents.put(holiday.getStringedDate(), currEvents);
							}								

							try{
								for(Leave leave :leaves){
									if(leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY) {
										Calendar startCalendar = Calendar.getInstance();
										Calendar endCalendar = Calendar.getInstance();
										startCalendar.setTime(app.dateFormatDefault.parse(leave.getStartDate()));
										endCalendar.setTime(app.dateFormatDefault.parse(leave.getEndDate()));

										while (startCalendar.compareTo(endCalendar) <= 0) {
											String currDate = app.dateFormatDefault.format(startCalendar.getTime());
											ArrayList<CalendarEvent> currEvents = (propMapDayEvents.containsKey(currDate)) ? propMapDayEvents.get(currDate) : new ArrayList<CalendarEvent>();
											String desc = leave.getTypeDescription();
											CalendarEventDuration duration;
											if (leave.getDays() == 0.1f)
												duration = CalendarEventDuration.AM;
											else if (leave.getDays() == 0.2f)
												duration = CalendarEventDuration.PM;
											else duration = CalendarEventDuration.AllDay;

											boolean shouldFill = (leave.getStatusID() == Leave.LEAVESTATUSPENDINGID) ? false : true;

											if (desc.equals(Leave.LEAVETYPEVACATIONDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_vacation), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPESICKDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_sick), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEBIRTHDAYDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_birthday), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEUNPAIDDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_unpaid), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEBEREAVEMENTDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_bereavement), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEMATERNITYDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_matpat), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEDOCTORDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_doctor), duration, shouldFill));
											else if (desc.equals(Leave.LEAVETYPEHOSPITALIZATIONDESC))
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_hospitalization), duration, shouldFill));
											else
												currEvents.add(new CalendarEvent(desc, activity.getResources().getColor(R.color.leavetype_businesstrip), duration, shouldFill));

											propMapDayEvents.put(currDate, currEvents);
											startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) + 1);
										}
									}
								}																
							}catch(Exception e){
								e.printStackTrace();
								activity.finishLoading(e.getMessage());
							}
							
						}

						//update calendars
						currDateCalendar = Calendar.getInstance();
						headerSelectionCalendar = Calendar.getInstance();
						minCalendar = Calendar.getInstance();
						minCalendar.set(Calendar.MONTH, Calendar.JANUARY);
						minCalendar.set(Calendar.YEAR, Integer.parseInt(app.dropDownYears.get(0)));
						maxCalendar = Calendar.getInstance();
						maxCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
						maxCalendar.set(Calendar.YEAR, Integer.parseInt(app.dropDownYears.get(app.dropDownYears.size()-1)));
						//automatic selection of date spinners
						int monthSelection = headerSelectionCalendar.get(Calendar.MONTH);
						int yearSelection = app.dropDownYears.indexOf(String.valueOf(headerSelectionCalendar.get(Calendar.YEAR)));
						//setting tags as a flag for automatic selection
						propSpinnerMonth.setTag(monthSelection);
						propSpinnerMonth.setSelection(monthSelection);
						propSpinnerYear.setTag(yearSelection);
						propSpinnerYear.setSelection(yearSelection);
						reloadClassHolidays();
					}
				});
			}
		}).start();
	}	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(parent == calendarView){
			System.out.println("Calendar item selected");
		}else{
			if(parent.getTag()==null || Integer.parseInt(parent.getTag().toString())!=pos){ //selected manually
				parent.setTag(null);
				
				if(parent == propSpinnerMonth) headerSelectionCalendar.set(Calendar.MONTH, propSpinnerMonth.getSelectedItemPosition());
				if(parent == propSpinnerYear) headerSelectionCalendar.set(Calendar.YEAR, Integer.parseInt(propSpinnerYear.getSelectedItem().toString()));
			}
			
			//avoid showing of calendars beyond limit and decrement or increment header calendar according to the pressed button
			propButtonPrev.setVisibility((headerSelectionCalendar.equals(minCalendar))?View.INVISIBLE:View.VISIBLE);
			propButtonNext.setVisibility((headerSelectionCalendar.equals(maxCalendar))?View.INVISIBLE:View.VISIBLE);				
		}
			
		
		reloadClassHolidays();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarRefreshButton){
			reloadAppHolidays();
		}else if(v == actionbarNowButton){
			int monthSelection = currDateCalendar.get(Calendar.MONTH);
			int yearSelection = app.dropDownYears.indexOf(String.valueOf(currDateCalendar.get(Calendar.YEAR)));
			//setting tags as a flag for automatic selection
			propSpinnerMonth.setSelection(monthSelection);
			propSpinnerYear.setSelection(yearSelection);			
		}else if(v==propButtonPrev || v==propButtonNext){
			headerSelectionCalendar.set(Calendar.MONTH, (v == propButtonPrev)?headerSelectionCalendar.get(Calendar.MONTH)-1:headerSelectionCalendar.get(Calendar.MONTH)+1);
			//automatic selection of date spinners
			int monthSelection = headerSelectionCalendar.get(Calendar.MONTH);
			int yearSelection = app.dropDownYears.indexOf(String.valueOf(headerSelectionCalendar.get(Calendar.YEAR)));
			//setting tags as a flag for automatic selection
			propSpinnerMonth.setTag(monthSelection);
			propSpinnerMonth.setSelection(monthSelection);
			propSpinnerYear.setTag(yearSelection);
			propSpinnerYear.setSelection(yearSelection);
		}
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		propButtonPrev.setEnabled(false);
		propSpinnerMonth.setEnabled(false);
		calendarView.setEnabled(false);
		lvHolidayNames.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		propButtonPrev.setEnabled(true);
		propSpinnerMonth.setEnabled(true);
		calendarView.setEnabled(true);
		lvHolidayNames.setEnabled(true);
	}
}

