package applusvelosi.projects.android.salt.models;

public class CalendarEvent {

	public enum CalendarEventDuration{
		AM,
		PM,
		AllDay;
	}
	
	private String name;
	private int resColorID;
	private CalendarEventDuration duration;
	private boolean shouldFill;
	
	public CalendarEvent(String name, int resColorID, CalendarEventDuration duration, boolean shouldFill){
		this.name = name;
		this.resColorID = resColorID;
		this.duration = duration;
		this.shouldFill = shouldFill;
	}
	
	public String getName(){
		return name;
	}
	
	public int getColor(){
		return resColorID;
	}
	
	public CalendarEventDuration getDuration(){
		return duration;
	}
	
	public boolean shouldFill(){
		return shouldFill;
	}
}
