package applusvelosi.projects.android.salt.views.fragments.claims;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.FileManager.AttachmentDownloadListener;
import applusvelosi.projects.android.salt.views.ClaimItemDetailActivity;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

public class ClaimItemDetailGenericFragment extends LinearNavActionbarFragment implements AttachmentDownloadListener{
	//action bar buttons
	private TextView actionbarEditButton, actionbarTitle;
	private RelativeLayout actionbarBackButton;
	
	private LinearLayout containersAttendees;
	private TextView attachment;

	ClaimItemDetailActivity activity;

	@Override
	protected RelativeLayout setupActionbar() {
        activity = (ClaimItemDetailActivity)getActivity();
		RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backedit, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("Claim Item Detail");
		
		actionbarBackButton.setOnClickListener(this);
        System.out.println(activity.claimHeaderStatusKey+" "+ClaimHeader.STATUSKEY_SUBMITTED);
        if(activity.claimHeaderStatusKey == ClaimHeader.STATUSKEY_OPEN)
            actionbarEditButton.setOnClickListener(this);
        else
            actionbarEditButton.setVisibility(View.GONE);

		actionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}
	

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_claimitem_catgeneric_details, null);
		
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_id)).setText(activity.claimItem.getItemNumber());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_category)).setText(activity.claimItem.getCategoryName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_expenseDate)).setText(activity.claimItem.getExpenseDate(app));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amount)).setText(SaltApplication.decimalFormat.format(activity.claimItem.getForeignAmount())+" "+activity.claimItem.getForeignCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amountLocal)).setText(SaltApplication.decimalFormat.format(activity.claimItem.getLocalAmount())+" "+activity.claimItem.getLocalCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_exRate)).setText(String.valueOf(activity.claimItem.getStandardExchangeRate()));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_desc)).setText(activity.claimItem.getDescription());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_project)).setText(activity.claimItem.getProjectName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_taxRate)).setText(activity.claimItem.isTaxApplied()?String.valueOf(activity.claimItem.getTaxAmount()):"No");
		containersAttendees = (LinearLayout)view.findViewById(R.id.containers_claimitemdetail_attendees);
		if(activity.claimItem.isBillable()){
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText(activity.claimItem.getBillableCompanyName());
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_notesorbillstoclient)).setText(activity.claimItem.getNotes());
		}else{
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText("No");
			view.findViewById(R.id.trs_claimitemdetail_notestoclient).setVisibility(View.GONE);
		}
		
		attachment = (TextView)view.findViewById(R.id.tviews_claimitemdetail_attachment);
		if(activity.claimItem.hasReceipt()){
			attachment.setText(activity.claimItem.getAttachmentName());
			attachment.setTextColor(linearNavFragmentActivity.getResources().getColor(R.color.orange_velosi));
			attachment.setOnClickListener(this);
		}else{
			attachment.setText("No");
		}

		for(ClaimItemAttendee attendee :activity.claimItem.getAttendees()){
			LinearLayout v = (LinearLayout)inflater.inflate(R.layout.node_claimitemdetail_attendee, null);
            ((TextView)v.findViewById(R.id.tviews_nodes_claimitemdetail_attendee)).setText(attendee.getName());
            ((TextView)v.findViewById(R.id.tviews_nodes_claimitemdetail_jobtitle)).setText(attendee.getJobTitle());
            ((TextView)v.findViewById(R.id.tviews_nodes_claimitemdetail_notes)).setText(attendee.getNote());
			containersAttendees.addView(v);
		}

		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == actionbarTitle){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == actionbarEditButton){
            Intent intent = new Intent(new Intent(activity, ManageClaimItemActivity.class));
            intent.putExtra(ManageClaimItemActivity.INTENTKEY_CLAIMHEADERPOS, activity.getIntent().getExtras().getString(ClaimItemDetailActivity.INTENTKEY_CLAIMHEADERID));
            intent.putExtra(ManageClaimItemActivity.INTENTKEY_CLAIMITEM, activity.claimItem);
            startActivity(intent);
		}else if(v == attachment){
			if(activity.claimItem.getAttachments().size() > 0) {
				try {
					Document doc = activity.claimItem.getAttachments().get(0);
					int docID = doc.getDocID();
					int objectTypeID = doc.getObjectTypeID();
					int refID = doc.getRefID();
					String filename = doc.getDocName();
					activity.startLoading();
					app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, this);
				} catch (Exception e) {
					e.printStackTrace();
					activity.finishLoading();
					app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
				}
			}
        }
	}

	@Override
	public void onAttachmentDownloadFinish(File file) {
		activity.finishLoading();
		app.fileManager.openDocument(linearNavFragmentActivity, file);
	}
	
	@Override
	public void onAttachmentDownloadFailed(String errorMessage) {
		activity.finishLoading();
		app.showMessageDialog(linearNavFragmentActivity, errorMessage);
	}
}
