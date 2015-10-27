package applusvelosi.projects.android.salt.views.fragments.claims;

import java.io.File;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.FileManager.AttachmentDownloadListener;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimItemDetailGenericFragment extends ActionbarFragment implements AttachmentDownloadListener{
	private static final String KEY_CLAIMID = "myclaimitemdetailclaimIDkey";
	private static final String KEY_CLAIMITEMID = "myclaimitemdetailclaimitemIDKey";
	//action bar buttons
	private TextView actionbarEditButton, actionbarTitle;
	private RelativeLayout actionbarBackButton;
	
	private TableRow trAttendees;
	private ClaimHeader claimHeader;
	private ClaimItem claimItem;
	private TextView attachment;
	private SaltProgressDialog pd;
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backedit, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText("Claim Item Detail");
		
		actionbarBackButton.setOnClickListener(this);
		actionbarEditButton.setOnClickListener(this);;
		actionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}
	
	public static ClaimItemDetailGenericFragment newInstance(int claimID, int claimItemID){
		ClaimItemDetailGenericFragment fragment = new ClaimItemDetailGenericFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMID, claimID);
		b.putInt(KEY_CLAIMITEMID, claimItemID);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		claimHeader = app.getMyClaims().get(getArguments().getInt(KEY_CLAIMID));
		claimItem = claimHeader.getClaimItems(app).get(getArguments().getInt(KEY_CLAIMITEMID));
		View view = inflater.inflate(R.layout.fragment_claimitem_catgeneric_details, null);
		
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_id)).setText(claimItem.getItemNumber());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_category)).setText(claimItem.getCategoryName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_expenseDate)).setText(claimItem.getExpenseDate());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amount)).setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, claimItem.getForeignAmount())+" "+claimItem.getForeignCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_amountLocal)).setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, claimItem.getLocalAmount())+" "+claimItem.getLocalCurrencyName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_exRate)).setText(String.valueOf(claimItem.getOnFileExchangeRate()));
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_desc)).setText(claimItem.getDescription());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_project)).setText(claimItem.getProjectName());
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_taxRate)).setText(claimItem.isTaxApplied()?String.valueOf(claimItem.getTaxAmount()):"No");
		trAttendees = (TableRow)view.findViewById(R.id.trs_claimitemdetail_attendees);
		trAttendees.setOnClickListener(this);
		((TextView)view.findViewById(R.id.tviews_claimitemdetail_attendeecount)).setText(String.valueOf(claimItem.getAttendees().size()));
		if(claimItem.isBillable()){
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText(claimItem.getBillableCompanyName());			
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_notesorbillstoclient)).setText(claimItem.getNotes());
		}else{
			((TextView)view.findViewById(R.id.tviews_claimitemdetail_billto)).setText("No");
			view.findViewById(R.id.trs_claimitemdetail_notestoclientheader).setVisibility(View.GONE);
			view.findViewById(R.id.separators_claimitemdetail_notestoclientheader1).setVisibility(View.GONE);
			view.findViewById(R.id.separators_claimitemdetail_notestoclientheader2).setVisibility(View.GONE);
			view.findViewById(R.id.trs_claimitemdetail_notestoclient).setVisibility(View.GONE);
			view.findViewById(R.id.separators_claimitemdetail_notestoclient);
		}
		
		attachment = (TextView)view.findViewById(R.id.tviews_claimitemdetail_attachment);
		if(claimItem.hasReceipt()){
			attachment.setText(claimItem.getAttachmentName());
			attachment.setTextColor(activity.getResources().getColor(R.color.orange_velosi));
			attachment.setPaintFlags(attachment.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);			
			attachment.setTypeface(null, Typeface.BOLD);
			attachment.setOnClickListener(this);
		}else{
			attachment.setText("No");
		}
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton){
			activity.onBackPressed();
		}else if(v == actionbarTitle){
			activity.onBackPressed();
		}else if(v == actionbarEditButton){
			claimHeader.prepareForUpdatingClaimItem(getArguments().getInt(KEY_CLAIMITEMID), app);
			if(claimHeader.getTypeID() == ClaimHeader.TYPEKEY_ADVANCES)
				activity.changeChildPage(ItemInputFragmentBA.newInstance(getArguments().getInt(KEY_CLAIMID), getArguments().getInt(KEY_CLAIMITEMID)));
			else
				activity.changeChildPage(ItemInputFragmentClaims.newInstanceForEditingClaimItem(getArguments().getInt(KEY_CLAIMID), getArguments().getInt(KEY_CLAIMITEMID)));
		}else if(v == trAttendees){
			activity.changeChildPage(ClaimItemAttendeeListFragment.newInstance(getArguments().getInt(KEY_CLAIMID), getArguments().getInt(KEY_CLAIMITEMID)));
		}else if(v == attachment){
			try{
				if(pd == null)
					pd = new SaltProgressDialog(activity);
				app.fileManager.downloadAttachment(activity, claimItem, pd, this);
			}catch(Exception e){
				app.showMessageDialog(activity, e.getMessage());
			}
        }
	}

	@Override
	public void onAttachmentDownloadFinish(File file) {
    	try {
			app.fileManager.openAttachment(activity, claimItem.getAttachmentExtension(), file);
		} catch (Exception e) {
			e.printStackTrace();
			((SaltApplication)activity.getApplication()).showMessageDialog(activity, e.getMessage());
		}            								
	}
	
	@Override
	public void onAttachmentDownloadFailed(String errorMessage) {
		app.showMessageDialog(activity, "Download Failed! "+errorMessage);
	}
}
