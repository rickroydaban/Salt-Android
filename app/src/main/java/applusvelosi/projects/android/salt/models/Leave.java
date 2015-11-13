package applusvelosi.projects.android.salt.models;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.net.wifi.WifiEnterpriseConfig.Eap;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

public class Leave {
	public enum Duration { ONEDAY, AM, PM};

	private static String LEAVETYPEBIRTHDAY = "1-Birthday Leave-Birthday";
	private static String LEAVETYPEVACATION = "2-Vacation/Holiday-VacationOrHoliday";
	private static String LEAVETYPESICK = "3-Sick Leave-Sick";
	private static String LEAVETYPEUNPAID = "4-Unpaid Leave-Unpaid";
	private static String LEAVETYPEBEREAVEMENT = "5-Bereavement-Bereavement";
	private static String LEAVETYPEMATERNITY = "6-Maternity/Pat-MaternityOrPaternity";
	private static String LEAVETYPEDOCTORDENTIST = "7-Doctor/Dentist-DoctorOrDentist";
	private static String LEAVETYPEHOSPITALIZATION = "8-Hospitalization-Hospitalization";
	private static String LEAVETYPEBUSINESSTRIP = "9-Business Trip-BusinessTrip";
	
	private static String LEAVESTATUSPENDING = "18-Submitted-Submitted";
	private static String LEAVESTATUSCANCELLED = "19-Cancelled-Cancelled";
	private static String LEAVESTATUSAPPROVED = "20-Approved-Approved";
	private static String LEAVESTATUSREJECTED = "21-Rejected-Rejected";
//	private static String LEAVESTATUSCANCEL = "22-For Cancellation-Cancel";
	
	public static int LEAVETYPEBIRTHDAYKEY;
	public static int LEAVETYPEVACATIONKEY;
	public static int LEAVETYPESICKKEY;
	public static int LEAVETYPEUNPAIDKEY;
	public static int LEAVETYPEBEREAVEMENTKEY;
	public static int LEAVETYPEMATERNITYKEY;
	public static int LEAVETYPEDOCTORKEY;
	public static int LEAVETYPEHOSPITALIZATIONKEY;
	public static int LEAVETYPEBUSINESSTRIPKEY;
	public static int LEAVESTATUSPENDINGID, LEAVESTATUSCANCELLEDKEY, LEAVESTATUSAPPROVEDKEY, LEAVESTATUSREJECTEDKEY;

	public static String LEAVETYPEBIRTHDAYDESC, LEAVETYPEVACATIONDESC, LEAVETYPESICKDESC, LEAVETYPEUNPAIDDESC, LEAVETYPEBEREAVEMENTDESC, LEAVETYPEMATERNITYDESC, LEAVETYPEDOCTORDESC, LEAVETYPEHOSPITALIZATIONDESC, LEAVETYPEBUSINESSTRIPDESC;
	public static String LEAVESTATUSPENDINGDESC, LEAVESTATUSCANCELLEDDESC, LEAVESTATUSAPPROVEDDESC, LEAVESTATUSREJECTEDDESC;
	
	private static ArrayList<String> typeDescriptionList, statusDescriptionList;
	
