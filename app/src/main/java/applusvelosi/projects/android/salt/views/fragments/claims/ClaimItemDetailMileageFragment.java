package applusvelosi.projects.android.salt.views.fragments.claims;

import java.io.File;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItemMileage;
import applusvelosi.projects.android.salt.models.claimitems.MilageClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.FileManager.AttachmentDownloadListener;
import applusvelosi.projects.android.salt.views.ClaimItemDetailActivity;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public class ClaimItemDetailMileageFragment extends LinearNavActionbarFragment implements AttachmentDownloadListener{
	private static final String KEY_CLAIMID = "myclaimitemdetailclaimIDkey";
	private static final String KEY_CLAIMITEMID = "myclaimitemdetailclaimitemIDKey";
	//action bar buttons
	private TextView actionbarEditButton, actionbarTitle;
	private RelativeLayout actionbarBackButton;

	private LinearLayout containersAttendees;
	private TextView attachment;

	ClaimItemDetailActivity activity;
	ClaimItemMileage claimItemMileage;
	
	@Override
	protected RelativeLayout setupActionbar() {
		activity = (ClaimItemDetailActivity)getActivity();
		RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backedit, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("Claim Item Detail");
		
		actionbarBackButton.setOnClickListener(this);
		if(activity.claimHeaderStatusKey == ClaimHeader.STATUSKEY_OPEN)
			actionbarEditButton.setOnClickListener(this);
		else
			actionbarEditButton.setVisibility(View.GONE);
		actionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}
	
	public static ClaimItemDetailMileageFragment newInstance(int claimID, int claimItemID){
		ClaimItemDetailMileageFragment fragment = new ClaimItemDetailMileageFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMID, claimID);
		b.putInt(KEY_CLAIMITEMID, claimItemID);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		claimItemMileage = (ClaimItemMileage)activity.claimItem;
		View view = inflater.inflate(R.layout.fragment_claimitem_catmileage_details, null);

		((TextView)view.findViewById(R.id.tviews_claimitemdetail_id)).setText(claimItemMileage.getItemNumber());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_category)).setText(claimItemMileage.getCategoryName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_expenseDate)).setText(claimItemMileage.getExpenseDate(app));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amount)).setText(SaltApplication.decimalFormat.format(claimItemMileage.getForeignAmount())+" "+claimItemMileage.getForeignCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amountLocal)).setText(SaltApplication.decimalFormat.format(claimItemMileage.getLocalAmount()) + " " + claimItemMileage.getLocalCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_exRate)).setText(String.valueOf(claimItemMileage.getStandardExchangeRate()));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_desc)).setText(claimItemMileage.getDescription());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_project)).setText(claimItemMileage.getProjectName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_taxRate)).setText(claimItemMileage.isTaxApplied()?String.valueOf(claimItemMileage.getTaxAmount()):"No");
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_mileagefrom)).setText(claimItemMileage.getMileageFrom());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_mileageto)).setText(claimItemMileage.getMileageTo());
		((CheckBox)view.findViewById(R.id.cboxs_claimiteminput_mileagereturn)).setChecked(claimItemMileage.isReturn());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_mileagemileage)).setText(String.valueOf((int) claimItemMileage.getMileageMileage()));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_mileagetype)).setText((claimItemMileage.getMileageType()==1)?"KM":"MI");
		containersAttendees = (LinearLayout)view.findViewById(R.id.containers_claimitemdetail_attendees);
		if(claimItemMileage.isBillable()){
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText(claimItemMileage.getBillableCompanyName());
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_notesorbillstoclient)).setText(claimItemMileage.getNotes());
		}else{
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText("No");
			view.findViewById(R.id.trs_claimitemdetail_notestoclient).setVisibility(View.GONE);
		}

		attachment = (TextView)view.findViewById(R.id.tviews_claimitemdetail_attachment);
		if(claimItemMileage.hasReceipt()){
			attachment.setText(claimItemMileage.getAttachmentName());
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
