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
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.LeaveApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public class LeavesApprovalDetailFragment extends LinearNavActionbarFragment {
	private static final String KEY = "myleavesapprovaldetailfragmentkey";
	private final String CANCEL = "Cancel";

	private LeaveApprovalDetailActivity activity;
	//action bar buttons
	private LinearLayout containerActionbarRightbuttons;
	private RelativeLayout buttonActionbarBack;
	private TextView buttonActionbarReject, buttonActionbarApprove, textviewActionbarTitle;


	private RelativeLayout builderView;
	private EditText rejectionReason;
	private AlertDialog dialogReject;
	
	@Override
	protected RelativeLayout setupActionbar() {
		activity = (LeaveApprovalDetailActivity)getActivity();
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

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		builderView = (RelativeLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
		rejectionReason = (EditText)builderView.findViewById(R.id.etexts_dialogs_textinput);
        ((TextView)builderView.findViewById(R.id.tviews_dialogs_textinput)).setText("Reason for Rejection");
		dialogReject = new AlertDialog.Builder(linearNavFragmentActivity).setTitle(null).setView(builderView)
			.setPositiveButton("Reject", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

					activity.startLoading();
					new Thread(new Runnable() {

						@Override
						public void run() {
							String tempResult;
							try{
								activity.leave.reject(rejectionReason.getText().toString(), app.getStaff(), app.dateFormatDefault.format(new Date()));
								tempResult = app.onlineGateway.changeLeaveStatus(activity.leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSREJECTEDKEY);
							}catch(Exception e){
								tempResult = e.getMessage();
							}
							final String result = tempResult;

							new Handler(Looper.getMainLooper()).post(new Runnable() {

								@Override
								public void run() {
									if(result != null)
										activity.finishLoading(result.toString());
									else{
										activity.finishLoading();
										sendPush("Leave Rejected");
										Toast.makeText(activity, "Rejected Successfully!", Toast.LENGTH_SHORT).show();
										try {
                                            LeaveForApprovalFragment.getInstance().sync();
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
										activity.finish();
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

		try{

			activity.leave.fixFormatForLeavesForApprovalDetailPage(app.onlineGateway);
		}catch(Exception e){
			e.printStackTrace();
		}
		
//		if((activity.leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || activity.leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY) && activity.leave.isApprover(app.getStaff().getStaffID())){
        if((activity.leave.getStatusID()==Leave.LEAVESTATUSPENDINGID || activity.leave.getStatusID()==Leave.LEAVESTATUSAPPROVEDKEY)){
			containerActionbarRightbuttons.setVisibility(View.VISIBLE);
			if(activity.leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
				buttonActionbarApprove.setVisibility(View.GONE);
				buttonActionbarReject.setText(CANCEL);
			}
		}
		
		View view = inflater.inflate(R.layout.fragment_leaveforapproval_details, null);
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_type)).setText(activity.leave.getTypeDescription());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_status)).setText(activity.leave.getStatusDescription());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_staff)).setText(activity.leave.getStaffName());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_from)).setText(activity.leave.getStartDate());
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_to)).setText(activity.leave.getEndDate());
		TextView approverLabel = (TextView)view.findViewById(R.id.labels_myleavesoverview_approver);
		TextView approverField = (TextView)view.findViewById(R.id.tviews_myleavesoverview_approver);
		if(activity.leave.getStatusID() == Leave.LEAVESTATUSPENDINGID){
			approverLabel.setText("Approver");
			approverField.setText(activity.leave.getLeaveApprover1Name());
		}else if(activity.leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
			approverLabel.setText("Approved By");
			approverField.setText(activity.leave.getApproverName());
		}else if(activity.leave.getStatusID() == Leave.LEAVESTATUSREJECTEDKEY){
			approverLabel.setText("Rejected By");
			approverField.setText(activity.leave.getApproverName());
		}else if(activity.leave.getStatusID() == Leave.LEAVESTATUSCANCELLEDKEY){
			approverLabel.setText("Cancelled By");
			approverField.setText(activity.leave.getApproverName());
		}
		
		TextView tviewDays = (TextView)view.findViewById(R.id.tviews_myleavesoverview_days);
		if(activity.leave.getDays() >= 1)
			tviewDays.setText(String.valueOf(activity.leave.getDays()));
		else if(activity.leave.getDays() == 0.2f)
			tviewDays.setText("0.5 PM");
		else if(activity.leave.getDays() == 0.1f)
			tviewDays.setText("0.5 AM");
		else
			tviewDays.setText("NA");
		
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_workingDays)).setText(String.valueOf((activity.leave.getWorkingDays()>0)?activity.leave.getWorkingDays():0));
		((TextView)view.findViewById(R.id.tviews_myleavesoverview_notes)).setText(activity.leave.getNotes());
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == buttonActionbarBack || v == textviewActionbarTitle){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == buttonActionbarApprove){
			activity.startLoading();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String tempResult;
					try{
						activity.leave.approve(app.getStaff(), app.dateFormatDefault.format(new Date()));
						tempResult = app.onlineGateway.changeLeaveStatus(activity.leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSAPPROVEDKEY);
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
								activity.finishLoading();
								sendPush("Leave Approved");
								Toast.makeText(linearNavFragmentActivity, "Leave Approved!", Toast.LENGTH_SHORT).show();
								LeaveForApprovalFragment.getInstance().sync();
								activity.finish();
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
				activity.startLoading();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String tempResult;
						try{
							activity.leave.cancel(app.getStaff(), app.dateFormatDefault.format(new Date()));
							tempResult = app.onlineGateway.changeLeaveStatus(activity.leave.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSCANCELLEDKEY);
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
									activity.finishLoading();
									sendPush("Leave Cancelled");
									Toast.makeText(linearNavFragmentActivity, "Leave Cancelled!", Toast.LENGTH_SHORT).show();
									LeaveForApprovalFragment.getInstance().sync();
									activity.finish();
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

	private void sendPush(String message){
		ParsePush parsePush = new ParsePush();
		ParseQuery parseQuery = ParseInstallation.getQuery();
		parseQuery.whereEqualTo("staffID", activity.leave.getStaffID());
		parsePush.sendMessageInBackground("{\"Type\":\"Leave\", \"LeaveID\":1, \"Message\":\""+message+"\"}", parseQuery);
	}
}
