package applusvelosi.projects.android.salt.models;

import android.app.Activity;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;

public class GroupedListHeaderMarginedTop implements GroupedListItemInterface{
	private String headerLabel;

	public GroupedListHeaderMarginedTop(String headerLabel){
		this.headerLabel = headerLabel;
	}
	
	@Override
	public TextView getTextView(Activity activity) {
		TextView tv = (TextView)activity.getLayoutInflater().inflate(R.layout.tv_sidebar_header_maginedtop, null);
		tv.setText(headerLabel);
		tv.setEnabled(false);
		
		return tv;
	}

}
