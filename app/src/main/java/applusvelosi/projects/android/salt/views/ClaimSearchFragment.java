package applusvelosi.projects.android.salt.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

public class ClaimSearchFragment extends HomeActionbarFragment {
	RelativeLayout buttonActionbarCancel;
	TextView buttonActionbarSave, tviewActionbarTitle;
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
		buttonActionbarCancel = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		buttonActionbarSave = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
		tviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		tviewActionbarTitle.setText("Claims");
		
		buttonActionbarCancel.setOnClickListener(this);
		buttonActionbarSave.setOnClickListener(this);
		tviewActionbarTitle.setOnClickListener(this);
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_searchclaim, null);
		return v;
	}

	@Override
	public void onClick(View view) {
		if(view == buttonActionbarCancel || view == tviewActionbarTitle){
			activity.onBackPressed();
		}else if(view == buttonActionbarSave){
			app.showMessageDialog(activity, "Searched");
		}
	}

}
