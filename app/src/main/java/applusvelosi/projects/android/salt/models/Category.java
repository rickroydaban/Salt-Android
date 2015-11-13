package applusvelosi.projects.android.salt.models;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONObject;

public class Category implements Serializable{
	public static final int TYPE_MILEAGE = 1;
	public static final int TYPE_BUSINESSADVANCE = 4;
	public static final int TYPE_ASSET = 7;
	
	private HashMap<String, Object> map;
	
	public Category(JSONObject jsonCategory) throws Exception{
		map = new HashMap<String, Object>();
		map.put("Attendee", jsonCategory.getString("Attendee"));
		map.put("CategoryTypeID", jsonCategory.getString("CategoryTypeID"));
		map.put("SpendLimit", jsonCategory.getString("SpendLimit"));
		map.put("Description", jsonCategory.getString("Description"));
		map.put("Currency", jsonCategory.getString("Currency"));
		map.put("CurrencyAbb", jsonCategory.getString("CurrencyAbb"));
	}
	
	public Category(HashMap<String, Object> map){
		this.map = new HashMap<String, Object>();
		this.map.putAll(map);
	}
	
	public int getAttendeeTypeID(){
		return Integer.parseInt(map.get("Attendee").toString());
	}
	
	public int getCategoryTypeID(){
		return Integer.parseInt(map.get("CategoryTypeID").toString());
	}
	
	public float getSpendLimit(){
		return Float.parseFloat(map.get("SpendLimit").toString());
	}
	
	public String getName(){
		return map.get("Description").toString();
	}
	
	public int getCurrencyID(){
		return Integer.parseInt(map.get("Currency").toString());
	}
	
	public String getCurrencyName(){
		return map.get("CurrencyAbb").toString();
	}
}
