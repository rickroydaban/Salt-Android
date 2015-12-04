package applusvelosi.projects.android.salt.views.fragments.leaves;

import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.R.color;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.LeaveDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveListFragment;

public class LeaveDetailsFragment extends LinearNavActionbarFragment {
	private LeaveDetailActivity activity;
	//action bar button
	private TextView buttonActionbarEdit, buttonActionbarCancel, buttonFollowUp, textViewActionbarTitle;
	private RelativeLayout buttonActionbarBack;
	private LinearLayout pendingButtonContainer;
	
	private TextView tviewTypeDesc, tviewStatusDesc, tviewStaffname, tviewDateFrom, tviewDateTo, tviewDays, tviewWorkingDays, tviewNotes;
	private Leave leave;
	
	@Override
	protected RelativeLayout setupActionbar() {
		activity = (LeaveDetailActivity)getActivity();
		RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_myleavedetails, null);
		buttonActionbarBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		buttonActionbarEdit = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		buttonActionbarCancel = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_cancel);
		pendingButtonContainer = (LinearLayout)actionbarLayout.findViewById(R.id.containers_actionbar_leavedetails_right);
		textViewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		textViewActionbarTitle.setText("Leave Details");

		buttonActionbarBack.setOnClickListener(this);
		textViewActionbarTitle.setOnClickListener(this);
		buttonActionbarEdit.setOnClickListener(this);
		buttonActionbarCancel.setOnClickListener(this);
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leave_details, null);
		buttonFollowUp = (TextView)view.findViewById(R.id.buttons_myleavesoverview_followup);

        leave = app.getMyLeaves().get(((LeaveDetailActivity)getActivity()).leavePos);
		if(leave.getStatusID() != Leave.LEAVESTATUSPENDINGID){
			try{
				if(leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY && app.dateFormatDefault.parse(leave.getStartDate()).compareTo(new Date())>0)
					buttonActionbarEdit.setVisibility(View.GONE);
				else
					pendingButtonContainer.setVisibility(View.GONE);
			}catch(Exception e){
				e.printStackTrace();
				app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
			}
			
			buttonFollowUp.setTextColor(linearNavFragmentActivity.getResources().getColor(color.light_gray));
			buttonFollowUp.setText("Follow up");
		}else
			buttonFollowUp.setOnClickListener(this);

		tviewTypeDesc = (TextView)view.findViewById(R.id.tviews_myleavesoverview_type);
		tviewStatusDesc = (TextView)view.findViewById(R.id.tviews_myleavesoverview_status);
		tviewStaffname = (TextView)view.findViewById(R.id.tviews_myleavesoverview_staff);
		tviewDateFrom = (TextView)view.findViewById(R.id.tviews_myleavesoverview_from);
		tviewDateTo = (TextView)view.findViewById(R.id.tviews_myleavesoverview_to);
		tviewDays = (TextView)view.findViewById(R.id.tviews_myleavesoverview_days);
		tviewWorkingDays = (TextView)view.findViewById(R.id.tviews_myleavesoverview_workingDays);
		tviewNotes = (TextView)view.findViewById(R.id.tviews_myleavesoverview_notes);
				
		tviewTypeDesc.setText(leave.getTypeDescription());
		tviewStatusDesc.setText(leave.getStatusDescription());
		tviewStaffname.setText(leave.getStaffName());
		tviewDateFrom.setText(leave.getStartDate());
		tviewDateTo.setText(leave.getEndDate());
		
		if(leave.getDays() >= 1)			
			tviewDays.setText(String.valueOf(leave.getDays()));
		else if(leave.getDays() == 0.2f)
			tviewDays.setText("0.5 PM");
		else if(leave.getDays() == 0.1f)
			tviewDays.setText("0.5 AM");
		else
			tviewDays.setText("NA");
		
		tviewWorkingDays.setText(String.valueOf((leave.getWorkingDays()>0)?leave.getWorkingDays():0));
		tviewNotes.setText(leave.getNotes());
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == buttonActionbarBack || v == textViewActionbarTitle){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == buttonActionbarEdit){
			linearNavFragmentActivity.changePage(EditLeaveFragment.newInstance(((LeaveDetailActivity)getActivity()).leavePos));
		}else if(v == buttonActionbarCancel){
			new AlertDialog.Builder(linearNavFragmentActivity).setMessage("Are you sure you want to cancel this leave request?")
											 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
                                                    activity.startLoading();
													new Thread(new Runnable() {
														
														@Override
														public void run() {
															String tempResult;
															try{
																tempResult = app.onlineGateway.changeLeaveStatus(leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSCANCELLEDKEY);
															}catch(Exception e){
																tempResult = e.getMessage();
															}
															final String result = tempResult;
															
															new Handler(Looper.getMainLooper()).post(new Runnable() {
																
																@Override
																public void run() {
																	if(result != null)
                                                                        activity.finishLoading(result);
																	else{
                                                                        activity.finishLoading();
                                                                        Toast.makeText(activity, "Leave Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                                                        LeaveListFragment.getInstance().sync();
                                                                        activity.finish();
																	}
																}
															});
															
														}
													}).start();
												}
											})
											.setNegativeButton("No", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
												}
											}).create().show();
		}else if(v == buttonFollowUp){
            activity.startLoading();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String tempResult;
					try{
						String leaveJSON = leave.getJSONStringForProcessingLeave();
						System.out.println("leave JSON "+leaveJSON);
						tempResult = app.onlineGateway.followUpLeave(leaveJSON);
					}catch(Exception e){
						tempResult = e.getMessage();
					}
					final String result = tempResult;
					
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {
                            if(result==null){
                                activity.finishLoading();
                                Toast.makeText(activity, "Follow request was sent successfully! ",Toast.LENGTH_SHORT).show();
                            }else
                                activity.finishLoading(result);
						}
					});
					
				}
			}).start();
		}
	}
}
