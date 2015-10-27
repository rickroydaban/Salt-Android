package applusvelosi.projects.android.salt.models;

import java.util.ArrayList;
import applusvelosi.projects.android.salt.utils.enums.CalendarItemTypes;

public class CalendarItem {
	private CalendarItemTypes itemType;
	private String label;
	private ArrayList<CalendarEvent> dayEvents;
	
	public CalendarItem(CalendarItemTypes itemType, String label, ArrayList<CalendarEvent> dayEvents){
		this.dayEvents = dayEvents;
		this.label = label;
		this.itemType = itemType;
	}
	
	public CalendarItemTypes getItemType(){
		return itemType;
	}
	
	public String getLabel(){
		return label;
	}
	
	public ArrayList<CalendarEvent> getEvents(){
		return dayEvents;
	}
}
