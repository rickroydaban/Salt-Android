package applusvelosi.projects.android.salt.models;

import java.util.ArrayList;

public class CountryHoliday {
//	private Bitmap flag;
	private String country, name, dateStr;
	private ArrayList<String> officeNames;
	private ArrayList<Integer> officeIDs;
	
	public CountryHoliday(String name, String country, String dateStr){
		officeNames = new ArrayList<String>();
		officeIDs = new ArrayList<Integer>();
		this.name = name;
		this.country = country;
		this.dateStr = dateStr;
	}

	public CountryHoliday addOffice(int officeID, String officeName){
		officeIDs.add(officeID);
		officeNames.add(officeName);
		return this;
	}

	public String getCountry(){
		return country;
	}
	
	public String getName(){
		return name;
	}
	
	public String getStringedDate(){ return dateStr; }

	public ArrayList<String> getOfficeNames(){
		return officeNames;
	}
	
	public ArrayList<Integer> getOfficeIDs(){
		return officeIDs;
	}
}
