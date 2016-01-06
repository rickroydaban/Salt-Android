package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import applusvelosi.projects.android.salt.R;

public class ItemInputFragmentMileage extends ItemInputFragment {

	@Override
	protected View createClaimItemtView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_claimitem_editmileage, null);
	}

	@Override
	protected void saveToServer() {

	}

}
