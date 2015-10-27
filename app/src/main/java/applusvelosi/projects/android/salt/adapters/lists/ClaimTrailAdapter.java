package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.ClaimTrail;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimTrailFragment;

public class ClaimTrailAdapter extends BaseAdapter{
	private ClaimTrailFragment frag;
	private ArrayList<ClaimTrail> claimTrails;
	
	public ClaimTrailAdapter(ClaimTrailFragment frag, ArrayList<ClaimTrail> claimTrails){
		this.frag = frag;
		this.claimTrails = claimTrails;
	}
	
	@Override
	public View getView(int pos, View reusableView, ViewGroup parent) {
		View v = reusableView;
		ClaimTrailHolder holder;
		if(v == null){
			holder = new ClaimTrailHolder();
			v = frag.getActivity().getLayoutInflater().inflate(R.layout.cell_claim_trail, null);
			holder.tvName = (TextView)v.findViewById(R.id.tviews_cell_claim_trail_name);
			holder.tvDate = (TextView)v.findViewById(R.id.tviews_cell_claim_trail_date);
			holder.tvComment = (TextView)v.findViewById(R.id.tviews_cell_claim_trail_comment);
			v.setTag(holder);
		}
		
		holder = (ClaimTrailHolder)v.getTag();
		ClaimTrail claimTrail = claimTrails.get(pos);
		holder.tvName.setText(claimTrail.getName());
		holder.tvDate.setText(claimTrail.getDate());
		holder.tvComment.setText(claimTrail.getComment());
		
		return v;
	}
	
	@Override
	public int getCount() {
		return claimTrails.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	private class ClaimTrailHolder{
		private TextView tvName, tvDate, tvComment;
	}
}
