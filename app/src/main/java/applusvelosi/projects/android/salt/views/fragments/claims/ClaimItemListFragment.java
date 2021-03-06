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
import applusvelosi.projects.android.salt.views.ClaimItemDetailActivity;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

public class ClaimItemListFragment extends LinearNavActionbarFragment implements OnItemClickListener, ListAdapterInterface{
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarRefreshButton, actionbarNewButton;
	private TextView actionbarTitleTextview;
	
	private ListView lv;
	private ListAdapter adapter;
	private TextView tvNoItems;
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
		actionbarTitleTextview.setText("Claim Item");
		View view = li.inflate(R.layout.fragment_claimitemlist, null);
		lv = (ListView)view.findViewById(R.id.lists_claimItemList);
		adapter = new ListAdapter(this);
		tvNoItems = (TextView)view.findViewById(R.id.tviews_listempty);

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(activity.shouldLoadLineItemOnResume)
			refresh();
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitleTextview){
			linearNavFragmentActivity.onBackPressed();
		}else if(v == actionbarNewButton){
            Intent intent = new Intent(linearNavFragmentActivity, ManageClaimItemActivity.class);
            intent.putExtra(ManageClaimItemActivity.INTENTKEY_CLAIMHEADERPOS, activity.getClaimHeaderPos());
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
						activity.shouldLoadLineItemOnResume = true;
						if(result instanceof String){
							linearNavFragmentActivity.finishLoading(result.toString());
						}else{
							linearNavFragmentActivity.finishLoading();
							activity.claimItems.clear();
							activity.claimItems.addAll((ArrayList<ClaimItem>) result);
							app.offlineGateway.serializeMyClaimItem(claimHeader.getClaimID(), activity.claimItems);
							if(activity.claimItems.size() > 0){
								tvNoItems.setVisibility(View.GONE);
								adapter.notifyDataSetChanged();
							}else{
								lv.setVisibility(View.VISIBLE);
							}
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Intent intent = new Intent(linearNavFragmentActivity, ClaimItemDetailActivity.class);
		intent.putExtra(ClaimItemDetailActivity.INTENTKEY_CLAIMITEM, activity.claimItems.get(pos));
        intent.putExtra(ClaimItemDetailActivity.INTENTKEY_CLAIMHEADERTYPE, activity.claimHeader.getTypeID());
        intent.putExtra(ClaimItemDetailActivity.INTENTKEY_CLAIMHEADERID, activity.claimHeader.getClaimID());
		intent.putExtra(ClaimItemDetailActivity.INTENTKEY_CLAIMHEADERSTATUS, activity.claimHeader.getStatusID());
		startActivity(intent);
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
			holder.dateExpensedTV.setTypeface(SaltApplication.myFont(activity));

            view.setTag(holder);
        }

        holder = (ClaimItemCellHolder)view.getTag();
        ClaimItem claimItem = activity.claimItems.get(position);
        holder.categoryTV.setText(claimItem.getCategoryName());
        holder.amountFCTV.setText(SaltApplication.decimalFormat.format(claimItem.getForeignAmount()) + " " + claimItem.getForeignCurrencyName());
        holder.dateExpensedTV.setText(claimItem.getExpenseDate(app));
        holder.receiptIV.setVisibility((claimItem.hasReceipt()) ? View.VISIBLE : View.GONE);
		if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYAPPROVER) holder.statusTV.setText("Approver (A)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYAPPROVER) holder.statusTV.setText("Approver (R)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYACCOUNTS) holder.statusTV.setText("Accounts (A)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYACCOUNTS) holder.statusTV.setText("Accounts (R)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYCOUNTRYMANAGER) holder.statusTV.setText("CM (A)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYCOUNTRYMANAGER) holder.statusTV.setText("CM (R)");
		else if(claimItem.getStatusID()==ClaimHeader.STATUSKEY_PAIDUNDERCOMPANYCARD) holder.statusTV.setText("Paid by CC");
        else holder.statusTV.setText(claimItem.getStatusName());
		if(claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYACCOUNTS || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYAPPROVER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_APPROVEDBYCOUNTRYMANAGER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_PAID || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_PAIDUNDERCOMPANYCARD)
			holder.statusTV.setTextColor(activity.getResources().getColor(R.color.green));
		else if(claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYACCOUNTS || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYAPPROVER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDBYCOUNTRYMANAGER || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_REJECTEDFORSALARYDEDUCTION || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_RETURN || claimHeader.getStatusID()==ClaimHeader.STATUSKEY_CANCELLED)
			holder.statusTV.setTextColor(activity.getResources().getColor(R.color.red));
		else
			holder.statusTV.setTextColor(activity.getResources().getColor(R.color.black));

		if(claimItem.getDescription().length() < 1)
			holder.descTV.setVisibility(View.GONE);
		else{
			holder.descTV.setVisibility(View.VISIBLE);
			holder.descTV.setText(claimItem.getDescription());
		}

        return view;
    }

    @Override
    public int getCount() {
        return activity.claimItems.size();
    }

    private class ClaimItemCellHolder{
        public ImageView receiptIV;
        public TextView categoryTV, statusTV, dateExpensedTV, amountFCTV, descTV;
    }

}
