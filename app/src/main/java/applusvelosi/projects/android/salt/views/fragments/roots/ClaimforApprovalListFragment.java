package applusvelosi.projects.android.salt.views.fragments.roots;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.MyClaimsAdapter;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.interfaces.DataGetterInterface;

public class ClaimforApprovalListFragment extends RootFragment implements DataGetterInterface, OnItemClickListener, OnItemLongClickListener{
	//action bar buttons
	RelativeLayout actionbarSearchButton, actionbarRefreshButton, actionbarMenuButton;
	
	private ListView lv;
	private MyClaimsAdapter adapter;
	private ArrayList<ClaimHeader> claimHeaders;
	private static ClaimforApprovalListFragment instance;
	
	public static ClaimforApprovalListFragment getInstance(){
		if(instance == null)
			instance = new ClaimforApprovalListFragment();
		
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
		claimHeaders = new ArrayList<ClaimHeader>();
		adapter = new MyClaimsAdapter(activity, claimHeaders);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateList();
	}

	private void updateList(){
//		activity.startLoading();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Object tempResult;
//				try{
//					tempResult = app.onlineGateway.getap;
//				}catch(Exception e){
//					e.printStackTrace();
//					tempResult = e.getMessage();
//				}
//
//				final Object result = tempResult;
//				new Handler(Looper.getMainLooper()).post(new Runnable() {
//					@Override
//					public void run() {
//						if(result instanceof String){
//							activity.finishLoading(result.toString());
//						}else{
//							activity.finishLoading();
//							app.updateMyClaims((ArrayList<ClaimHeader>)result);
//							refetchClaims();
//						}
//					}
//				});
//			}
//		}).start();
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
//		lv.setOnItemClickListener(ClaimforApprovalListFragment.this);
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
		
		return true;
	}	
	
	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarRefreshButton){
//			updateList();
		}else if(v == actionbarSearchButton){
			//TODO
		}
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
		lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		lv.setEnabled(true);
	}

}
