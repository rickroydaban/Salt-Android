package applusvelosi.projects.android.salt.models.claimheaders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.models.claimitems.MilageClaimItem;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class ClaimHeader{
    public static final String KEY_TYPEID = "ClaimTypeID";

	protected LinkedHashMap<String, Object> map;

	public ClaimHeader(JSONObject jsonClaim, SaltApplication app) throws Exception{
		map = new LinkedHashMap<String, Object>();
		map.putAll(OnlineGateway.toMap(jsonClaim));
		//convert json time epoch to readable time
		map.put("DateCreated", app.onlineGateway.dJsonizeDate(map.get("DateCreated").toString()));
		map.put("DateCancelled", app.onlineGateway.dJsonizeDate(map.get("DateCancelled").toString()));
		map.put("DateSubmitted", app.onlineGateway.dJsonizeDate(map.get("DateSubmitted").toString()));
		map.put("DateApprovedByApprover", app.onlineGateway.dJsonizeDate(map.get("DateApprovedByApprover").toString()));
		map.put("DateRejected", app.onlineGateway.dJsonizeDate(map.get("DateRejected").toString()));
		map.put("DateApprovedByAccount", app.onlineGateway.dJsonizeDate(map.get("DateApprovedByAccount").toString()));
		map.put("DatePaid", app.onlineGateway.dJsonizeDate(map.get("DatePaid").toString()));
	}
	
	public ClaimHeader(HashMap<String, Object> map){
		this.map = new LinkedHashMap<String, Object>();
		this.map.putAll(map);
	}

	public ClaimHeader(SaltApplication app, int costCenterID, String costCenterName){
		String staffName = app.getStaff().getFname()+" "+app.getStaff().getLname();
		map = new LinkedHashMap<String, Object>();
		map.put("AccountManagerEmail", "");
		map.put("AccountName", app.getStaff().getAccountName());
		map.put("AccountsEmail", app.getStaff().getAccountEmail());
		map.put("AccountsID", app.getStaff().getAccountID());
		map.put("Active", true);
		map.put("ApproverID", app.getStaff().getExpenseApproverID());
		map.put("ApproverName", app.getStaff().getExpenseApproverName());
		map.put("ApproversEmail", app.getStaff().getExpenseApproverEmail());
		map.put("BACNumber", "");
		map.put("BusinessAdvanceIDCharged", 0);
		map.put("ClaimID", 0);
		map.put("ClaimLineItems", new JSONArray());
		map.put("ClaimNumber", "9999-TST-999999-9999");
		map.put("ClaimStatus", STATUSKEY_OPEN);
		map.put("ClaimTypeID", 0);
		map.put("ClaimTypeName", "");
		map.put("CostCenterID", costCenterID);
		map.put("CostCenterName", costCenterName);
		map.put("CountryManager", 0);
		map.put("CountryManagerEmail", "");
		map.put("CountryManagerName", "");
		map.put("CreatedBy", app.getStaff().getStaffID());
		map.put("CreatedByName", staffName);
		map.put("DateApprovedByAccount", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", app.onlineGateway.jsonizeDate(new Date())); //might be cause because of not valid date created value
		map.put("DateModified", "/Date(1427134500000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("DateSubmitted", "/Date(1426182960000+0000)/");
		map.put("HROfficerEmail", "");
		map.put("IsPaidByCompanyCC", false);
		map.put("ModifiedBy", app.getStaff().getStaffID());
		map.put("ModifiedByName", staffName);
		map.put("OfficeID", app.getStaff().getOfficeID());
		map.put("OfficeName", app.getStaff().getOfficeName());
		map.put("OfficeToCharge", app.getStaff().getOfficeID());
		map.put("ParentClaimID", 0);
		map.put("ParentClaimNumber", "");
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffEmail", app.getStaff().getEmail());
		map.put("StaffID", app.getStaff().getStaffID());
		map.put("StaffName", staffName);
		map.put("StatusName", STATUSDESC_OPEN);
		map.put("TotalAmount", 0);
		map.put("TotalAmountInLC", 0);
		map.put("TotalComputedApprovedInLC", 0);
		map.put("TotalComputedForDeductionInLC", 0);
		map.put("TotalComputedForPaymentInLC", 0);
		map.put("TotalComputedInLC", 0);
		map.put("TotalComputedRejectedInLC", 0);
	}
		
	public ClaimHeader(	int staffID, String staffName, String staffEmail,
						int officeID, String officeName, String hrEmail,
						int costCenterID, String costCenterName, 
						int approverID, String approverName, String approverEmail,
						int accountID, String accountName, String accountEmail,
						SaltApplication app) throws Exception{
	
		map = new LinkedHashMap<String, Object>();
		map.put("AccountManagerEmail", "");
		map.put("AccountName", accountName);
		map.put("AccountsEmail", accountEmail);
		map.put("AccountsID", accountID);
		map.put("Active", true);
		map.put("ApproverID", approverID);
		map.put("ApproverName", approverName);
		map.put("ApproversEmail", approverEmail);
		map.put("BACNumber", "");
		map.put("BusinessAdvanceIDCharged", 0);
		map.put("ClaimID", 0);
		map.put("ClaimLineItems", new JSONArray());
		map.put("ClaimNumber", "9999-TST-999999-9999");
		map.put("ClaimStatus", STATUSKEY_OPEN);
		map.put("ClaimTypeID", 0);
		map.put("ClaimTypeName", "");
		map.put("CostCenterID", costCenterID);
		map.put("CostCenterName", costCenterName);
		map.put("CountryManager", 0);
		map.put("CountryManagerEmail", "");
		map.put("CountryManagerName", "");
		map.put("CreatedBy", staffID);
		map.put("CreatedByName", staffName);
		map.put("DateApprovedByAccount", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", "/Date(1426092420000+0000)/"); //might be cause because of not valid date created value
		map.put("DateModified", "/Date(1427134500000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("DateSubmitted", "/Date(1426182960000+0000)/");
		map.put("HROfficerEmail", "");
		map.put("IsPaidByCompanyCC", false);
		map.put("ModifiedBy", staffID);
		map.put("ModifiedByName", staffName);
		map.put("OfficeID", officeID);
		map.put("OfficeName", officeName);
		map.put("OfficeToCharge", officeID);
		map.put("ParentClaimID", 0);
		map.put("ParentClaimNumber", "");
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffEmail", staffEmail);
		map.put("StaffID", staffID);
		map.put("StaffName", staffName);
		map.put("StatusName", STATUSDESC_OPEN);
		map.put("TotalAmount", 0);
		map.put("TotalAmountInLC", 0);
		map.put("TotalComputedApprovedInLC", 0);
		map.put("TotalComputedForDeductionInLC", 0);
		map.put("TotalComputedForPaymentInLC", 0);
		map.put("TotalComputedInLC", 0);
		map.put("TotalComputedRejectedInLC", 0);
	}

    public void cancelClaim(SaltApplication app) throws Exception{
//		map.put("ClaimLineItems", new JSONArray());
        map.put("ClaimStatus",STATUSKEY_CANCELLED);
		map.put("StatusName", STATUSDESC_CANCELLED);
//		map.put("ModifiedBy", app.getStaff().getStaffID());
//		map.put("ModifiedByName", app.getStaff().getFname()+" "+app.getStaff().getLname());
//        map.put("DateCreated", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateCreated").toString())));
        map.put("DateCancelled", app.onlineGateway.jsonizeDate(new Date()));
        map.put("DateSubmitted", app.onlineGateway.jsonizeDate(new Date()));
//        map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
//        map.put("DateRejected", "/Date(-2208988800000+0000)/");
//        map.put("DateApprovedByAccount", "/Date(-2208988800000+0000)/");
//        map.put("DatePaid", "/Date(-2208988800000+0000)/");
//        map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
    }

	public void editClaimHeader(boolean isPaidByCC){
		if(getTypeID() == TYPEKEY_CLAIMS)
			map.put("IsPaidByCompanyCC", isPaidByCC);
	}	
	
	public void updateLineItems(ArrayList<ClaimItem> items, SaltApplication app){
		ArrayList<HashMap<String, Object>> claimItemMaps = new ArrayList<HashMap<String,Object>>();
		for(ClaimItem item :items)
			claimItemMaps.add(item.getMap());
		
		map.put("ClaimLineItems", app.gson.toJson(claimItemMaps, app.types.arrayListOfHashmapOfStringObject));
	}
	
	public int getClaimID(){
		return Integer.parseInt(map.get("ClaimID").toString());
	}
	
	public String getClaimNumber(){
		return map.get("ClaimNumber").toString();
	}
	
	public int getStaffID(){
		return Integer.parseInt(map.get("StaffID").toString());
	}
	
	public String getStaffName(){
		return map.get("StaffName").toString();
	}
	
	public int getOfficeID(){
		return Integer.parseInt(map.get("OfficeID").toString());
	}
	
	public String getOfficeName(){
		return map.get("OfficeName").toString();
	}
	
	public int getCostCenterID(){
		return Integer.parseInt(map.get("CostCenterID").toString());
	}
	public String getCostCenterName(){
		return map.get("CostCenterName").toString();
	}
	
	public int getApproverID(){
		return Integer.parseInt(map.get("ApproverID").toString());
	}
	
	public String getApproverName(){
		return map.get("ApproverName").toString();
	}
	
	public String getApproverEmail(){
		return map.get("ApproversEmail").toString();
	}
		
	public int getAccountID(){
		return Integer.parseInt(map.get("AccountsID").toString());
	}
	
	public String getAccountEmail(){
		return map.get("AccountsEmail").toString();
	}
		
	public int getTypeID(){
		return Integer.parseInt(map.get("ClaimTypeID").toString());
	}
		
	public String getTypeName(){
		return getTypeDescriptionForKey(getTypeID());
	}
	
	public int getStatusID(){
		return Integer.parseInt(map.get("ClaimStatus").toString());
	}
	
	public String getStatusName(){
		return getStatusDescriptionForKey(getStatusID());
	}

	public float getTotalClaim(){
		return Float.parseFloat(map.get("TotalComputedInLC").toString());
	}
	
	public float getApprovedAmount(){
		return Float.parseFloat(map.get("TotalComputedApprovedInLC").toString());
	}
	
	public float getRejectedAmount(){
		return Float.parseFloat(map.get("TotalComputedRejectedInLC").toString());
	}
	
	public float getForPaymentAmount(){
		return Float.parseFloat(map.get("TotalComputedForPaymentInLC").toString());
	}
	
	public String getDateCreated(){
		return map.get("DateCreated").toString();
	}
	
	public String getDateCancelled(){
		return map.get("DateCancelled").toString();
	}
	
	public String getDateSubmitted(){
		return getStringedDate(map.get("DateSubmitted").toString());
	}
	
	public String getDateRejected(){
		return getStringedDate(map.get("DateRejected").toString());
	}
	
	public String getDateApprovedByApprover(){
		return getStringedDate(map.get("DateApprovedByApprover").toString());
	}
	
	public String getDateApprovedByAccount(){
		return getStringedDate(map.get("DateApprovedByAccount").toString());
	}
	
	public String getDatePaid(){
		return getStringedDate(map.get("DatePaid").toString());
	}
	
	public ArrayList<ClaimItem> getClaimItems(SaltApplication app){
		ArrayList<ClaimItem> claimItems = new ArrayList<ClaimItem>();
		ArrayList<HashMap<String,Object>> mapItems = app.gson.fromJson(map.get("ClaimLineItems").toString(), app.types.arrayListOfHashmapOfStringObject);
		for(int i=0; i<mapItems.size(); i++){
			HashMap<String, Object> itemMap = mapItems.get(i);
			if(Integer.parseInt(map.get(KEY_TYPEID).toString()) == Category.TYPE_MILEAGE)
				claimItems.add(new MilageClaimItem(itemMap, app));
			else
				claimItems.add(new ClaimItem(itemMap, app));				
		}
		
		return claimItems;
	}

	public HashMap<String, Object> getMap(){
		return map;
	}
		
	public String jsonize(SaltApplication app) throws Exception{
		LinkedHashMap<String, Object> jsonizableMap = new LinkedHashMap<String, Object>();
		jsonizableMap.putAll(map);
		jsonizableMap.put("AccountsID", Integer.parseInt(jsonizableMap.get("AccountsID").toString()));
		jsonizableMap.put("Active", Boolean.parseBoolean(jsonizableMap.get("Active").toString()));
		jsonizableMap.put("ApproverID", Integer.parseInt(jsonizableMap.get("ApproverID").toString()));
		jsonizableMap.put("BusinessAdvanceIDCharged", Integer.parseInt(jsonizableMap.get("BusinessAdvanceIDCharged").toString()));
		jsonizableMap.put("ClaimID", Integer.parseInt(jsonizableMap.get("ClaimID").toString()));
		jsonizableMap.put("ClaimStatus", Integer.parseInt(jsonizableMap.get("ClaimStatus").toString()));
		jsonizableMap.put("ClaimTypeID", Integer.parseInt(jsonizableMap.get("ClaimTypeID").toString()));
		jsonizableMap.put("CostCenterID", Integer.parseInt(jsonizableMap.get("CostCenterID").toString()));
		jsonizableMap.put("CountryManager", Integer.parseInt(jsonizableMap.get("CountryManager").toString()));
		jsonizableMap.put("CreatedBy", Integer.parseInt(jsonizableMap.get("CreatedBy").toString()));
		jsonizableMap.put("IsPaidByCompanyCC", Boolean.parseBoolean(jsonizableMap.get("IsPaidByCompanyCC").toString()));
		jsonizableMap.put("ModifiedBy", Integer.parseInt(jsonizableMap.get("ModifiedBy").toString()));
		jsonizableMap.put("OfficeID", Integer.parseInt(jsonizableMap.get("OfficeID").toString()));
		jsonizableMap.put("OfficeToCharge", Integer.parseInt(jsonizableMap.get("OfficeToCharge").toString()));
		jsonizableMap.put("ParentClaimID", Integer.parseInt(jsonizableMap.get("ParentClaimID").toString()));
		jsonizableMap.put("RejectedBy", Integer.parseInt(jsonizableMap.get("RejectedBy").toString()));
		jsonizableMap.put("StaffID", Integer.parseInt(jsonizableMap.get("StaffID").toString()));
		jsonizableMap.put("TotalAmount", Float.parseFloat(jsonizableMap.get("TotalAmount").toString()));
		jsonizableMap.put("TotalAmountInLC", Float.parseFloat(jsonizableMap.get("TotalAmountInLC").toString()));
		jsonizableMap.put("TotalComputedApprovedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedApprovedInLC").toString()));
		jsonizableMap.put("TotalComputedForDeductionInLC", Float.parseFloat(jsonizableMap.get("TotalComputedForDeductionInLC").toString()));
		jsonizableMap.put("TotalComputedForPaymentInLC", Float.parseFloat(jsonizableMap.get("TotalComputedForPaymentInLC").toString()));
		jsonizableMap.put("TotalComputedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedInLC").toString()));
		jsonizableMap.put("TotalComputedRejectedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedRejectedInLC").toString()));
		//convert date into epoch form otherwise request will fail
		jsonizableMap.put("DateCreated", map.get("DateCreated").toString());
		jsonizableMap.put("DateCancelled", map.get("DateCancelled").toString());
		jsonizableMap.put("DateSubmitted", map.get("DateSubmitted").toString());
		jsonizableMap.put("DateApprovedByApprover", map.get("DateApprovedByApprover").toString());
		jsonizableMap.put("DateRejected", map.get("DateRejected").toString());
		jsonizableMap.put("DateApprovedByAccount", map.get("DateApprovedByAccount").toString());
		jsonizableMap.put("DatePaid", map.get("DatePaid").toString());

        //does not need to pass the claim items along with saving claim header
		jsonizableMap.put("ClaimLineItems", new ArrayList<Object>());
		return app.gson.toJson(jsonizableMap, app.types.hashmapOfStringObject);
	}

    public String jsonize(SaltApplication app, int key) throws Exception{
        LinkedHashMap<String, Object> jsonizableMap = new LinkedHashMap<String, Object>();
        jsonizableMap.putAll(map);
        jsonizableMap.put("AccountsID", Integer.parseInt(jsonizableMap.get("AccountsID").toString()));
        jsonizableMap.put("Active", Boolean.parseBoolean(jsonizableMap.get("Active").toString()));
        jsonizableMap.put("ApproverID", Integer.parseInt(jsonizableMap.get("ApproverID").toString()));
        jsonizableMap.put("BusinessAdvanceIDCharged", Integer.parseInt(jsonizableMap.get("BusinessAdvanceIDCharged").toString()));
        jsonizableMap.put("ClaimID", Integer.parseInt(jsonizableMap.get("ClaimID").toString()));
        jsonizableMap.put("ClaimStatus", Integer.parseInt(jsonizableMap.get("ClaimStatus").toString()));
        jsonizableMap.put("ClaimTypeID", Integer.parseInt(jsonizableMap.get("ClaimTypeID").toString()));
        jsonizableMap.put("CostCenterID", Integer.parseInt(jsonizableMap.get("CostCenterID").toString()));
        jsonizableMap.put("CountryManager", Integer.parseInt(jsonizableMap.get("CountryManager").toString()));
        jsonizableMap.put("CreatedBy", Integer.parseInt(jsonizableMap.get("CreatedBy").toString()));
        jsonizableMap.put("IsPaidByCompanyCC", Boolean.parseBoolean(jsonizableMap.get("IsPaidByCompanyCC").toString()));
        jsonizableMap.put("ModifiedBy", Integer.parseInt(jsonizableMap.get("ModifiedBy").toString()));
        jsonizableMap.put("OfficeID", Integer.parseInt(jsonizableMap.get("OfficeID").toString()));
        jsonizableMap.put("OfficeToCharge", Integer.parseInt(jsonizableMap.get("OfficeToCharge").toString()));
        jsonizableMap.put("ParentClaimID", Integer.parseInt(jsonizableMap.get("ParentClaimID").toString()));
        jsonizableMap.put("RejectedBy", Integer.parseInt(jsonizableMap.get("RejectedBy").toString()));
        jsonizableMap.put("StaffID", Integer.parseInt(jsonizableMap.get("StaffID").toString()));
        jsonizableMap.put("TotalAmount", Float.parseFloat(jsonizableMap.get("TotalAmount").toString()));
        jsonizableMap.put("TotalAmountInLC", Float.parseFloat(jsonizableMap.get("TotalAmountInLC").toString()));
        jsonizableMap.put("TotalComputedApprovedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedApprovedInLC").toString()));
        jsonizableMap.put("TotalComputedForDeductionInLC", Float.parseFloat(jsonizableMap.get("TotalComputedForDeductionInLC").toString()));
        jsonizableMap.put("TotalComputedForPaymentInLC", Float.parseFloat(jsonizableMap.get("TotalComputedForPaymentInLC").toString()));
        jsonizableMap.put("TotalComputedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedInLC").toString()));
        jsonizableMap.put("TotalComputedRejectedInLC", Float.parseFloat(jsonizableMap.get("TotalComputedRejectedInLC").toString()));
        //convert date into epoch form otherwise request will fail
        jsonizableMap.put("DateCreated", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateCreated").toString())));
        jsonizableMap.put("DateCancelled", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateCancelled").toString())));
        jsonizableMap.put("DateSubmitted", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateSubmitted").toString())));
        jsonizableMap.put("DateApprovedByApprover", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateApprovedByApprover").toString())));
        jsonizableMap.put("DateRejected", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateRejected").toString())));
        jsonizableMap.put("DateApprovedByAccount", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DateApprovedByAccount").toString())));
        jsonizableMap.put("DatePaid", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(map.get("DatePaid").toString())));
        //does not need to pass the claim items along with saving claim header
        jsonizableMap.put("ClaimLineItems", new ArrayList<Object>());
        return app.gson.toJson(jsonizableMap, app.types.hashmapOfStringObject);
    }

	public static String getEmptyJSON(SaltApplication app) throws Exception{
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("AccountManagerEmail", "");
		map.put("AccountName", "");
		map.put("AccountsEmail", "");
		map.put("AccountsID", 0);
		map.put("Active", true);
		map.put("ApproverID", 0);
		map.put("ApproverName", "");
		map.put("ApproversEmail", "");
		map.put("BACNumber", "");
		map.put("BusinessAdvanceIDCharged", 0);
		map.put("ClaimID", 0);
		map.put("ClaimLineItems", new JSONArray());
		map.put("ClaimNumber", "");
		map.put("ClaimStatus", STATUSKEY_OPEN);
		map.put("ClaimTypeID", 0);
		map.put("ClaimTypeName", "");
		map.put("CostCenterID", 0);
		map.put("CostCenterName", "");
		map.put("CountryManager", 0);
		map.put("CountryManagerEmail", "");
		map.put("CountryManagerName", "");
		map.put("CreatedBy", 0);
		map.put("CreatedByName", "");
		map.put("DateApprovedByAccount", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", "/Date(1426092420000+0000)/"); //might be cause because of not valid date created value
		map.put("DateModified", "/Date(1427134500000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("DateSubmitted", "/Date(1426182960000+0000)/");
		map.put("HROfficerEmail", "");
		map.put("IsPaidByCompanyCC", false);
		map.put("ModifiedBy", 0);
		map.put("ModifiedByName", "");
		map.put("OfficeID", 0);
		map.put("OfficeName", "");
		map.put("OfficeToCharge", 0);
		map.put("ParentClaimID", 0);
		map.put("ParentClaimNumber", "");
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffEmail", "");
		map.put("StaffID", 0);
		map.put("StaffName", "");
		map.put("StatusName", STATUSKEY_OPEN);
		map.put("TotalAmount", 0.0);
		map.put("TotalAmountInLC", 0.0);
		map.put("TotalComputedApprovedInLC", 0.0);
		map.put("TotalComputedForDeductionInLC", 0.0);
		map.put("TotalComputedForPaymentInLC", 0.0);
		map.put("TotalComputedInLC", 0.0);
		map.put("TotalComputedRejectedInLC", 0.0);
		
		return new JSONObject(map).toString();		
	}	
	
	//for creating new claim item
//	private ClaimItem tempNewClaimItem, tempUpdatedClaimItem;
	
//	public void prepareForCreatingNewClamItem(SaltApplication app){
//		tempNewClaimItem = new ClaimItem(app);
//	}

//	public ClaimItem getPreparedClaimItemForCreation(){
//		return tempNewClaimItem;
//	}

//	public void prepareForUpdatingClaimItem(int claimItemPos, SaltApplication app){
//		tempUpdatedClaimItem = getClaimItems(app).get(claimItemPos);
//	}
//
//	public ClaimItem getPreparedClaimItemForEdit(){
//		return tempUpdatedClaimItem;
//	}

	protected String getStringedDate(String stringedDate){
		if(Integer.parseInt(stringedDate.split("-")[2]) == 1900)
			return "-";
		else return stringedDate;
	}

	private static final String TYPE_CLAIMS = "1-Claim";
	private static final String TYPE_ADVANCES = "2-Business Advance";
	private static final String TYPE_LIQUIDATION = "3-Liquidation of BA";

	private static final String STATUS_OPEN = "1-Open";
	private static final String STATUS_SUBMITTED = "2-Submitted";
	private static final String STATUS_APPROVEDBYAPPROVER = "3-Approved by Approver";
	private static final String STATUS_REJECTEDBYAPPROVER = "4-Rejected by Approver";
	private static final String STATUS_CANCELLED = "5-Cancelled";
	private static final String STATUS_PAID = "6-Paid";
	private static final String STATUS_APPROVEDBYCOUNTRYMANAGER = "7-Approved by Country Manager";
	private static final String STATUS_REJECTEDBYCOUNTRYMANAGER = "9-Rejected by Country Manager";
	private static final String STATUS_REJECTEDBYACCOUNTS = "11-Rejected by Accounts";
	private static final String STATUS_APPROVEDBYACCOUNTS = "16-Approved by Accounts";
	private static final String STATUS_RETURN = "17-Return";
    private static final String STATUS_LIQUIDATED = "23-Liquidated";
	private static final String STATUS_PAIDUNDERCOMPANYCARD = "24-Paid Under Company Card";
	private static final String STATUS_REJECTEDFORSALARYDEDUCTION = "25-Rejected For Salary Deduction";

	public static int TYPEKEY_CLAIMS, TYPEKEY_ADVANCES, TYPEKEY_LIQUIDATION;
	public static String TYPEDESC_CLAIMS, TYPEDESC_ADVANCES, TYPEDESC_LIQUIDATION;
	public static int STATUSKEY_OPEN, STATUSKEY_SUBMITTED, STATUSKEY_APPROVEDBYAPPROVER, STATUSKEY_REJECTEDBYAPPROVER, STATUSKEY_CANCELLED,
			STATUSKEY_PAID, STATUSKEY_APPROVEDBYCOUNTRYMANAGER, STATUSKEY_REJECTEDBYCOUNTRYMANAGER, STATUSKEY_RETURN,
			STATUSKEY_REJECTEDBYACCOUNTS, STATUSKEY_APPROVEDBYACCOUNTS, STATUSKEY_PAIDUNDERCOMPANYCARD, STATUSKEY_REJECTEDFORSALARYDEDUCTION, STATUSKEY_LIQUIDATED;
	public static String STATUSDESC_OPEN, STATUSDESC_SUBMITTED, STATUSDESC_APPROVEDBYAPPROVER, STATUSDESC_REJECTEDBYAPPROVER, STATUSDESC_CANCELLED,
			STATUSDESC_PAID, STATUSDESC_APPROVEDBYCOUNTRYMANAGER, STATUSDESC_REJECTEDBYCOUNTRYMANAGER, STATUSDESC_RETURN,
			STATUSDESC_REJECTEDBYACCOUNTS, STATUSDESC_APPROVEDBYACCOUNTS, STATUSDESC_PAIDUNDERCOMPANYCARD, STATUSDESC_REJECTEDFORSALARYDEDUCTION, STATUSDESC_LIQUIDATED;

	private static ArrayList<String> typeDescriptionList, statusDescriptionList;

	static{
		TYPEKEY_CLAIMS = Integer.parseInt(TYPE_CLAIMS.split("-")[0]);
		TYPEKEY_ADVANCES = Integer.parseInt(TYPE_ADVANCES.split("-")[0]);
		TYPEKEY_LIQUIDATION = Integer.parseInt(TYPE_LIQUIDATION.split("-")[0]);

		TYPEDESC_CLAIMS = TYPE_CLAIMS.split("-")[1];
		TYPEDESC_ADVANCES = TYPE_ADVANCES.split("-")[1];
		TYPEDESC_LIQUIDATION = TYPE_LIQUIDATION.split("-")[1];

		STATUSKEY_OPEN = Integer.parseInt(STATUS_OPEN.split("-")[0]);
		STATUSKEY_SUBMITTED = Integer.parseInt(STATUS_SUBMITTED.split("-")[0]);
		STATUSKEY_APPROVEDBYAPPROVER = Integer.parseInt(STATUS_APPROVEDBYAPPROVER.split("-")[0]);
		STATUSKEY_REJECTEDBYAPPROVER = Integer.parseInt(STATUS_REJECTEDBYAPPROVER.split("-")[0]);
		STATUSKEY_CANCELLED = Integer.parseInt(STATUS_CANCELLED.split("-")[0]);
		STATUSKEY_PAID = Integer.parseInt(STATUS_PAID.split("-")[0]);
		STATUSKEY_APPROVEDBYCOUNTRYMANAGER = Integer.parseInt(STATUS_APPROVEDBYCOUNTRYMANAGER.split("-")[0]);
		STATUSKEY_REJECTEDBYCOUNTRYMANAGER = Integer.parseInt(STATUS_REJECTEDBYCOUNTRYMANAGER.split("-")[0]);
		STATUSKEY_REJECTEDBYACCOUNTS = Integer.parseInt(STATUS_REJECTEDBYACCOUNTS.split("-")[0]);
		STATUSKEY_APPROVEDBYACCOUNTS = Integer.parseInt(STATUS_APPROVEDBYACCOUNTS.split("-")[0]);
		STATUSKEY_RETURN = Integer.parseInt(STATUS_RETURN.split("-")[0]);
		STATUSKEY_PAIDUNDERCOMPANYCARD = Integer.parseInt(STATUS_PAIDUNDERCOMPANYCARD.split("-")[0]);
		STATUSKEY_REJECTEDFORSALARYDEDUCTION = Integer.parseInt(STATUS_REJECTEDFORSALARYDEDUCTION.split("-")[0]);
        STATUSKEY_LIQUIDATED = Integer.parseInt(STATUS_LIQUIDATED.split("-")[0]);

		STATUSDESC_OPEN = STATUS_OPEN.split("-")[1];
		STATUSDESC_SUBMITTED = STATUS_SUBMITTED.split("-")[1];
		STATUSDESC_APPROVEDBYAPPROVER = STATUS_APPROVEDBYAPPROVER.split("-")[1];
		STATUSDESC_REJECTEDBYAPPROVER = STATUS_REJECTEDBYAPPROVER.split("-")[1];
		STATUSDESC_CANCELLED = STATUS_CANCELLED.split("-")[1];
		STATUSDESC_PAID = STATUS_PAID.split("-")[1];
		STATUSDESC_APPROVEDBYCOUNTRYMANAGER = STATUS_APPROVEDBYCOUNTRYMANAGER.split("-")[1];
		STATUSDESC_REJECTEDBYCOUNTRYMANAGER = STATUS_REJECTEDBYCOUNTRYMANAGER.split("-")[1];
		STATUSDESC_REJECTEDBYACCOUNTS = STATUS_REJECTEDBYACCOUNTS.split("-")[1];
		STATUSDESC_APPROVEDBYACCOUNTS = STATUS_APPROVEDBYACCOUNTS.split("-")[1];
		STATUSDESC_RETURN = STATUS_RETURN.split("-")[1];
		STATUSDESC_PAIDUNDERCOMPANYCARD = STATUS_PAIDUNDERCOMPANYCARD.split("-")[1];
		STATUSDESC_REJECTEDFORSALARYDEDUCTION = STATUS_REJECTEDFORSALARYDEDUCTION.split("-")[1];
        STATUSDESC_LIQUIDATED = STATUS_LIQUIDATED.split("-")[1];

		typeDescriptionList = new ArrayList<String>();
		typeDescriptionList.add(TYPEDESC_CLAIMS);
		typeDescriptionList.add(TYPEDESC_ADVANCES);
		typeDescriptionList.add(TYPEDESC_LIQUIDATION);

		statusDescriptionList = new ArrayList<String>();
		statusDescriptionList.add(STATUSDESC_OPEN);
		statusDescriptionList.add(STATUSDESC_SUBMITTED);
		statusDescriptionList.add(STATUSDESC_APPROVEDBYAPPROVER);
		statusDescriptionList.add(STATUSDESC_REJECTEDBYAPPROVER);
		statusDescriptionList.add(STATUSDESC_CANCELLED);
		statusDescriptionList.add(STATUSDESC_PAID);
		statusDescriptionList.add(STATUSDESC_APPROVEDBYCOUNTRYMANAGER);
		statusDescriptionList.add(STATUSDESC_REJECTEDBYCOUNTRYMANAGER);
		statusDescriptionList.add(STATUSDESC_REJECTEDBYACCOUNTS);
		statusDescriptionList.add(STATUSDESC_APPROVEDBYACCOUNTS);
		statusDescriptionList.add(STATUSDESC_RETURN);
		statusDescriptionList.add(STATUSDESC_PAIDUNDERCOMPANYCARD);
		statusDescriptionList.add(STATUSDESC_REJECTEDFORSALARYDEDUCTION);
        statusDescriptionList.add(STATUSDESC_LIQUIDATED);
	}

	public static String getTypeDescriptionForKey(int key){
		if(key == TYPEKEY_ADVANCES) return TYPEDESC_ADVANCES;
		else if(key == TYPEKEY_CLAIMS) return TYPEDESC_CLAIMS;
		else return TYPEDESC_LIQUIDATION;
	}

	public static int getTypeKeyForDescription(String description){
		if(description == TYPEDESC_ADVANCES) return TYPEKEY_ADVANCES;
		else if(description == TYPEDESC_CLAIMS) return TYPEKEY_CLAIMS;
		else return TYPEKEY_LIQUIDATION;
	}

	public static String getStatusDescriptionForKey(int key){
		if(key == STATUSKEY_OPEN) return STATUSDESC_OPEN;
		else if(key == STATUSKEY_SUBMITTED) return STATUSDESC_SUBMITTED;
		else if(key == STATUSKEY_APPROVEDBYAPPROVER) return STATUSDESC_APPROVEDBYAPPROVER;
		else if(key == STATUSKEY_REJECTEDBYAPPROVER) return STATUSDESC_REJECTEDBYAPPROVER;
		else if(key == STATUSKEY_CANCELLED) return STATUSDESC_CANCELLED;
		else if(key == STATUSKEY_PAID) return STATUSDESC_PAID;
		else if(key == STATUSKEY_APPROVEDBYCOUNTRYMANAGER) return STATUSDESC_APPROVEDBYCOUNTRYMANAGER;
		else if(key == STATUSKEY_REJECTEDBYCOUNTRYMANAGER) return STATUSDESC_REJECTEDBYCOUNTRYMANAGER;
		else if(key == STATUSKEY_REJECTEDBYACCOUNTS) return STATUSDESC_REJECTEDBYACCOUNTS;
		else if(key == STATUSKEY_APPROVEDBYACCOUNTS) return STATUSDESC_APPROVEDBYACCOUNTS;
		else if(key == STATUSKEY_RETURN) return STATUSDESC_RETURN;
		else if(key == STATUSKEY_PAIDUNDERCOMPANYCARD) return STATUSDESC_PAIDUNDERCOMPANYCARD;
		else if(key == STATUSKEY_REJECTEDFORSALARYDEDUCTION) return STATUSDESC_REJECTEDFORSALARYDEDUCTION;
        else return STATUSDESC_LIQUIDATED;
	}

	public static int getStatusKeyForDescription(String description){
		if(description == STATUSDESC_OPEN) return STATUSKEY_OPEN;
		else if(description == STATUSDESC_SUBMITTED) return STATUSKEY_SUBMITTED;
		else if(description == STATUSDESC_APPROVEDBYAPPROVER) return STATUSKEY_APPROVEDBYAPPROVER;
		else if(description == STATUSDESC_REJECTEDBYAPPROVER) return STATUSKEY_REJECTEDBYAPPROVER;
		else if(description == STATUSDESC_CANCELLED) return STATUSKEY_CANCELLED;
		else if(description == STATUSDESC_PAID) return STATUSKEY_PAID;
		else if(description == STATUSDESC_APPROVEDBYCOUNTRYMANAGER) return STATUSKEY_APPROVEDBYCOUNTRYMANAGER;
		else if(description == STATUSDESC_REJECTEDBYCOUNTRYMANAGER) return STATUSKEY_REJECTEDBYCOUNTRYMANAGER;
		else if(description == STATUSDESC_REJECTEDBYACCOUNTS) return STATUSKEY_REJECTEDBYACCOUNTS;
		else if(description == STATUSDESC_APPROVEDBYACCOUNTS) return STATUSKEY_APPROVEDBYACCOUNTS;
		else if(description == STATUSDESC_RETURN) return STATUSKEY_RETURN;
		else if(description == STATUSDESC_PAIDUNDERCOMPANYCARD) return STATUSKEY_PAIDUNDERCOMPANYCARD;
		else if(description == STATUSDESC_REJECTEDFORSALARYDEDUCTION) return STATUSKEY_REJECTEDFORSALARYDEDUCTION;
        else return STATUSKEY_LIQUIDATED;
	}
}
