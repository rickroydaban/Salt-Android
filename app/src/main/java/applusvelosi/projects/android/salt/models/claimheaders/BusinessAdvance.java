package applusvelosi.projects.android.salt.models.claimheaders;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class BusinessAdvance extends ClaimHeader{

	public BusinessAdvance(SaltApplication app, int costCenterID, String costCenterName) throws Exception{

		super(app, costCenterID, costCenterName);
		map.put("ClaimTypeID", ClaimHeader.TYPEKEY_ADVANCES);
		map.put("ClaimTypeName",ClaimHeader.TYPEDESC_ADVANCES);
	}
	
//	public static String getEmptyJSON(OnlineGateway onlineGateway) throws Exception{
//		HashMap<String, Object> h = new HashMap<String, Object>();
//		h.put("ClaimID", 0);
//		h.put("StaffID", 0);
//		h.put("StaffName", "");
//		h.put("CreatedBy", 0);
//		h.put("CreatedByName", "");
//		h.put("OfficeID", 0);
//		h.put("OfficeName", "");
//		h.put("CostCenterID", 0);
//		h.put("CostCenterName", "");
//		h.put("ApproverID", 0);
//		h.put("ApproverName", "");
//		h.put("ApproversEmail", "");
//		h.put("AccountsID", 0);
//		h.put("AccountsEmail", "");
//		h.put("DateCreated", onlineGateway.defaultDate());
//		h.put("DateCancelled", onlineGateway.defaultDate());
//		h.put("DateSubmitted", onlineGateway.defaultDate());
//		h.put("DateApprovedByApprover", onlineGateway.defaultDate());
//		h.put("DateRejected", onlineGateway.defaultDate());
//		h.put("DateApprovedByAccount", onlineGateway.defaultDate());
//		h.put("DatePaid", onlineGateway.defaultDate());
//		h.put("TotalComputedInLC", 0);
//		h.put("TotalComputedApprovedInLC", 0);
//		h.put("TotalComputedRejectedInLC", 0);
//		h.put("TotalComputedForPaymentInLC", 0);		
//		//specific for business advance
//		h.put("ClaimTypeID", ClaimHeader.TYPEKEY_ADVANCES);
//		h.put("ClaimTypeName", ClaimHeader.TYPEDESC_ADVANCES);
//		h.put("ClaimStatus", ClaimHeader.STATUSKEY_OPEN);
//		h.put("StatusName", ClaimHeader.STATUSDESC_OPEN);
//		h.put("CountryManager", 0);
//		h.put("CountryManagerName", "");
//		h.put("CountryManagerEmail", "");
//		
////		return URLEncoder.encode(new JSONObject(h).toString(), "UTF-8");		
//		return new JSONObject(h).toString();		
//	}
		
	public BusinessAdvance(JSONObject jsonClaim, SaltApplication app) throws Exception{
		super(jsonClaim, app);
		map.put("DateApprovedByDirector", app.onlineGateway.dJsonizeDate(map.get("DateApprovedByDirector").toString()));
	}

	public BusinessAdvance(HashMap<String, Object> map) {
		super(map);
	}

	public int getCMID(){
		return Integer.parseInt(map.get("CountryManager").toString());
	}
	
	public String getCMEmail(){
		return map.get("CountryManagerEmail").toString();
	}

	public String getDateApprovedByCM(){
		return getStringedDate(map.get("DateApprovedByDirector").toString());
	}
	
}
