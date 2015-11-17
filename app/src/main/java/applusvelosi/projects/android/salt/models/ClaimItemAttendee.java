package applusvelosi.projects.android.salt.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

public class ClaimItemAttendee {
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

}
