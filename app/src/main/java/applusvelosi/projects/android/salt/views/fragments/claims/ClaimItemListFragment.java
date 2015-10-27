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
import applusvelosi.projects.android.salt.adapters.lists.ClaimItemAdapter;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.interfaces.LoaderInterface;
import applusvelosi.projects.android.salt.utils.threads.ClaimItemLoader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimItemListFragment extends ActionbarFragment implements OnItemClickListener, LoaderInterface{
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
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backrefreshnew, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
		actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_add);
		actionbarTitleTextview = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		
		actionbarBackButton.setOnClickListener(this);
		actionbarRefreshButton.setOnClickListener(this);
		actionbarNewButton.setOnClickListener(this);
		actionbarTitleTextview.setOnClickListener(this);

		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		claimHeader = app.getMyClaims().get(getArguments().getInt(KEY));
		claimItems = new ArrayList<ClaimItem>();
		claimItems.addAll(claimHeader.getClaimItems(app));
		actionbarTitleTextview.setText("Claim Items ("+claimItems.size()+")");
		View view = li.inflate(R.layout.fragment_claimitemlist, null);
		lv = (ListView)view.findViewById(R.id.lists_claimItemList);		
		adapter = new ClaimItemAdapter(activity, claimItems);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitleTextview){
			activity.onBackPressed();
		}else if(v == actionbarNewButton){
			claimHeader.prepareForCreatingNewClamItem(app);
			if(claimHeader.getTypeID() == ClaimHeader.TYPEKEY_ADVANCES)
				activity.changeChildPage(ItemInputFragmentBA.newInstance(getArguments().getInt(KEY), -1));
			else
				activity.changeChildPage(ItemInputFragmentClaims.newInstanceForCreatingNewClaimItem(getArguments().getInt(KEY)));
		}else if(v == actionbarRefreshButton){
			if(pd == null)
				pd = new SaltProgressDialog(activity);
			
			pd.show();
			new Thread(new ClaimItemLoader(app, claimHeader.getClaimID(), this)).start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if(claimHeader.getClaimItems(app).get(pos).getCategoryTypeID() == Category.TYPE_MILEAGE) //type mileage
			activity.changeChildPage(ClaimItemDetailMileageFragment.newInstance(getArguments().getInt(KEY), pos));
		else
			activity.changeChildPage(ClaimItemDetailGenericFragment.newInstance(getArguments().getInt(KEY), pos));
	}

	@Override
	public void onLoadSuccess(Object claimItems) {
		pd.dismiss();
		this.claimItems.clear();
		this.claimItems.addAll((ArrayList<ClaimItem>)claimItems);
		adapter.notifyDataSetChanged();
		claimHeader.updateLineItems(this.claimItems, app);
	}

	@Override
	public void onLoadFailed(String failureMessage) {
		pd.dismiss();
		app.showMessageDialog(activity, failureMessage);
	}
}
