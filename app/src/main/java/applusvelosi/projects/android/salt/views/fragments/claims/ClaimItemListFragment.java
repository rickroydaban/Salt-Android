package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

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
import applusvelosi.projects.android.salt.adapters.lists.ClaimItemAdapter;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

public class ClaimItemListFragment extends HomeActionbarFragment implements OnItemClickListener{
	private static String KEY = "claimItemListFragmentKey";
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarRefreshButton, actionbarNewButton;
	private TextView actionbarTitleTextview;
	
	private SaltProgressDialog pd;
	private ListView lv;
	private ClaimItemAdapter adapter;
	private ClaimHeader claimHeader;
	private ArrayList<ClaimItem> claimItems;

	public static ClaimItemListFragment newInstance(int position){
		ClaimItemListFragment fragment = new ClaimItemListFragment();
		Bundle b = new Bundle();
		b.putInt(KEY, position);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
        claimHeader = app.getMyClaims().get(getArguments().getInt(KEY));
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backrefreshnew, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_add);
		actionbarTitleTextview = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		
		actionbarBackButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarTitleTextview.setOnClickListener(this);

        if(claimHeader.getStatusID() == ClaimHeader.STATUSKEY_OPEN)
            actionbarNewButton.setOnClickListener(this);
        else
            actionbarNewButton.setVisibility(View.GONE);
        return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		claimItems = new ArrayList<ClaimItem>();
		claimItems.addAll(claimHeader.getClaimItems(app));
		actionbarTitleTextview.setText("Claim Item");
		View view = li.inflate(R.layout.fragment_claimitemlist, null);
		lv = (ListView)view.findViewById(R.id.lists_claimItemList);
		adapter = new ClaimItemAdapter(activity, claimItems);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

        pd = new SaltProgressDialog(activity);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitleTextview){
			activity.onBackPressed();
		}else if(v == actionbarNewButton){
			claimHeader.prepareForCreatingNewClamItem(app);
			if(claimHeader.getTypeID() == ClaimHeader.TYPEKEY_ADVANCES)
				activity.changeChildPage(ItemInputFragmentBA.newInstance(getArguments().getInt(KEY), -1));
			else{
				Intent intent = new Intent(activity, NewClaimItemActivity.class);
				intent.putExtra(NewClaimItemActivity.INTENTKEY_CLAIMHEADER, app.gson.toJson(claimHeader.getMap()));
				startActivity(intent);
			}
		}else if(v == actionbarRefreshButton){
			refresh();
		}
	}

	private void refresh(){
		pd.show();
		new Thread(new Runnable(){

			@Override
			public void run() {
				Object tempResult;
				try{
					tempResult = app.onlineGateway.getClaimItemsWithClaimID(claimHeader.getClaimID());
				}catch(Exception e){
					e.printStackTrace();
					tempResult = e.getMessage();
				}

				final Object result = tempResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
						if(result instanceof String){
							app.showMessageDialog(activity, result.toString());
						}else{
							claimItems.clear();
							claimItems.addAll((ArrayList<ClaimItem>)result);
							adapter.notifyDataSetChanged();
							claimHeader.updateLineItems(claimItems, app);
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if(claimHeader.getClaimItems(app).get(pos).getCategoryTypeID() == Category.TYPE_MILEAGE) //type mileage
			activity.changeChildPage(ClaimItemDetailMileageFragment.newInstance(getArguments().getInt(KEY), pos));
		else
			activity.changeChildPage(ClaimItemDetailGenericFragment.newInstance(getArguments().getInt(KEY), pos));
	}
}
