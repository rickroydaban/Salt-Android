package applusvelosi.projects.android.salt.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.views.HomeActivity;

public abstract class HomeActionbarFragment extends Fragment implements OnClickListener{
	protected HomeActivity activity;
	public SaltApplication app;
	protected abstract RelativeLayout setupActionbar();
	protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		activity = (HomeActivity)getActivity();
		app = (SaltApplication)activity.getApplication();
		
		activity.setupActionbar(setupActionbar());
		return createView(inflater, container, savedInstanceState);
	}
}
