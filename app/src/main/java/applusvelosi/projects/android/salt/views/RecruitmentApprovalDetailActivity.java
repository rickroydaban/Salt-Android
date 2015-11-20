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
    public Recruitment recruitment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recruitment = new Recruitment((HashMap<String, Object>) app.gson.fromJson(getIntent().getExtras().getString(INTENTKEY_RECRUITMENT), app.types.hashmapOfStringObject));
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new RecruitmentForApprovalDetailFragment()).commit();
    }
}
