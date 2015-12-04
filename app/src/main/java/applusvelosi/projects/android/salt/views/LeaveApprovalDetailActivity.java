package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import com.google.gson.Gson;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.utils.TypeHolder;
import applusvelosi.projects.android.salt.views.fragments.leaves.LeavesApprovalDetailFragment;

/**
 * Created by Velosi on 11/19/15.
 */
public class LeaveApprovalDetailActivity extends  LinearNavFragmentActivity{
    public static final String INTENTKEY_LEAVEJSON = "intentkeyleave";
    public Leave leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(app.gson == null){
            app.gson = new Gson();
            app.types = new TypeHolder();
            app.onlineGateway = new OnlineGateway(app);
        }

        leave = new Leave((HashMap<String, Object>) app.gson.fromJson(getIntent().getExtras().getString(INTENTKEY_LEAVEJSON), app.types.hashmapOfStringObject));
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new LeavesApprovalDetailFragment()).commit();
    }
}
