package applusvelosi.projects.android.salt.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.CountryHoliday;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;

public class OfflineGateway {

	private final String PREFERENCE_KEY = "saltsharedprefskey";
	//keys
	private final String KEYJSONSTAFF = "ogJSONStaff";
	private final String KEYJSONOFFICE = "ogJSONOffice";
	private final String KEYJSONMYLEAVES = "ogJSONMyLeaveList";
	private final String KEYJSONMYCLAIMS = "ogJSONMyClaimList";
	private final String KEYJSONNATIONALHOLIDAYS = "ogJSONNationalHolidays";
	private final String KEYJSONLOCALHOLIDAYS = "ogJSONLocalHolidays";
	private final String KEYMYCALENDARHOLIDAYS = "ogJSONMyCalendarHolidays";
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
		ArrayList<HashMap<String, Object>> currenciesMap = new ArrayList<HashMap<String,Object>>();
		for(Currency currency :currencies)
            currenciesMap.add(currency.getMap());

		editor.putString(KEYJSONCURRENCIES, app.gson.toJson(currenciesMap, app.types.arrayListOfHashmapOfStringObject)).commit();
	}
	
	public void serializeStaffData(Staff staff, Office office){
		editor.putString(KEYJSONSTAFF, app.gson.toJson(staff, app.types.staff));
		editor.putString(KEYJSONOFFICE, app.gson.toJson(office.getMap(), app.types.hashmapOfStringString)).commit();
	}

	public void serializeNationHolidays(SaltApplication key, ArrayList<CountryHoliday> holidays){
		editor.putString(KEYJSONNATIONALHOLIDAYS, app.gson.toJson(holidays, app.types.arrayListOfHolidays)).commit();
	}
	
	public void serializeLocalHolidays(SaltApplication key, ArrayList<Holiday> holidays){
		editor.putString(KEYJSONLOCALHOLIDAYS, app.gson.toJson(holidays, app.types.arrayListOfLocalHolidays)).commit();
	}

    public void serializeMyCalendarHolidays(ArrayList<CountryHoliday> holidays){
        editor.putString(KEYMYCALENDARHOLIDAYS, app.gson.toJson(holidays, app.types.arrayListOfHolidays)).commit();
    }

	public void serializeMyLeaves(ArrayList<Leave> myLeaves){
		editor.putString(KEYJSONMYLEAVES, app.gson.toJson(myLeaves, app.types.arrayListOfLeaves)).commit();
	}

	public void serializeMyClaims(ArrayList<ClaimHeader> claimHeaders){
        ArrayList<HashMap<String, Object>> myClaimMaps = new ArrayList<HashMap<String,Object>>();
        for(ClaimHeader claimHeader:claimHeaders)
            myClaimMaps.add(claimHeader.getMap());

        editor.putString(KEYJSONMYCLAIMS, app.gson.toJson(myClaimMaps, app.types.arrayListOfHashmapOfStringObject)).commit();
    }

	public void serializeMyClaimItem(int claimID, ClaimItem claimItem){
		ArrayList<ClaimItem> claimItems = deserializeMyClaimItems(claimID);
		claimItems.add(claimItem);
		editor.putString("CLAIM"+claimID, app.gson.toJson(claimItems, app.types.arrayListOfClaimItems)).commit();
	}

    public void serializeMyClaimItem(int claimID, ArrayList<ClaimItem> claimItems){
        ArrayList<ClaimItem> offlineClaimItems = deserializeMyClaimItems(claimID);
        offlineClaimItems.clear();
        offlineClaimItems.addAll(claimItems);
        editor.putString("CLAIM"+claimID, app.gson.toJson(claimItems, app.types.arrayListOfClaimItems)).commit();
    }

    public Staff deserializeStaff(){
//		HashMap<String, Object>staffMap = app.gson.fromJson(prefs.getString(KEYJSONSTAFF, "{}"), app.types.hashmapOfStringObject);
//		return new Staff(staffMap);
		return app.gson.fromJson(prefs.getString(KEYJSONSTAFF, "{}"), app.types.staff);
	}
	
	public Office deserializeStaffOffice(){
		HashMap<String, String>staffOfficeMap = app.gson.fromJson(prefs.getString(KEYJSONOFFICE, "{}"), app.types.hashmapOfStringString);
		return new Office(staffOfficeMap);
	}
		
	public ArrayList<CountryHoliday> deserializeNationalHolidays(){
		return app.gson.fromJson(prefs.getString(KEYJSONNATIONALHOLIDAYS, "[]"), app.types.arrayListOfHolidays);
	}

	public ArrayList<Holiday> deserializeLocalHolidays(){
		return app.gson.fromJson(prefs.getString(KEYJSONLOCALHOLIDAYS, "[]"), app.types.arrayListOfLocalHolidays);
	}

	public ArrayList<CountryHoliday> deserializeMyCalendarHolidays(){
		return app.gson.fromJson(prefs.getString(KEYMYCALENDARHOLIDAYS, "[]"), app.types.arrayListOfHolidays);
	}

	public ArrayList<Leave> deserializeMyLeaves(){
		return app.gson.fromJson(prefs.getString(KEYJSONMYLEAVES, "[]"), app.types.arrayListOfLeaves);
	}

	public ArrayList<HashMap<String, Object>> deserializeMyClaims(){
		return app.gson.fromJson(prefs.getString(KEYJSONMYCLAIMS, "[]"), app.types.arrayListOfHashmapOfStringObject);
	}

	public ArrayList<Currency> deserializeCurrencies(){
		ArrayList<Currency> currencies = new ArrayList<Currency>();
		ArrayList<HashMap<String, Object>> currencyMapList = app.gson.fromJson(prefs.getString(KEYJSONCURRENCIES, "[]"), app.types.arrayListOfHashmapOfStringObject);
		for(int i=0; i<currencyMapList.size(); i++)
			currencies.add(new Currency(currencyMapList.get(i)));
		
		return currencies;
	}

	public ArrayList<ClaimItem> deserializeMyClaimItems(int claimHeaderID){
		return app.gson.fromJson(prefs.getString("CLAIM"+claimHeaderID, "[]"), app.types.arrayListOfClaimItems);
	}
	
}
