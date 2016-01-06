package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.LiquidationOfBA;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderBAFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderClaimFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimHeaderLiquidationFragment;

/**
 * Created by Velosi on 11/18/15.
 */
public class ClaimDetailActivity extends LinearNavFragmentActivity{
    public static final String INTENTKEY_CLAIMHEADERPOS = "claimheaderkey";
    public ClaimHeader claimHeader;
    public ArrayList<ClaimItem> claimItems;
    public boolean shouldLoadLineItemOnResume = true;
    private int claimHeaderPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        claimHeaderPos = getIntent().getExtras().getInt(INTENTKEY_CLAIMHEADERPOS);
        claimHeader = app.getMyClaims().get(claimHeaderPos);

        if(claimHeader.getTypeID()==ClaimHeader.TYPEKEY_CLAIMS)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderClaimFragment()).commit();
        else if(claimHeader.getTypeID()==ClaimHeader.TYPEKEY_ADVANCES)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderBAFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderLiquidationFragment()).commit();

//		int typeID = Integer.parseInt(mapClaimHeader.get(ClaimHeader.KEY_TYPEID).toString());
//		if(typeID == ClaimHeader.TYPEKEY_CLAIMS) {
//            claimHeader = (Boolean.parseBoolean(mapClaimHeader.get("IsPaidByCompanyCC").toString()))?new ClaimPaidByCC(mapClaimHeader):new ClaimNotPaidByCC(mapClaimHeader);
//            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderClaimFragment()).commit();
//        }else if(typeID == ClaimHeader.TYPEKEY_ADVANCES) {
//            claimHeader = new BusinessAdvance(mapClaimHeader);
//            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderBAFragment()).commit();
//        }else if(typeID == ClaimHeader.TYPEKEY_LIQUIDATION) {
//            claimHeader = new LiquidationOfBA(mapClaimHeader);
//            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimHeaderLiquidationFragment()).commit();
//        }

        claimItems = app.offlineGateway.deserializeMyClaimItems(claimHeader.getClaimID());
    }

    public int getClaimHeaderPos(){ return claimHeaderPos; };
}
