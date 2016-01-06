package applusvelosi.projects.android.salt.models.claimitems;

import com.google.gson.JsonArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;

public class ClaimItem implements Serializable {
	public static final String KEY_CATEGORYID = "CategoryID";
	public static final String KEY_CATEGORYTYPEID = "CategoryTypeID";
	private static final String KEY_ATTENDEE_REQUIREMENT = "Attendee";
	private static final String KEY_SPENDLIMIT = "SpendLimit";

	protected boolean isActive;
    protected float amount, amountLC;
    protected ArrayList<Document> attachments;
    protected ArrayList<ClaimItemAttendee> attendees;
    protected int categoryID, categoryTypeID;
    protected String categoryName;
    protected int claimID, claimLineItemID;
    protected String claimLineItemNumber;
    protected int claimStatusID, companyToChargeID;
    protected String companyToChargeName, costCenterName;
    protected int currencyID;
    protected String currencyName, dateApprovedByApprover, dateApprovedByDirector, dateCancelled, dateCreated, dateModified, datePaid, dateRejected, description;
    protected float forex;
    protected String dateExpensed;
    protected boolean isRechargeable, isTaxRate, hasReceipt;
    protected int localCurrencyID;
    protected String localCurrencyName;
    protected float mileage;
    protected String mileageFrom;
    protected float mileageRate;
    protected boolean mileageReturn;
    protected String mileageTo;
    protected int mileageType, modifiedBy;
    protected String notes;
    protected int projectCodeID;
    protected String projectName;
    protected int rejectedByID;
    protected String rejectedByName;
    protected int staffID;
    protected String staffName, statusName;
    protected float stdExchangeRate, taxAmount;

    protected Category category;

	public ClaimItem(JSONObject jsonClaimItem, JSONObject jsonCategory) throws Exception{
        categoryTypeID = jsonCategory.getInt("CategoryTypeID");
        isActive = jsonClaimItem.getBoolean("Active");
        amount = (float)jsonClaimItem.getDouble("Amount");
        amountLC = (float)jsonClaimItem.getDouble("AmountInLC");
        categoryID = jsonClaimItem.getInt("CategoryID");
        categoryName = jsonClaimItem.getString("CategoryName");
        claimID = jsonClaimItem.getInt("ClaimID");
        claimLineItemID = jsonClaimItem.getInt("ClaimLineItemID");
        claimLineItemNumber = jsonClaimItem.getString("ClaimLineItemNumber");
        claimStatusID = jsonClaimItem.getInt("ClaimStatus");
        companyToChargeID = jsonClaimItem.getInt("CompanyToChargeID");
        companyToChargeName = jsonClaimItem.getString("CompanyToChargeName");
        costCenterName = jsonClaimItem.getString("CostCenterName");
        currencyID = jsonClaimItem.getInt("Currency");
        currencyName = jsonClaimItem.getString("CurrencyName");
        dateApprovedByApprover = jsonClaimItem.getString("DateApprovedByApprover");
        dateApprovedByDirector = jsonClaimItem.getString("DateApprovedByDirector");
        dateCancelled = jsonClaimItem.getString("DateCancelled");
        dateModified = jsonClaimItem.getString("DateModified");
        datePaid = jsonClaimItem.getString("DatePaid");
        dateRejected = jsonClaimItem.getString("DateRejected");
        description = jsonClaimItem.getString("Description");
        forex = (float)jsonClaimItem.getDouble("ExchangeRate");
        dateExpensed = jsonClaimItem.getString("ExpenseDate");
        isRechargeable = jsonClaimItem.getBoolean("IsRechargable");
        isTaxRate = jsonClaimItem.getBoolean("IsTaxRate");
        hasReceipt = jsonClaimItem.getBoolean("IsWithReceipt");
        localCurrencyID = jsonClaimItem.getInt("LocalCurrency");
        localCurrencyName = jsonClaimItem.getString("LocalCurrencyName");
        mileage = (float)jsonClaimItem.getDouble("Mileage");
        mileageFrom = jsonClaimItem.getString("MileageFrom");
        mileageRate = (float)jsonClaimItem.getDouble("MileageRate");
        mileageTo = jsonClaimItem.getString("MileageTo");
        mileageType = jsonClaimItem.getInt("MileageType");
        modifiedBy = jsonClaimItem.getInt("ModifiedBy");
        notes = jsonClaimItem.getString("Notes");
        projectCodeID = jsonClaimItem.getInt("ProjectCodeID");
        projectName = jsonClaimItem.getString("ProjectName");
        rejectedByID = jsonClaimItem.getInt("RejectedBy");
        rejectedByName = jsonClaimItem.getString("RejectedByName");
        staffID = jsonClaimItem.getInt("StaffID");
        staffName = jsonClaimItem.getString("StaffName");
        statusName = jsonClaimItem.getString("StatusName");
        stdExchangeRate = (float)jsonClaimItem.getDouble("StdExchangeRate");
        taxAmount = (float)jsonClaimItem.getDouble("TaxAmount");

        JSONArray jsonDocs = jsonClaimItem.getJSONArray("Attachment");
        attachments = new ArrayList<Document>();
        for(int i=0; i<jsonDocs.length(); i++)
            attachments.add(new Document(jsonDocs.getJSONObject(i)));

        JSONArray jsonAttendees = jsonClaimItem.getJSONArray("Attendees");
        attendees = new ArrayList<ClaimItemAttendee>();
        for(int i=0; i<jsonAttendees.length(); i++)
            attendees.add(new ClaimItemAttendee(jsonAttendees.getJSONObject(i)));

        category = new Category(jsonCategory);
	}

