package applusvelosi.projects.android.salt.views.fragments.roots;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.LeaveApprovalDetailActivity;

public class LeaveForApprovalFragment extends RootFragment implements OnItemSelectedListener,OnItemClickListener, ListAdapterInterface, TextWatcher, CompoundButton.OnCheckedChangeListener{
	private static LeaveForApprovalFragment instance;
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton;
    private TextView actionbarButtonSelect, actionbarButtonApprove, actionbarButtonReject, actionbarButtonCancel;
	
	private Spinner typeSpinner;
	private HashMap<Integer, Boolean> pendingPosChecked, processedPosChecked;
	private ListAdapter adapter;
	private ListView lv;
	private ArrayList<Integer> selectedStatusesIDs;
	private ArrayList<Leave> tempLeavesForApproval, leavesForApproval;
	private ArrayList<String> types;
	private RelativeLayout pendingButton, acceptedButton, cancelledButton;
	private EditText nameET;

	private boolean isSelecting = false;

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
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_leavesforapproval, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        actionbarButtonSelect = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_select);
        actionbarButtonApprove = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_approve);
        actionbarButtonReject = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_reject);
        actionbarButtonCancel = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_cancel);

        ((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Leave for Approval");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
        actionbarButtonSelect.setOnClickListener(this);
        actionbarButtonApprove.setOnClickListener(this);
        actionbarButtonReject.setOnClickListener(this);
        actionbarButtonCancel.setOnClickListener(this);

		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_leaveforapproval, null);
		adapter = new ListAdapter(this);
		lv = (ListView)v.findViewById(R.id.lists_leaveforapproval);
        leavesForApproval = new ArrayList<Leave>();
		tempLeavesForApproval = new ArrayList<Leave>();
		pendingPosChecked = new HashMap<Integer, Boolean>();
        processedPosChecked = new HashMap<Integer, Boolean>();
		selectedStatusesIDs = new ArrayList<Integer>();
				
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
		
//		yearSpinner.setAdapter(new SimpleSpinnerAdapter(activity, app.dropDownYears, NodeSize.SIZE_NORMAL));
		typeSpinner.setAdapter(new SimpleSpinnerAdapter(activity, types, NodeSize.SIZE_NORMAL));

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

//		yearSpinner.setOnItemSelectedListener(this);
		typeSpinner.setOnItemSelectedListener(this);
//		yearSpinner.setSelection(4);
		
		sync();

		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		lv.setAdapter(adapterUnprocessedLFA);
		selectedStatusesIDs.clear();
		selectedStatusesIDs.add(Leave.LEAVESTATUSPENDINGID);
		blurTabNodeBackground();
		((TextView)pendingButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
		((ImageView)pendingButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_leavesforapproval_sel));
		refetchLeaves();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder;

		if(v == null){
			v = LayoutInflater.from(activity).inflate(R.layout.node_leavesforapproval, null);
			holder = new Holder();
			holder.cbIsSelected = (CheckBox)v.findViewById(R.id.cboxes_cells_leavesforapproval_isSelected);
			holder.ivNext = (ImageView)v.findViewById(R.id.iviews_cells_leaveforapproval_next);
			holder.tvDates = (TextView)v.findViewById(R.id.tviews_cells_leaveforapproval_dates);
			holder.tvName = (TextView)v.findViewById(R.id.tviews_cells_leaveforapproval_name);
			holder.tvType = (TextView)v.findViewById(R.id.tviews_cells_leaveforapproval_type);

			v.setTag(holder);
		}

		holder = (Holder)v.getTag();
		Leave leave = tempLeavesForApproval.get(position);

		holder.tvDates.setText(leave.getStartDate() + " - " + leave.getEndDate());
		holder.tvType.setText(leave.getTypeDescription());
		holder.tvName.setText(leave.getStaffName());
		holder.cbIsSelected.setTag(position);
        holder.cbIsSelected.setOnCheckedChangeListener(null);
        holder.cbIsSelected.setChecked(false);
		if((leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY) && isSelecting) {
			holder.cbIsSelected.setVisibility(View.VISIBLE);
			holder.ivNext.setVisibility(View.GONE);
            if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID){
    			if(pendingPosChecked.containsKey(position))
				    holder.cbIsSelected.setChecked(pendingPosChecked.get(position));
			}else{
                if(processedPosChecked.containsKey(position))
                    holder.cbIsSelected.setChecked(processedPosChecked.get(position));
            }
		}else{
			holder.cbIsSelected.setVisibility(View.GONE);
			holder.ivNext.setVisibility(View.VISIBLE);
		}

		holder.cbIsSelected.setOnCheckedChangeListener(this);

		return v;
	}

	@Override
	public int getCount() {
		return tempLeavesForApproval.size();
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

		adapter.notifyDataSetChanged();
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
        Leave leave = tempLeavesForApproval.get(pos);
        if(isSelecting){
            if(leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY)
                (((Holder)view.getTag()).cbIsSelected).setChecked(!(((Holder)view.getTag()).cbIsSelected).isChecked());
        }else{
			Intent intent = new Intent(activity, LeaveApprovalDetailActivity.class);
			intent.putExtra(LeaveApprovalDetailActivity.INTENTKEY_LEAVEJSON, leave.jsonize(app));
			startActivity(intent);
		}
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
	public void onClick(View view) {		
		if(view == actionbarMenuButton){
			activity.toggleSidebar(actionbarMenuButton);
		}else if(view == actionbarRefreshButton){
			sync();
		}else if(view == actionbarButtonSelect){
            isSelecting = !isSelecting;
            if(isSelecting){
                ((TextView)view).setText("Deselect");
            }else{
                pendingPosChecked.clear();
                processedPosChecked.clear();
                ((TextView)view).setText("Select");
                actionbarButtonApprove.setVisibility(View.GONE);
                actionbarButtonReject.setVisibility(View.GONE);
                actionbarButtonCancel.setVisibility(View.GONE);
                actionbarRefreshButton.setVisibility(View.VISIBLE);
            }
            refetchLeaves();
        }else if(view == actionbarButtonApprove){
			for(final int pos :pendingPosChecked.keySet()){
				activity.startLoading();
				new Thread(new Runnable() {

					@Override
					public void run() {
						String tempResult;
						try{
							tempLeavesForApproval.get(pos).approve(app.getStaff(), app.dateFormatDefault.format(new Date()));
							tempResult = app.onlineGateway.changeLeaveStatus(tempLeavesForApproval.get(pos).getJSONStringForProcessingLeave(), Leave.LEAVESTATUSAPPROVEDKEY);
						}catch(Exception e){
							e.printStackTrace();
							tempResult = e.getMessage();
						}
						final String result = tempResult;

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								if(result != null)
									activity.finishLoading(result.toString());
								else{
									pendingPosChecked.remove(pos);
									activity.finishLoading();
									sendPush("Leave Approved", pos);
									Toast.makeText(activity, "Leave Approved!", Toast.LENGTH_SHORT).show();
									sync();
								}
							}
						});

					}
				}).start();
            }
		}else if(view == actionbarButtonReject){
			for(final int pos :pendingPosChecked.keySet()) {

				activity.startLoading();
				new Thread(new Runnable() {

					@Override
					public void run() {
						String tempResult;
						try {
							tempLeavesForApproval.get(pos).reject("Rejected.", app.getStaff(), app.dateFormatDefault.format(new Date()));
							tempResult = app.onlineGateway.changeLeaveStatus(tempLeavesForApproval.get(pos).getJSONStringForProcessingLeave(), Leave.LEAVESTATUSREJECTEDKEY);
						} catch (Exception e) {
							tempResult = e.getMessage();
						}
						final String result = tempResult;

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								if (result != null)
									activity.finishLoading(result.toString());
								else {
                                    pendingPosChecked.remove(pos);
                                    activity.finishLoading();
                                    sendPush("Leave Rejected", pos);
                                    Toast.makeText(activity, "Leave Rejected!", Toast.LENGTH_SHORT).show();
                                    sync();
								}
							}
						});

					}
				}).start();
			}
		}else if(view == actionbarButtonCancel){
            for(final int pos :pendingPosChecked.keySet()){
                activity.startLoading();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String tempResult;
                        try{
                            tempLeavesForApproval.get(pos).cancel(app.getStaff(), app.dateFormatDefault.format(new Date()));
                            tempResult = app.onlineGateway.changeLeaveStatus(tempLeavesForApproval.get(pos).getJSONStringForProcessingLeave(), Leave.LEAVESTATUSCANCELLEDKEY);
                        }catch(Exception e){
                            e.printStackTrace();
                            tempResult = e.getMessage();
                        }
                        final String result = tempResult;

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                if(result != null)
                                    activity.finishLoading(result);
                                else{
                                    processedPosChecked.remove(pos);
                                    activity.finishLoading();
                                    sendPush("Leave Cancelled", pos);
                                    Toast.makeText(activity, "Leave Cancelled!", Toast.LENGTH_SHORT).show();
                                    sync();
                                }
                            }
                        });

                    }
                }).start();
            }
		}else if(view == pendingButton){
			blurTabNodeBackground();
			((TextView)pendingButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)pendingButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_leavesforapproval_sel));
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSPENDINGID);

            if(isSelecting){
                if (pendingPosChecked.size() > 0) {
                    actionbarButtonApprove.setVisibility(View.VISIBLE);
                    actionbarButtonReject.setVisibility(View.VISIBLE);
                    actionbarButtonCancel.setVisibility(View.GONE);
                    actionbarRefreshButton.setVisibility(View.GONE);
                }else{
                    actionbarButtonApprove.setVisibility(View.GONE);
                    actionbarButtonReject.setVisibility(View.GONE);
                    actionbarButtonCancel.setVisibility(View.GONE);
                    actionbarRefreshButton.setVisibility(View.VISIBLE);
                }
            }

            refetchLeaves();
		}else if(view == acceptedButton){
			blurTabNodeBackground();
			((TextView)acceptedButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)acceptedButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_approvedleave_sel));
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSAPPROVEDKEY);
			selectedStatusesIDs.add(Leave.LEAVESTATUSREJECTEDKEY);

            if (isSelecting) {
                if(processedPosChecked.size() > 0){
                    actionbarButtonApprove.setVisibility(View.GONE);
                    actionbarButtonReject.setVisibility(View.GONE);
                    actionbarButtonCancel.setVisibility(View.VISIBLE);
                    actionbarRefreshButton.setVisibility(View.GONE);
                }else{
                    actionbarButtonApprove.setVisibility(View.GONE);
                    actionbarButtonReject.setVisibility(View.GONE);
                    actionbarButtonCancel.setVisibility(View.GONE);
                    actionbarRefreshButton.setVisibility(View.VISIBLE);
                }
            }
            refetchLeaves();
		}else if(view == cancelledButton){
			blurTabNodeBackground();
			((TextView)cancelledButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.orange_velosi));
			((ImageView)cancelledButton.getChildAt(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_rejectedleave_sel));
			selectedStatusesIDs.clear();
			selectedStatusesIDs.add(Leave.LEAVESTATUSCANCELLEDKEY);

            if (isSelecting) {
                actionbarButtonApprove.setVisibility(View.GONE);
                actionbarButtonReject.setVisibility(View.GONE);
                actionbarButtonCancel.setVisibility(View.GONE);
                actionbarRefreshButton.setVisibility(View.GONE);
            }

			refetchLeaves();
		}
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(selectedStatusesIDs.contains(Leave.LEAVESTATUSPENDINGID)) {
            this.pendingPosChecked.put(Integer.parseInt(buttonView.getTag().toString()), isChecked);

            if (pendingPosChecked.size() > 0) {
                actionbarButtonApprove.setVisibility(View.VISIBLE);
                actionbarButtonReject.setVisibility(View.VISIBLE);
                actionbarRefreshButton.setVisibility(View.GONE);
            }else{
                actionbarButtonApprove.setVisibility(View.GONE);
                actionbarButtonReject.setVisibility(View.GONE);
                actionbarRefreshButton.setVisibility(View.VISIBLE);
            }
        }else if(selectedStatusesIDs.contains(Leave.LEAVESTATUSAPPROVEDKEY)) {
			this.processedPosChecked.put(Integer.parseInt(buttonView.getTag().toString()), isChecked);
            if(processedPosChecked.size() > 0){
                actionbarButtonCancel.setVisibility(View.VISIBLE);
                actionbarRefreshButton.setVisibility(View.GONE);
            }else{
                actionbarButtonCancel.setVisibility(View.GONE);
                actionbarRefreshButton.setVisibility(View.VISIBLE);
            }
        }
	}

	private class Holder{
		private ImageView ivNext;
		private CheckBox cbIsSelected;
		private TextView tvType, tvDates, tvName;
	}

	private void sendPush(String message, int pos){
		ParsePush parsePush = new ParsePush();
		ParseQuery parseQuery = ParseInstallation.getQuery();
		parseQuery.whereEqualTo("staffID", tempLeavesForApproval.get(pos).getStaffID());
		parsePush.sendMessageInBackground("{\"Type\":\"Leave\", \"LeaveID\":1, \"Message\":\""+message+"\"}", parseQuery);
	}

}
