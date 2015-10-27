package applusvelosi.projects.android.salt.adapters.spinners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;

public class SimpleSpinnerAdapter implements SpinnerAdapter{
	public enum NodeSize{
		SIZE_NORMAL, SIZE_SMALL;
	}
	
	private ArrayList<String> values;
	private Activity activity;
	private NodeSize nodeSize;
	
	public SimpleSpinnerAdapter(Activity activity,ArrayList<String> values, NodeSize nodeSize){
		this.activity = activity;
		this.values = values;
		this.nodeSize = nodeSize;
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null)
			view = LayoutInflater.from(activity).inflate((nodeSize == NodeSize.SIZE_NORMAL)?R.layout.node_spinner_bg:R.layout.node_spinner_bg_smal, parent, false);

		((TextView)view).setText(values.get(pos));
		return view;
	}
	
	@Override
	public View getDropDownView(int pos, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView v = (TextView)inflater.inflate((nodeSize == NodeSize.SIZE_NORMAL)?R.layout.node_spinner_list:R.layout.node_spinner_list_small, parent, false);
		v.setText(values.get(pos));
		
		return v;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int pos) {
		return values.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int type) {
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return values.size();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
	}
}
