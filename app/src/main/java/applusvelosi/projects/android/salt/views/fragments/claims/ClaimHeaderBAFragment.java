package applusvelosi.projects.android.salt.views.fragments.claims;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;

public class ClaimHeaderBAFragment extends ClaimHeaderFragment{
	
	public static ClaimHeaderBAFragment newInstance(int pos){
		ClaimHeaderBAFragment fragment = new ClaimHeaderBAFragment();
		Bundle b = new Bundle();
		b.putInt(KEY, pos);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	protected String getActionbarTitle() {
		return "Business Advance";
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		BusinessAdvance ba = (BusinessAdvance)claim;
		View view = inflater.inflate(R.layout.fragment_claimdetails_businessadvance, null);

		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_id)).setText(String.valueOf(claim.getClaimNumber()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_staff)).setText(claim.getStaffName());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_costcenter)).setText(claim.getCostCenterName());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_approver)).setText(claim.getApproverName());
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));
		
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_total)).setText(String.valueOf(claim.getTotalClaim()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_amountapproved)).setText(String.valueOf(claim.getApprovedAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_amountrejected)).setText(String.valueOf(claim.getRejectedAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_fordeduction)).setText(String.valueOf(claim.getForPaymentAmount()));
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_forpayment)).setText(String.valueOf(claim.getForPaymentAmount()));

		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_datesubmitted)).setText(claim.getDateSubmitted());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_onapprover)).setText(claim.getDateApprovedByApprover());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_oncountrymanager)).setText(ba.getDateApprovedByCM());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_onaccount)).setText(claim.getDateApprovedByAccount());
		((TextView)view.findViewById(R.id.tviews_claimdetail_ba_datepaid)).setText(claim.getDatePaid());

		containerLineItem = (RelativeLayout)view.findViewById(R.id.containers_claimheader_lineitems);
		containerLineItem.setOnClickListener(this);
		return view;
	}

}