	static{		
		LEAVETYPEBIRTHDAYKEY = Integer.parseInt(LEAVETYPEBIRTHDAY.split("-")[0]);
		LEAVETYPEVACATIONKEY = Integer.parseInt(LEAVETYPEVACATION.split("-")[0]);
		LEAVETYPESICKKEY = Integer.parseInt(LEAVETYPESICK.split("-")[0]);
		LEAVETYPEUNPAIDKEY = Integer.parseInt(LEAVETYPEUNPAID.split("-")[0]);
		LEAVETYPEBEREAVEMENTKEY = Integer.parseInt(LEAVETYPEBEREAVEMENT.split("-")[0]);
		LEAVETYPEMATERNITYKEY = Integer.parseInt(LEAVETYPEMATERNITY.split("-")[0]);
		LEAVETYPEDOCTORKEY = Integer.parseInt(LEAVETYPEDOCTORDENTIST.split("-")[0]);
		LEAVETYPEHOSPITALIZATIONKEY = Integer.parseInt(LEAVETYPEHOSPITALIZATION.split("-")[0]);
		LEAVETYPEBUSINESSTRIPKEY = Integer.parseInt(LEAVETYPEBUSINESSTRIP.split("-")[0]);

		LEAVETYPEBIRTHDAYDESC = LEAVETYPEBIRTHDAY.split("-")[1];
		LEAVETYPEVACATIONDESC = LEAVETYPEVACATION.split("-")[1];
		LEAVETYPESICKDESC = LEAVETYPESICK.split("-")[1];
		LEAVETYPEUNPAIDDESC = LEAVETYPEUNPAID.split("-")[1];
		LEAVETYPEBEREAVEMENTDESC = LEAVETYPEBEREAVEMENT.split("-")[1];
		LEAVETYPEMATERNITYDESC = LEAVETYPEMATERNITY.split("-")[1];
		LEAVETYPEDOCTORDESC = LEAVETYPEDOCTORDENTIST.split("-")[1];
		LEAVETYPEHOSPITALIZATIONDESC = LEAVETYPEHOSPITALIZATION.split("-")[1];
		LEAVETYPEBUSINESSTRIPDESC = LEAVETYPEBUSINESSTRIP.split("-")[1];
		
		LEAVESTATUSPENDINGID = Integer.parseInt(LEAVESTATUSPENDING.split("-")[0]);
		LEAVESTATUSCANCELLEDKEY = Integer.parseInt(LEAVESTATUSCANCELLED.split("-")[0]);
		LEAVESTATUSAPPROVEDKEY = Integer.parseInt(LEAVESTATUSAPPROVED.split("-")[0]);
		LEAVESTATUSREJECTEDKEY = Integer.parseInt(LEAVESTATUSREJECTED.split("-")[0]);
//		LEAVESTATUSCANCELKEY = Integer.parseInt(LEAVESTATUSCANCEL.split("-")[0]);

		LEAVESTATUSPENDINGDESC = LEAVESTATUSPENDING.split("-")[1];
		LEAVESTATUSCANCELLEDDESC = LEAVESTATUSCANCELLED.split("-")[1];
		LEAVESTATUSAPPROVEDDESC = LEAVESTATUSAPPROVED.split("-")[1];
		LEAVESTATUSREJECTEDDESC = LEAVESTATUSREJECTED.split("-")[1];
//		LEAVESTATUSCANCELDESC = LEAVESTATUSCANCEL.split("-")[1];
		
		typeDescriptionList = new ArrayList<String>();
		typeDescriptionList.add(LEAVETYPEBIRTHDAYDESC);
		typeDescriptionList.add(LEAVETYPEVACATIONDESC);
		typeDescriptionList.add(LEAVETYPESICKDESC);
		typeDescriptionList.add(LEAVETYPEUNPAIDDESC);
		typeDescriptionList.add(LEAVETYPEBEREAVEMENTDESC);
		typeDescriptionList.add(LEAVETYPEMATERNITYDESC);
		typeDescriptionList.add(LEAVETYPEDOCTORDESC);
		typeDescriptionList.add(LEAVETYPEHOSPITALIZATIONDESC);
		typeDescriptionList.add(LEAVETYPEBUSINESSTRIPDESC);
		
		statusDescriptionList = new ArrayList<String>();
		statusDescriptionList.add(LEAVESTATUSPENDINGDESC);
		statusDescriptionList.add(LEAVESTATUSCANCELLEDDESC);
		statusDescriptionList.add(LEAVESTATUSAPPROVEDDESC);
		statusDescriptionList.add(LEAVESTATUSREJECTEDDESC);		
//		statusDescriptionList.add(LEAVESTATUSCANCELDESC);
	}
	
