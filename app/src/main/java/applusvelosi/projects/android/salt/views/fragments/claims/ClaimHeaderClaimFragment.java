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
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;

public class ClaimHeaderClaimFragment extends ClaimHeaderFragment{

	@Override
	protected String getActionbarTitle() {
		return "Claim";
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;

		if(Boolean.parseBoolean(claim.getMap().get(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD).toString())){ //claim paid by company card
			ClaimPaidByCC claimPaidByCC = (ClaimPaidByCC)claim;
			view = inflater.inflate(R.layout.fragment_claimdetails_claimpaidbycc, null);
			
//			initializeCommonFields(view);
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_id)).setText(String.valueOf(claim.getClaimNumber()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_staff)).setText(claim.getStaffName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_costcenter)).setText(claim.getCostCenterName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_approver)).setText(claim.getApproverName());
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));

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
			
//			initializeCommonFields(view);
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_id)).setText(String.valueOf(claim.getClaimNumber()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_staff)).setText(claim.getStaffName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_costcenter)).setText(claim.getCostCenterName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_approver)).setText(claim.getApproverName());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_parentclaim)).setText(claimNotPaidByCC.getParentClaimNumber());
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_type)).setText(ClaimHeader.getTypeDescriptionForKey(claim.getTypeID()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_status)).setText(ClaimHeader.getStatusDescriptionForKey(claim.getStatusID()));

			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_total)).setText(String.valueOf(claim.getTotalClaim()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountApproved)).setText(String.valueOf(claim.getApprovedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_amountRejected)).setText(String.valueOf(claim.getRejectedAmount()));
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_forPayment)).setText(String.valueOf(claim.getForPaymentAmount()));
			
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datesubmitted)).setText(claim.getDateSubmitted());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onApprover)).setText(claim.getDateApprovedByApprover());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_onAccount)).setText(claim.getDateApprovedByAccount());
			((TextView)view.findViewById(R.id.tviews_claimdetail_claim_datepaid)).setText(claim.getDatePaid());
		}

		containerLineItem = (RelativeLayout)view.findViewById(R.id.containers_claimheader_lineitems);
		containerLineItem.setOnClickListener(this);

		return view;
	}

	//	private void initializeCommonFields(View view){
//		tvLineItem = (TextView)view.findViewById(R.id.tviews_claimdetail_items);
//		tvLineItem.setPaintFlags(tvLineItem.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//		tvLineItem.setOnClickListener(this);
//
//		updateActionbar();
//	}
}
