package applusvelosi.projects.android.salt.models;

public class ManagerCalendarItem {
	private String name;
	private String office;
	
	
	public ManagerCalendarItem(String name, String office){
		this.name = name;
		this.office = office;
	}
	
	public String getName(){
		return name;
	}
	
	public String getOffice(){
		return office;
	}
}