	public static String getLeaveTypeDescForKey(int key){
		if(key == LEAVETYPEBIRTHDAYKEY)
			return LEAVETYPEBIRTHDAYDESC;
		else if(key == LEAVETYPEVACATIONKEY)
			return LEAVETYPEVACATIONDESC;
		else if(key == LEAVETYPESICKKEY)
			return LEAVETYPESICKDESC;
		else if(key == LEAVETYPEUNPAIDKEY)
			return LEAVETYPEUNPAIDDESC;
		else if(key == LEAVETYPEBEREAVEMENTKEY)
			return LEAVETYPEBEREAVEMENTDESC;
		else if(key == LEAVETYPEMATERNITYKEY)
			return LEAVETYPEMATERNITYDESC;
		else if(key == LEAVETYPEDOCTORKEY)
			return LEAVETYPEDOCTORDESC;
		else if(key == LEAVETYPEHOSPITALIZATIONKEY)
			return LEAVETYPEHOSPITALIZATIONDESC;
		else
			return LEAVETYPEBUSINESSTRIPDESC;		
	}
	
	public static int getLeaveTypeIDForDesc(String desc){
		if(desc.equals(LEAVETYPEBIRTHDAYDESC))
			return LEAVETYPEBIRTHDAYKEY;
		else if(desc.equals(LEAVETYPEVACATIONDESC))
			return LEAVETYPEVACATIONKEY;
		else if(desc.equals(LEAVETYPESICKDESC))
			return LEAVETYPESICKKEY;
		else if(desc.equals(LEAVETYPEUNPAIDDESC))
			return LEAVETYPEUNPAIDKEY;
		else if(desc.equals(LEAVETYPEBEREAVEMENTDESC))
			return LEAVETYPEBEREAVEMENTKEY;
		else if(desc.equals(LEAVETYPEMATERNITYDESC))
			return LEAVETYPEMATERNITYKEY;
		else if(desc.equals(LEAVETYPEDOCTORDESC))
			return LEAVETYPEDOCTORKEY;
		else if(desc.equals(LEAVETYPEHOSPITALIZATIONDESC))
			return LEAVETYPEHOSPITALIZATIONKEY;
		else
			return LEAVETYPEBUSINESSTRIPKEY;
	}
	
	public static String getLeaveStatusDescForKey(int key){
		if(key == LEAVESTATUSPENDINGID)
			return LEAVESTATUSPENDINGDESC;
		else if(key == LEAVESTATUSCANCELLEDKEY)
			return LEAVESTATUSCANCELLEDDESC;
		else if(key == LEAVESTATUSAPPROVEDKEY)
			return LEAVESTATUSAPPROVEDDESC;
//		else if(key == LEAVESTATUSREJECTEDKEY)
		else
			return LEAVESTATUSREJECTEDDESC;
//		else
//			return LEAVESTATUSCANCELDESC;
	}
	
	public static int getLeaveStatusKeyForDesc(String desc){
		if(desc.equals(LEAVESTATUSPENDINGDESC))
			return LEAVESTATUSPENDINGID;
		else if(desc.equals(LEAVESTATUSCANCELLEDDESC))
			return LEAVESTATUSCANCELLEDKEY;
		else if(desc.equals(LEAVESTATUSAPPROVEDDESC))
			return LEAVESTATUSAPPROVEDKEY;
//		else if(desc.equals(LEAVESTATUSREJECTEDDESC))
		else
			return LEAVESTATUSREJECTEDKEY;
//		else
//			return LEAVESTATUSCANCELKEY;
	}
	
	private HashMap<String, Object> mapLeave;
	private int posOnAppLeaves;
	
