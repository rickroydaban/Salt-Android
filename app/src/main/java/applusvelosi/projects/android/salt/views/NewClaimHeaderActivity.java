package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderInputType;

/**
 * Created by Velosi on 11/18/15.
 */
public class NewClaimHeaderActivity extends LinearNavFragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderInputType()).commit();
    }
}
