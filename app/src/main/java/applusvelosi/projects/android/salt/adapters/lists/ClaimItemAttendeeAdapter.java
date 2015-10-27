package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class ClaimItemAttendeeAdapter extends BaseAdapter{
	HomeActivity activity;
	ArrayList<ClaimItemAttendee> attendees;
	
	public ClaimItemAttendeeAdapter(HomeActivity activity, ArrayList<ClaimItemAttendee> attendees){
		this.activity = activity;
		this.attendees = attendees;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		View view = recyclableView;
		Holder holder;
		
		if(view == null){
			holder = new Holder();
			view = activity.getLayoutInflater().inflate(R.layout.node_claimitemattendee, null);
			holder.nameTV = (TextView)view.findViewById(R.id.tviews_cells_claimitemattendee_name);
			holder.jobTV = (TextView)view.findViewById(R.id.tviews_cells_claimitemattendee_jobtitle);
			holder.descTV = (TextView)view.findViewById(R.id.tviews_cells_claimitemattendee_description);
			
			view.setTag(holder);
		}
		
		holder = (Holder)view.getTag();
		ClaimItemAttendee attendee = attendees.get(pos);
		holder.nameTV.setText(attendee.getName());
		holder.jobTV.setText(attendee.getJobTitle());
		holder.descTV.setText(attendee.getNote());
		
		return view;
	}

	@Override
	public int getCount() {
		return attendees.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return attendees.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	
	private class Holder{ 
		public TextView nameTV, jobTV, descTV;
	}
}
