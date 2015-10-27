package applusvelosi.projects.android.salt.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

public class ClaimItemAttendee {
	private LinkedHashMap<String, Object> map;
	
	public ClaimItemAttendee(JSONObject jsonAttendee) throws Exception{ //getting from online gateway
		map = new LinkedHashMap<String, Object>();
		map.put("FullName", jsonAttendee.getString("FullName"));
		map.put("JobTitle", jsonAttendee.getString("JobTitle"));
		map.put("Notes", jsonAttendee.getString("Notes"));
	}
	
	public ClaimItemAttendee(String fullName, String job, String notes){
		map = new LinkedHashMap<String, Object>();
		map.put("FullName", fullName);
		map.put("JobTitle", job);
		map.put("Notes", notes);
		System.out.println("map "+map);
	}

	public ClaimItemAttendee(HashMap<String, Object> map){
		this.map = new LinkedHashMap<String, Object>();
		this.map.putAll(map);
	}
	
	public LinkedHashMap<String, Object> getMap(){
		return map;
	}
	
	public String getName(){
		return map.get("FullName").toString();
	}
	
	public String getJobTitle(){
		return map.get("JobTitle").toString();
	}
	
	public String getNote(){
		return map.get("Notes").toString();
	}
}
