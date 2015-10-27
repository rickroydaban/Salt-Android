package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class MyClaimsAdapter extends BaseAdapter{
	HomeActivity activity;
	OnlineGateway onlineGateway;
	ArrayList<ClaimHeader> claimHeaders;
	
	public MyClaimsAdapter(HomeActivity activity, ArrayList<ClaimHeader> claimHeaders){
		this.activity = activity;
		this.claimHeaders = claimHeaders;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		ClaimNodeHolder holder;
		
		if(view == null){
			holder = new ClaimNodeHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_myclaims, null);
			holder.tvClaimNumber = (TextView)view.findViewById(R.id.tviews_myclaims_node_name);
			holder.tvType = (TextView)view.findViewById(R.id.tviews_myclaims_node_type);
			holder.tvStatus = (TextView)view.findViewById(R.id.tviews_myclaims_node_status);			
			holder.tvCostCenter = (TextView)view.findViewById(R.id.tviews_myclaims_node_chargeTo);
			holder.tvTotal = (TextView)view.findViewById(R.id.tviews_myclaims_node_total);
			
			view.setTag(holder);
		}
		
		holder = (ClaimNodeHolder)view.getTag();
		ClaimHeader claimHeader = claimHeaders.get(pos);
		holder.tvClaimNumber.setText(claimHeader.getClaimNumber());
		holder.tvType.setText(ClaimHeader.getTypeDescriptionForKey(claimHeader.getTypeID()));
		holder.tvStatus.setText(ClaimHeader.getStatusDescriptionForKey(claimHeader.getStatusID()));
		holder.tvCostCenter.setText(claimHeader.getCostCenterName());
		holder.tvTotal.setText(String.valueOf(claimHeader.getTotalClaim()));
		
		return view;
	}

	@Override
	public int getCount() {
		return claimHeaders.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return claimHeaders.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class ClaimNodeHolder{ 
		public TextView tvClaimNumber, tvType, tvStatus, tvCostCenter, tvTotal;
	}
}
