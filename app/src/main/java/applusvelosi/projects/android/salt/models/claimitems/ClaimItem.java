package applusvelosi.projects.android.salt.models.claimitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class ClaimItem {
	public static final String KEY_CATEGORYID = "CategoryID";
	public static final String KEY_CATEGORYTYPEID = "CategoryTypeID";
	private static final String KEY_ATTENDEE_REQUIREMENT = "Attendee";
	private static final String KEY_SPENDLIMIT = "SpendLimit";
	protected LinkedHashMap<String, Object> map;
	private SaltApplication app;
	
 	public ClaimItem(HashMap<String, Object> map, SaltApplication app){
 		this.app = app;
 		this.map = new LinkedHashMap<String, Object>();
 		this.map.putAll(map);
 	}
 	
	public ClaimItem(JSONObject jsonClaimItem, JSONObject jsonCategory, SaltApplication app) throws Exception{
		this.app = app;
		map = new LinkedHashMap<String, Object>();
		//category specific data
		map.put("Attendee", jsonCategory.getString(KEY_ATTENDEE_REQUIREMENT)); //1-required, 2-Optional, 3-Not Required 
		map.put("CategoryTypeID", jsonCategory.getString(KEY_CATEGORYTYPEID));
		map.put("SpendLimit", jsonCategory.getString(KEY_SPENDLIMIT));
		//item specific data
		map.putAll(OnlineGateway.toMap(jsonClaimItem));
		//convert JSON time epoch to readable time
		map.put("ExpenseDate", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("ExpenseDate")));
		map.put("DateApprovedByApprover", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateApprovedByApprover")));
		map.put("DateApprovedByDirector", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateApprovedByDirector")));
		map.put("DateCancelled", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateCancelled")));
		map.put("DateCreated", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateCreated")));
		map.put("DateModified", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateModified")));
		map.put("DatePaid", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DatePaid")));
		map.put("DateRejected", app.onlineGateway.dJsonizeDate(jsonClaimItem.getString("DateRejected")));

		ArrayList<HashMap<String, Object>> attendeeMaps = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i<jsonClaimItem.getJSONArray("Attendees").length(); i++)
			attendeeMaps.add(new ClaimItemAttendee(jsonClaimItem.getJSONArray("Attendees").getJSONObject(i)).getMap());
		map.put("Attendees", app.gson.toJson(attendeeMaps, app.types.arrayListOfHashmapOfStringObject));
				 
		if(Boolean.parseBoolean(jsonClaimItem.getString("IsWithReceipt"))){
			JSONArray jsonAttachments = jsonClaimItem.getJSONArray("Attachment");
			if(jsonAttachments.length() > 0){ //for instances where attachment is true but attachment data are missing
				ArrayList<LinkedHashMap<String, Object>> attachments = new ArrayList<LinkedHashMap<String,Object>>();
				for(int i=0; i<jsonAttachments.length(); i++)
					attachments.add(new Document(jsonAttachments.getJSONObject(i), app).getMap());
				map.put("Attachment", app.gson.toJson(attachments, app.types.arrayListOfHashmapOfStringObject));
			}else
				map.put("IsWithReceipt", false);
				
		}
	}
	
	//constructor to be used when creating temporary claim items for claim item input fragment
	public ClaimItem(SaltApplication app){
		this.app = app;	
		map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
		map.put("Amount", 0);
		map.put("AmountInLC", 0);
		map.put("Attachment", new JSONArray());
		map.put("Attendees", new JSONArray());
		map.put("CategoryID", 0);
		map.put("CategoryName", "");
		map.put("ClaimID", 0);
		map.put("ClaimLineItemID", 0);
		map.put("ClaimLineItemNumber", "");
		map.put("ClaimStatus", ClaimHeader.STATUSKEY_OPEN);
		map.put("CompanyToChargeID", 0);
		map.put("CompanyToChargeName", "");
		map.put("CostCenterName", "");
		map.put("Currency", 0);
		map.put("CurrencyName", "");
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", "/Date(-2208988800000+0000)/");
		map.put("DateModified", "/Date(-2208988800000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("Description", "");
		map.put("ExchangeRate", 0);
		map.put("ExpenseDate", "/Date(-2208988800000+0000)/");
		map.put("IsRechargable", false);
		map.put("IsTaxRate", false);
		map.put("IsWithReceipt", false);
		map.put("LocalCurrency", 0);
		map.put("LocalCurrencyName", "");
		map.put("Mileage", 0);
		map.put("MileageFrom", "");
		map.put("MileageRate", 0);
		map.put("MileageReturn", false);
		map.put("MileageTo", "");
		map.put("MileageType", 0);
		map.put("ModifiedBy", 0);
		map.put("Notes", "");
		map.put("ProjectCodeID", 0);
		map.put("ProjectName", "");
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffID", 0);
		map.put("StaffName", "");
		map.put("StatusName", ClaimHeader.STATUSDESC_OPEN);
		map.put("StdExchangeRate", 0);
		map.put("TaxAmount", 0);	
	}
	
	//constructor to be used as the new claim item object in creating line items
	public ClaimItem(ClaimHeader claimHeader, SaltApplication app,
					 float amountInFC, float amountInLC, 
					 ArrayList<LinkedHashMap<String, Object>> attachments,
					 ArrayList<LinkedHashMap<String, Object>> attendees,
					 int catID, String catName,
					 int companyChargeToID, String companyChargeToName, 
					 int currencyID, String currencyName,
					 String desc,
					 float exchangeRate,
					 String expenseDate,
					 boolean isRechargeable, boolean isTaxRate, boolean hasReceipt,
					 int localCurrencyID, String localCurrencyName,
					 String notes,
					 int projectID, String projectName,
					 float standardExchangeRate,
					 float taxAmount){
		this.app = app;
		map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
		map.put("Amount", amountInFC);
		map.put("AmountInLC", amountInLC);
		map.put("Attachment", app.gson.toJson(attachments, app.types.arrayListOfHashmapOfStringObject));
		map.put("Attendees", app.gson.toJson(attendees, app.types.arrayListOfHashmapOfStringObject));
		map.put("CategoryID", catID);
		map.put("CategoryName", catName);
		map.put("ClaimID", claimHeader.getClaimID());
		map.put("ClaimLineItemID", 0);
		map.put("ClaimLineItemNumber", "");
		map.put("ClaimStatus", ClaimHeader.STATUSKEY_OPEN);
		map.put("CompanyToChargeID", companyChargeToID);
		map.put("CompanyToChargeName", companyChargeToName);
		map.put("CostCenterName", claimHeader.getCostCenterName());
		map.put("Currency", currencyID);
		map.put("CurrencyName", currencyName);
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", "/Date(-2208988800000+0000)/");
		map.put("DateModified", "/Date(-2208988800000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("Description", desc);
		map.put("ExchangeRate", exchangeRate);
		map.put("ExpenseDate", expenseDate);
		map.put("IsRechargable", isRechargeable);
		map.put("IsTaxRate", isTaxRate);
		map.put("IsWithReceipt", hasReceipt);
		map.put("LocalCurrency", localCurrencyID);
		map.put("LocalCurrencyName", localCurrencyName);
		map.put("ModifiedBy", claimHeader.getStaffID());
		map.put("Notes", notes);
		map.put("ProjectCodeID", projectID);
		map.put("ProjectName", projectName);
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffID", claimHeader.getStaffID());
		map.put("StaffName", claimHeader.getStaffName());
		map.put("StatusName", ClaimHeader.STATUSDESC_OPEN);
		map.put("StdExchangeRate", standardExchangeRate);
		map.put("TaxAmount", taxAmount);		
	}	
	
	public HashMap<String, Object> getMap(){
		return map;
	}
	
	public ArrayList<ClaimItemAttendee> getAttendees() {
		ArrayList<ClaimItemAttendee> attendees = new ArrayList<ClaimItemAttendee>();
		ArrayList<HashMap<String, Object>> attendeeMaps = app.gson.fromJson(map.get("Attendees").toString(), app.types.arrayListOfHashmapOfStringObject);
		for(HashMap<String, Object> map :attendeeMaps)
			attendees.add(new ClaimItemAttendee(map));
		
		return attendees;
	}
	
	public void updateAttachmentName(String name){
		map.put("OrigDocName", name);
	}
	
	public void updateAttendees(SaltApplication app, ArrayList<ClaimItemAttendee> attendees){
		ArrayList<HashMap<String, Object>> attendeeMaps = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i<attendees.size(); i++)
			attendeeMaps.add(attendees.get(i).getMap());
		map.put("Attendees", app.gson.toJson(attendeeMaps, app.types.arrayListOfHashmapOfStringObject));
	}
	
	public int getAttendeeRequirementTypeID(){
		return Integer.parseInt(map.get("Attendee").toString());
	}
	
	public int getStatusID(){
		return Integer.parseInt(map.get("ClaimStatus").toString());
	}
	
	public String getStatusName(){
		return ClaimHeader.getStatusDescriptionForKey(getStatusID());
	}
	
	public int getCategoryTypeID(){
		return Integer.parseInt(map.get("CategoryTypeID").toString());
	}
	
	public float getCategorySpendLimit(){
		return Float.parseFloat(map.get("SpendLimit").toString());
	}
	
	public int getItemID(){
		return Integer.parseInt(map.get("ClaimLineItemID").toString());
	}
	
	public String getItemNumber(){
		return map.get("ClaimLineItemNumber").toString();
	}
		
	public int statusID(){
		return Integer.parseInt(map.get("ClaimStatus").toString());
	}
	
	public int getProjectCodeID(){
		return Integer.parseInt(map.get("ProjectCodeID").toString());
	}
	
	public String getProjectName(){
		return map.get("ProjectName").toString();
	}
	
	public int getCategoryID(){
		return Integer.parseInt(map.get("CategoryID").toString());
	}
	
	public String getCategoryName(){
		return map.get("CategoryName").toString();
	}
	
	public float getForeignAmount(){
		return Float.parseFloat(map.get("Amount").toString());
	}
	
	public float getLocalAmount(){
		return Float.parseFloat(map.get("AmountInLC").toString());
	}
	
	public float getTaxAmount(){
		return Float.parseFloat(map.get("TaxAmount").toString());
	}
	
	public float getOnFileExchangeRate(){ //standard exchange rate
		return Float.parseFloat(map.get("StdExchangeRate").toString());
	}
	
	public float getOnActualConversionExchangeRate(){ //actual exchange rate
		return Float.parseFloat(map.get("ExchangeRate").toString());
	}
		
	public String getExpenseDate(){
		return map.get("ExpenseDate").toString();
	}
	
	public String getDescription(){
		return map.get("Description").toString();
	}
	
	public int getForeignCurrencyID(){
		return Integer.parseInt(map.get("Currency").toString());
	}
	
	public String getForeignCurrencyName(){
		return map.get("CurrencyName").toString();
	}
	
	public int getLocalCurrencyID(){
		return Integer.parseInt(map.get("LocalCurrency").toString());
	}
	
	public String getLocalCurrencyName(){
		return map.get("LocalCurrencyName").toString();
	}
	
	public String getNotes(){
		return map.get("Notes").toString();
	}
	
	public boolean hasReceipt(){
		return Boolean.parseBoolean(map.get("IsWithReceipt").toString());
	}
	
	public ArrayList<Document> getAttachment(SaltApplication app){
		ArrayList<Document> docs = new ArrayList<Document>();
		ArrayList<LinkedHashMap<String, Object>> mapDocs = app.gson.fromJson(map.get("Attachment").toString(), app.types.arrayListOfHashmapOfStringObject);
		for(int i=0; i<mapDocs.size(); i++)
			docs.add(new Document(mapDocs.get(i)));
	
		return docs;
	}
	
	public boolean isTaxApplied(){
		return Boolean.parseBoolean(map.get("IsTaxRate").toString());
	}
	
	public boolean isBillable(){
		return Boolean.parseBoolean(map.get("IsRechargable").toString());
	}
	
	public Integer getBillableCompanyID(){
		return Integer.parseInt(map.get("CompanyToChargeID").toString());
	}
	
	public String getBillableCompanyName(){
		return map.get("CompanyToChargeName").toString();
	}	
	
	public int getAttachmentDocumentID(){
		return Integer.parseInt(map.get("DocID").toString());
	}
	
	public String getAttachmentName(){
		ArrayList<HashMap<String, Object>> attachments = app.gson.fromJson(map.get("Attachment").toString(), app.types.arrayListOfHashmapOfStringObject);
		HashMap<String, Object> docMap = attachments.get(0);
		return docMap.get("OrigDocName").toString();
	}
	
	public String getAttachmentExtension(){
		ArrayList<HashMap<String, Object>> attachments = app.gson.fromJson(map.get("Attachment").toString(), app.types.arrayListOfHashmapOfStringObject);
		HashMap<String, Object> docMap = attachments.get(0);
		return docMap.get("Ext").toString();
	}
	
	public String jsonize(SaltApplication app){
		LinkedHashMap<String, Object> jsonizableMap = new LinkedHashMap<String, Object>();
		jsonizableMap.putAll(map);
		jsonizableMap.remove(KEY_CATEGORYTYPEID);
		jsonizableMap.remove(KEY_ATTENDEE_REQUIREMENT);
		jsonizableMap.remove(KEY_SPENDLIMIT);
		
		return app.gson.toJson(jsonizableMap, app.types.hashmapOfStringObject);
	}
	
	public static String getEmptyJSON(SaltApplication app) throws Exception{
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
		map.put("Amount", 0);
		map.put("AmountInLC", 0);
		map.put("Attachment", new JSONArray());
		map.put("Attendees", new JSONArray());
		map.put("CategoryID", 0);
		map.put("CategoryName", "");
		map.put("ClaimID", 0);
		map.put("ClaimLineItemID", 0);
		map.put("ClaimLineItemNumber", "");
		map.put("ClaimStatus", ClaimHeader.STATUSKEY_OPEN);
		map.put("CompanyToChargeID", 0);
		map.put("CompanyToChargeName", "");
		map.put("CostCenterName", "");
		map.put("Currency", 0);
		map.put("CurrencyName", "");
		map.put("DateApprovedByApprover", "/Date(-2208988800000+0000)/");
		map.put("DateApprovedByDirector", "/Date(-2208988800000+0000)/");
		map.put("DateCancelled", "/Date(-2208988800000+0000)/");
		map.put("DateCreated", "/Date(-2208988800000+0000)/");
		map.put("DateModified", "/Date(-2208988800000+0000)/");
		map.put("DatePaid", "/Date(-2208988800000+0000)/");
		map.put("DateRejected", "/Date(-2208988800000+0000)/");
		map.put("Description", "");
		map.put("ExchangeRate", 0);
		map.put("ExpenseDate", "/Date(-2208988800000+0000)/");
		map.put("IsRechargable", false);
		map.put("IsTaxRate", false);
		map.put("IsWithReceipt", false);
		map.put("LocalCurrency", 0);
		map.put("LocalCurrencyName", "");
		map.put("Mileage", 0);
		map.put("MileageFrom", "");
		map.put("MileageRate", 0);
		map.put("MileageReturn", false);
		map.put("MileageTo", "");
		map.put("MileageType", 0);
		map.put("ModifiedBy", 0);
		map.put("Notes", "");
		map.put("ProjectCodeID", 0);
		map.put("ProjectName", "");
		map.put("RejectedBy", 0);
		map.put("RejectedByName", "");
		map.put("StaffID", 0);
		map.put("StaffName", "");
		map.put("StatusName", ClaimHeader.STATUSDESC_OPEN);
		map.put("StdExchangeRate", 0);
		map.put("TaxAmount", 0);
		
		return new JSONObject(map).toString();
	}
	
}
