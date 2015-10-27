package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class GroupedListAdapter extends BaseAdapter{
	private ArrayList<GroupedListItemInterface> items;
	private HomeActivity activity;
	
	public GroupedListAdapter(HomeActivity activity, ArrayList<GroupedListItemInterface> items){
		this.activity = activity;
		this.items = items;
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		return items.get(pos).getTextView(activity);
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
	public long getItemId(int pos) {
		return 0;
	}
}
