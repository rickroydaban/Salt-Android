package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.MyClaimsAdapter;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.ClaimSearchFragment;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimListFragment extends ActionbarFragment implements OnItemClickListener, RootFragment{
	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarSearchButton, actionbarRefreshButton, actionbarNewButton;
	
	private ListView lv;
	private MyClaimsAdapter adapter;
	private ArrayList<ClaimHeader> claimHeaders;
	private static ClaimListFragment instance;
	private SaltProgressDialog pd;
	
	public static ClaimListFragment getInstance(){
		if(instance == null)
			instance = new ClaimListFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	

	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_claimlist, null);
		actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
		actionbarSearchButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_search);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newclaim);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("My Claims");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarSearchButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarNewButton.setOnClickListener(this);		

		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_claimlist, null);
		lv = (ListView)view.findViewById(R.id.lists_myclaims);		
		claimHeaders = new ArrayList<ClaimHeader>();
		adapter = new MyClaimsAdapter(activity, claimHeaders);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refetchClaims();
	}
	
	private void updateList(){
		if(pd == null)
			pd = new SaltProgressDialog(activity);
		
//		app.updatenMyClaims(pd, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		int typeID = claimHeaders.get(pos).getTypeID();
		if(typeID == ClaimHeader.TYPEKEY_CLAIMS){
			if(Boolean.parseBoolean(claimHeaders.get(pos).getMap().get("IsPaidByCompanyCC").toString()))
				activity.changeChildPage(ClaimHeaderClaimFragment.newInstance(pos));
			else
				activity.changeChildPage(ClaimHeaderClaimFragment.newInstance(pos));
		}else if(typeID == ClaimHeader.TYPEKEY_ADVANCES)
			activity.changeChildPage(ClaimHeaderBAFragment.newInstance(pos));
		else if(typeID == ClaimHeader.TYPEKEY_LIQUIDATION)
			activity.changeChildPage(ClaimHeaderLiquidationFragment.newInstance(pos));
	}
	
	private void refetchClaims(){
		claimHeaders.clear();
		for(ClaimHeader myClaims :app.getMyClaims()){
			if(myClaims.getStaffID() == app.getStaff().getStaffID())
				claimHeaders.add(myClaims);
		}
		
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarNewButton){
			activity.changeChildPage(new ClaimInputFragment());			
		}else if(v == actionbarRefreshButton){
			updateList();
		}else if(v == actionbarSearchButton){
			activity.changeChildPage(new ClaimSearchFragment());
		}
	}

	@Override
	public void enableListButtonOnSidebarAnimationFinished() {
		actionbarMenuButton.setEnabled(true);
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		try{
			lv.setEnabled(true);
		}catch(NullPointerException e){
			System.out.println("Null pointer exception at ClaimListFragment enableUserInteractionOnSidebarHidden()");			
		}
	}

//	@Override
//	public void onMyLeavesLoadSuccess() {
//		refetchClaims();
//	}
//
//	@Override
//	public void onMyLeavesLoadFailed(String errorMessage) {
//		app.showMessageDialog(activity, errorMessage);
//	}
}
