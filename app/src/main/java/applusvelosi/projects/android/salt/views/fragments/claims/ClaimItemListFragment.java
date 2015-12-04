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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.ClaimDetailActivity;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

public class ClaimItemListFragment extends LinearNavActionbarFragment implements OnItemClickListener, ListAdapterInterface{
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarRefreshButton, actionbarNewButton;
	private TextView actionbarTitleTextview;
	
	private ListView lv;
	private ListAdapter adapter;
	private ArrayList<ClaimItem> claimItems;
	private ClaimDetailActivity activity;
	private ClaimHeader claimHeader;

	@Override
	protected RelativeLayout setupActionbar() {
		activity = (ClaimDetailActivity)getActivity();
		claimHeader = activity.claimHeader;
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backrefreshnew, null);
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
		adapter = new ListAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

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
			linearNavFragmentActivity.onBackPressed();
		}else if(v == actionbarNewButton){
//			claimHeader.prepareForCreatingNewClamItem(app);
            Intent intent = new Intent(linearNavFragmentActivity, NewClaimItemActivity.class);
            intent.putExtra(NewClaimItemActivity.INTENTKEY_CLAIMHEADER, app.gson.toJson(claimHeader.getMap()));
            startActivity(intent);
		}else if(v == actionbarRefreshButton){
			refresh();
		}
	}

	private void refresh(){
		linearNavFragmentActivity.startLoading();
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
						if(result instanceof String){
							linearNavFragmentActivity.finishLoading(result.toString());
						}else{
							linearNavFragmentActivity.finishLoading();
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
//		if(claimHeader.getClaimItems(app).get(pos).getCategoryTypeID() == Category.TYPE_MILEAGE) //type mileage
//			linearNavFragmentActivity.changePage(ClaimItemDetailMileageFragment.newInstance(getArguments().getInt(KEY), pos));
//		else
//			linearNavFragmentActivity.changePage(ClaimItemDetailGenericFragment.newInstance(getArguments().getInt(KEY), pos));
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ClaimItemCellHolder holder;

        if(view == null){
            holder = new ClaimItemCellHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_claim_itemoverview, null);
            holder.categoryTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_category);
            holder.receiptIV = (ImageView)view.findViewById(R.id.iviews_cells_claimitem_receipt);
            holder.amountFCTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_foreign);
            holder.dateExpensedTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_expensedate);
            holder.statusTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_status);
            holder.descTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_description);

            view.setTag(holder);
        }

        holder = (ClaimItemCellHolder)view.getTag();
        ClaimItem claimItem = claimItems.get(position);
        holder.categoryTV.setText(claimItem.getCategoryName());
        holder.amountFCTV.setText(SaltApplication.decimalFormat.format(claimItem.getForeignAmount())+" "+claimItem.getForeignCurrencyName());
        holder.dateExpensedTV.setText(claimItem.getExpenseDate());
        holder.receiptIV.setVisibility((claimItem.hasReceipt()) ? View.VISIBLE : View.GONE);
        holder.statusTV.setText(claimItem.getStatusName());
        holder.descTV.setText(claimItem.getDescription());

        return view;
    }

    @Override
    public int getCount() {
        return claimItems.size();
    }

    private class ClaimItemCellHolder{
        public ImageView receiptIV;
        public TextView categoryTV, statusTV, dateExpensedTV, amountFCTV, descTV;
    }

}
