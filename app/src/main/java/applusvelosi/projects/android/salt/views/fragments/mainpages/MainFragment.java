package applusvelosi.projects.android.salt.views.fragments.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.HomeFragment;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class MainFragment extends Fragment{
	private static MainFragment instance;
	private HomeActivity activity;
	private View view; //global visibility to avoid NPE on resume for setupActionbar
	private RelativeLayout actionbar;
	public RootFragment currRootFragment;
	
	public static MainFragment getInstance(HomeActivity key){
		if(instance == null)
			instance = new MainFragment();
		
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (HomeActivity)getActivity();
		view = inflater.inflate(R.layout.fragment_main, null);
		
		actionbar = (RelativeLayout)view.findViewById(R.id.actionbar_top);		
		changePage(new HomeFragment());
		return view;
	}
		
	public void changePage(ActionbarFragment fragment){
		if(fragment == null)
			fragment = HomeFragment.getInstance();
		
		if(fragment instanceof RootFragment){
			currRootFragment = (RootFragment) fragment;
			for(int i=activity.getSupportFragmentManager().getBackStackEntryCount(); i>0; i--)
				activity.getSupportFragmentManager().popBackStack();

			activity.getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, fragment).commit();		
		}else		
			activity.getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.activity_view, fragment).commit();		

	}
	
	public void setupActionbar(RelativeLayout actionbarContentViews){	
		if(actionbar!=null && actionbarContentViews!=null){
			actionbar.removeAllViews();
			actionbar.addView(actionbarContentViews);									
		}else{
			activity.startActivity(new Intent(activity, HomeActivity.class));
			activity.finish();
		}
	}
}
