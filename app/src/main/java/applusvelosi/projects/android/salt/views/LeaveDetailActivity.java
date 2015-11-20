package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.views.fragments.leaves.LeaveDetailsFragment;

/**
 * Created by Velosi on 11/17/15.
 */
public class LeaveDetailActivity extends LinearNavFragmentActivity {
    public static final String INTENTKEY_LEAVEPOS = "intentkeyleavepos";
    public int leavePos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leavePos = getIntent().getExtras().getInt(INTENTKEY_LEAVEPOS);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new LeaveDetailsFragment()).commit();
    }
}
