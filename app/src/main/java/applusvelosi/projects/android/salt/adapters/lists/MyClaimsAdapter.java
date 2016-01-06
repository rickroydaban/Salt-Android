package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class MyClaimsAdapter extends BaseAdapter{
	SaltApplication app;
	HomeActivity activity;
	OnlineGateway onlineGateway;
	ArrayList<ClaimHeader> claimHeaders;
	
	public MyClaimsAdapter(HomeActivity activity, ArrayList<ClaimHeader> claimHeaders){
		this.activity = activity;
		app = (SaltApplication)activity.getApplication();
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
			holder.tvStatus = (TextView)view.findViewById(R.id.tviews_myclaims_node_status);
			holder.tvDateCreated = (TextView)view.findViewById(R.id.tviews_myclaims_node_date);
			holder.tvTotal = (TextView)view.findViewById(R.id.tviews_myclaims_node_total);

			view.setTag(holder);
		}
		
		holder = (ClaimNodeHolder)view.getTag();
		ClaimHeader claimHeader = claimHeaders.get(pos);
		String minStatusName;
		if(claimHeader.getStatusID() == ClaimHeader.STATUSKEY_PAIDUNDERCOMPANYCARD) minStatusName = "Paid Under CC";
		else minStatusName = ClaimHeader.getStatusDescriptionForKey(claimHeader.getStatusID());

		holder.tvClaimNumber.setText(claimHeader.getClaimNumber());
		holder.tvStatus.setText(minStatusName);
		holder.tvDateCreated.setText(claimHeader.getDateCreated());
		holder.tvTotal.setText(app.getStaffOffice().getBaseCurrencyThree()+" "+SaltApplication.decimalFormat.format(claimHeader.getTotalClaim()));

		if(claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYACCOUNTS || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYAPPROVER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYCOUNTRYMANAGER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_PAID || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_PAIDUNDERCOMPANYCARD)
			holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.green));
		else if(claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYACCOUNTS || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYAPPROVER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYCOUNTRYMANAGER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDFORSALARYDEDUCTION || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_RETURN || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_CANCELLED)
			holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.red));
		else
			holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.black));

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
		public TextView tvClaimNumber, tvStatus, tvDateCreated, tvTotal;
	}
}
