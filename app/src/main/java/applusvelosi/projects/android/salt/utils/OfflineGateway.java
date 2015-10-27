package applusvelosi.projects.android.salt.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.LocalHoliday;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.views.fragments.HolidaysLocalFragment;

public class OfflineGateway {
	
	public enum SerializableClaimTypes{
		MY_CLAIM("ogJSONMyClaimList"),
		FOR_APPROVAL("ogJSONClaimsForApproval"),
		FOR_PAYMENT("ogJSONClaimsForPayment");
		
		private String name;
		
		private SerializableClaimTypes(String name){
			this.name = name;
		}
		
		public String toString(){
			return name;
		}		
	}
	
	private final String PREFERENCE_KEY = "saltsharedprefskey";
	//keys
	private final String KEYJSONSTAFF = "ogJSONStaff";
	private final String KEYJSONOFFICE = "ogJSONOffice";
	private final String KEYJSONMYLEAVES = "ogJSONMyLeaveList";
	private final String KEYJSONLEAVESFORAPPROVAL = "ogJSONLeavesForApproval";
	private final String KEYJSONNATIONALHOLIDAYS = "ogJSONNationalHolidays";
	private final String KEYJSONLOCALHOLIDAYS = "ogJSONLocalHolidays";
	private final String KEYJSONCURRENCIES = "ogJSONCurrencies";
	
	private SaltApplication app;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	public OfflineGateway(SaltApplication app){
		this.app = app;
		prefs = app.getSharedPreferences(PREFERENCE_KEY, 0);
		editor = prefs.edit();
	}
	
	public boolean isLoggedIn(){
		return (prefs.contains(KEYJSONSTAFF))?true:false;
	}
	
	public void logout(){
		editor.clear().commit();
	}
	
	public void serializeCurrencies(ArrayList<Currency> currencies){
		ArrayList<HashMap<String, Object>> myClaimMaps = new ArrayList<HashMap<String,Object>>();
		for(Currency currency :currencies)
			myClaimMaps.add(currency.getMap());

		editor.putString(KEYJSONCURRENCIES, app.gson.toJson(myClaimMaps, app.types.arrayListOfHashmapOfStringObject)).commit();		
	}
	
	public void serializeStaffData(Staff staff, Office office){
		editor.putString(KEYJSONSTAFF, app.gson.toJson(staff.getMap(), app.types.hashmapOfStringObject));
		editor.putString(KEYJSONOFFICE, app.gson.toJson(office.getMap(), app.types.hashmapOfStringString)).commit();
	}

	public void serializeNationHolidays(SaltApplication key, ArrayList<Holiday> holidays){
		editor.putString(KEYJSONNATIONALHOLIDAYS, app.gson.toJson(holidays, app.types.arrayListOfHolidays)).commit();
	}
	
	public void serializeLocalHolidays(SaltApplication key, ArrayList<LocalHoliday> holidays){
		editor.putString(KEYJSONLOCALHOLIDAYS, app.gson.toJson(holidays, app.types.arrayListOfLocalHolidays)).commit();		
	}
	
	public void serializeMyLeaves(ArrayList<Leave> myLeaves){
		editor.putString(KEYJSONMYLEAVES, app.gson.toJson(myLeaves, app.types.arrayListOfLeaves)).commit();
	}
	
	public void serializeLeaveForApproval(ArrayList<Leave> leavesForApproval){
		editor.putString(KEYJSONLEAVESFORAPPROVAL, app.gson.toJson(leavesForApproval, app.types.arrayListOfLeaves)).commit();		
	}
	
	public void addMyLeave(SaltApplication key, Leave leave){
		ArrayList<Leave> myLeaves = app.gson.fromJson(prefs.getString(KEYJSONMYLEAVES, "[]"), app.types.arrayListOfLeaves);
		myLeaves.add(leave);
		editor.putString(KEYJSONMYLEAVES, app.gson.toJson(myLeaves, app.types.arrayListOfLeaves)).commit();
	}
	
	public void addLeaveForApproval(SaltApplication key, Leave leave){
		ArrayList<Leave> leavesForApproval = app.gson.fromJson(prefs.getString(KEYJSONLEAVESFORAPPROVAL, "[]"), app.types.arrayListOfLeaves);
		leavesForApproval.add(leave);
		editor.putString(KEYJSONLEAVESFORAPPROVAL, app.gson.toJson(leavesForApproval, app.types.arrayListOfLeaves)).commit();		
	}
			
	public void serializeClaimHeaders(ArrayList<ClaimHeader> claimHeaders, SerializableClaimTypes type){	
		ArrayList<HashMap<String, Object>> myClaimMaps = new ArrayList<HashMap<String,Object>>();
		for(ClaimHeader claimHeader :claimHeaders)
			myClaimMaps.add(claimHeader.getMap());

		editor.putString(type.toString(), app.gson.toJson(myClaimMaps, app.types.arrayListOfHashmapOfStringObject)).commit();
	}

	public Staff deserializeStaff(){
		HashMap<String, Object>staffMap = app.gson.fromJson(prefs.getString(KEYJSONSTAFF, "{}"), app.types.hashmapOfStringObject);
		return new Staff(staffMap);
	}
	
	public Office deserializeStaffOffice(){
		HashMap<String, String>staffOfficeMap = app.gson.fromJson(prefs.getString(KEYJSONOFFICE, "{}"), app.types.hashmapOfStringString);
		return new Office(staffOfficeMap);
	}
		
	public ArrayList<Holiday> deserializeNationalHolidays(){
		return app.gson.fromJson(prefs.getString(KEYJSONNATIONALHOLIDAYS, "[]"), app.types.arrayListOfHolidays);
	}

	public ArrayList<LocalHoliday> deserializeLocalHolidays(){
		return app.gson.fromJson(prefs.getString(KEYJSONLOCALHOLIDAYS, "[]"), app.types.arrayListOfLocalHolidays);
	}
	
	public ArrayList<Leave> deserializeMyLeaves(){
		return app.gson.fromJson(prefs.getString(KEYJSONMYLEAVES, "[]"), app.types.arrayListOfLeaves);
	}
	
	public ArrayList<Leave> deserializeLeavesForApproval(){
		return app.gson.fromJson(prefs.getString(KEYJSONLEAVESFORAPPROVAL, "[]"), app.types.arrayListOfLeaves);
	}
	
	public ArrayList<HashMap<String, Object>> deserializeClaimHeader(SerializableClaimTypes type){
		return app.gson.fromJson(prefs.getString(type.toString(), "[]"), app.types.arrayListOfHashmapOfStringObject);
	}
	
	public ArrayList<Currency> deserializeCurrencies(){
		ArrayList<Currency> currencies = new ArrayList<Currency>();
		ArrayList<HashMap<String, Object>> currencyMapList = app.gson.fromJson(prefs.getString(KEYJSONCURRENCIES, "[]"), app.types.arrayListOfHashmapOfStringObject);
		for(int i=0; i<currencyMapList.size(); i++)
			currencies.add(new Currency(currencyMapList.get(i)));
		
		return currencies;
	}
	
}
