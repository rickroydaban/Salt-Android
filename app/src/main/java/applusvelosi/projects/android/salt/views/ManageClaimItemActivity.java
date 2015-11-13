package applusvelosi.projects.android.salt.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;

/**
 * Created by Velosi on 11/13/15.
 */
public class ManageClaimItemActivity extends FragmentActivity {
    private SaltApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        app = (SaltApplication)getApplication();
    }
}