    //constructor for new claim
    public ClaimItem(ClaimHeader claimHeader){
        isActive = true;
        amount = 0;
        amountLC = 0;
        categoryID = 0;
        categoryName = "";
        claimID = claimHeader.getClaimID();
        claimLineItemID = 0;
        claimLineItemNumber = "";
        claimStatusID = ClaimHeader.STATUSKEY_SUBMITTED;
        companyToChargeID = 0;
        companyToChargeName = "";
        costCenterName = claimHeader.getCostCenterName();
        currencyID = 0;
        currencyName = "";
        dateApprovedByApprover = "/Date(-2208988800000+0000)/";
        dateApprovedByDirector = "/Date(-2208988800000+0000)/";
        dateCancelled = "/Date(-2208988800000+0000)/";
        dateModified = "/Date(-2208988800000+0000)/";
        datePaid = "/Date(-2208988800000+0000)/";
        dateRejected = "/Date(-2208988800000+0000)/";
        description = "";
        forex = 0;
        dateExpensed = "\"/Date(-2208988800000+0000)/\"";
        isRechargeable = false;
        isTaxRate = false;
        hasReceipt = false;
        localCurrencyID = 0;
        localCurrencyName = "";
        mileage = 0;
        mileageFrom = "";
        mileageRate = 0;
        mileageTo = "";
        mileageType = 0;
        modifiedBy = 0;
        notes = "";
        projectCodeID = 0;
        projectName = "";
        rejectedByID = 0;
        rejectedByName = "";
        staffID = claimHeader.getStaffID();
        staffName = claimHeader.getStaffName();
        statusName = ClaimHeader.STATUSDESC_SUBMITTED;
        stdExchangeRate = 0;
        taxAmount = 0;
        attachments = new ArrayList<Document>();
        attendees = new ArrayList<ClaimItemAttendee>();
    }

    public void setClaimLineItemID(int itemID){ this.claimLineItemID = itemID; }
    public void setAmount(float amount){ this.amount = amount; }
    public void setAmountLC(float amountLC){ this.amountLC = amountLC; }
    public void setCategory(Category category){ this.categoryID = category.getCategoryID(); this.categoryName = category.getName(); }
    public void setCompanyToCharge(Office office){ this.companyToChargeID = office.getID(); this.companyToChargeName = office.getName(); }
    public void setCurrency(Currency currency){ this.currencyID = currency.getCurrencyID(); this.currencyName = currency.getCurrencySymbol(); }
    public void setDescription(String description){ this.description = description; }
    public void setForex(float forex){ this.forex = forex; }
    public void setDateExpensed(Date expenseDate, SaltApplication app){ this.dateExpensed = app.onlineGateway.jsonizeDate(expenseDate); }
    public void setIsRechargable(boolean isRechargable){ this.isRechargeable = isRechargable; }
    public void setIsTaxRated(boolean isTaxRate){ this.isTaxRate = isTaxRate; }
    public void setHasReceipt(boolean hasReceipt){ this.hasReceipt = hasReceipt; }
    public void setLocalCurrency(Currency localCurrency){ this.localCurrencyID = localCurrency.getCurrencyID(); this.localCurrencyName = localCurrency.getCurrencySymbol(); }
    public void setNotes(String notes){ this.notes = notes; }
    public void setProject(Project project){ this.projectCodeID = project.getID(); this.projectName = project.getName(); }
    public void setStandardExchangeRate(float standardExchangeRate){ this.stdExchangeRate = standardExchangeRate; }
    public void setTaxAmount(float taxAmount){ this.taxAmount = taxAmount; }
    public void addAttachment(Document document){ this.attachments.add(0, document); }
    public void removeAttachment(Document document){ this.attachments.remove(document); }
    public void removeAllAttachments(){ this.attachments.clear(); }
    public void addAttendee(ClaimItemAttendee attendee){ this.attendees.add(attendee); }
    public void removeAttendee(ClaimItemAttendee attendee){ this.attendees.remove(attendee); }
    public void setDateCreated(String dateCreated){ this.dateCreated = dateCreated; }

