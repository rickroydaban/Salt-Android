package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

public abstract class ClaimHeaderFragment extends HomeActionbarFragment {
	protected static final String KEY = "myclaimdetailgetclaimforappwithposition";
	//action bar buttons
	protected TextView actionbarEditButton, actionbarProcessButton, actionbarCancelButton, textviewActionbarTitle;
	protected RelativeLayout actionbarBackButton;
	protected ClaimHeader claim;
	protected int itemCnt;
	protected Animation flashAnimation;
	protected RelativeLayout containerLineItem;
	private SaltProgressDialog pd;
		
	@Override
	protected RelativeLayout setupActionbar() {
		flashAnimation = new AlphaAnimation(0.0f, 1.0f);
		flashAnimation.setDuration(500); //You can manage the blinking time with this parameter
		flashAnimation.setRepeatMode(Animation.REVERSE);
		flashAnimation.setRepeatCount(Animation.INFINITE);
		
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_claimdetails, null);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarProcessButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_process);
		actionbarCancelButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_cancel);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		textviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		textviewActionbarTitle.setText(getActionbarTitle());
		claim = app.getMyClaims().get(getArguments().getInt(KEY));

        if(claim.getStatusID() == ClaimHeader.STATUSKEY_OPEN){
            actionbarEditButton.setOnClickListener(this);
            actionbarCancelButton.setOnClickListener(this);
        }else{
            actionbarEditButton.setVisibility(View.GONE);
            actionbarCancelButton.setVisibility(View.GONE);
        }
		actionbarProcessButton.setOnClickListener(this);
		actionbarBackButton.setOnClickListener(this);
		textviewActionbarTitle.setOnClickListener(this);
		return actionbarLayout;
	}

	protected void manageActionbarControls(){
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
		}else if(v == containerLineItem) {
            activity.changeChildPage(ClaimItemListFragment.newInstance(getArguments().getInt(KEY)));
		}
	}

    protected abstract String getActionbarTitle();
}
