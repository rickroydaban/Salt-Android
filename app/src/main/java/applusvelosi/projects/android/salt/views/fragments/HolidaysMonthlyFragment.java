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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.CalendarListAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;

public class HolidaysMonthlyFragment extends HomeActionbarFragment implements RootFragment, OnItemSelectedListener{
	
	private static HolidaysMonthlyFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton;
	private ImageView propButtonPrev, propButtonNext;
	private Spinner propSpinnerYear, propSpinnerMonth;
	private ListView lv;
	
	private CalendarListAdapter propAdapterHolidays;
	private SimpleDateFormat dateTitleFormat;
	private ArrayList<Holiday> holidaysOfTheMonth;
	private SaltProgressDialog pd;
	private int currMonth, currYear;
	
	public static HolidaysMonthlyFragment getInstance(){
		if(instance == null)
			instance = new HolidaysMonthlyFragment();
		
		return instance;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Monthly Holidays");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_holidays_monthly, null);
		propButtonPrev = (ImageView)v.findViewById(R.id.buttons_monthlycalendar_prev);
		propButtonNext = (ImageView)v.findViewById(R.id.buttons_monthlycalendar_next);
		propSpinnerMonth = (Spinner)v.findViewById(R.id.choices_calendar_monthly_month);
		propSpinnerYear = (Spinner)v.findViewById(R.id.choices_calendar_monthly_year);
		lv = (ListView)v.findViewById(R.id.lists_calendarmonthly);
		
		propSpinnerMonth.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownMonths, NodeSize.SIZE_NORMAL));
		propSpinnerYear.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownYears, NodeSize.SIZE_NORMAL));
		dateTitleFormat = new SimpleDateFormat("M-yyyy", Locale.ENGLISH);
		String [] currDate = dateTitleFormat.format(new Date()).split("-");
		currMonth = Integer.parseInt(currDate[0]);
		currYear = Integer.parseInt(currDate[1]);

		if(String.valueOf(currYear).equals(app.dropDownYears.get(0))) //check if minimum year
			propButtonPrev.setEnabled(false);
		if(String.valueOf(currYear).equals(app.dropDownYears.get(app.dropDownYears.size()-1))) //check if maximum year
			propButtonNext.setEnabled(false);

		int monthSelection = currMonth-1;
		int yearSelection = app.dropDownYears.indexOf(String.valueOf(currYear));
		propSpinnerMonth.setTag(monthSelection);
		propSpinnerYear.setTag(yearSelection);
		propSpinnerMonth.setSelection(monthSelection);
		propSpinnerYear.setSelection(yearSelection);

		holidaysOfTheMonth = new ArrayList<Holiday>();
		
		propAdapterHolidays = new CalendarListAdapter(activity, holidaysOfTheMonth);
		lv.setAdapter(propAdapterHolidays);
		
		if(!app.hasLoadedMonthlyHolidays())
			reloadAppHolidays();
		else reloadLocalHolidays();
		
		propButtonPrev.setOnClickListener(this);
		propButtonNext.setOnClickListener(this);
		propSpinnerMonth.setOnItemSelectedListener(this);
		propSpinnerYear.setOnItemSelectedListener(this);
				
		return v;
	}
		
	private void reloadAppHolidays(){
		if(pd == null)
			pd = new SaltProgressDialog(activity);
		pd.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Object tempResult;
				try{
					tempResult = app.onlineGateway.getAllHolidayArrayListOrErrorMessage(true);					
				}catch(Exception e){
					e.printStackTrace();
					tempResult = e.getMessage();
				}
				final Object result = tempResult;
						
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if(!(result instanceof String)){
							app.updateNationalHolidays((ArrayList<Holiday>)result);
							app.setMonthlyHolidaysLoaded(HolidaysMonthlyFragment.this);
							reloadLocalHolidays();
						}else
							app.showMessageDialog(activity, result.toString());
						
						pd.dismiss();
					}
				});
			}
		}).start();		
	}
		
	private void reloadLocalHolidays(){
		holidaysOfTheMonth.clear();
		for(Holiday holiday:app.getNationalHolidays()){
			String [] holidayDate = holiday.getStringedDate().split("-");
			String holidayDateYear = holidayDate[2];
			String holidayDateMonth = String.valueOf(getMonthOrdinalByAbbr(holidayDate[1]));
//			if(holidayDateYear.equals(String.valueOf(currYear)) && holidayDateMonth.equals(String.valueOf(currMonth)) && holiday.getOfficeIDs().contains(app.getStaff().getOfficeID()))
			if(holidayDateYear.equals(String.valueOf(currYear)) && holidayDateMonth.equals(String.valueOf(currMonth)))
				holidaysOfTheMonth.add(holiday);
		}
		
		propAdapterHolidays.notifyDataSetChanged();
	}
	
	public int getMonthOrdinalByAbbr(String abbr){
		if(abbr.equals("Jan")) return 1;
		else if(abbr.equals("Feb")) return 2;
		else if(abbr.equals("Mar")) return 3;
		else if(abbr.equals("Apr")) return 4;
		else if(abbr.equals("May")) return 5;
		else if(abbr.equals("Jun")) return 6;
		else if(abbr.equals("Jul")) return 7;
		else if(abbr.equals("Aug")) return 8;
		else if(abbr.equals("Sep")) return 9;
		else if(abbr.equals("Oct")) return 10;
		else if(abbr.equals("Nov")) return 11;
		else return 12;
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarRefreshButton){
			reloadAppHolidays();
		}else if(v == propButtonPrev){
			if(currMonth == 1){
				if(String.valueOf(currYear).equals(app.dropDownYears.get(0))) //check if minimum year
					propButtonPrev.setEnabled(false);
				else{
					currYear--;
					currMonth = 12;					
				}
			}else
				currMonth--;
			
			int monthSelection = currMonth-1;
			int yearSelection = app.dropDownYears.indexOf(String.valueOf(currYear));
			propSpinnerMonth.setSelection(monthSelection);
			propSpinnerMonth.setTag(monthSelection);
			propSpinnerYear.setSelection(yearSelection);
			propSpinnerYear.setTag(yearSelection);
			propButtonNext.setEnabled(true);
		}else if(v == propButtonNext){
			if(currMonth == 12){
				if(String.valueOf(currYear).equals(app.dropDownYears.get(app.dropDownYears.size()-1))) //check if maximum year
					propButtonNext.setEnabled(false);
				else{
					currYear++;
					currMonth = 1;
				}
			}else
				currMonth++;

			int monthSelection = currMonth-1;
			int yearSelection = app.dropDownYears.indexOf(String.valueOf(currYear));
			propSpinnerMonth.setSelection(monthSelection);
			propSpinnerMonth.setTag(monthSelection);
			propSpinnerYear.setSelection(yearSelection);
			propSpinnerYear.setTag(yearSelection);
			propButtonPrev.setEnabled(true);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(parent.getTag()!=null || (parent.getTag()!=null&&Integer.parseInt(parent.getTag().toString())!=pos)){
			if(parent == propSpinnerMonth)
				currMonth = pos+1;
			else if(parent == propSpinnerYear)
				currYear = Integer.parseInt(app.dropDownYears.get(pos));
		}
		
		reloadLocalHolidays();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void enableListButtonOnSidebarAnimationFinished() {
		actionbarMenuButton.setEnabled(true);
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		propButtonPrev.setEnabled(false);
		propSpinnerMonth.setEnabled(false);
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		try{
			propButtonPrev.setEnabled(true);
			propSpinnerMonth.setEnabled(true);
			lv.setEnabled(true);			
		}catch(NullPointerException e){
			System.out.println("Null pointer exception at CalendarMyCalendarFragment enableUserInteractionOnSidebarHidden()");
		}
	}
}