package applusvelosi.projects.android.salt.views.fragments.homepages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;

public class OverviewAdvancesFragment extends Fragment{
	private static OverviewAdvancesFragment instance;
	
	public static OverviewAdvancesFragment getInstance(){
		if(instance == null)
			instance = new OverviewAdvancesFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}		
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_overviewadvances, null);
		
		((TextView)v.findViewById(R.id.labels_overviewadvances_open)).setText("1");
		((TextView)v.findViewById(R.id.labels_overviewadvances_submitted)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewadvances_approvedByManager)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewadvances_approvedByDirector)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewadvances_approvedByAccounts)).setText("0");
		((TextView)v.findViewById(R.id.labels_overviewadvances_forApproval)).setText("0");
		
		return v;
	}

}
