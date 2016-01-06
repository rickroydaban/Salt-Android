package applusvelosi.projects.android.salt.views.fragments.roots;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.lists.MyLeavesAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.views.LeaveDetailActivity;
import applusvelosi.projects.android.salt.views.NewLeaveRequestActivity;

public class LeaveListFragment extends RootFragment implements OnItemClickListener, OnItemSelectedListener{
	private static LeaveListFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarNewButton, actionbarRefreshButton;

	private ListView lv;
	private MyLeavesAdapter adapter;
	private Spinner statusSpinner, typeSpinner;
	private ArrayList<String> types, statuses;
	private ArrayList<Leave> filteredLeaves;
	private HashMap<Integer, Integer> mapPositions;

	public static LeaveListFragment getInstance(){
		if(instance == null)
			instance = new LeaveListFragment();

		return instance;
	}

	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}

	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_myleaves, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newleaverequest);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("My Leaves");

		actionbarMenuButton.setOnClickListener(this);
		actionbarNewButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);

		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_leavelist, null);
		mapPositions = new HashMap<Integer, Integer>();
		lv = (ListView)view.findViewById(R.id.lists_myleaves);

//		yearSpinner = (Spinner)view.findViewById(R.id.choices_myleaves_search_year);
		statusSpinner = (Spinner)view.findViewById(R.id.choices_myleaves_search_status);
		typeSpinner = (Spinner)view.findViewById(R.id.choices_myleaves_searh_type);

		filteredLeaves = new ArrayList<Leave>();
		types = new ArrayList<String>();
		statuses = new ArrayList<String>();

		types.add("All");
		for(int i=0; i<Leave.getTypeDescriptionList().size(); i++)
			types.add(Leave.getTypeDescriptionList().get(i));

		statuses.add("All");
		for(int i=0; i<Leave.getStatusDescriptionList().size(); i++)
			statuses.add(Leave.getStatusDescriptionList().get(i));

//		yearSpinner.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownYears, NodeSize.SIZE_NORMAL));
		statusSpinner.setAdapter(new SimpleSpinnerAdapter(activity, statuses, NodeSize.SIZE_NORMAL));
		typeSpinner.setAdapter(new SimpleSpinnerAdapter(activity, types, NodeSize.SIZE_NORMAL));

//		yearSpinner.setOnItemSelectedListener(this);
		statusSpinner.setOnItemSelectedListener(this);
		typeSpinner.setOnItemSelectedListener(this);
//		yearSpinner.setSelection(4);

		adapter = new MyLeavesAdapter(activity, filteredLeaves);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		updateList();

		return view;
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		statusSpinner.setEnabled(false);
		typeSpinner.setEnabled(false);
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		statusSpinner.setEnabled(true);
		typeSpinner.setEnabled(true);
		lv.setEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		filterLeaves();
	}

	private void updateList(){
		activity.startLoading();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Object tempMyLeaveResult;

				try{
					tempMyLeaveResult = app.onlineGateway.getMyLeaves();
				}catch(Exception e){
					tempMyLeaveResult = e.getMessage();
				}

				final Object myLeaveResult = tempMyLeaveResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						if(myLeaveResult instanceof String){
							String errorMessage = (String)myLeaveResult;
                            if(errorMessage.contains(SaltApplication.CONNECTION_ERROR)){
								activity.finishLoadingAndShowOutdatedData();
								updateListSuccess(app.offlineGateway.deserializeMyLeaves());
							}else
								activity.finishLoading(errorMessage);
						}else{
							activity.finishLoading();
                            updateListSuccess((ArrayList<Leave>)myLeaveResult);
						}
					}
				});
			}
		}).start();
	}

    private void updateListSuccess(ArrayList<Leave> leaves){
        app.updateMyLeaves(leaves);
        statusSpinner.setSelection(1);
        filterLeaves();
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Intent intent = new Intent(activity, LeaveDetailActivity.class);
		intent.putExtra(LeaveDetailActivity.INTENTKEY_LEAVEPOS, mapPositions.get(pos));
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		filterLeaves();
	}

	private void filterLeaves(){
		mapPositions.clear();
		filteredLeaves.clear();
		ArrayList<Leave>  appLeaves = app.getMyLeaves();
		for(int i=0; i<appLeaves.size(); i++){
			Leave leave = appLeaves.get(i);
//			if(leave.getYear()==Integer.parseInt(yearSpinner.getSelectedItem().toString())){ // filter by year
			   if(statusSpinner.getSelectedItem().toString().equals(leave.getStatusDescription()) || statusSpinner.getSelectedItem().toString().equals("All")) {//filter by status
				   if(typeSpinner.getSelectedItem().toString().equals(leave.getTypeDescription()) || typeSpinner.getSelectedItem().toString().equals("All")) {
					   mapPositions.put(filteredLeaves.size(), i);
					   filteredLeaves.add(leave);
				   }
			   }
//		   }
		}

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarNewButton){
			startActivity(new Intent(activity, NewLeaveRequestActivity.class));
		}else if(v == actionbarRefreshButton){
			updateList();
		}
	}

	public void sync(){
		updateList();
	}

}
