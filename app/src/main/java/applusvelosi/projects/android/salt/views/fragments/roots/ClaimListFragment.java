package applusvelosi.projects.android.salt.views.fragments.roots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.lists.MyClaimsAdapter;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.ClaimDetailActivity;
import applusvelosi.projects.android.salt.views.NewClaimHeaderActivity;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderBAFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderClaimFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderLiquidationFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimInputFragment;

public class ClaimListFragment extends RootFragment implements OnItemClickListener{
	private static ClaimListFragment instance;

	//action bar buttons
	private RelativeLayout actionbarMenuButton, actionbarRefreshButton, actionbarNewButton;
	
	private ListView lv;
	private MyClaimsAdapter adapter;

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
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_newclaim);
		((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("My Claims");
		
		actionbarMenuButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarNewButton.setOnClickListener(this);		

		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_claimlist, null);
		lv = (ListView)view.findViewById(R.id.lists_myclaims);		
		adapter = new MyClaimsAdapter(activity, app.getMyClaims());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void disableUserInteractionsOnSidebarShown() {
        lv.setEnabled(false);
	}

	@Override
	public void enableUserInteractionsOnSidebarHidden() {
		try{
			lv.setEnabled(true);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("SALTX "+e.getMessage());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
        updateList();
	}
	
	private void updateList(){
		activity.startLoading();
		new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                    tempResult = app.onlineGateway.getMyClaims();
                }catch(Exception e){
                    e.printStackTrace();
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
					if(result instanceof String){
						String errorMessage = (String)result;
						if(errorMessage.contains(SaltApplication.CONNECTION_ERROR)){
							activity.finishLoadingAndShowOutdatedData();
							ArrayList<HashMap<String, Object>> myClaimMaps = app.offlineGateway.deserializeMyClaims();
							ArrayList<ClaimHeader> claimHeaders = new ArrayList<ClaimHeader>();
							for(HashMap<String, Object> myClaimMap :myClaimMaps)
								claimHeaders.add(new ClaimHeader(myClaimMap));
							updateListSuccess(claimHeaders);
						}else
							activity.finishLoading(result.toString());
					}else{
						activity.finishLoading();
						updateListSuccess((ArrayList<ClaimHeader>)result);
					}
                    }
                });
            }
        }).start();
	}

	private void updateListSuccess(ArrayList<ClaimHeader> claimHeaders){
		ArrayList<ClaimHeader> tempClaimHeaders = new ArrayList<ClaimHeader>();
		tempClaimHeaders.clear();
		for(ClaimHeader myClaims :claimHeaders){
			if(myClaims.getStaffID() == app.getStaff().getStaffID())
				tempClaimHeaders.add(myClaims);
		}

		Collections.sort(tempClaimHeaders, new Comparator<ClaimHeader>() {
			@Override
			public int compare(ClaimHeader lhs, ClaimHeader rhs) {
				return rhs.getClaimID() - lhs.getClaimID();
			}
		});

		app.updateMyClaims(tempClaimHeaders);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Intent intent = new Intent(activity, ClaimDetailActivity.class);
        intent.putExtra(ClaimDetailActivity.INTENTKEY_CLAIMHEADERPOS, pos);
        startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarMenuButton){
			actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
			activity.toggleSidebar(actionbarMenuButton);
		}else if(v == actionbarNewButton){
            startActivity(new Intent(activity, NewClaimHeaderActivity.class));
		}else if(v == actionbarRefreshButton){
			updateList();
		}
	}
}
