package applusvelosi.projects.android.salt.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.views.LoginActivity;

public class Staff {
	public enum USERPOSITION{
		USERPOSITION_DEFAULT,
		USERPOSITION_CM,
		USERPOSITION_RM,
		USERPOSITION_CFO,
		USERPOSITION_CEO
	}

	public static final int SECURITYLEVEL_USER = 1;
	public static final int SECURITYLEVEL_MANAGER = 2;
	public static final int SECURITYLEVEL_ACCOUNT = 3;
	public static final int SECURITYLEVEL_ADMIN = 4;
	public static final int SECURITYLEVEL_COUNTRYMANAGER = 5;
	public static final int SECURITYLEVEL_ACCOUNTMANAGER = 6;
	
	public static final String GENDER_MALE = "Male";
	public static final String GENDER_FEMALE = "Female";
	
	private HashMap<String, Object> map;
	
	public Staff(OnlineGateway key, JSONObject jsonStaff) throws Exception{
		map = new HashMap<String, Object>();
		//staff specific related
		map.put("Active", jsonStaff.getString("Active"));
		map.put("StaffID", jsonStaff.getString("StaffID"));
		map.put("SecurityLevel", jsonStaff.getString("SecurityLevel"));
		map.put("LastName", jsonStaff.getString("LastName"));
		map.put("FirstName", jsonStaff.getString("FirstName"));
		map.put("OfficeID", jsonStaff.getString("OfficeID"));
		map.put("OfficeName", jsonStaff.getString("OfficeName"));
		map.put("Email", jsonStaff.getString("Email"));
		map.put("JobTitle", jsonStaff.getString("JobTitle"));
		map.put("GenderSex", jsonStaff.getString("GenderSex"));
		map.put("MaxConsecutiveDays", jsonStaff.get("MaxConsecutiveDays"));
		//leave specific related
		map.put("TotalVacationLeave", jsonStaff.getString("TotalVacationLeave"));
		map.put("SickLeaveAllowance", jsonStaff.getString("SickLeaveAllowance"));
		map.put("LeaveApprover1", jsonStaff.getString("LeaveApprover1"));
		map.put("LeaveApprover1Name", jsonStaff.getString("LeaveApprover1Name"));
		map.put("LeaveApprover1Email", jsonStaff.getString("LeaveApprover1Email"));
		map.put("LeaveApprover2", jsonStaff.getInt("LeaveApprover2"));
		map.put("LeaveApprover2Name", jsonStaff.getString("LeaveApprover2Name"));
		map.put("LeaveAprrover2Email", jsonStaff.getString("LeaveApprover2Email"));
		map.put("LeaveApprover3", jsonStaff.getInt("LeaveApprover3"));
		map.put("LeaveApprover3Name", jsonStaff.getString("LeaveApprover3Name"));
		map.put("LeaveApprover3Email", jsonStaff.getString("LeaveApprover3Email"));
		map.put("Monday", jsonStaff.getString("Monday"));
		map.put("Tuesday", jsonStaff.getString("Tuesday"));
		map.put("Wednesday", jsonStaff.getString("Wednesday"));
		map.put("Thursday", jsonStaff.getString("Thursday"));
		map.put("Friday", jsonStaff.getString("Friday"));
		map.put("Saturday", jsonStaff.getString("Saturday"));
		map.put("Sunday", jsonStaff.getString("Sunday"));
		map.put("IsApprover", jsonStaff.getString("IsApprover"));
		map.put("IsRegional", jsonStaff.getString("IsRegional"));
		map.put("IsCorporateApprover", jsonStaff.getString("IsCorporateApprover"));
		//claim specific related
		map.put("CostCenterID", jsonStaff.getString("CostCenterID"));
		map.put("CostCenterName", jsonStaff.getString("CostCenterName"));
		map.put("ExpApprover", jsonStaff.getString("ExpApprover"));
		map.put("ExpenseApproverName", jsonStaff.getString("ExpenseApproverName"));
		map.put("AccountsPerson", jsonStaff.getString("AccountsPerson"));
	}
	
	public Staff(LoginActivity key, String staffID, String securityLevel, String staffOfficeID){ //initial temporary staff data only to be called bY the loginactivitY class
		map = new HashMap<String, Object>();
		map.put("StaffID", staffID);
		map.put("SecurityLevel", securityLevel);
		map.put("OfficeID", staffOfficeID);
	}
		
	public Staff(HashMap<String, Object> staffMap){
		this.map = staffMap;
	}
	
	public boolean isActive(){
		return Boolean.parseBoolean(map.get("Active").toString());
	}
	
	public int getStaffID(){
		return Integer.parseInt(map.get("StaffID").toString());
	}
	
	public int getCostCenterID(){
		return Integer.parseInt(map.get("CostCenterID").toString());
	}
	
	public int getExpenseApproverID(){
		return Integer.parseInt(map.get("ExpApprover").toString());
	}
	
