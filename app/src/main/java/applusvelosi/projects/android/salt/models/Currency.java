package applusvelosi.projects.android.salt.models;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONObject;

public class Currency implements Serializable{
	private HashMap<String, Object> map;
	
	public Currency(HashMap<String, Object> map){
		this.map = new HashMap<String, Object>();
		this.map.putAll(map);
	}
	
	public Currency(JSONObject jsonCurrency) throws Exception{
		map = new HashMap<String, Object>();
		map.put("CurrencyID", jsonCurrency.getString("CurrencyID"));
		map.put("CurrencyName", jsonCurrency.getString("CurrencyName"));
		map.put("CurrencySymbol", jsonCurrency.getString("CurrencySymbol"));
	}
	
	public HashMap<String, Object> getMap(){
		return map;
	}
	
	public int getCurrencyID(){
		return Integer.parseInt(map.get("CurrencyID").toString());
	}
	
	public String getCurrencyName(){
		return map.get("CurrencyName").toString();
	}
	
	public String getCurrencySymbol(){
		return map.get("CurrencySymbol").toString();
	}
}
