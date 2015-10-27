package applusvelosi.projects.android.salt.models.claimheaders;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class ClaimNotPaidByCC extends ClaimHeader{
	
	public ClaimNotPaidByCC(	int staffID, String staffName, String staffEmail,
								int officeID, String officeName, String hrEmail,
								int costCenterID, String costCenterName, 
								int approverID, String approverName, String approverEmail,
								int accountID, String accountName, String accountEmail,
								SaltApplication app) throws Exception{
		super(	staffID, staffName, staffEmail, officeID, officeName, hrEmail, costCenterID, costCenterName, approverID, approverName, 
				approverEmail, accountID, accountName, accountEmail, app);
		map.put("ClaimTypeID", 1);
		map.put("ClaimTypeName","Claim");
		map.put("IsPaidByCompanyCC", false);
	}
		
	public ClaimNotPaidByCC(JSONObject jsonClaim, SaltApplication app) throws Exception{
		super(jsonClaim, app);
	}

	public ClaimNotPaidByCC(HashMap<String, Object> map) {
		super(map);
	}

	public boolean isPaidByCompanyCard() {
		return Boolean.parseBoolean(map.get("IsPaidByCompanyCC").toString());
	}
	
	public int getParentClaimID(){
		return Integer.parseInt(map.get("ParentClaimNumber").toString());
	}
	
	public String getParentClaimNumber(){
		return map.get("ParentClaimNumber").toString();
	}
	
}
