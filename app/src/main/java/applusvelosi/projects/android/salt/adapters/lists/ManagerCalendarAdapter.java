package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.R.id;
import applusvelosi.projects.android.salt.models.ManagerCalendarItem;

public class ManagerCalendarAdapter extends BaseAdapter{
	private FragmentActivity activity;
	private ArrayList<ManagerCalendarItem> items;
	
	public ManagerCalendarAdapter(FragmentActivity activity, ArrayList<ManagerCalendarItem> items){
		this.activity = activity;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		return items.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		View v = view;
		NameOfficeHolder holder;
		
		if(v == null){			
			v = activity.getLayoutInflater().inflate(R.layout.node_simplenameoffice, null);
			holder = new NameOfficeHolder();
			holder.nameTV = (TextView)v.findViewById(id.nodes_mgrcal_name);
			holder.officeTV = (TextView)v.findViewById(R.id.nodes_mgrcal_office);
			v.setTag(holder);
		}
		
		holder = (NameOfficeHolder)v.getTag();
		ManagerCalendarItem item = items.get(pos);
		
		holder.nameTV.setText(item.getName());
		holder.officeTV.setText(item.getOffice());
		
		return v;
	}

	private class NameOfficeHolder{
		public TextView nameTV, officeTV;
	}
}
