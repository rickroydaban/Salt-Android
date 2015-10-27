package applusvelosi.projects.android.salt.views.fragments.claims;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;

public class ClaimHeaderClaimFragment extends ClaimHeaderFragment{

	public static ClaimHeaderClaimFragment newInstance(int pos){
		ClaimHeaderClaimFragment fragment = new ClaimHeaderClaimFragment();
		Bundle b = new Bundle();
		b.putInt(KEY, pos);
		fragment.setArguments(b);
		
		return fragment;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		
		if(Boolean.parseBoolean(claim.getMap().get(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD).toString())){ //claim paid by company card
			ClaimPaidByCC claimPaidByCC = (ClaimPaidByCC)claim;
			view = inflater.inflate(R.layout.fragment_claimdetails_claimpaidbycc, null);
			
			initializeCommonFields(view);
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_id)).setText(String.valueOf(claim.getClaimNumber()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_staff)).setText(claim.getStaffName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_costcenter)).setText(claim.getCostCenterName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_approver)).setText(claim.getApproverName());
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_compcard)).setText("Yes");
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_total)).setText(String.valueOf(claim.getTotalClaim()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountApproved)).setText(String.valueOf(claim.getApprovedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountRejected)).setText(String.valueOf(claim.getRejectedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_forDeduction)).setText(String.valueOf(claimPaidByCC.getForDeductionAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_forPayment)).setText(String.valueOf(claim.getForPaymentAmount()));
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datesubmitted)).setText(claim.getDateSubmitted());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onApprover)).setText(claim.getDateApprovedByApprover());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onAccount)).setText(claim.getDateApprovedByAccount());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datepaid)).setText(claim.getDatePaid());			
		}else{
			ClaimNotPaidByCC claimNotPaidByCC = (ClaimNotPaidByCC)claim;
			view = inflater.inflate(R.layout.fragment_claimdetails_claimnotpaidbycc, null);
			
			initializeCommonFields(view);
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_id)).setText(String.valueOf(claim.getClaimNumber()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_staff)).setText(claim.getStaffName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_costcenter)).setText(claim.getCostCenterName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_approver)).setText(claim.getApproverName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_parentclaim)).setText(claimNotPaidByCC.getParentClaimNumber());
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_compcard)).setText("No");
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_total)).setText(String.valueOf(claim.getTotalClaim()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountApproved)).setText(String.valueOf(claim.getApprovedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountRejected)).setText(String.valueOf(claim.getRejectedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_forPayment)).setText(String.valueOf(claim.getForPaymentAmount()));
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datesubmitted)).setText(claim.getDateSubmitted());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onApprover)).setText(claim.getDateApprovedByApprover());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onAccount)).setText(claim.getDateApprovedByAccount());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datepaid)).setText(claim.getDatePaid());
		}
		
		return view;
	}

	private void initializeCommonFields(View view){
		tvLineItem = (TextView)view.findViewById(R.id.tviews_claimdetail_items);
		tvLineItem.setPaintFlags(tvLineItem.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
		tvLineItem.setOnClickListener(this);

		updateActionbar();		
	}
}