	//new leave request
	public Leave(Staff staff, float staffRemVL, float staffRemSL, int typeID, int statusID, String dateFrom, String dateTo, float days,  float workingDays, String notes, String dateSubmitted){
		mapLeave = new HashMap<String, Object>();
		mapLeave.put("LeaveID", 0);
		mapLeave.put("StaffID", staff.getStaffID());
		mapLeave.put("StaffName", staff.getFname()+" "+staff.getLname());
		mapLeave.put("Email", staff.getEmail());
		mapLeave.put("LeaveApprover1ID", staff.getApprover1ID());
		mapLeave.put("LeaveApprover1Name", staff.getApprover1Name());
		mapLeave.put("LeaveApprover1Email", staff.getApprover1Email());
		mapLeave.put("LeaveApprover2ID", staff.getApprover2ID());
		mapLeave.put("LeaveApprover2Name", staff.getApprover2Name());
		mapLeave.put("LeaveApprover2Email", staff.getApprover2Email());
		mapLeave.put("LeaveApprover3ID", staff.getApprover3ID());
		mapLeave.put("LeaveApprover3Name", staff.getApprover3Name());
		mapLeave.put("LeaveApprover3Email", staff.getApprover3Email());
		mapLeave.put("VacationAllowance", staffRemVL);
		mapLeave.put("SickAllowance", staffRemSL);
		mapLeave.put("LeaveTypeID", typeID);
		mapLeave.put("Description", Leave.getLeaveTypeDescForKey(typeID));
		mapLeave.put("DateFrom", dateFrom);
		mapLeave.put("DateTo", dateTo);
		mapLeave.put("Days", days);
		mapLeave.put("WorkingDays", workingDays);
		mapLeave.put("Notes", notes);
		mapLeave.put("DateSubmitted", dateSubmitted);
		mapLeave.put("LeaveStatus", statusID);
	}
	
	//edit leave
	public void editLeave(	int typeID, float remVL, float remSL, String dateFrom, String dateTo, float days, float workingDays, String notes, String dateSubmitted)  throws Exception{
		mapLeave.put("LeaveTypeID", typeID);
		mapLeave.put("Description", getLeaveTypeDescForKey(typeID));
		mapLeave.put("SickAllowance", remSL);
		mapLeave.put("VacationAllowance", remVL);
		mapLeave.put("LeaveStatus", LEAVESTATUSPENDINGID); //only pending leaves can be edited
		mapLeave.put("LeaveStatusDesc", LEAVESTATUSPENDINGDESC);
		mapLeave.put("DateFrom", dateFrom);
		mapLeave.put("DateTo", dateTo);
		mapLeave.put("Days", days);
		mapLeave.put("WorkingDays", workingDays);
		mapLeave.put("Notes", notes);
		mapLeave.put("DateSubmitted", dateSubmitted);
	}
	
	public String getJSONStringForEditingLeave() throws Exception{
		HashMap<String, Object> mapLeaveCopy = new HashMap<String, Object>();
//		mapLeaveCopy.putAll(mapLeave);
		mapLeaveCopy.put("LeaveID", Integer.parseInt(mapLeave.get("LeaveID").toString()));
		mapLeaveCopy.put("StaffID", Integer.parseInt(mapLeave.get("StaffID").toString()));
		mapLeaveCopy.put("StaffName", mapLeave.get("StaffName").toString());
		mapLeaveCopy.put("LeaveTypeID", Integer.parseInt(mapLeave.get("LeaveTypeID").toString()));
		mapLeaveCopy.put("Description", mapLeave.get("Description").toString());
		mapLeaveCopy.put("SickAllowance", Float.parseFloat(mapLeave.get("SickAllowance").toString()));
		mapLeaveCopy.put("VacationAllowance", Float.parseFloat(mapLeave.get("VacationAllowance").toString()));
		mapLeaveCopy.put("LeaveStatus", Integer.parseInt(mapLeave.get("LeaveStatus").toString()));
		mapLeaveCopy.put("DateFrom", mapLeave.get("DateFrom").toString());
		mapLeaveCopy.put("DateTo", mapLeave.get("DateTo").toString());
		mapLeaveCopy.put("Days", Float.parseFloat(mapLeave.get("Days").toString()));
		mapLeaveCopy.put("WorkingDays", Float.parseFloat(mapLeave.get("WorkingDays").toString()));
		mapLeaveCopy.put("Notes", mapLeave.get("Notes").toString());
		mapLeaveCopy.put("DateSubmitted", mapLeave.get("DateSubmitted").toString());
		mapLeaveCopy.put("LeaveApprover1ID", Integer.parseInt(mapLeave.get("LeaveApprover1ID").toString()));
		mapLeaveCopy.put("LeaveApprover1Email", mapLeave.get("LeaveApprover1Email").toString());
		mapLeaveCopy.put("LeaveApprover1Name", mapLeave.get("LeaveApprover1Name").toString());
		mapLeaveCopy.put("LeaveApprover2ID", Integer.parseInt(mapLeave.get("LeaveApprover2ID").toString()));
		mapLeaveCopy.put("LeaveApprover2Email", mapLeave.get("LeaveApprover2Email").toString());
		mapLeaveCopy.put("LeaveApprover2Name", mapLeave.get("LeaveApprover2Name").toString());
		mapLeaveCopy.put("LeaveApprover3ID", Integer.parseInt(mapLeave.get("LeaveApprover3ID").toString()));
		mapLeaveCopy.put("LeaveApprover3Email", mapLeave.get("LeaveApprover3Email").toString());
		mapLeaveCopy.put("LeaveApprover3Name", mapLeave.get("LeaveApprover3Name").toString());

		System.out.println("Edit DateFrom " + mapLeaveCopy.get("DateFrom"));
		System.out.println("Edit DateTo "+mapLeaveCopy.get("DateTo"));
		JSONObject tempJSONObject = new JSONObject(mapLeaveCopy);
		return URLEncoder.encode(tempJSONObject.toString(), "UTF-8");
	}
	
