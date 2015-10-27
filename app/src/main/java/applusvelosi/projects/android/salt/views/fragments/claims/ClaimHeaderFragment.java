package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.ClaimTrail;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.OfflineGateway.SerializableClaimTypes;
import applusvelosi.projects.android.salt.utils.interfaces.LoaderInterface;
import applusvelosi.projects.android.salt.utils.threads.ClaimItemLoader;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public abstract class ClaimHeaderFragment extends ActionbarFragment implements LoaderInterface{
	protected static final String KEY = "myclaimdetailgetclaimforappwithposition";
	//action bar buttons
	protected TextView actionbarEditButton, actionbarTrailsButton, actionbarProcessButton, actionbarCancelButton, textviewActionbarTitle;
	protected RelativeLayout actionbarBackButton;
	protected TextView tvLineItem;
	protected ClaimHeader claim;
	protected int itemCnt;
	protected Animation flashAnimation;
	
	private SaltProgressDialog pd;
		
	@Override
	protected RelativeLayout setupActionbar() {
		flashAnimation = new AlphaAnimation(0.0f, 1.0f);
		flashAnimation.setDuration(500); //You can manage the blinking time with this parameter
		flashAnimation.setRepeatMode(Animation.REVERSE);
		flashAnimation.setRepeatCount(Animation.INFINITE);
		
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_claimdetails, null);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarTrailsButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_trails);
		actionbarProcessButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_process);
		actionbarCancelButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_cancel);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		textviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		textviewActionbarTitle.setText("Claim");
		claim = app.getMyClaims().get(getArguments().getInt(KEY));
		
		actionbarEditButton.setOnClickListener(this);
		actionbarTrailsButton.setOnClickListener(this);
		actionbarProcessButton.setOnClickListener(this);
		actionbarCancelButton.setOnClickListener(this);
		actionbarBackButton.setOnClickListener(this);
		textviewActionbarTitle.setOnClickListener(this);
		return actionbarLayout;
	}
	
	protected void updateActionbar(){
		if(!claim.hasLoadedClaimItems()){
			pd = new SaltProgressDialog(activity);
			pd.show();
			new Thread(new ClaimItemLoader(app, claim.getClaimID(), this)).start();			
		}else
			manageActionbarControls();		
	}
	
	protected void manageActionbarControls(){
		itemCnt = claim.getClaimItems(app).size();
		if(itemCnt == 0)
			tvLineItem.setText("Add An Item");
		else if(itemCnt == 1)
			tvLineItem.setText("1 Item");
		else
			tvLineItem.setText(itemCnt+" Items");
		
		//generic management for claim controls
		//to define specific controls for children claim types will have to override this method
		if(claim.getStaffID() != app.getStaff().getStaffID()) //only the claimee can cancel his own request
			actionbarCancelButton.setVisibility(View.GONE);
		if(claim.getStatusID() == ClaimHeader.STATUSKEY_CANCELLED){
			//no controls for cancelled  claims, just viewing
			actionbarProcessButton.setVisibility(View.GONE);
			actionbarCancelButton.setVisibility(View.GONE);
			actionbarEditButton.setVisibility(View.GONE);
		}else if(claim.getStatusID() == ClaimHeader.STATUSKEY_OPEN){
			if(itemCnt>0)
				actionbarProcessButton.setText("Submit");
			else
				actionbarProcessButton.setVisibility(View.GONE);
		}else if(claim.getStatusID() == ClaimHeader.STATUSKEY_SUBMITTED){
			actionbarEditButton.setVisibility(View.VISIBLE);
			//will have to double check to ensure that only the assigned approver can approver the claim
			if(app.getStaff().isAdmin() || claim.getApproverID()==app.getStaff().getStaffID()){
				ArrayList<ClaimItem> claimItems = claim.getClaimItems(app);
				int toBeProcessedItemCnt = 0;
				for(ClaimItem claimItem :claimItems){
					if(claimItem.getStatusID() == claim.getStatusID())
						toBeProcessedItemCnt++;
				}
				
				if(toBeProcessedItemCnt>0)
					actionbarProcessButton.setVisibility(View.GONE);
			}else{
				actionbarProcessButton.setText("Process");
			}
		}else{
			actionbarEditButton.setVisibility(View.GONE);
			actionbarCancelButton.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == textviewActionbarTitle){
			activity.onBackPressed();
		}else if(v == actionbarEditButton){
			activity.changeChildPage(ClaimInputFragment.newInstance(getArguments().getInt(KEY)));
		}else if(v == tvLineItem){ //claim items on the list
			activity.changeChildPage(ClaimItemListFragment.newInstance(getArguments().getInt(KEY)));
		}else if(v == actionbarTrailsButton){
			activity.changeChildPage(ClaimTrailFragment.newInstance(claim.getClaimID(), app.gson.toJson(new ArrayList<ClaimTrail>(), app.types.arrayListOfClaimTrails)));
		}
	}	
	
	@Override
	public void onLoadSuccess(Object claimItems) {
		claim.updateLineItems((ArrayList<ClaimItem>)claimItems, app);
		manageActionbarControls();
		app.offlineGateway.serializeClaimHeaders(app.getMyClaims(), SerializableClaimTypes.MY_CLAIM);
		pd.dismiss();
	}

	@Override
	public void onLoadFailed(String failureMessage) {
		pd.dismiss();
		app.showMessageDialog(activity, failureMessage);
	}
		
}
