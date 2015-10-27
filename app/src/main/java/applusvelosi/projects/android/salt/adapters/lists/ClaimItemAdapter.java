package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class ClaimItemAdapter extends BaseAdapter implements OnClickListener{
	HomeActivity activity;
	ArrayList<ClaimItem> claimItems;
	
	public ClaimItemAdapter(HomeActivity activity, ArrayList<ClaimItem> claimItems){
		this.activity = activity;
		this.claimItems = claimItems;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		ClaimItemCellHolder holder;
		
		if(view == null){
			holder = new ClaimItemCellHolder();
			view = activity.getLayoutInflater().inflate(R.layout.node_claim_itemoverview, parent, false);
			holder.categoryTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_category);
			holder.amountLCTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_local);
			holder.buttonToggle = (RelativeLayout)view.findViewById(R.id.buttons_nodes_claimitem_detail);
			holder.containerOther = (LinearLayout)view.findViewById(R.id.containers_cells_claimitem_other);
			holder.amountFCTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_foreign);
			holder.dateExpensedTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_expensedate);
			holder.receiptIV = (ImageView)view.findViewById(R.id.iviews_cells_claimitem_receipt);
			holder.statusTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_status);
			holder.descTV = (TextView)view.findViewById(R.id.tviews_cells_claimitem_description);
			
			view.setTag(holder);
			holder.buttonToggle.setTag(false);
			holder.buttonToggle.setOnClickListener(this);
		}
		
		holder = (ClaimItemCellHolder)view.getTag();
		ClaimItem claimItem = claimItems.get(pos);
		holder.categoryTV.setText(claimItem.getCategoryName());
		holder.amountLCTV.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, claimItem.getLocalAmount())+" "+claimItem.getLocalCurrencyName());
		holder.amountFCTV.setText("In Foreign: "+String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, claimItem.getForeignAmount())+" "+claimItem.getForeignCurrencyName());
		holder.dateExpensedTV.setText(claimItem.getExpenseDate());
		holder.receiptIV.setVisibility((claimItem.hasReceipt())?View.VISIBLE:View.GONE);
		holder.statusTV.setText(claimItem.getStatusName());
		holder.descTV.setText(claimItem.getDescription());

		if(Boolean.parseBoolean((holder).buttonToggle.getTag().toString()))
			hideOtherDetails(holder);
		else
			showOtherDetails(holder);			
		
		return view;
	}

	@Override
	public int getCount() {
		return claimItems.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return claimItems.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class ClaimItemCellHolder{ 
		public ImageView receiptIV;
		public TextView categoryTV, statusTV, dateExpensedTV, amountLCTV, amountFCTV, descTV;
		public RelativeLayout buttonToggle;
		public LinearLayout containerOther;
	}

	@Override
	public void onClick(View v) {
		ClaimItemCellHolder holder = (ClaimItemCellHolder)((RelativeLayout)v.getParent()).getTag();
		if(Boolean.parseBoolean((holder).buttonToggle.getTag().toString()))
			hideOtherDetails(holder);
		else
			showOtherDetails(holder);			
	}
	
	private void showOtherDetails(ClaimItemCellHolder holder){
		((ImageView)holder.buttonToggle.getChildAt(0)).setImageDrawable((activity.getResources().getDrawable(R.drawable.icon_i_sel)));
		holder.amountLCTV.setText("In Local: "+holder.amountLCTV.getText());
		holder.containerOther.setVisibility(View.VISIBLE);
		holder.buttonToggle.setTag(true);
	}
	
	private void hideOtherDetails(ClaimItemCellHolder holder){
		((ImageView)holder.buttonToggle.getChildAt(0)).setImageDrawable((activity.getResources().getDrawable(R.drawable.icon_i)));		
		String[] lc = holder.amountLCTV.getText().toString().split(" ");
		holder.amountLCTV.setText(lc[1]);
		holder.containerOther.setVisibility(View.GONE);
		holder.buttonToggle.setTag(false);
	}
}
