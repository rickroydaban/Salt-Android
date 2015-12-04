package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.views.fragments.recruitment.RecruitmentForApprovalDetailFragment;

/**
 * Created by Velosi on 11/19/15.
 */
public class RecruitmentApprovalDetailActivity extends LinearNavFragmentActivity {
    public static final String INTENTKEY_RECRUITMENT = "intentkeyrecruitment";
    public Recruitment tempRecruitment, recruitment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempRecruitment = (Recruitment)getIntent().getExtras().getSerializable(INTENTKEY_RECRUITMENT);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new RecruitmentForApprovalDetailFragment()).commit();
    }
}
