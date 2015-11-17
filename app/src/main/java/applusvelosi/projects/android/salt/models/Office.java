package applusvelosi.projects.android.salt.models;

import java.util.HashMap;

import org.json.JSONObject;

public class Office {
	private final HashMap<String, String> map;
	
	public Office(JSONObject jsonOffice, boolean isFullDetail) throws Exception{
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("OfficeID", jsonOffice.getString("OfficeID"));
		temp.put("OfficeName", jsonOffice.getString("OfficeName"));
		if(isFullDetail){
			temp.put("Sunday", jsonOffice.getString("Sunday"));
			temp.put("Monday", jsonOffice.getString("Monday"));
			temp.put("Tuesday", jsonOffice.getString("Tuesday"));
			temp.put("Wednesday", jsonOffice.getString("Wednesday"));
			temp.put("Thursday", jsonOffice.getString("Thursday"));
			temp.put("Friday", jsonOffice.getString("Friday"));
			temp.put("Saturday", jsonOffice.getString("Saturday"));
			temp.put("IsUseBdayLeave", jsonOffice.getString("IsUseBdayLeave"));
			temp.put("IsHeadQuarter", jsonOffice.getString("IsHeadQuarter"));
			temp.put("MaternityLimit", jsonOffice.getString("MaternityLimit"));
			temp.put("PaternityLimit", jsonOffice.getString("PaternityLimit"));
			temp.put("HospitalizationLimit", jsonOffice.getString("HospitalizationLimit"));
			temp.put("BereavementLimit", jsonOffice.getString("BereavementLimit"));
			temp.put("SickLeaveAllowance", jsonOffice.getString("SickLeaveAllowance"));
			temp.put("VacationLeaveAllowance", jsonOffice.getString("VacationLeaveAllowance"));
			temp.put("BaseCurrency", jsonOffice.getString("BaseCurrency"));
			temp.put("BaseCurrencyName", jsonOffice.getString("BaseCurrencyName"));
			temp.put("MileageCurrency", jsonOffice.getString("MileageCurrency"));
			temp.put("MileageCurrencyName", jsonOffice.getString("MileageCurrencyName"));		
			temp.put("HROfficerID", jsonOffice.getString("HROfficerID"));
			temp.put("Active", jsonOffice.getString("Active"));
			temp.put("CountryManager",jsonOffice.getString("CountryManager"));
			temp.put("RegionalManager", jsonOffice.getString("RegionalManager"));
			temp.put("DefaultTax", jsonOffice.getString("DefaultTax"));
		}
		
		map = temp;
	}
	
	public Office(HashMap<String, String> officeMap){
		this.map = officeMap;
	}
	
	public int getID(){
		return Integer.parseInt(map.get("OfficeID")); 
	}
	
	public String getName(){
		return map.get("OfficeName");
	}
	
	public int getHROfficerID(){
		return Integer.parseInt(map.get("HROfficerID"));
	}
	
	public boolean isActive(){
		return Boolean.parseBoolean(map.get("Active"));
	}
	
	public boolean hasSunday(){
		return Boolean.parseBoolean(map.get("Sunday"));
	}
	
	public boolean hasMonday(){
		return Boolean.parseBoolean(map.get("Monday"));
	}
	
	public boolean hasTuesday(){
		return Boolean.parseBoolean(map.get("Tuesday"));
	}
	
	public boolean hasWednesday(){
		return Boolean.parseBoolean(map.get("Wednesday"));
	}
	
	public boolean hasThursday(){
		return Boolean.parseBoolean(map.get("Thursday"));
	}
	
	public boolean hasFriday(){
		return Boolean.parseBoolean(map.get("Friday"));
	}
	
	public boolean hasSaturday(){
		return Boolean.parseBoolean(map.get("Saturday"));
	}
	
	public boolean hasBirthdayLeave(){
		return Boolean.parseBoolean(map.get("IsUseBdayLeave"));
	}

	public boolean isHeadQuarter(){ return Boolean.parseBoolean(map.get("IsHeadQuarter")); }
	
	public float getMaternityLimit(){
		return Float.parseFloat(map.get("MaternityLimit"));
	}
	
	public float getPaternityLimit(){
		return Float.parseFloat(map.get("PaternityLimit"));
	}
		
	public float getHospitalizationLimit(){
		return Float.parseFloat(map.get("HospitalizationLimit"));
	}
	
	public float getBereavementLimit(){
		return Float.parseFloat(map.get("BereavementLimit"));
	}
	
	public float getSickLimit(){
		return Float.parseFloat(map.get("SickLeaveAllowance"));
	}
	
	public float getVacationLimit(){
		return Float.parseFloat(map.get("VacationLeaveAllowance"));
	}
	
	public int getBaseCurrencyID(){
		return Integer.parseInt(map.get("BaseCurrency").toString());
	}
	
	public String getBaseCurrencyName(){
		String baseName = map.get("BaseCurrencyName").split("\\(")[0];
		return baseName.substring(0, baseName.length() - 1);
	}
	
	public String getBaseCurrencyThree(){
		String baseThree = map.get("BaseCurrencyName").split("\\(")[1];
		return baseThree.substring(0, baseThree.length()-1);
	}
	
	public int getMileageCurrencyID(){
		return Integer.parseInt(map.get("MileageCurrency").toString());
	}
	
	public String getMileageCurrencyName(){
		String baseName = map.get("MileageCurrencyName").split("\\(")[0];
		return baseName.substring(0, baseName.length() - 1);
	}

	public String getMileageCurrencyThree(){
		String baseThree = map.get("MileageCurrencyName").split("\\(")[1];
		return baseThree.substring(0, baseThree.length()-1);
	}

	public int getCMID(){
		return Integer.parseInt(map.get("CountryManager").toString());
	}

	public int getRMID(){
		return Integer.parseInt(map.get("RegionalManager").toString());
	}

	public float getDefaultTax(){ return Float.parseFloat(map.get("DefaultTax").toString()); }

	public HashMap<String, String> getMap(){
		return map;
	}
	
}