	public String getJSONStringForProcessingLeave() throws Exception{
		HashMap<String, Object> mapLeaveCopy = new HashMap<String, Object>();
		mapLeaveCopy.putAll(mapLeave);
		System.out.println("Edit DateFrom " + mapLeaveCopy.get("DateFrom"));
		System.out.println("Edit DateTo " + mapLeaveCopy.get("DateTo"));
		return URLEncoder.encode(new JSONObject(mapLeaveCopy).toString(), "UTF-8");
 	}
	
	public static String createEmptyJSON() throws Exception{
		HashMap<String, Object> leave = new HashMap<String, Object>();
		leave.put("LeaveID", 0);
		leave.put("StaffID", 0);
		leave.put("LeaveTypeID", 0);
		leave.put("Description", "");
		leave.put("SickAllowance", 0.0);
		leave.put("VacationAllowance", 0.0);
		leave.put("LeaveStatus", LEAVESTATUSPENDINGID);
		leave.put("DateFrom", "01-Jan-1900");
		leave.put("DateTo", "01-Jan-1900");
		leave.put("Days", 0.0);
		leave.put("WorkingDays", 0.0);
		leave.put("Notes", "");
		leave.put("DateSubmitted", "01-Jan-1900");
		leave.put("LeaveApprover1ID", 0);
		leave.put("LeaveApprover1Email", "");
		leave.put("LeaveApprover1Name", "");
		leave.put("LeaveApprover2ID", 0);
		leave.put("LeaveApprover2Email", "");
		leave.put("LeaveApprover2Name", "");
		leave.put("LeaveApprover3ID", 0);
		leave.put("LeaveApprover3Email", "");
		leave.put("LeaveApprover3Name", "");
		
		return URLEncoder.encode(new JSONObject(leave).toString(), "UTF-8");		
	}
	
