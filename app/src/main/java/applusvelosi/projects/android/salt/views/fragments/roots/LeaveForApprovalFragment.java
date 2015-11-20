package applusvelosi.projects.android.salt.views.fragments.roots;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.LeavesForApprovalProcessedAdapter;
import applusvelosi.projects.android.salt.adapters.lists.LeavesForApprovalUnprocessedAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.views.LeaveApprovalDetailActivity;

public class LeaveForApprovalFragment extends RootFragment implements OnItemSelectedListener,OnItemClickListener,
																	  OnItemLongClickListener, TextWatcher{
	private static LeaveForApprovalFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton;
	
	private Spinner typeSpinner;
	private ArrayList<Integer> selectedStatusesIDs;
	private ListView lv;
	private ArrayList<Leave> tempLeavesForApproval, leavesForApproval;
	private LeavesForApprovalUnprocessedAdapter adapterUnprocessedLFA;
	private LeavesForApprovalProcessedAdapter adapterProcessedLFA;
	private ArrayList<String> types;
	private RelativeLayout pendingButton, acceptedButton, cancelledButton;
	private EditText nameET;

	public static LeaveForApprovalFragment getInstance(){
		if(instance == null)
			instance = new LeaveForApprovalFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Leave for Approval");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_leaveforapproval, null);
		lv = (ListView)v.findViewById(R.id.lists_leaveforapproval);
        leavesForApproval = new ArrayList<Leave>();
		tempLeavesForApproval = new ArrayList<Leave>();
				
//		yearSpinner = (Spinner)v.findViewById(R.id.choices_leaveforapproval_year);
		typeSpinner = (Spinner)v.findViewById(R.id.choices_leaveforapproval_type);
		pendingButton = (RelativeLayout)v.findViewById(R.id.buttons_leaveforapproval_pending);
		acceptedButton = (RelativeLayout)v.findViewById(R.id.buttons_leaveforapproval_approved);
		cancelledButton = (RelativeLayout)v.findViewById(R.id.buttons_leaveforapproval_cancelled);
		nameET = (EditText)v.findViewById(R.id.etexts_leaveforapproval_name);
		
		pendingButton.setOnClickListener(this);
		acceptedButton.setOnClickListener(this);
		cancelledButton.setOnClickListener(this);
		nameET.addTextChangedListener(this);
		
		types = new ArrayList<String>();
		
		types.add("All");
		for(int i=0; i<Leave.getTypeDescriptionList().size(); i++)
			types.add(Leave.getTypeDescriptionList().get(i));
		
		adapterUnprocessedLFA = new LeavesForApprovalUnprocessedAdapter(activity, tempLeavesForApproval);
		adapterProcessedLFA = new LeavesForApprovalProcessedAdapter(activity, tempLeavesForApproval);
//		yearSpinner.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownYears, NodeSize.SIZE_NORMAL));
		typeSpinner.setAdapter(new SimpleSpinnerAdapter(activity, types, NodeSize.SIZE_NORMAL));

		lv.setAdapter(adapterUnprocessedLFA);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);

//		yearSpinner.setOnItemSelectedListener(this);
		typeSpinner.setOnItemSelectedListener(this);
//		yearSpinner.setSelection(4);
		
		selectedStatusesIDs = new ArrayList<Integer>();
		sync();

		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		lv.setAdapter(adapterUnprocessedLFA);
		selectedStatusesIDs.clear();
		selectedStatusesIDs.add(Leave.LEAVESTATUSPENDINGID);
		blurTabNodeBackground();
		((TextView)pendingButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
		((ImageView)pendingButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_leavesforapproval_sel));
		refetchLeaves();
	}
		
	public void sync(){
		activity.startLoading();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Object tempLeavesForApprovalResult;
				try{
					tempLeavesForApprovalResult = app.onlineGateway.getLeavesForApproval();
				}catch(Exception e){
					tempLeavesForApprovalResult = e.getMessage();
				}
				
				final Object leavesForApprovalResult = tempLeavesForApprovalResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if(leavesForApprovalResult instanceof String)
							activity.finishLoading(leavesForApprovalResult.toString());
						else{
							activity.finishLoading();
							leavesForApproval.clear();
							leavesForApproval.addAll((ArrayList<Leave>) leavesForApprovalResult);
							refetchLeaves();
						}
					}
				});
			}
		}).start();
	}
	
	private void refetchLeaves(){
		tempLeavesForApproval.clear();
		for(Leave leave: leavesForApproval){
//			if(leave.getYear() == Integer.parseInt(yearSpinner.getSelectedItem().toString())){//show only leaves for approval this year
				if(selectedStatusesIDs.contains(leave.getStatusID())){ //filter by status
					if(typeSpinner.getSelectedItem().toString().equals(leave.getTypeDescription()) || typeSpinner.getSelectedItem().toString().equals("All")){
						if(leave.getStaffName().toLowerCase().contains(nameET.getText().toString().toLowerCase()) || nameET.getText().length()<1)
							tempLeavesForApproval.add(leave);
					}
				}
//			}
		}
		
		((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		refetchLeaves();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Intent intent = new Intent(activity, LeaveApprovalDetailActivity.class);
		intent.putExtra(LeaveApprovalDetailActivity.INTENTKEY_LEAVEJSON, tempLeavesForApproval.get(pos).jsonize(app));
		startActivity(intent);
//		activity.changeChildPage(LeavesApprovalDetailFragment.newInstance(app.gson.toJson(tempLeavesForApproval.get(pos), app.types.leave)));
	}

	@Override
	public void afterTextChanged(Editable e) {
		refetchLeaves();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
		Leave leave= tempLeavesForApproval.get(pos);
		
		new AlertDialog.Builder(activity).setMessage(" leave by "+leave.getStaffName())
										 .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										})
										.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										}).create().show();
		
		return true;
	}	
	
	@Override
	public void onClick(View view) {		
		if(view == actionbarMenuButton){
			activity.toggleSidebar(actionbarMenuButton);
		}else if(view == actionbarRefreshButton){
			sync();
		}else if(view == pendingButton){
			blurTabNodeBackground();
			((TextView)pendingButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)pendingButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_leavesforapproval_sel));
			lv.setAdapter(adapterUnprocessedLFA);
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSPENDINGID);
		}else if(view == acceptedButton){
			blurTabNodeBackground();
			((TextView)acceptedButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)acceptedButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_approvedleave_sel));
			lv.setAdapter(adapterProcessedLFA);
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSAPPROVEDKEY);
			selectedStatusesIDs.add(Leave.LEAVESTATUSREJECTEDKEY);
		}else if(view == cancelledButton){
			blurTabNodeBackground();
			((TextView)cancelledButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)cancelledButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_rejectedleave_sel));
			lv.setAdapter(adapterUnprocessedLFA);
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSCANCELLEDKEY);
		}
		
		refetchLeaves();
	}	
	
	private void blurTabNodeBackground(){
		((TextView)pendingButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
		((ImageView)pendingButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_leavesforapproval));
		((TextView)acceptedButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
		((ImageView)acceptedButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_approvedleave));
		((TextView)cancelledButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
		((ImageView)cancelledButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_rejectedleave));
	}


	@Override
	public void disableUserInteractionsOnSidebarShown() {
//		yearSpinner.setEnabled(false);
		nameET.setEnabled(false);
		lv.setEnabled(false);
		pendingButton.setEnabled(false);
		acceptedButton.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		try{
//			yearSpinner.setEnabled(true);
			nameET.setEnabled(true);
			lv.setEnabled(true);	
			pendingButton.setEnabled(true);
			acceptedButton.setEnabled(true);
		}catch(NullPointerException e){
			app.showMessageDialog(activity, "Null pointer exception at CalendarMyCalendarFragment enableUserInteractionOnSidebarHidden()");
		}
	}
}
