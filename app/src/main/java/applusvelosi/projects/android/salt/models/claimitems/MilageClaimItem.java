package applusvelosi.projects.android.salt.models.claimitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class MilageClaimItem {
	public static final String MILEAGETYPE_VAL_KILOMETER = "KM";
	public static final String MILEAGETYPE_VAL_MILE = "MI";
	public static final int MILEAGETYPE_KEY_KILOMETER = 1;
	public static final int MILEAGETYPE_KEY_MILE = 2;
		

//	public MilageClaimItem(JSONObject jsonClaimItem, JSONObject category, SaltApplication app) throws Exception{
//		super(jsonClaimItem, category, app);
//		map.put("MileageType", jsonClaimItem.getString("MileageType"));
//		map.put("MileageRate", jsonClaimItem.getString("MileageRate"));
//		map.put("Mileage", jsonClaimItem.getString("Mileage"));
//		map.put("MileageFrom", jsonClaimItem.getString("MileageFrom"));
//		map.put("MileageTo", jsonClaimItem.getString("MileageTo"));
//		map.put("MileageReturn", jsonClaimItem.getString("MileageReturn"));
//	}
//
//	public MilageClaimItem(ClaimHeader claimHeader, SaltApplication app,
//			 float amountInFC, float amountInLC,
//			 ArrayList<LinkedHashMap<String, Object>> attachments,
//			 ArrayList<LinkedHashMap<String, Object>> attendees,
//			 int catID, String catName,
//			 int companyChargeToID, String companyChargeToName,
//			 int currencyID, String currencyName,
//			 String desc,
//			 float exchangeRate,
//			 String expenseDate,
//			 boolean isRechargeable, boolean isTaxRate, boolean hasReceipt,
//			 int localCurrencyID, String localCurrencyName,
//			 String notes,
//			 int projectID, String projectName,
//			 float standardExchangeRate,
//			 float taxAmount,
//			 int mileageType, float mileageRate, float mileage, String mileageFrom, String mileageTo, boolean isMileageReturn){
//		super(claimHeader, app, amountInFC, amountInLC, attachments, attendees, catID, catName, companyChargeToID, companyChargeToName,
//			  currencyID, currencyName, desc, exchangeRate, expenseDate, isRechargeable, isTaxRate, hasReceipt,
//			  localCurrencyID, localCurrencyName, notes, projectID, projectName, standardExchangeRate, taxAmount);
//
//		map.put("MileageType", mileageType);
//		map.put("MileageRate", mileageRate);
//		map.put("Mileage", mileage);
//		map.put("MileageFrom", mileageFrom);
//		map.put("MileageTo", mileageTo);
//		map.put("MileageReturn", isMileageReturn);
//
//
//	}
//
//	public int getMilageTypeID(){
//		return Integer.parseInt(map.get("MileageType").toString());
//	}
//
//	public String getMilageTypeName(){
//		return (getMilageTypeID()==MILEAGETYPE_KEY_KILOMETER)?MILEAGETYPE_VAL_KILOMETER:MILEAGETYPE_VAL_MILE;
//	}
//
//	public float getMilageRate(){
//		return Float.parseFloat(map.get("MileageRate").toString());
//	}
//
//	public float getMileage(){
//		return Float.parseFloat(map.get("Mileage").toString());
//	}
//
//	public String getMileageFrom(){
//		return map.get("MileageFrom").toString();
//	}
//
//	public String getMileageTo(){
//		return map.get("MileageTo").toString();
//	}
//
//	public boolean isMileageReturn(){
//		return Boolean.parseBoolean(map.get("MileageReturn").toString());
//	}
//
}
