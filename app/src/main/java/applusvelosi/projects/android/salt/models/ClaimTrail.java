package applusvelosi.projects.android.salt.models;

public class ClaimTrail {
	private final String name, date, comment;
		
	public ClaimTrail(String name, String date, String comment){
		this.name = name;
		this.date = date;
		this.comment = comment;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getComment(){
		return comment;
	}

}
