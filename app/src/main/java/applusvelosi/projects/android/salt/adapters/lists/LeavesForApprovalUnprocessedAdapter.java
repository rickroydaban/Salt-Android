package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.leaves.LeaveForApprovalFragment;

public class LeavesForApprovalUnprocessedAdapter extends BaseAdapter implements OnClickListener{
	
	private final String TAGKEY_BUTTONTYPE_APPROVE = "approve";
	private final String TAGKEY_BUTTONTYPE_REJECT = "reject";
	private final String TAGKEY_BUTTONTYPE_CANCEL = "cancel";
	
	private HomeActivity activity;
	private SaltApplication app;
	private ArrayList<Leave> leavesForApproval;
	private AlertDialog dialogReject;
	private LinearLayout builderView;
	private EditText rejectionReason;
	private SaltProgressDialog pd;
	private Leave leaveSelected;
	
	public LeavesForApprovalUnprocessedAdapter(final HomeActivity activity, ArrayList<Leave> leavesForApproval){
		this.activity = activity;
		app = (SaltApplication)activity.getApplication();
		this.leavesForApproval = leavesForApproval;
		builderView = (LinearLayout)LayoutInflater.from(activity).inflate(R.layout.dialog_textinput, null);
		rejectionReason = (EditText)builderView.getChildAt(0);
		dialogReject = new AlertDialog.Builder(activity).setTitle("Reject").setView(builderView)
														.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
															
															@Override
															public void onClick(DialogInterface dialog, int which) {
																dialog.dismiss();
																if(pd == null)
																	pd = new SaltProgressDialog(activity);			
																pd.show();
																new Thread(new Runnable() {
																	
																	@Override
																	public void run() {
																		String tempResult;
																		try{
																			leaveSelected.reject(rejectionReason.getText().toString(), app.getStaff(), app.dateFormatDefault.format(new Date()));
																			tempResult = app.onlineGateway.changeLeaveStatus(leaveSelected.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSREJECTEDKEY);
																			System.out.println("rejecting leave succeeed");
																		}catch(Exception e){
																			e.printStackTrace();
																			System.out.println("rejecting leave failed "+e.getMessage());
																			tempResult = e.getMessage();
																		}
																		final String result = tempResult;
																		
																		new Handler(Looper.getMainLooper()).post(new Runnable() {
																			
																			@Override
																			public void run() {	
																				pd.dismiss();
																				if(result != null)
																					app.showMessageDialog(activity, result.toString());
																				else{
																					app.showMessageDialog(activity, "Leave Rejected!");
																					LeaveForApprovalFragment.getInstance().sync();
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
		
		pd = new SaltProgressDialog(activity);
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		LeaveNodeHolder holder;
		
		if(view == null){
			holder = new LeaveNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_leavesforapproval_unprocessed, null);
			holder.typeTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_type);
			holder.nameTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_name);
			holder.datesTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_dates);
			holder.buttonApprove = (TextView)view.findViewById(R.id.buttons_cells_leaveforapproval_approve);
			holder.buttonApprove.setOnClickListener(this);
			holder.buttonReject = (TextView)view.findViewById(R.id.buttons_cells_leaveforapproval_reject);
			holder.buttonReject.setOnClickListener(this);
			
			view.setTag(holder);
		}
		
		holder = (LeaveNodeHolder)view.getTag();
		Leave leave = leavesForApproval.get(pos);
		if(leave.isApprover(((SaltApplication)activity.getApplication()).getStaff().getStaffID())){
			if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID){
				if(holder.buttonApprove.getVisibility() == View.GONE){
					holder.buttonApprove.setVisibility(View.VISIBLE);
					holder.buttonReject.setVisibility(View.VISIBLE);				
				}
				
				holder.buttonApprove.setTag(TAGKEY_BUTTONTYPE_APPROVE+"-"+pos);
				holder.buttonReject.setTag(TAGKEY_BUTTONTYPE_REJECT+"-"+pos);				
			}else{ //cancel leaves
				holder.buttonApprove.setVisibility(View.GONE);
				holder.buttonReject.setVisibility(View.GONE);				
			}
		}else{
			holder.buttonApprove.setVisibility(View.GONE);
			holder.buttonReject.setVisibility(View.GONE);
		}
		
		holder.typeTV.setText(leave.getTypeDescription());
		holder.nameTV.setText(leave.getStaffName());

		if(leave.getEndDate() != null)
			holder.datesTV.setText(leave.getStartDate()+" - "+leave.getEndDate());
		else
			holder.datesTV.setText(leave.getStartDate());			
		
		return view;
	}

	@Override
	public int getCount() {
		return leavesForApproval.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return leavesForApproval.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class LeaveNodeHolder{ 
		public TextView typeTV, nameTV, datesTV,  buttonApprove, buttonReject;
	}

	@Override
	public void onClick(View v) {
		String [] tagParts = v.getTag().toString().split("-");
		String type = tagParts[0];
		int pos = Integer.parseInt(tagParts[1]);
		leaveSelected = leavesForApproval.get(pos);
		if(type.equals(TAGKEY_BUTTONTYPE_APPROVE)){
			if(pd == null)
				pd = new SaltProgressDialog(activity);			
			pd.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String tempResult;
					try{
						leaveSelected.approve(app.getStaff(), app.dateFormatDefault.format(new Date()));
						tempResult = app.onlineGateway.changeLeaveStatus(leaveSelected.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSAPPROVEDKEY);
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
								app.showMessageDialog(activity, result.toString());
							else{
								app.showMessageDialog(activity, "Leave Approved!");
								LeaveForApprovalFragment.getInstance().sync();
							}
							
//							if(result != null)
//								app.showMessageDialog(activity, result);
//							else{
//								pd.show();
//								new Thread(new AppLeavesForApprovalUpdater("Leave Approved!")).start();
//							}
						}
					});
					
				}
			}).start();		
		}else if(type.equals(TAGKEY_BUTTONTYPE_REJECT)){
			rejectionReason.setText("");
			dialogReject.show();
		}else if(type.equals(TAGKEY_BUTTONTYPE_CANCEL)){
			app.showMessageDialog(activity, "Cancel the approved leave "+leaveSelected.getTypeDescription()+" ("+leaveSelected.getStartDate()+" - "+leaveSelected.getEndDate()+") ");
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
//			Object tempEmailResult;
//			try{
//				tempEmailResult = app.onlineGateway.followUpLeave(leaveSelected.getJSONString());
//			}catch(Exception e){
//				e.printStackTrace();
//				tempEmailResult = e.getMessage();
//			}
//			
//			final Object emailResult = tempEmailResult;
//			new Handler(Looper.getMainLooper()).post(new Runnable() {
//				
//				@Override
//				public void run() {
//					pd.dismiss();
//					app.showMessageDialog(activity, (emailResult instanceof String)?emailResult.toString():successMessage);
//					LeaveForApprovalFragment.getInstance().sync();
//				}
//			});
//		}
//	}
}
