package applusvelosi.projects.android.salt.views.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.roots.CapexesForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.ClaimforApprovalListFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.HomeFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RecruitmentsForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public class MainFragment extends Fragment{

	private HomeActivity activity;
	private RootFragment currRootFragment;
	private RelativeLayout actionbar;

    public RelativeLayout containersLoader;
    public TextView tviewsLoader;
    public ImageView ivLoader;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (HomeActivity)getActivity();
		View view = inflater.inflate(R.layout.fragment_main, null);

        containersLoader = (RelativeLayout)view.findViewById(R.id.containers_loader);
        tviewsLoader = (TextView)view.findViewById(R.id.tviews_loader);
        ivLoader = (ImageView)view.findViewById(R.id.iviews_loader);
        ((AnimationDrawable)ivLoader.getDrawable()).start();

        actionbar = (RelativeLayout)view.findViewById(R.id.actionbar_top);
		RootFragment toBeDisplayedFragment = HomeFragment.getInstance();
		if(activity.getIntent().hasExtra(HomeActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN)){
			switch (activity.getIntent().getExtras().getInt(HomeActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN)){
				case HomeActivity.AUTONAV_POS_LEAVESAPPROVAL: toBeDisplayedFragment = LeaveForApprovalFragment.getInstance(); break;
				case HomeActivity.AUTONAV_POS_CLAIMSAPPROVAL: toBeDisplayedFragment = ClaimforApprovalListFragment.getInstance(); break;
				case HomeActivity.AUTONAV_POS_CAPEXAPPROVAL: toBeDisplayedFragment = CapexesForApprovalFragment.getInstance(); break;
				case HomeActivity.AUTONAV_POS_RCMNTAPPROVAL: toBeDisplayedFragment = RecruitmentsForApprovalFragment.getInstance(); break;
				default: toBeDisplayedFragment = HomeFragment.getInstance();
			}
		}

		changePage(toBeDisplayedFragment);
		return view;
	}

	public void setupActionbar(RelativeLayout actionbarContentViews){
		if(actionbar!=null && actionbarContentViews!=null){
			actionbar.removeAllViews();
			actionbar.addView(actionbarContentViews);
		}else{
//			Toast.makeText(getActivity(), "Actionbar is null", Toast.LENGTH_SHORT).show();
			System.out.println("WHAT THE!");
		}
	}

	public void changePage(RootFragment fragment){
		currRootFragment = fragment;
		activity.getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, fragment).commit();
	}

	public RootFragment getCurrRootFragment(){ return currRootFragment; }

}
