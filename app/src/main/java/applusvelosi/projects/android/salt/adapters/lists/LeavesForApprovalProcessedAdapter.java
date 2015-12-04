package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;
import java.util.Date;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import applusvelosi.projects.android.salt.ParseReceiver;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;

public class LeavesForApprovalProcessedAdapter extends BaseAdapter implements OnClickListener{
	
	private final String TAGKEY_BUTTONTYPE_CANCEL = "cancel";
	
	private HomeActivity activity;
	private SaltApplication app;
	private ArrayList<Leave> leavesForApproval;
	private SaltProgressDialog pd;
	private Leave leaveSelected;
	
	public LeavesForApprovalProcessedAdapter(final HomeActivity activity, ArrayList<Leave> leavesForApproval){
		this.activity = activity;
		app = (SaltApplication)activity.getApplication();
		this.leavesForApproval = leavesForApproval;		
		pd = new SaltProgressDialog(activity);
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		LeaveNodeHolder holder;
		
		if(view == null){
			holder = new LeaveNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_leavesforapproval_processed, null);
			holder.typeTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_type);
			holder.nameTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_name);
			holder.datesTV = (TextView)view.findViewById(R.id.tviews_cells_leaveforapproval_dates);
			holder.buttonCancel = (TextView)view.findViewById(R.id.buttons_cells_leaveforapproval_reject);
			holder.buttonCancel.setOnClickListener(this);
			
			view.setTag(holder);
		}
		
		holder = (LeaveNodeHolder)view.getTag();
		Leave leave = leavesForApproval.get(pos);
//		if(leave.isApprover(((SaltApplication)activity.getApplication()).getStaff().getStaffID())){
			if(leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
				if(holder.buttonCancel.getVisibility() == View.GONE)
					holder.buttonCancel.setVisibility(View.VISIBLE);
				
				holder.buttonCancel.setTag(TAGKEY_BUTTONTYPE_CANCEL+"-"+pos);
			}else if(leave.getStatusID() == Leave.LEAVESTATUSREJECTEDKEY){
				holder.buttonCancel.setVisibility(View.GONE);
			}
//		}else{
//			holder.buttonCancel.setVisibility(View.GONE);
//		}
		
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
		public TextView typeTV, nameTV, datesTV, buttonCancel;
	}

	@Override
	public void onClick(View v) {
		String [] tagParts = v.getTag().toString().split("-");
		String type = tagParts[0];
		final int pos = Integer.parseInt(tagParts[1]);
		leaveSelected = leavesForApproval.get(pos);
		if(type.equals(TAGKEY_BUTTONTYPE_CANCEL)){
			if(pd == null)
				pd = new SaltProgressDialog(activity);			
			pd.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String tempResult;
					try{
						leaveSelected.cancel(app.getStaff(), app.dateFormatDefault.format(new Date()));
						tempResult = app.onlineGateway.changeLeaveStatus(leaveSelected.getJSONStringForProcessingLeave(), Leave.LEAVESTATUSCANCELLEDKEY);
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
								sendPush(pos, "Leave Cancelled");
								app.showMessageDialog(activity, "Leave Cancelled!");
								LeaveForApprovalFragment.getInstance().sync();
							}
							
//							if(result != null)
//								app.showMessageDialog(activity, result);
//							else{
//								pd.show();
//								new Thread(new AppLeavesForApprovalUpdater("Leave Cancelled!")).start();
//							}
						}
					});
					
				}
			}).start();		
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
//				tempFollowupEmail = app.onlineGateway.followUpLeave(leaveSelected.getJSONString());
//			}catch(Exception e){
//				e.printStackTrace();
//				tempFollowupEmail = e.getMessage();
//			}
//			
//			final Object emailResult = tempFollowupEmail;
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

	private void sendPush(int pos, String message){
		ParsePush parsePush = new ParsePush();
		ParseQuery parseQuery = ParseInstallation.getQuery();
		parseQuery.whereEqualTo("staffID", leavesForApproval.get(pos).getStaffID());
		parsePush.sendMessageInBackground(ParseReceiver.createProcessedItemMessage(message), parseQuery);
	}

}
