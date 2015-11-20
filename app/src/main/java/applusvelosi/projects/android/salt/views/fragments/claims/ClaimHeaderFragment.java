package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.ClaimDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public abstract class ClaimHeaderFragment extends LinearNavActionbarFragment {
	protected static final String KEY = "myclaimdetailgetclaimforappwithposition";
	//action bar buttons
	protected TextView actionbarEditButton, actionbarProcessButton, actionbarCancelButton, textviewActionbarTitle;
	protected RelativeLayout actionbarBackButton;
	protected int itemCnt;
	protected Animation flashAnimation;
	protected RelativeLayout containerLineItem;

	private ClaimDetailActivity activity;
    protected ClaimHeader claim;

	@Override
	protected RelativeLayout setupActionbar() {
        activity = (ClaimDetailActivity)linearNavFragmentActivity;
        claim = activity.claimHeader;
        flashAnimation = new AlphaAnimation(0.0f, 1.0f);
		flashAnimation.setDuration(500); //You can manage the blinking time with this parameter
		flashAnimation.setRepeatMode(Animation.REVERSE);
		flashAnimation.setRepeatCount(Animation.INFINITE);
		
		RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_claimdetails, null);
		actionbarEditButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
		actionbarProcessButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_process);
		actionbarCancelButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_cancel);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		textviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		textviewActionbarTitle.setText(getActionbarTitle());

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

//	protected void manageActionbarControls(){
//		//generic management for claim controls
//		//to define specific controls for children claim types will have to override this method
//		if(claim.getStaffID() != app.getStaff().getStaffID()) //only the claimee can cancel his own request
//			actionbarCancelButton.setVisibility(View.GONE);
//		if(claim.getStatusID() == ClaimHeader.STATUSKEY_CANCELLED){
//			//no controls for cancelled  claims, just viewing
//			actionbarProcessButton.setVisibility(View.GONE);
//			actionbarCancelButton.setVisibility(View.GONE);
//			actionbarEditButton.setVisibility(View.GONE);
//		}else if(claim.getStatusID() == ClaimHeader.STATUSKEY_OPEN){
//			if(itemCnt>0)
//				actionbarProcessButton.setText("Submit");
//			else
//				actionbarProcessButton.setVisibility(View.GONE);
//		}else if(claim.getStatusID() == ClaimHeader.STATUSKEY_SUBMITTED){
//			actionbarEditButton.setVisibility(View.VISIBLE);
//			//will have to double check to ensure that only the assigned approver can approver the claim
//			if(app.getStaff().isAdmin() || claim.getApproverID()==app.getStaff().getStaffID()){
//				ArrayList<ClaimItem> claimItems = activity.claimHeader.getClaimItems(app);
//				int toBeProcessedItemCnt = 0;
//				for(ClaimItem claimItem :claimItems){
//					if(claimItem.getStatusID() == activity.claimHeader.getStatusID())
//						toBeProcessedItemCnt++;
//				}
//
//				if(toBeProcessedItemCnt>0)
//					actionbarProcessButton.setVisibility(View.GONE);
//			}else{
//				actionbarProcessButton.setText("Process");
//			}
//		}else{
//			actionbarEditButton.setVisibility(View.GONE);
//			actionbarCancelButton.setVisibility(View.GONE);
//		}
//	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == textviewActionbarTitle){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == actionbarEditButton){
			linearNavFragmentActivity.changePage(ClaimInputFragment.newInstance(getArguments().getInt(KEY)));
		}else if(v == containerLineItem) {
			linearNavFragmentActivity.changePage(new ClaimItemListFragment());
		}else if(v == actionbarCancelButton){
            actionbarEditButton.setEnabled(false);
            actionbarCancelButton.setEnabled(false);
            activity.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object tempResult;
                    try{
                        ClaimHeader newClaimHeader = new ClaimHeader(claim.getMap());
                        newClaimHeader.cancelClaim(app);
                        tempResult = app.onlineGateway.saveClaim(newClaimHeader.jsonize(app), claim.jsonize(app, 0));
                    }catch(Exception e){
                        e.printStackTrace();
                        tempResult = e.getMessage();
                    }

                    final Object result = tempResult;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            actionbarEditButton.setEnabled(true);
                            actionbarCancelButton.setEnabled(true);
                            if(result == null){
                                Toast.makeText(activity, "Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                activity.finishLoading();
                                linearNavFragmentActivity.finish();
                            }else{
                                activity.finishLoading(result.toString());
                            }
                        }
                    });
                }
            }).start();
        }
	}

    protected abstract String getActionbarTitle();
}
