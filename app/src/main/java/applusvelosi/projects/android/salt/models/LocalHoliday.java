package applusvelosi.projects.android.salt.models;

public class LocalHoliday {
	private final String name, date, day, monthName;
	
	public LocalHoliday(String name, String date, String day, String month){
		this.name = name;
		this.date = date;
		this.day = day;
		this.monthName = month;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getDay(){
		return day;
	}
	
	public String getMonth(){
		return monthName;
	}
	
}
