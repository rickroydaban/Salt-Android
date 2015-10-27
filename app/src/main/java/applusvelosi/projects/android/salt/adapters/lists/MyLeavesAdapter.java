package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class MyLeavesAdapter extends BaseAdapter{
	HomeActivity activity;
	ArrayList<Leave> leaves;
	
	public MyLeavesAdapter(HomeActivity activity, ArrayList<Leave> leaves){
		this.activity = activity;
		this.leaves = leaves;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		LeaveNodeHolder holder;
		
		if(view == null){
			holder = new LeaveNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_headerdetailstatus, null);
			holder.typeTV = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_header);
			holder.statusTV = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_status);
			holder.datesTV = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_detail);
			
			view.setTag(holder);
		}
		
		holder = (LeaveNodeHolder)view.getTag();
		Leave leave = leaves.get(pos);
		holder.typeTV.setText(leave.getTypeDescription());
		holder.statusTV.setText(leave.getStatusDescription());
		if(leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY) holder.statusTV.setTextColor(activity.getResources().getColor(R.color.green));
		else if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID) holder.statusTV.setTextColor(activity.getResources().getColor(R.color.black));
		else holder.statusTV.setTextColor(activity.getResources().getColor(R.color.red));
		
		if(leave.getEndDate() != null)
			holder.datesTV.setText(leave.getStartDate()+" - "+leave.getEndDate());
		else
			holder.datesTV.setText(leave.getStartDate());			
		
		return view;
	}

	@Override
	public int getCount() {
		return leaves.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return leaves.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class LeaveNodeHolder{ 
		public TextView typeTV, statusTV, datesTV;
	}
}
