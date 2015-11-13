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

public class ClaimItemAdapter extends BaseAdapter{
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
		ClaimItem claimItem = claimItems.get(pos);
		holder.categoryTV.setText(claimItem.getCategoryName());
		holder.amountFCTV.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, claimItem.getForeignAmount())+" "+claimItem.getForeignCurrencyName());
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
		public TextView categoryTV, statusTV, dateExpensedTV, amountFCTV, descTV;
	}
}
