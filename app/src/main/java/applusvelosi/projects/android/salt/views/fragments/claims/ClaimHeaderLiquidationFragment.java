package applusvelosi.projects.android.salt.views.fragments.claims;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.LiquidationOfBA;

public class ClaimHeaderLiquidationFragment extends ClaimHeaderFragment{	
	
	public static ClaimHeaderLiquidationFragment newInstance(int pos){
		ClaimHeaderLiquidationFragment fragment = new ClaimHeaderLiquidationFragment();
		Bundle b = new Bundle();
		b.putInt(KEY, pos);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	protected String getActionbarTitle() {
		return "Liquidation";
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LiquidationOfBA liquidation = (LiquidationOfBA)claim;
		View view = inflater.inflate(R.layout.fragment_claimdetails_liquidation, null);
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_id)).setText(String.valueOf(claim.getClaimNumber()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_staff)).setText(claim.getStaffName());
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_costcenter)).setText(claim.getCostCenterName());
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_approver)).setText(claim.getApproverName());
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_bafreference)).setText(liquidation.getBACNumber());
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_total)).setText(String.valueOf(claim.getTotalClaim()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_amountapproved)).setText(String.valueOf(claim.getApprovedAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_amountrejected)).setText(String.valueOf(claim.getRejectedAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_fordeduction)).setText(String.valueOf(liquidation.getForDeductionAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_forpayment)).setText(String.valueOf(claim.getForPaymentAmount()));
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_datesubmitted)).setText(claim.getDateSubmitted());
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_onapprover)).setText(claim.getDateApprovedByApprover());
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_onaccount)).setText(claim.getDateApprovedByAccount());
		((TextView)view.findViewById(R.id.tviews_claimdetail_liquidation_datepaid)).setText(claim.getDatePaid());
		
		return view;
	}
}
