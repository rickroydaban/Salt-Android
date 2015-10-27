package applusvelosi.projects.android.salt.views.fragments.homepages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;

public class OverviewClaimsFragment extends Fragment{
	private static OverviewClaimsFragment instance;
	
	public static OverviewClaimsFragment getInstance(){
		if(instance == null)
			instance = new OverviewClaimsFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_overviewclaims, null);
		
		((TextView)v.findViewById(R.id.labels_overviewclaims_open)).setText("1");
		((TextView)v.findViewById(R.id.labels_overviewclaims_submitted)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewclaims_approvedbymanager)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewclaims_approvedbyaccounts)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewclaims_forapproval)).setText("0");

		return v;
	}
}
