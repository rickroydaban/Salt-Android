package applusvelosi.projects.android.salt.adapters.lists;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class SimpleAdapter extends BaseAdapter{
	HomeActivity activity;
	ArrayList<String> titles;
	
	public SimpleAdapter(HomeActivity activity, ArrayList<String> titles){
		this.activity = activity;
		this.titles = titles;
	}
	
	@Override
	public View getView(int pos, View recyclableView, ViewGroup parent) {
		TextView label = (TextView)recyclableView;
		
		if(label == null)
			label = (TextView)activity.getLayoutInflater().inflate(R.layout.node_labelonly, null);
		
		label.setText(titles.get(pos));
		return label;
	}

	@Override
	public int getCount() {
		return titles.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return titles.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
}
