package applusvelosi.projects.android.salt.views.fragments;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.CalendarListAdapter;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;

public class CalendarWeeklyFragment extends HomeActionbarFragment implements OnClickListener{
	private static CalendarWeeklyFragment instance;
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarRefreshButton;
	private TextView actionbarTitle;
	
	private ArrayList<Holiday> holidays;
	private ListView lv;
	private CalendarListAdapter adapter;
	private ProgressDialog pd;
	
	public static CalendarWeeklyFragment getInstance(){
		if(instance == null)
			instance = new CalendarWeeklyFragment();
		
		return instance;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backrefresh, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("Weekly Calendar");
		
		actionbarBackButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_holidays_weekly, null);
		lv = (ListView)view.findViewById(R.id.list_weeklycalendar);
		pd = new SaltProgressDialog(getActivity());
		if(holidays == null){
			holidays = new ArrayList<Holiday>();
			sync();
		}
		adapter = new CalendarListAdapter(activity, holidays);
		lv.setAdapter(adapter);
		
	
		
		return view;
	}
		
	private void sync(){
		pd.show();
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
							app.showMessageDialog(getActivity(), result.toString());
						else{
							if(result != null){
								holidays.clear();
								holidays.addAll((ArrayList<Holiday>)result);
								adapter.notifyDataSetChanged();
							}else
								app.showMessageDialog(getActivity(), "Error");
						}
							
						pd.dismiss();								
					}
				});
			}
		}).start();
		
	}
	
	@Override
	public void onClick(View view) {
		if(view == actionbarBackButton || view == actionbarTitle){
			activity.onBackPressed();
		}else if(view == actionbarRefreshButton){
			sync();
		}
	}

}
