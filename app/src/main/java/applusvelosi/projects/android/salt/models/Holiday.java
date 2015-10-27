package applusvelosi.projects.android.salt.models;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;

public class Holiday {
//	private Bitmap flag;
	private String country, name, dateStr;
	private Date date;
	private ArrayList<String> officeNames;
	private ArrayList<Integer> officeIDs;
	
	public Holiday(String name, String country, String dateStr, Date date){
		officeNames = new ArrayList<String>();
		officeIDs = new ArrayList<Integer>();
		this.name = name;
		this.country = country;
		this.date = date;
		this.dateStr = dateStr;
	}
	
//	public Holiday setFlag(Bitmap flag){
//		this.flag = flag;
//		return this;
//	}
	
	public Holiday addOffice(int officeID, String officeName){
		officeIDs.add(officeID);
		officeNames.add(officeName);
		return this;
	}
	
//	public Bitmap getBitmap(){
//		return flag;
//	}
	
	public String getCountry(){
		return country;
	}
	
	public String getName(){
		return name;
	}
	
	public String getStringedDate(){
		return dateStr;
	}
		
	public Date getDate(){
		return date;
	}

	public ArrayList<String> getOfficeNames(){
		return officeNames;
	}
	
	public ArrayList<Integer> getOfficeIDs(){
		return officeIDs;
	}
}
