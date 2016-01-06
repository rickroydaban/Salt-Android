package applusvelosi.projects.android.salt.models.claimitems;

import org.json.JSONObject;

/**
 * Created by Velosi on 12/23/15.
 */
public class ClaimItemMileage extends ClaimItem{

    public ClaimItemMileage(JSONObject jsonClaimItem, JSONObject jsonCategory) throws Exception {
        super(jsonClaimItem, jsonCategory);
    }

    public String getMileageFrom(){ return mileageFrom; }
    public String getMileageTo(){ return mileageTo; }
    public boolean isReturn(){ return mileageReturn; }
    public float getMileageMileage(){ return mileage; }
    public int getMileageType(){ return  mileageType; }
    public float getRate(){ return mileageRate; }
}
