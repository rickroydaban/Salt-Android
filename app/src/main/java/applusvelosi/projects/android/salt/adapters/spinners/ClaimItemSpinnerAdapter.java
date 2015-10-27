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

public class ClaimItemSpinnerAdapter implements SpinnerAdapter{
	private ArrayList<String> values;
	private Activity activity;
	
	public ClaimItemSpinnerAdapter(Activity activity,ArrayList<String> values){
		this.activity = activity;
		this.values = values;
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null)
			view = LayoutInflater.from(activity).inflate(R.layout.node_claimitemcurrencyspinner_bg, null);
		
		((TextView)view).setText(values.get(pos));
		return view;
	}
	
	@Override
	public View getDropDownView(int pos, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView v = (TextView)inflater.inflate(R.layout.node_spinner_list, parent, false);
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
