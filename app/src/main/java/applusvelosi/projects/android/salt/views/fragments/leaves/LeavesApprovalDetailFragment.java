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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public class LeavesApprovalDetailFragment extends LinearNavActionbarFragment {
	private static final String KEY = "myleavesapprovaldetailfragmentkey";
	private final String CANCEL = "Cancel";
	//action bar buttons
	private LinearLayout containerActionbarRightbuttons;
	private RelativeLayout buttonActionbarBack;
	private TextView buttonActionbarReject, buttonActionbarApprove, textviewActionbarTitle;
	private Leave leave;
	private SaltProgressDialog pd;
	
	private LinearLayout builderView;
	private EditText rejectionReason;
	private AlertDialog dialogReject;
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_leaveforapprovaldetail, null);
		containerActionbarRightbuttons = (LinearLayout)actionbarLayout.findViewById(R.id.containers_actionbar_leavedetails_approver);
		buttonActionbarBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		buttonActionbarApprove = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_approve);
		buttonActionbarReject = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_reject);
		textviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		textviewActionbarTitle.setText("Leave Details");
		
		buttonActionbarBack.setOnClickListener(this);
		buttonActionbarApprove.setOnClickListener(this);
		buttonActionbarReject.setOnClickListener(this);
		textviewActionbarTitle.setOnClickListener(this);
		return actionbarLayout;
	}
	
	public static LeavesApprovalDetailFragment newInstance(String leave){
		LeavesApprovalDetailFragment fragment = new LeavesApprovalDetailFragment();
		Bundle b = new Bundle();
		b.putString(KEY, leave);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		builderView = (LinearLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
		rejectionReason = (EditText)builderView.getChildAt(0);
		dialogReject = new AlertDialog.Builder(linearNavFragmentActivity).setTitle("Reject").setView(builderView)
														.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
															
															@Override
															public void onClick(DialogInterface dialog, int which) {
																dialog.dismiss();
																if(pd == null)
																	pd = new SaltProgressDialog(linearNavFragmentActivity);
																pd.show();
																new Thread(new Runnable() {
																	
																	@Override
																	public void run() {
																		String tempResult;
																		try{
																			leave.reject(rejectionReason.getText().toString(), app.getStaff(), app.dateFormatDefault.format(new Date()));
																			tempResult = app.onlineGateway.changeLeaveStatus(leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSREJECTEDKEY);
																		}catch(Exception e){
																			tempResult = e.getMessage();
																		}
																		final String result = tempResult;
																		
																		new Handler(Looper.getMainLooper()).post(new Runnable() {
																			
																			@Override
																			public void run() {
																				pd.dismiss();
																				if(result != null)
																					app.showMessageDialog(linearNavFragmentActivity, result);
																				else{
//																					app.showMessageDialog(linearNavFragmentActivity, "Leave Rejected!");
//																					linearNavFragmentActivity.changePage(LeaveForApprovalFragment.getInstance());
//																					LeaveForApprovalFragment.getInstance().sync();
																				}
																				
//																				if(result != null){
//																					pd.dismiss();
//																					app.showMessageDialog(activity, result);
//																				}else
//																					new Thread(new AppLeavesForApprovalUpdater("Leave Rejected!")).start();
																			}
																		});
																		
																	}
																}).start();																	
															}
														}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
															
															@Override
															public void onClick(DialogInterface dialog, int which) {
																dialog.dismiss();
															}
														}).create();

		leave = app.gson.fromJson(getArguments().getString(KEY), app.types.leave);
		try{
			leave.fixFormatForLeavesForApprovalDetailPage(app.onlineGateway);
		}catch(Exception e){
			e.printStackTrace();
//			app.showMessageDialog(activity, "Exception while fixing leave from serializing "+e.getMessage());
		}
		
		if((leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY) && leave.isApprover(app.getStaff().getStaffID())){
			containerActionbarRightbuttons.setVisibility(View.VISIBLE);
			if(leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
				buttonActionbarApprove.setVisibility(View.GONE);
				buttonActionbarReject.setText(CANCEL);
			}
		}
		
		View view = inflater.inflate(R.layout.fragment_leaveforapproval_details, null);
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_type)).setText(leave.getTypeDescription());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_status)).setText(leave.getStatusDescription());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_staff)).setText(leave.getStaffName());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_from)).setText(leave.getStartDate());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_to)).setText(leave.getEndDate());
		TextView approverLabel = (TextView)view.findViewById(R.id.labels_myleavesoverview_approver);
		TextView approverField = (TextView)view.findViewById(R.id.tviews_myleavesoverview_approver);
		if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID){
			approverLabel.setText("Approver");
			approverField.setText(leave.getLeaveApprover1Name());
		}else if(leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
			approverLabel.setText("Approved By");
			approverField.setText(leave.getApproverName());
		}else if(leave.getStatusID() == Leave.LEAVESTATUSREJECTEDKEY){
			approverLabel.setText("Rejected By");
			approverField.setText(leave.getApproverName());
		}else if(leave.getStatusID() == Leave.LEAVESTATUSCANCELLEDKEY){
			approverLabel.setText("Cancelled By");
			approverField.setText(leave.getApproverName());
		}
		
		TextView tviewDays = (TextView)view.findViewById(R.id.tviews_myleavesoverview_days);
		if(leave.getDays() >= 1)			
			tviewDays.setText(String.valueOf(leave.getDays()));
		else if(leave.getDays() == 0.2f)
			tviewDays.setText("0.5 PM");
		else if(leave.getDays() == 0.1f)
			tviewDays.setText("0.5 AM");
		else
			tviewDays.setText("NA");
		
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_workingDays)).setText(String.valueOf((leave.getWorkingDays()>0)?leave.getWorkingDays():0));
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_notes)).setText(leave.getNotes());
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == buttonActionbarBack || v == textviewActionbarTitle){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == buttonActionbarApprove){
			if(pd == null)
				pd = new SaltProgressDialog(linearNavFragmentActivity);
			pd.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String tempResult;
					try{
						leave.approve(app.getStaff(), app.dateFormatDefault.format(new Date()));
						tempResult = app.onlineGateway.changeLeaveStatus(leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSAPPROVEDKEY);
					}catch(Exception e){
						e.printStackTrace();
						tempResult = e.getMessage();
					}
					final String result = tempResult;
					
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {							
							pd.dismiss();
							if(result != null)
								app.showMessageDialog(linearNavFragmentActivity, result);
							else{
//								app.showMessageDialog(linearNavFragmentActivity, "Leave Approved!");
//								linearNavFragmentActivity.changePage(LeaveForApprovalFragment.getInstance());
//								LeaveForApprovalFragment.getInstance().sync();
							}

//							if(result != null){
//								pd.dismiss();
//								app.showMessageDialog(activity, result);
//							}else
//								new Thread(new AppLeavesForApprovalUpdater("Leave Approved!")).start();
						}
					});
					
				}
			}).start();		
		}else if(v == buttonActionbarReject){
			if(buttonActionbarReject.getText().equals(CANCEL)){
				if(pd == null)
					pd = new SaltProgressDialog(linearNavFragmentActivity);
				pd.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String tempResult;
						try{
							leave.cancel(app.getStaff(), app.dateFormatDefault.format(new Date()));
							tempResult = app.onlineGateway.changeLeaveStatus(leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSCANCELLEDKEY);
						}catch(Exception e){
							e.printStackTrace();
							tempResult = e.getMessage();
						}
						final String result = tempResult;
						
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							
							@Override
							public void run() {
								pd.dismiss();
								if(result != null)
									app.showMessageDialog(linearNavFragmentActivity, result);
								else{
									app.showMessageDialog(linearNavFragmentActivity, "Leave Cancelled!");
//									linearNavFragmentActivity.changePage(LeaveForApprovalFragment.getInstance());
									LeaveForApprovalFragment.getInstance().sync();									
								}
								
//								if(result != null)
//									app.showMessageDialog(activity, result);
//								else{
//									pd.show();
//									new Thread(new AppLeavesForApprovalUpdater("Leave Cancelled!")).start();
//								}
							}
						});
						
					}
				}).start();	
			}else{
				rejectionReason.setText("");
				dialogReject.show();
			}			
		}
	}
	
//	private class AppLeavesForApprovalUpdater implements Runnable{
//		private String successMessage;
//		
//		public AppLeavesForApprovalUpdater(String successMessage){
//			this.successMessage = successMessage;
//		}
//		
//		@Override
//		public void run() {
//			Object tempFollowupEmail;
//			try{
//				tempFollowupEmail = app.onlineGateway.followUpLeave(leave.getJSONString());
//			}catch(Exception e){
//				e.printStackTrace();
//				tempFollowupEmail = e.getMessage();
//			}
//			
//			final Object followupEmailResult = tempFollowupEmail;
//			new Handler(Looper.getMainLooper()).post(new Runnable() {
//				
//				@Override
//				public void run() {
//					pd.dismiss();
//					if(followupEmailResult instanceof String)
//						app.showMessageDialog(activity, followupEmailResult.toString());
//					else{
//						//TODO
////						app.updateLeavesForApproval((ArrayList<Leave>)leavesForApprovalResult);
//						app.showMessageDialog(activity, successMessage);
//						activity.changeChildPage(LeaveForApprovalFragment.getInstance());
//						LeaveForApprovalFragment.getInstance().sync();
//					}
//				}
//			});
//		}
//	}
}
