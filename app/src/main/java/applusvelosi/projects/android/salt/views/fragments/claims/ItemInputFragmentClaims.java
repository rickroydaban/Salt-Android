package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;

public class ItemInputFragmentClaims extends ItemInputEditFragment {

	@Override
	protected View createClaimItemtView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_claimitem_editclaim, null);
	}

	@Override
	protected void saveToServer() {

	}
}
