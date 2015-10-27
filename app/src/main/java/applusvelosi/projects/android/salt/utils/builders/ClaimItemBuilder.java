package applusvelosi.projects.android.salt.utils.builders;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;

public class ClaimItemBuilder {

	private int foreignCurrencyID, localCurrencyID, categoryID, companyChargeToID, projectCodeID;
	private String dateExpensed, localCurrencyName, foreignCurrencyName, categoryName, companyChargeToName, desc, notes, projectCodeName;
	private float localAmount, foreignAmount, taxAmount, exchangeRate, standardExchangeRate, taxRate;
	private boolean isRechargeable, isTaxRate, hasReceipt;
	private ArrayList<Document> documents;
	private ArrayList<ClaimItemAttendee> attendees;
	private SaltApplication app;
	private ClaimHeader claimHeader;
	
	//checker if boolean values have been set
	private boolean isForeignCurrencySet, isLocalCurrencySet, isCategorySet, isCompanyChargeToSet, isProjectSet, isExpenseDateSet, isDescSet,
					isAmountSet, isExchangeRateSet, isStandardExchangeRateSet, isTaxRateSet, 
					isRechargeableSet, isTaxableSet, isHasReceiptSet, isAttendeeSet;
	
//	public ClaimItem build() throws Exception{
//		if(!isForeignCurrencySet) throw new Exception("Foreign Currency must be set!");
//		else if(!isLocalCurrencySet) throw new Exception("Local Currency must be set!");
//		else if(!isCategorySet) throw new Exception("Category must be set!");
//		else if(!isCompanyChargeToSet) throw new Exception("Company Charge To must be set!");
//		else if(!isProjectSet) throw new Exception("Project must be set!");
//		else if(!isAmountSet) throw new Exception("Amounts must be set!");
//		else if(!isExchangeRateSet) throw new Exception("Exchange Rate must be set!");
//		else if(!isStandardExchangeRateSet) throw new Exception("Standard Exchange Rate must be set!");
//		else if(!isTaxRateSet) throw new Exception("Tax Rate must be set!");
//		else if(!isRechargeableSet) throw new Exception("Rechargeability must be set!");
//		else if(!isTaxableSet) throw new Exception("Taxability must be set!");
//		else if(!isHasReceiptSet) throw new Exception("Receipt Availability must be set!");
//		else if (!isAttendeeSet) throw new Exception("Attendees must be set!");
//		
//		return new ClaimItem(	claimHeader, app, foreignAmount, localAmount, documents, attendees, categoryID, categoryName, 
//								companyChargeToID, companyChargeToName, foreignCurrencyID, foreignCurrencyName, desc, exchangeRate, dateExpensed, 
//								isRechargeable, isTaxRate, hasReceipt, localCurrencyID, localCurrencyName, notes, projectCodeID, projectCodeName, 
//								standardExchangeRate, taxAmount);
//	}
	
	
	public ClaimItemBuilder(SaltApplication app, ClaimHeader claimHeader){
		this.app = app;
		this.claimHeader = claimHeader;
	}
	
	public ClaimItemBuilder setForeignCurrency(int foreignCurrencyID, String foreignCurrencyName){
		this.foreignCurrencyID = foreignCurrencyID;
		this.foreignCurrencyName = foreignCurrencyName;
		isForeignCurrencySet = true;
		return this;
	}
	
	public ClaimItemBuilder setLocalCurrencyID(int localCurrencyID, String localCurrencyName){
		this.localCurrencyID = localCurrencyID;
		this.localCurrencyName = localCurrencyName;
		isLocalCurrencySet = true;
		return this;
	}
	
	public ClaimItemBuilder setCategoryID(int categoryID, String categoryName){
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		isCategorySet = true;
		return this;
	}
	
	public ClaimItemBuilder setCompanyChargeToID(int companyChargeToID, String companyChargeToName){
		this.companyChargeToID = companyChargeToID;
		this.companyChargeToName = companyChargeToName;
		isCompanyChargeToSet = true;
		return this;
	}
	
	public ClaimItemBuilder setProjectCodeID(int projectCodeID, String projectCodeName){
		this.projectCodeID = projectCodeID;
		this.projectCodeName = projectCodeName;
		isProjectSet = true;
		return this;
	}
	
	public ClaimItemBuilder setDateExpensed(String dateExpensed){
		this.dateExpensed = dateExpensed;
		isExpenseDateSet = true;
		return this;
	}
	
	public ClaimItemBuilder setDescription(String description){
		this.desc = description;
		isDescSet = true;
		return this;
	}
	
	public ClaimItemBuilder setNotes(String notes){
		this.notes = notes;
		return this;
	}
	
	public ClaimItemBuilder setExchangeRate(float exchangeRate){
		this.exchangeRate = exchangeRate;
		isExchangeRateSet = true;
		return this;
	}
	
	public ClaimItemBuilder setStandardExchangeRate(float standardExchangeRate){
		this.standardExchangeRate = standardExchangeRate;
		isStandardExchangeRateSet = true;
		return this;
	}
	
	public ClaimItemBuilder setTaxRate(float taxRate){
		this.taxRate = taxRate;
		isTaxRateSet = true;
		return this;
	}
	
	public ClaimItemBuilder setRechargeable(boolean isRechargeable){
		this.isRechargeable = isRechargeable;
		isRechargeableSet = true;
		return this;
	}
	
	public ClaimItemBuilder setTaxable(boolean isTaxRate){
		this.isTaxRate = isTaxRate;
		isTaxableSet = true;
		return this;
	}
	
	public ClaimItemBuilder setReceiptRequired(boolean hasReceipt){
		this.hasReceipt = hasReceipt;
		isHasReceiptSet = true;
		return this;
	}
	
	public ClaimItemBuilder setDocuments(ArrayList<Document> documents){
		this.documents = new ArrayList<Document>();
		this.documents.addAll(documents);
		return this;
	}
	
	public ClaimItemBuilder setAttendees(ArrayList<ClaimItemAttendee> attendees){
		this.attendees = new ArrayList<ClaimItemAttendee>();
		this.attendees.addAll(attendees);
		isAttendeeSet = true;
		return this;
	}	
	
	public ClaimItemBuilder setAmount(float localAmount, float foreignAmount, float taxAmount){
		this.localAmount = localAmount;
		this.foreignAmount = foreignAmount;
		this.taxAmount = taxAmount;
		isAmountSet = true;
		return this;
	}
}