	//new leave by onlinegateway
	public Leave(JSONObject jsonLeave, OnlineGateway onlineGateway) throws Exception{		
		mapLeave = new HashMap<String, Object>();
		mapLeave.putAll(OnlineGateway.toMap(jsonLeave));
		mapLeave.put("LeaveID", Integer.parseInt(mapLeave.get("LeaveID").toString()));
		mapLeave.put("StaffID", Integer.parseInt(mapLeave.get("StaffID").toString()));
		mapLeave.put("SickAllowance", Float.parseFloat(mapLeave.get("SickAllowance").toString()));
		mapLeave.put("WorkingDays", Float.parseFloat(mapLeave.get("WorkingDays").toString()));
		mapLeave.put("ApproverID", Integer.parseInt(mapLeave.get("ApproverID").toString()));
		mapLeave.put("HROfficerID", Integer.parseInt(mapLeave.get("HROfficerID").toString()));
		mapLeave.put("Days", Float.parseFloat(mapLeave.get("Days").toString()));
		mapLeave.put("LeaveApprover1ID", Integer.parseInt(mapLeave.get("LeaveApprover1ID").toString()));
		mapLeave.put("LeaveStatus", Integer.parseInt(mapLeave.get("LeaveStatus").toString()));
		mapLeave.put("LeaveTypeID", Integer.parseInt(mapLeave.get("LeaveTypeID").toString()));
		mapLeave.put("VacationAllowance", Float.parseFloat(mapLeave.get("VacationAllowance").toString()));
		mapLeave.put("LeaveApprover3ID", Integer.parseInt(mapLeave.get("LeaveApprover3ID").toString()));
		mapLeave.put("OfficeID", Integer.parseInt(mapLeave.get("OfficeID").toString()));
		mapLeave.put("LeaveApprover2ID", Integer.parseInt(mapLeave.get("LeaveApprover2ID").toString()));
		mapLeave.put("Documents", new JSONArray(mapLeave.get("Documents").toString()));
		mapLeave.put("DateFrom", onlineGateway.dJsonizeDate(mapLeave.get("DateFrom").toString()));
		mapLeave.put("DateTo", onlineGateway.dJsonizeDate(mapLeave.get("DateTo").toString()));
		mapLeave.put("DateSubmitted", onlineGateway.dJsonizeDate(mapLeave.get("DateSubmitted").toString()));
		mapLeave.put("DateCancelled", onlineGateway.dJsonizeDate(mapLeave.get("DateCancelled").toString()));
		mapLeave.put("DateRejected", onlineGateway.dJsonizeDate(mapLeave.get("DateRejected").toString()));
		mapLeave.put("DateApproved", onlineGateway.dJsonizeDate(mapLeave.get("DateApproved").toString()));		
	}
	
	//to be used by the leaves for approval fragment to fix the inconsistensies from converting the leave object into gson string
	public void fixFormatForLeavesForApprovalDetailPage(OnlineGateway onlineGateway) throws Exception{
		mapLeave.put("Documents", new JSONObject(mapLeave.get("Documents").toString()).getJSONArray("values"));
	}
	
	public int getLeaveID(){
		return Integer.parseInt(mapLeave.get("LeaveID").toString());
	}
	
	public int getStaffID(){
		return Integer.parseInt(mapLeave.get("StaffID").toString());
	}
	
	public String getStaffName(){
		return mapLeave.get("StaffName").toString();
	}
	
	public String getStaffEmail(){
		return mapLeave.get("Email").toString();
	}
		
	public String getStartDate(){
		return mapLeave.get("DateFrom").toString();
	}
	
	public String getEndDate(){
		return mapLeave.get("DateTo").toString();
	}
	
	public int getYearSubmitted(){
		return Integer.parseInt(mapLeave.get("DateSubmitted").toString().split("-")[2]);
	}
	
	public int getTypeID(){
		return (int)Float.parseFloat(mapLeave.get("LeaveTypeID").toString());
	}
	
	public String getTypeDescription(){
		return getLeaveTypeDescForKey(getTypeID());
	}
	
	public static ArrayList<String> getTypeDescriptionList(){
		return typeDescriptionList;
	}
	
	public int getStatusID(){
		return (int)Float.parseFloat(mapLeave.get("LeaveStatus").toString());
	}
	
	public String getStatusDescription(){
		return getLeaveStatusDescForKey(getStatusID());
	}
	
	public static ArrayList<String> getStatusDescriptionList(){
		return statusDescriptionList;
	}
	
	public float getDays(){
		return Float.parseFloat(mapLeave.get("Days").toString());
	}
		
	public float getWorkingDays(){
		return Float.parseFloat(mapLeave.get("WorkingDays").toString());
	}
	
	public String getNotes(){
		return mapLeave.get("Notes").toString();
	}
	
	public int getYear(){ //for filters
		return Integer.parseInt(mapLeave.get("DateFrom").toString().split("-")[2]);
	}
	
	public float getRemainingVL(){
		return Float.parseFloat(mapLeave.get("VacationAllowance").toString());
	}
	
