package applusvelosi.projects.android.salt.models.claimheaders;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class ClaimPaidByCC extends ClaimHeader{
	public static final String KEY_ISPAIDBYCOMPANYCARD = "IsPaidByCompanyCC";
		
	public ClaimPaidByCC(SaltApplication app, int costCenterID, String costCenterName) throws Exception{
		super(app, costCenterID, costCenterName);
		
		map.put("ClaimTypeID", ClaimHeader.TYPEKEY_CLAIMS);
		map.put("ClaimTypeName", ClaimHeader.TYPEDESC_CLAIMS);
		map.put("IsPaidByCompanyCC", true);

	}
		
	public ClaimPaidByCC(JSONObject jsonClaim, SaltApplication app) throws Exception{
		super(jsonClaim, app);
	}

	public ClaimPaidByCC(HashMap<String, Object> map) {
		super(map);
	}

	public boolean isPaidByCompanyCard() {
		return Boolean.parseBoolean(map.get("IsPaidByCompanyCC").toString());
	}
	
	public float getForDeductionAmount() {
		return Float.parseFloat(map.get("TotalComputedForDeductionInLC").toString());
	}
	
//	public void updateDeductionAmount(float deduction){
//		this.forDeductionAmount = deduction;
//	}	
}
