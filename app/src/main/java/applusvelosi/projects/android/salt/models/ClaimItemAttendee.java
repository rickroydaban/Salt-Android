package applusvelosi.projects.android.salt.models;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

public class ClaimItemAttendee implements Serializable{
	private String name, jobTitle, notes;

	public ClaimItemAttendee(JSONObject jsonAttendee) throws Exception{ //getting from online gateway
		name = jsonAttendee.getString("FullName");
		jobTitle = jsonAttendee.getString("JobTitle");
		notes = jsonAttendee.getString("Notes");
	}
	
	public ClaimItemAttendee(String fullName, String job, String notes){
		this.name = fullName;
		this.jobTitle = job;
		this.notes = notes;
	}

	public String getName(){ return name; }
	
	public String getJobTitle(){ return jobTitle; }
	
	public String getNote(){ return notes; }

	public JsonObject getJSONObject(){
        JsonObject obj = new JsonObject();
		obj.addProperty("FullName", name);
		obj.addProperty("JobTitle", jobTitle);
		obj.addProperty("Notes", notes);

		return obj;
	}
}
