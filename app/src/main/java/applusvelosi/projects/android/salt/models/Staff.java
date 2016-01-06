package applusvelosi.projects.android.salt.models;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.SaltApplication;
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

	private String accountPersonName;
	private int accountPersonID;
	private boolean isActive;
	private String activeDirUsername;
	private float approverLimit, carryOverLeave;
	private int costCenterID;
	private String costCenterName, dateCreated, dateModified, dateOfBirth;
	private int departmentID;
	private String departmentName, email, employmentEndDate, employmentStartDate;
	private int expApprover;
	private String expenseApproverName, firstName;
	private boolean hasFriday;
	private String genderSex;
	private boolean isADAuth, isAllowProxyubmission, isApprover, isCorporateApprover, isRegional;
	private String jobTitle, lastName;
	private int leaveApprover1ID;
	private String leaveApprover1Email, leaveApprover1Name;
	private int leaveApprover2ID;
	private String leaveApprover2Email, leaveApprover2Name;
	private int leaveApprover3ID;
	private String leaveApprover3Email, leaveApprover3Name, location, lockDate;
	private int maxConsecutiveDays;
	private String mobile;
	private boolean hasMonday;
	private String notes, officeAddress;
	private int officeID;
	private String officeName, password, payrollID, phone, phoneExt;
	private int prefCurrency;
	private String prefCurrencyName;
	private int proxyStaffID;
	private String proxyStaffName;
	private boolean hasSaturday;
	private int securityLevel;
	private String securityLevelName;
	private float sickLeaveAllowance;
	private int staffID;
	private String staffNumber, status;
	private int statusID;
	private boolean hasSunday, hasThursday;
	private String title;
	private float totalVacationLeave;
	private boolean hasTuesday;
	private float vacationLeaveAllowance;
	private boolean hasWednesday;
	private int workingDays;

	private USERPOSITION userPosition;

	public Staff(OnlineGateway key, JSONObject jsonStaff) throws Exception{
		accountPersonName = jsonStaff.getString("AccountPersonName");
		accountPersonID = jsonStaff.getInt("AccountsPerson");
		isActive = jsonStaff.getBoolean("Active");
		activeDirUsername = jsonStaff.getString("ActiveDirectoryUsername");
		approverLimit = (float)jsonStaff.getDouble("ApproverLimit");
		carryOverLeave = (float)jsonStaff.getDouble("CarryOverLeave");
		costCenterID = jsonStaff.getInt("CostCenterID");
		costCenterName = jsonStaff.getString("CostCenterName");
		dateCreated = jsonStaff.getString("DateCreated");
		dateModified = jsonStaff.getString("DateModified");
		dateOfBirth = jsonStaff.getString("DateOfBirth");
		departmentID = jsonStaff.getInt("DepartmentID");
		departmentName = jsonStaff.getString("DepartmentName");
		email = jsonStaff.getString("Email");
		employmentEndDate = jsonStaff.getString("EmploymentEndDate");
		employmentStartDate = jsonStaff.getString("EmploymentStartDate");
		expApprover = jsonStaff.getInt("ExpApprover");
		expenseApproverName = jsonStaff.getString("ExpenseApproverName");
		firstName = jsonStaff.getString("FirstName");
		hasFriday = jsonStaff.getBoolean("Friday");
		genderSex = jsonStaff.getString("GenderSex");
		isADAuth = jsonStaff.getBoolean("IsADAuth");
		isAllowProxyubmission = jsonStaff.getBoolean("IsAllowProxySubmission");
		isApprover = jsonStaff.getBoolean("IsApprover");
		isCorporateApprover = jsonStaff.getBoolean("IsCorporateApprover");
		isRegional = jsonStaff.getBoolean("IsRegional");
		jobTitle = jsonStaff.getString("JobTitle");
		lastName = jsonStaff.getString("LastName");
		leaveApprover1ID = jsonStaff.getInt("LeaveApprover1");
		leaveApprover1Email = jsonStaff.getString("LeaveApprover1Email");
		leaveApprover1Name = jsonStaff.getString("LeaveApprover1Name");
		leaveApprover2ID = jsonStaff.getInt("LeaveApprover2");
		leaveApprover2Email = jsonStaff.getString("LeaveApprover2Email");
		leaveApprover2Name = jsonStaff.getString("LeaveApprover2Name");
		leaveApprover3ID = jsonStaff.getInt("LeaveApprover3");
		leaveApprover3Email = jsonStaff.getString("LeaveApprover3Email");
		leaveApprover3Name = jsonStaff.getString("LeaveApprover3Name");
		location = jsonStaff.getString("Location");
		lockDate = jsonStaff.getString("LockDate");
		maxConsecutiveDays = jsonStaff.getInt("MaxConsecutiveDays");
		mobile = jsonStaff.getString("Mobile");
		hasMonday = jsonStaff.getBoolean("Monday");
		notes = jsonStaff.getString("Notes");
		officeAddress = jsonStaff.getString("OfficeAddress");
		officeID = jsonStaff.getInt("OfficeID");
		officeName = jsonStaff.getString("OfficeName");
		password = jsonStaff.getString("Password");
		payrollID = jsonStaff.getString("PayrollID");
		phone = jsonStaff.getString("Phone");
		phoneExt = jsonStaff.getString("PhoneExt");
		prefCurrency = jsonStaff.getInt("PrefCurrency");
		prefCurrencyName = jsonStaff.getString("PrefCurrencyName");
		proxyStaffID = jsonStaff.getInt("ProxyStaffID");
		proxyStaffName = jsonStaff.getString("ProxyStaffName");
		hasSaturday = jsonStaff.getBoolean("Saturday");
		securityLevel = jsonStaff.getInt("SecurityLevel");
		securityLevelName = jsonStaff.getString("SecurityLevelName");
		sickLeaveAllowance = (float)jsonStaff.getDouble("SickLeaveAllowance");
		staffID = jsonStaff.getInt("StaffID");
		staffNumber = jsonStaff.getString("StaffNumber");
		status = jsonStaff.getString("Status");
		statusID = jsonStaff.getInt("StatusID");
		hasSunday = jsonStaff.getBoolean("Sunday");
		hasThursday = jsonStaff.getBoolean("Thursday");
		title = jsonStaff.getString("Title");
		totalVacationLeave = (float)jsonStaff.getDouble("TotalVacationLeave");
		hasTuesday = jsonStaff.getBoolean("Tuesday");
		vacationLeaveAllowance = (float)jsonStaff.getDouble("VacationLeaveAllowance");
		hasWednesday = jsonStaff.getBoolean("Wednesday");
		workingDays = jsonStaff.getInt("WorkingDays");
	}
	
	public Staff(LoginActivity key, int staffID, int securityLevel, int staffOfficeID){ //initial temporary staff data only to be called bY the loginactivitY class
		this.staffID = staffID;
		this.securityLevel = securityLevel;
		this.officeID = staffOfficeID;
	}

    public String getAccountEmail(){ return ""; }
	public String getAccountName(){ return accountPersonName; }
	public int getAccountID(){ return accountPersonID; }
	public boolean isActive(){ return isActive; }
	public String getActiveDirUsername(){ return activeDirUsername; }
	public float getApproverLimit(){ return approverLimit; }
	public float getCarryOverLeave(){ return carryOverLeave; }
	public int getCostCenterID(){ return costCenterID; }
	public String getCostCenterName(){ return costCenterName; }
	public String getDateCreated(SaltApplication app){ return app.onlineGateway.dJsonizeDate(dateCreated); }
	public String getDateModified(SaltApplication app){ return app.onlineGateway.dJsonizeDate(dateModified); }
	public String getDateOfBirth(SaltApplication app){ return app.onlineGateway.dJsonizeDate(dateOfBirth); }
	public int getDepartmentID(){ return departmentID; }
	public String getDepartmentName() { return departmentName; }
	public String getEmail(){ return email; }
	public String getEmploymentEndDate(SaltApplication app){ return app.onlineGateway.dJsonizeDate(employmentEndDate); }
	public String getEmploymentStartDate(SaltApplication app){ return app.onlineGateway.dJsonizeDate(employmentStartDate); }
	public int getExpenseApproverID(){ return expApprover; }
    public String getExpenseApproverEmail(){ return ""; }
	public String getExpenseApproverName(){ return expenseApproverName; }
	public String getFname(){ return firstName; }
	public boolean hasFriday(){ return hasFriday; }
	public String getGenderSex(){ return genderSex; }
	public boolean isADAuth(){ return isADAuth; }
	public boolean isAllowProxyubmission(){ return isAllowProxyubmission; }
	public boolean isApprover(){ return isApprover; }
	public boolean isCorporateApprover(){ return isCorporateApprover; }
	public boolean isRegional(){ return isRegional; }
	public String getJobTitle(){ return jobTitle; }
	public String getLname(){ return lastName; }
	public int getLeaveApprover1ID(){ return leaveApprover1ID; }
	public String getLeaveApprover1Email(){ return leaveApprover1Email; }
	public String getLeaveApprover1Name(){ return leaveApprover1Name; }
	public int getLeaveApprover2ID(){ return leaveApprover2ID; }
	public String getLeaveApprover2Email(){ return leaveApprover2Email; }
	public String getLeaveApprover2Name(){ return leaveApprover2Name; }
	public int getLeaveApprover3ID(){ return leaveApprover3ID; }
	public String getLeaveApprover3Email(){ return leaveApprover3Email; }
	public String getLeaveApprover3Name(){ return leaveApprover3Name; }
	public String getLocation(){ return location; }
	public String getLockDate(SaltApplication app){ return app.onlineGateway.dJsonizeDate(lockDate); }
	public int getMaxConsecutiveDays(){ return maxConsecutiveDays; }
	public String getMobile(){ return mobile; }
	public boolean hasMonday(){ return hasMonday; }
	public String getNotes(){ return notes; }
	public String getOfficeAddress(){ return officeAddress; }
	public int getOfficeID(){ return officeID; }
	public String getOfficeName(){ return officeName; }
	public boolean hasSaturday(){ return hasSaturday; }
    public int getSecurityLevel(){ return securityLevel; }
	public int getStaffID(){ return staffID; }
    public boolean hasSunday(){ return hasSunday; }
	public boolean hasThursday(){ return hasThursday; }
	public boolean hasTuesday(){ return hasTuesday; }
	public boolean hasWednesday(){ return hasWednesday; }
    public float getVacationLeaveAllowance(){ return vacationLeaveAllowance; }
    public float getSickLeaveAllowance(){ return sickLeaveAllowance; }

	public boolean isUser(){ return (securityLevel == SECURITYLEVEL_USER)?true:false; }
	public boolean isManager(){ return (securityLevel == SECURITYLEVEL_MANAGER)?true:false; }
	public boolean isAccount(){ return (securityLevel == SECURITYLEVEL_ACCOUNT)?true:false; }
	public boolean isAdmin(){ return (securityLevel == SECURITYLEVEL_ADMIN)?true:false; }
	public boolean isCM(){ return (securityLevel == SECURITYLEVEL_COUNTRYMANAGER)?true:false; }
	public boolean isAM(){ return (securityLevel == SECURITYLEVEL_ACCOUNTMANAGER)?true:false; }

	public void setUserPosition(USERPOSITION userPosition){ this.userPosition = userPosition; }
	public USERPOSITION getUserPosition(){ return userPosition; }
}