	public static String getEmptyClaimLineItemJSON(SaltApplication app){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
		map.put("Amount", 0);
		map.put("AmountInLC", 0);
		map.put("Attachment", new JsonArray());
		map.put("Attendees", new JsonArray());
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

        return app.gson.toJson(map, app.types.hashmapOfStringObject);
	}

    public String getAttachmentName(){ return (attachments.size()>0)?attachments.get(0).getDocName():""; }
	public int getStatusID(){ return claimStatusID; }
	public String getStatusName(){ return statusName; }
	public int getItemID(){ return claimLineItemID; }
	public String getItemNumber(){ return claimLineItemNumber; }
	public int statusID(){ return claimStatusID; }
	public int getProjectCodeID(){ return projectCodeID; }
	public String getProjectName(){ return projectName; }
	public int getCategoryID(){ return categoryID; }
    public int getCategoryTypeID(){ return categoryTypeID; }
	public String getCategoryName(){ return categoryName; }
	public float getForeignAmount(){ return amount; }
	public float getLocalAmount(){ return amountLC; }
	public float getTaxAmount(){ return taxAmount; }
	public float getStandardExchangeRate(){ return stdExchangeRate; }
	public float getForex(){ return forex; }
	public String getExpenseDate(SaltApplication app){ return app.onlineGateway.dJsonizeDate(dateExpensed); }
	public String getDescription(){ return description; }
	public int getForeignCurrencyID(){ return currencyID; }
	public String getForeignCurrencyName(){ return currencyName; }
	public int getLocalCurrencyID(){ return localCurrencyID; }
    public String getLocalCurrencyName(){ return localCurrencyName; }
	public String getNotes(){ return notes; }
	public boolean hasReceipt(){ return hasReceipt; }
	public ArrayList<Document> getAttachments(){ return attachments; }
    public ArrayList<ClaimItemAttendee> getAttendees(){ return attendees; }
	public boolean isTaxApplied(){ return isTaxRate; }
	public boolean isBillable(){ return isRechargeable; }
	public Integer getBillableCompanyID(){ return companyToChargeID; }
	public String getBillableCompanyName(){ return companyToChargeName; }

    public Category getCategory(){ return category; }

	public String jsonize(SaltApplication app) throws Exception{
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
        map.put("Amount", amount);
        map.put("AmountInLC", amountLC);
        JsonArray jsonAttachments = new JsonArray();
        for(Document attachment :attachments)
            jsonAttachments.add(attachment.getJSONObject());
        map.put("Attachment", jsonAttachments);
        JsonArray jsonAttendees = new JsonArray();
        for(ClaimItemAttendee attendee :attendees)
            jsonAttendees.add(attendee.getJSONObject());
        map.put("Attendees", jsonAttendees);
        map.put("CategoryID", categoryID);
        map.put("CategoryName",categoryName);
        map.put("ClaimID", claimID);
        map.put("ClaimLineItemID", claimLineItemID);
        map.put("ClaimLineItemNumber", claimLineItemNumber);
        map.put("ClaimStatus", claimStatusID);
        map.put("CompanyToChargeID", companyToChargeID);
        map.put("CompanyToChargeName", companyToChargeName);
        map.put("CostCenterName", costCenterName);
        map.put("Currency", currencyID);
        map.put("CurrencyName", currencyName);
        map.put("DateApprovedByApprover", dateApprovedByApprover);
        map.put("DateApprovedByDirector", dateApprovedByDirector);
        map.put("DateCancelled", dateCancelled);
        map.put("DateCreated", dateCreated);
        map.put("DateModified", dateModified);
        map.put("DatePaid", datePaid);
        map.put("DateRejected", dateRejected);
        map.put("Description", description);
        map.put("ExchangeRate", forex);
        map.put("ExpenseDate", dateExpensed);
        map.put("IsRechargable", isRechargeable);
        map.put("IsTaxRate", isTaxRate);
        map.put("IsWithReceipt", hasReceipt);
        map.put("LocalCurrency", localCurrencyID);
        map.put("LocalCurrencyName", localCurrencyName);
        map.put("Mileage", 0);
        map.put("MileageFrom", "");
        map.put("MileageRate", 0);
        map.put("MileageReturn", false);
        map.put("MileageTo", "");
        map.put("MileageType", 0);
        map.put("ModifiedBy", modifiedBy);
        map.put("Notes", notes);
        map.put("ProjectCodeID", projectCodeID);
        map.put("ProjectName", projectName);
        map.put("RejectedBy", rejectedByID);
        map.put("RejectedByName", rejectedByName);
        map.put("StaffID", staffID);
        map.put("StaffName", staffName);
        map.put("StatusName", statusName);
        map.put("StdExchangeRate", stdExchangeRate);
        map.put("TaxAmount", taxAmount);

		return app.gson.toJson(map, app.types.hashmapOfStringObject);
	}

}
