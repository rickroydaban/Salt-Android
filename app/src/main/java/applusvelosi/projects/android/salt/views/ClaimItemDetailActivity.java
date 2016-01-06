package applusvelosi.projects.android.salt.views;

import android.os.Bundle;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemDetailGenericFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemDetailMileageFragment;

/**
 * Created by Velosi on 11/18/15.
 */
public class ClaimItemDetailActivity extends LinearNavFragmentActivity{
    public static final String INTENTKEY_CLAIMITEM = "claimitemkey";
    public static final String INTENTKEY_CLAIMHEADERTYPE = "claimheadertypekey";
    public static final String INTENTKEY_CLAIMHEADERID = "claimheaderid";
    public static final String INTENTKEY_CLAIMHEADERSTATUS = "claimheaderstatus";
    public int claimHeaderTypekey;
    public int claimHeaderStatusKey;
    public int claimHeaderID;
    public ClaimItem claimItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            claimItem = (ClaimItem)getIntent().getSerializableExtra(INTENTKEY_CLAIMITEM);
            claimHeaderTypekey = getIntent().getExtras().getInt(INTENTKEY_CLAIMHEADERTYPE);
            claimHeaderStatusKey = getIntent().getExtras().getInt(INTENTKEY_CLAIMHEADERSTATUS);
            claimHeaderID = getIntent().getExtras().getInt(INTENTKEY_CLAIMHEADERID);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, (claimItem.getCategoryTypeID()== Category.TYPE_MILEAGE)?new ClaimItemDetailMileageFragment():new ClaimItemDetailGenericFragment()).commit();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