	public int getAccountID(){
		return Integer.parseInt(map.get("AccountsPerson").toString());
	}
	
	//TODO
	public String getAccountName(){
		return "";
	}
	
	public String getAccountEmail(){
		return "";
	}
	
	public int getOfficeID(){
		return Integer.parseInt(map.get("OfficeID").toString());
	}
	
	public String getOfficeName(){
		return toValidString(map.get("OfficeName"));
	}
	
	public boolean isUser(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_USER)?true:false;
	}

	public boolean isManager(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_MANAGER)?true:false;
	}

	public boolean isAccount(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_ACCOUNT)?true:false;
	}

	public boolean isAdmin(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_ADMIN)?true:false;
	}

	public boolean isCM(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_COUNTRYMANAGER)?true:false;
	}

	public boolean isAM(){
		return (Integer.parseInt(map.get("SecurityLevel").toString()) == SECURITYLEVEL_ACCOUNTMANAGER)?true:false;
	}
	
	public int getSecurityLevel(){
		return Integer.parseInt(map.get("SecurityLevel").toString());
	}
	
	
	public String getFname(){
		return toValidString(map.get("FirstName"));
	}
	
	public String getLname(){
		return toValidString(map.get("LastName"));
	}
	
	public String getEmail(){
		return toValidString(map.get("Email"));
	}
	
	public String getJobTitle(){
		return toValidString(map.get("JobTitle"));
	}
	
	public String getGender(){
		return toValidString(map.get("GenderSex"));
	}
	
	public float getMaxVL(){
		return toValidFloat(map.get("TotalVacationLeave"));
	}
	
	public float getMaxSL(){
		return  toValidFloat(map.get("SickLeaveAllowance"));
	}
	
	public float getMaxConsecutiveLeave(){
		return Float.parseFloat(map.get("MaxConsecutiveDays").toString());
	}

	public boolean isMyLeaveApprover(int staffID){
		return (getApprover1ID()==staffID || getApprover2ID()==staffID || getApprover3ID()==staffID);
	}
	
	public int getApprover1ID(){
		return Integer.parseInt(map.get("LeaveApprover1").toString());
	}
	
	public String getApprover1Name(){
		return toValidString(map.get("LeaveApprover1Name"));
	}
	
	public String getApprover1Email(){
		return toValidString(map.get("LeaveApprover1Email"));
	}
	
	public int getApprover2ID(){
		return Integer.parseInt(map.get("LeaveApprover2").toString());
	}
	
	public String getApprover2Name(){
		return toValidString(map.get("LeaveApprover2Name"));
	}
	
	public String getApprover2Email(){
		return toValidString(map.get("LeaveApprover2Email"));
	}
	
	public int getApprover3ID(){
		return Integer.parseInt(map.get("LeaveApprover3").toString());
	}
	
	public String getApprover3Name(){
		return toValidString(map.get("LeaveApprover3Name"));
	}
	
	public String getApprover3Email(){
		return toValidString(map.get("LeaveApprover3Email"));
	}
	
	public boolean hasMonday(){
		return toValidBool(map.get("Monday"));
	}
	
	public boolean hasTuesday(){
		return toValidBool(map.get("Tuesday"));
	}
	
	public boolean hasWednesday(){
		return toValidBool(map.get("Wednesday"));
	}
	
	public boolean hasThursday(){
		return toValidBool(map.get("Thursday"));
	}
	
	public boolean hasFriday(){
		return toValidBool(map.get("Friday"));
	}
	
	public boolean hasSaturday(){
		return toValidBool(map.get("Saturday"));
	}
	
	public boolean hasSunday(){
		return toValidBool(map.get("Sunday"));
	}
	
	public boolean isApprover(){
		return toValidBool(map.get("IsApprover"));
	}
	
	public boolean isRegional(){
		return toValidBool(map.get("IsRegional"));
	}
	
	public boolean isCorporateApprover(){
		return toValidBool(map.get("IsCorporateApprover"));
	}

	public String getCostCenterName(){
		return toValidString(map.get("CostCenterName"));
	}
	
	public String getExpenseApproverName(){
		return toValidString(map.get("ExpenseApproverName"));
	}

	public void setUserPosition(USERPOSITION userPosition){
		System.out.println("userposition set "+userPosition); map.put("UserPosition", userPosition); }

	public USERPOSITION getUserPosition(){ return USERPOSITION.valueOf(map.get("UserPosition").toString()); }

	//TODO
	public String getExpenseApproverEmail(){
		return "";
	}
	
	public HashMap<String, Object> getMap(){
		return map;
	}
	
	public String toValidString(Object field){
		return (field!=null)?field.toString():"";
	}
	
	public float toValidFloat(Object field){
		return (field!=null)?Float.parseFloat(field.toString()):0;
	}

	public boolean toValidBool(Object field){
		return (field!=null)?Boolean.parseBoolean(field.toString()):false;
	}
	
}
