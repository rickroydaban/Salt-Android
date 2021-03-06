package applusvelosi.projects.android.salt.models.claimheaders;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class LiquidationOfBA extends ClaimHeader{
		
	public LiquidationOfBA(SaltApplication app, int costCenterID, String costCenterName, int BAID, String BACNumber) throws Exception{
		super(app, costCenterID, costCenterName);
		map.put("ClaimTypeID", ClaimHeader.TYPEKEY_LIQUIDATION);
		map.put("ClaimTypeName",ClaimHeader.TYPEDESC_LIQUIDATION);
		map.put("BusinessAdvanceIDCharged", BAID);
		map.put("BACNumber", BACNumber);
	}
		
	public LiquidationOfBA(JSONObject jsonClaim, SaltApplication app) throws Exception{
		super(jsonClaim, app);
	}

	public LiquidationOfBA(HashMap<String, Object> map) {
		super(map);
	}

	public int getBusinessAdvanceID(){
		return Integer.parseInt(map.get("BusinessAdvanceIDCharged").toString());
	}
	
	public String getBACNumber(){
		return map.get("BACNumber").toString();
	}
	
	public float getForDeductionAmount(){
		return Float.parseFloat(map.get("TotalComputedForDeductionInLC").toString());
	}
}
