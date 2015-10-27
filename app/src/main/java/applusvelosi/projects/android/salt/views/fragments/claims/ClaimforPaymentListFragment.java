package applusvelosi.projects.android.salt.views.fragments.claims;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.interfaces.DataGetterInterface;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimforPaymentListFragment extends ActionbarFragment implements DataGetterInterface, OnItemClickListener, OnItemLongClickListener,
																		  RootFragment{	
	//action bar buttons
	RelativeLayout actionbarRefreshButton, actionbarMenuButton, actionbarSearchButton;
	
	private ListView lv;
	private static ClaimforPaymentListFragment instance;
	
	public static ClaimforPaymentListFragment getInstance(){
		if(instance == null)
			instance = new ClaimforPaymentListFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	

	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_claimforapproval, null);
		actionbarSearchButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_search);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Claims for Approval");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarSearchButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);	
		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_claimforapprovallist, null);
		lv = (ListView)view.findViewById(R.id.lists_claimsforapproval);
		
//		if(app.getClaims() == null)
//			updateList();
//		else{
//			lv.setAdapter(new MyClaimsAdapter(activity, app.getClaims()));
//			lv.setOnItemClickListener(this);
//			lv.setOnItemLongClickListener(this);
//		}
		
		return view;
	}
	
	private void updateList(){
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//		activity.changePage(ClaimDetailFragment.newInstance(app.gson.toJson(app.getClaims().get(pos), app.types.claim)), true);
	}

	@Override
	public void updateDataSource() throws Exception {
	}

	@Override
	public void onSuccess() {
//		lv.setAdapter(new MyClaimsAdapter(activity, app.getClaims()));
//		lv.setOnItemClickListener(ClaimforPaymentListFragment.this);
//		lv.setOnItemLongClickListener(this);
	}

	@Override
	public Activity getSaltActivity() {
		return activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
//		Claim claim = app.getClaims().get(pos);
//		
//		new AlertDialog.Builder(activity).setMessage("Pending claim by "+claim.getStaffFullname())
//										 .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
//											
//											@Override
//											public void onClick(DialogInterface dialog, int which) {
//												dialog.dismiss();
//											}
//										})
//										.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
//											
//											@Override
//											public void onClick(DialogInterface dialog, int which) {
//												dialog.dismiss();
//											}
//										}).create().show();
//		
		return true;
	}	
	
	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarRefreshButton){
			updateList();
		}else if(v == actionbarSearchButton){
			//TODO
		}
	}

	@Override
	public void enableListButtonOnSidebarAnimationFinished() {
		actionbarMenuButton.setEnabled(true);
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
	}
}