	public float getRemainingSL(){
		return Float.parseFloat(mapLeave.get("SickAllowance").toString());
	}
	
	public int getHRID(){
		return Integer.parseInt(mapLeave.get("HROfficerID").toString());
	}
	
	public String getHREmail(){
		return mapLeave.get("HROfficerEmail").toString();
	}
	
	public String getHRName(){
		return mapLeave.get("HROfficerName").toString();
	}

	public String getAMEmail(){
		return mapLeave.get("AccountManagerEmail").toString();
	}
	
	public String getAMName(){
		return mapLeave.get("AccountManagerName").toString();
	}
	
	public int getLeaveApprover1ID(){
		return (int)Float.parseFloat(mapLeave.get("LeaveApprover1ID").toString());
	}
	
	public String getLeaveApprover1Email(){
		return mapLeave.get("LeaveApprover1Email").toString();
	}
	
	public String getLeaveApprover1Name(){
		return mapLeave.get("LeaveApprover1Name").toString();
	}
	
	public int getLeaveApprover2ID(){
		return (int)Float.parseFloat(mapLeave.get("LeaveApprover2ID").toString());
	}
	
	public String getLeaveApprover2Email(){
		return mapLeave.get("LeaveApprover2Email").toString();
	}
	
	public String getLeaveApproveer2Name(){
		return mapLeave.get("LeaveApprover2Name").toString();
	}
	
	public int getLeaveApprover3ID(){
		return (int)Float.parseFloat(mapLeave.get("LeaveApprover3ID").toString());
	}
	
	public String getLeaveApprover3Email(){
		return mapLeave.get("LeaveApproverEmail").toString();
	}
	
	public String getLeaveApprover3Name(){
		return mapLeave.get("LeaveApprover3Name").toString();
	}
	public boolean isApprover(int staffID){
		return (getLeaveApprover1ID()==staffID || getLeaveApprover2ID()==staffID || getLeaveApprover3ID()==staffID)?true:false;
	}
	
	public int getOfficeID(){
		return Integer.parseInt(mapLeave.get("OfficeID").toString());
	}
	
	public String getApproverName(){
		return mapLeave.get("ApproverName").toString();
	}
	
	public void reject(String notes, Staff staff, String now){
		mapLeave.put("ApproverID", staff.getStaffID());
		mapLeave.put("ApproverName", staff.getLname()+", "+staff.getFname());
		mapLeave.put("ApproverEmail", staff.getEmail());
		mapLeave.put("ApproversNotes", notes);
		mapLeave.put("DateRejected", now);
		mapLeave.put("LeaveStatus", LEAVESTATUSREJECTEDKEY);
		mapLeave.put("LeaveStatusDesc", LEAVESTATUSREJECTEDDESC);
	}

	public void approve(Staff staff, String now){
		mapLeave.put("ApproverID", staff.getStaffID());
		mapLeave.put("ApproverName", staff.getLname()+", "+staff.getFname());
		mapLeave.put("ApproverEmail", staff.getEmail());
		mapLeave.put("DateApproved", now);
		mapLeave.put("LeaveStatus", LEAVESTATUSAPPROVEDKEY);
		mapLeave.put("LeaveStatusDesc", LEAVESTATUSAPPROVEDDESC);
		mapLeave.put("ApproversNotes", "Approved");
	}
	
	public void cancel(Staff staff, String now){
		mapLeave.put("ApproverID", staff.getStaffID());
		mapLeave.put("ApproverName", staff.getLname()+", "+staff.getFname());
		mapLeave.put("ApproverEmail", staff.getEmail());
		mapLeave.put("DateCancelled", now);
		mapLeave.put("LeaveStatus", LEAVESTATUSCANCELLEDKEY);
		mapLeave.put("LeaveStatusDesc", LEAVESTATUSCANCELLEDDESC);
		mapLeave.put("ApproversNotes", "Cancelled");
	}
	
	public void setPosOnAppLeaves(int pos){
		posOnAppLeaves = pos;
	}
	
	public int getPosOnAppLeaves(){
		return posOnAppLeaves;
	}
}
