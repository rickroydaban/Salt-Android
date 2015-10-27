package applusvelosi.projects.android.salt.utils.enums;

public enum Months {

	JANUARY("January"),
	FEBRUARY("February"),
	MARCH("March"),
	APRIL("April"),
	MAY("May"),
	JUNE("June"),
	JULY("July"),
	AUGUST("August"),
	SEPTEMBER("September"),
	OCTOBER("October"),
	NOVEMBER("November"),
	DECEMBER("December");
	
	private String name;
	
	private Months(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
