package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderBAFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderClaimFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderLiquidationFragment;

/**
 * Created by Velosi on 11/18/15.
 */
public class ClaimDetailActivity extends LinearNavFragmentActivity{
    public static final String INTENTKEY_CLAIMHEADER = "claimheaderkey";
    public ClaimHeader claimHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        claimHeader = new ClaimHeader((HashMap<String, Object>)app.gson.fromJson(getIntent().getExtras().getString(INTENTKEY_CLAIMHEADER), app.types.hashmapOfStringObject));

		int typeID = claimHeader.getTypeID();
		if(typeID == ClaimHeader.TYPEKEY_CLAIMS){
//			if(Boolean.parseBoolean(claimHeader.getMap().get("IsPaidByCompanyCC").toString()))
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderClaimFragment()).commit();
//			else
//                getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderClaimFragment()).commit();
		}else if(typeID == ClaimHeader.TYPEKEY_ADVANCES)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderBAFragment()).commit();
		else if(typeID == ClaimHeader.TYPEKEY_LIQUIDATION)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderLiquidationFragment()).commit();
    }
}
