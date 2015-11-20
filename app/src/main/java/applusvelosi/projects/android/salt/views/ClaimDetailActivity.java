package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.LiquidationOfBA;
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
        HashMap<String, Object> mapClaimHeader = app.gson.fromJson(getIntent().getExtras().getString(INTENTKEY_CLAIMHEADER), app.types.hashmapOfStringObject);

		int typeID = Integer.parseInt(mapClaimHeader.get(ClaimHeader.KEY_TYPEID).toString());
		if(typeID == ClaimHeader.TYPEKEY_CLAIMS) {
            claimHeader = (Boolean.parseBoolean(mapClaimHeader.get("IsPaidByCompanyCC").toString()))?new ClaimPaidByCC(mapClaimHeader):new ClaimNotPaidByCC(mapClaimHeader);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderClaimFragment()).commit();
        }else if(typeID == ClaimHeader.TYPEKEY_ADVANCES) {
            claimHeader = new BusinessAdvance(mapClaimHeader);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderBAFragment()).commit();
        }else if(typeID == ClaimHeader.TYPEKEY_LIQUIDATION) {
            claimHeader = new LiquidationOfBA(mapClaimHeader);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderLiquidationFragment()).commit();
        }
    }
}
