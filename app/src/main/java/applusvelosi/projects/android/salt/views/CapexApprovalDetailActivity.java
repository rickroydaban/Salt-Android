package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.views.fragments.capex.CapexForApprovalDetailFragment;
import applusvelosi.projects.android.salt.views.fragments.recruitment.RecruitmentForApprovalDetailFragment;

/**
 * Created by Velosi on 11/19/15.
 */
public class CapexApprovalDetailActivity extends LinearNavFragmentActivity {
    public static final String INTENTKEY_CAPEXHEADERID = "intentkeycapexheaderid";
    public int capexHeaderID;
    public CapexHeader capexHeader;
    public ArrayList<CapexLineItem> capexLineItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        capexHeaderID = getIntent().getExtras().getInt(INTENTKEY_CAPEXHEADERID);
        capexLineItems = new ArrayList<CapexLineItem>();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new CapexForApprovalDetailFragment()).commit();
    }

}
