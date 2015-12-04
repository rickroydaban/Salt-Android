package applusvelosi.projects.android.salt.models.recruitments;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Document;

/**
 * Created by Velosi on 10/9/15.
 */
public class Recruitment implements Serializable{
    public static int RECRUITMENT_STATUSID_OPEN = 26;
    public static int RECRUITMENT_STATUSID_SUBMITTED = 27;
    public static int RECRUITMENT_STATUSID_APPROVEDBYCM = 28;
    public static int RECRUITMENT_STATUSID_REJECTEDBYCM = 29;
    public static int RECRUITMENT_STATUSID_CANCELLED = 30;
    public static int RECRUITMENT_STATUSID_APPROVEDBYRM = 54;
    public static int RECRUITMENT_STATUSID_REJECTEDBYRM = 55;
    public static int RECRUITMENT_STATUSID_APPROVEDBYMHR = 56;
    public static int RECRUITMENT_STATUSID_REJECTEDBYMR  = 57;
    public static int RECRUITMENT_STATUSID_APPROVEDBYCEO = 58;
    public static int RECRUITMENT_STATUSID_REJECTEDBYCEO = 59;

    private boolean isActive;
    private float annualRev;
    private String approverNote;
    private int cmID;
    private String cmEmail, cmName;
    private String dateProcessedByCM, dateProcessedByRM, dateRejected, dateRequested;
    private int departmentToBeAssignedID;
    private String departmentToBeAssignedName;
    private String email;
    private int empCatID;
    private String empCatName;
    private int empTypeID;
    private String empTypeName;
    private float grossBaseBonus, hrsPerWeek;
    private boolean isBudgetedCost, isPositionMaybePermanent, isSpecificPeron, isWithAttachment;
    private String jobTitle;
    private int officeDeploymentID;
    private String officeDeploymentName;
    private int posTypeID;
    private String posTypeName;
    private String reason, recNum;
    private int recReqID, rmID;
    private String rmEmail, rmName, replacementFor, requesterDepartmentName;
    private int requesterID;
    private String requesterName;
    private int requesterOfficeID;
    private String requesterOfficeName, requesterPhoneNumber;
    private float salaryRangeFrom, salaryRangeTo;
    private int statusID;
    private String statusName;
    private String targetStartDate;
    private int timebaseTypeID;
    private String timebaseTypeName;

    private ArrayList<Document> documents;
    private ArrayList<Benefit> benefits;

    public Recruitment(JSONObject jsonRecruitment) throws Exception{
        isActive = jsonRecruitment.getBoolean("Active");
        annualRev = (float)jsonRecruitment.getDouble("AnnualRevenue");
        approverNote = jsonRecruitment.getString("ApproverNote");
        cmID = jsonRecruitment.getInt("CountryManager");
        cmEmail = jsonRecruitment.getString("CountryManagerEmail");
        cmName = jsonRecruitment.getString("CountryManagerName");
        dateProcessedByCM = jsonRecruitment.getString("DateProcessedByCountryManager");
        dateProcessedByRM = jsonRecruitment.getString("DateProcessedByRegionalManager");
        dateRejected = jsonRecruitment.getString("DateRejected");
        dateRequested = jsonRecruitment.getString("DateRequested");
        departmentToBeAssignedID = jsonRecruitment.getInt("DepartmentToBeAssigned");
        departmentToBeAssignedName = jsonRecruitment.getString("DepartmentToBeAssignedName");
        email = jsonRecruitment.getString("Email");
        empCatID = jsonRecruitment.getInt("EmployeeCategoryID");
        empCatName = jsonRecruitment.getString("EmployeeCategoryName");
        empTypeID = jsonRecruitment.getInt("EmploymentTypeID");
        empTypeName = jsonRecruitment.getString("EmploymentTypeName");
        grossBaseBonus = (float)jsonRecruitment.getDouble("GrossBaseBonus");
        hrsPerWeek = (float)jsonRecruitment.getDouble("HoursPerWeek");
        isBudgetedCost = jsonRecruitment.getBoolean("IsBudgetedCost");
        isPositionMaybePermanent = jsonRecruitment.getBoolean("IsPositionMayBePermanent");
        isSpecificPeron = jsonRecruitment.getBoolean("IsSpecificPerson");
        isWithAttachment = jsonRecruitment.getBoolean("IsWithAttachment");
        jobTitle = jsonRecruitment.getString("JobTitle");
        officeDeploymentID = jsonRecruitment.getInt("OfficeOfDeployment");
        officeDeploymentName = jsonRecruitment.getString("OfficeOfDeploymentName");
        posTypeID = jsonRecruitment.getInt("PositionTypeID");
        posTypeName = jsonRecruitment.getString("PositionTypeName");
        reason = jsonRecruitment.getString("Reason");
        recNum = jsonRecruitment.getString("RecruitmentNumber");
        recReqID = jsonRecruitment.getInt("RecruitmentRequestID");
        rmID = jsonRecruitment.getInt("RegionalManager");
        rmEmail = jsonRecruitment.getString("RegionalManagerEmail");
        rmName = jsonRecruitment.getString("RegionalManagerName");
        replacementFor = jsonRecruitment.getString("ReplacementFor");
        requesterDepartmentName = jsonRecruitment.getString("RequesterDepartment");
        requesterID = jsonRecruitment.getInt("RequesterID");
        requesterName = jsonRecruitment.getString("RequesterName");
        requesterOfficeID = jsonRecruitment.getInt("RequesterOfficeID");
        requesterOfficeName = jsonRecruitment.getString("RequesterOfficeName");
        requesterPhoneNumber = jsonRecruitment.getString("RequesterPhoneNumber");
        salaryRangeFrom = (float)jsonRecruitment.getDouble("SalaryRangeFrom");
        salaryRangeTo = (float)jsonRecruitment.getDouble("SalaryRangeTo");
        statusID = jsonRecruitment.getInt("StatusID");
        statusName = jsonRecruitment.getString("StatusName");
        targetStartDate = jsonRecruitment.getString("TargettedStartDate");
        timebaseTypeID = jsonRecruitment.getInt("TimeBaseTypeID");
        timebaseTypeName = jsonRecruitment.getString("TimeBaseTypeName");

        JSONArray jsonDocs = jsonRecruitment.getJSONArray("Documents");
        documents = new ArrayList<Document>();
        for(int i=0; i<jsonDocs.length(); i++)
            documents.add(new Document(jsonDocs.getJSONObject(i)));

        JSONArray jsonBenefits = jsonRecruitment.getJSONArray("OtherBenefits");
        benefits = new ArrayList<Benefit>();
        for(int i=0; i<jsonBenefits.length(); i++)
            benefits.add(new Benefit(jsonBenefits.getJSONObject(i)));
    }

    public float getAnnualRevenue(){ return annualRev; }
    public int getCMID(){ return cmID; }
    public String getCMEmail(){ return cmEmail; }
    public String getCMName(){ return cmName; }
    public String getDateProcessedByCM(SaltApplication app){
        String tempDateProcessedByCM = app.onlineGateway.dJsonizeDate(dateProcessedByCM);
        return (tempDateProcessedByCM.contains("-Jan-1900"))?"-":tempDateProcessedByCM;
    }
    public String getDateProcessedBYRM(SaltApplication app){
        String tempDateProcessedByRM = app.onlineGateway.dJsonizeDate(dateProcessedByRM);
        return (tempDateProcessedByRM.contains("-Jan-1900"))?"-":tempDateProcessedByRM;
    }

    public String getDateRejected(SaltApplication app){
        String tempDateRejcted = app.onlineGateway.dJsonizeDate(dateRejected);
        return (tempDateRejcted.contains("-Jan-1900"))?"-":tempDateRejcted;
    }

    public String getDateRequested(SaltApplication app){
        String tempDateRequested = app.onlineGateway.dJsonizeDate(dateRequested);
        return (tempDateRequested.contains("-Jan-1900"))?"-":tempDateRequested;
    }
    public int getDepartmentToBeAssignedID(){ return departmentToBeAssignedID; }
    public String getDepartmentToBeAssignedName(){ return departmentToBeAssignedName; }
    public String getEmail(){ return email; }
    public int getEmloyeeCategoryID(){ return empCatID; }
    public String getEmployeeCategoryName(){ return empCatName; }
    public int getEmploymentTypeID(){ return empTypeID; }
    public String getEmploymentTymeName(){ return empTypeName; }
    public float getGrossBaseBonus(){ return grossBaseBonus; }
    public float getHorsePerWeek(){ return hrsPerWeek; }
    public boolean isBudgetedCost(){ return isBudgetedCost; }
    public boolean isPositionMayBePermanent(){ return isPositionMaybePermanent; }
    public boolean isSpecificPerson(){ return isSpecificPeron; }
    public boolean isWithAttachment(){ return isWithAttachment; }
    public String getJobTitle(){ return jobTitle; }
    public int getOfficeOfDeploymentID(){ return officeDeploymentID; }
    public String getOfficeOfDeploymentName(){ return officeDeploymentName; }
    public int getPositionTypeID(){ return posTypeID; }
    public String getPositionTypeName(){ return posTypeName; }
    public String getReason(){ return reason; }
    public String getRecruitmentNumber(){ return recNum; }
    public int getRecruitmentRequestID(){ return recReqID; }
    public int getRMID(){ return rmID; }
    public String getRMEmail(){ return rmEmail; }
    public String getRMName(){ return rmName; }
    public String getReplacementFor(){ return replacementFor; }
    public String getRequesterDepartmentName(){ return requesterDepartmentName; }
    public int getRequesterID(){ return requesterID; }
    public String getRequesterName(){ return requesterName; }
    public int getRequesterOfficeID(){ return requesterOfficeID; }
    public  String getRequesterOfficeName(){ return requesterOfficeName; }
    public String getRequesterPhoneNumber(){ return requesterPhoneNumber; }
    public float getSalaryRangeFrom(){ return salaryRangeFrom; }
    public float getSalaryRangeTo(){ return salaryRangeTo; }
    public int getStatusID(){ return statusID; }
    public String getStatusName(){ return statusName; }
    public String getTargettedStartDate(SaltApplication app){ return app.onlineGateway.dJsonizeDate(targetStartDate); }
    public int getTimeBaseTypeID(){ return timebaseTypeID; }
    public String getTimeBaseTypeName(){ return timebaseTypeName; }

    public ArrayList<Document> getDocuments(){ return documents; }
    public ArrayList<Benefit> getBenefits(){ return benefits; }

    public String getJSONFromUpdatingRecruitment(int statusID, String keyForUpdatableDate, String approverNote, SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();

        tempMap.put("Active", isActive);
        tempMap.put("AnnualRevenue", annualRev);
        tempMap.put("ApproverNote", approverNote);
        tempMap.put("CountryManager", cmID);
        tempMap.put("CountryManagerEmail", cmEmail);
        tempMap.put("CountryManagerName", cmName);
        tempMap.put("DateProcessedByCountryManager", dateProcessedByCM);
        tempMap.put("DateProcessedByRegionalManager", dateProcessedByRM);
        tempMap.put("DateRejected", dateRejected);
        tempMap.put("DateRequested", dateRequested);
        tempMap.put("DepartmentToBeAssigned", departmentToBeAssignedID);
        tempMap.put("DepartmentToBeAssignedName", departmentToBeAssignedName);
        tempMap.put("DocName","");
        JSONArray jsonDocs = new JSONArray();
        for(Document doc :documents)
            jsonDocs.put(doc.getJSONObject());
        tempMap.put("Documents",jsonDocs);
        tempMap.put("Email",email);
        tempMap.put("EmployeeCategoryID", empCatID);
        tempMap.put("EmployeeCategoryName", empCatName);
        tempMap.put("EmploymentTypeID", empTypeID);
        tempMap.put("EmploymentTypeName", empTypeName);
        tempMap.put("GrossBaseBonus", grossBaseBonus);
        tempMap.put("HoursPerWeek", hrsPerWeek);
        tempMap.put("IsBudgetedCost", isBudgetedCost);
        tempMap.put("IsPositionMayBePermanent", isPositionMaybePermanent);
        tempMap.put("IsSpecificPerson", isSpecificPeron);
        tempMap.put("IsWithAttachment", isWithAttachment);
        tempMap.put("JobTitle", jobTitle);
        tempMap.put("OfficeOfDeployment", officeDeploymentID);
        tempMap.put("OfficeOfDeploymentName", officeDeploymentName);
        tempMap.put("OrigDoc", "");
        JSONArray jsonBenefits = new JSONArray();
        for(Benefit benefit :benefits)
            jsonBenefits.put(benefit.getJsonObject());
        tempMap.put("OtherBenefits", jsonBenefits);
        tempMap.put("PositionTypeID", posTypeID);
        tempMap.put("PositionTypeName", posTypeName);
        tempMap.put("Reason", reason);
        tempMap.put("RecruitmentNumber", recNum);
        tempMap.put("RecruitmentRequestID", recReqID);
        tempMap.put("RegionalManager", rmID);
        tempMap.put("RegionalManagerEmail", rmEmail);
        tempMap.put("RegionalManagerName", rmName);
        tempMap.put("ReplacementFor", replacementFor);
        tempMap.put("RequesterDepartment", requesterDepartmentName);
        tempMap.put("RequesterID", requesterID);
        tempMap.put("RequesterName", requesterName);
        tempMap.put("RequesterOfficeID", requesterOfficeID);
        tempMap.put("RequesterOfficeName", requesterOfficeName);
        tempMap.put("RequesterPhoneNumber", requesterPhoneNumber);
        tempMap.put("SalaryRangeFrom", salaryRangeFrom);
        tempMap.put("SalaryRangeTo", salaryRangeTo);
        tempMap.put("StatusID", statusID);
        tempMap.put("StatusName", statusName);
        tempMap.put("TargettedStartDate", targetStartDate);
        tempMap.put("TimeBaseTypeID", timebaseTypeID);
        tempMap.put("TimeBaseTypeName", timebaseTypeName);

        if(!keyForUpdatableDate.equals("NA"))
            tempMap.put(keyForUpdatableDate, app.onlineGateway.jsonizeDate(new Date()));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

    public String jsonize(SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();

        tempMap.put("Active", isActive);
        tempMap.put("AnnualRevenue", annualRev);
        tempMap.put("ApproverNote", approverNote);
        tempMap.put("CountryManager", cmID);
        tempMap.put("CountryManagerEmail", cmEmail);
        tempMap.put("CountryManagerName", cmName);
        tempMap.put("DateProcessedByCountryManager", dateProcessedByCM);
        tempMap.put("DateProcessedByRegionalManager", dateProcessedByRM);
        tempMap.put("DateRejected", dateRejected);
        tempMap.put("DateRequested", dateRequested);
        tempMap.put("DepartmentToBeAssigned", departmentToBeAssignedID);
        tempMap.put("DepartmentToBeAssignedName", departmentToBeAssignedName);
        tempMap.put("DocName","");
        JSONArray jsonDocs = new JSONArray();
        for(Document doc :documents)
            jsonDocs.put(doc.getJSONObject());
        tempMap.put("Documents",jsonDocs);
        tempMap.put("Email",email);
        tempMap.put("EmployeeCategoryID", empCatID);
        tempMap.put("EmployeeCategoryName", empCatName);
        tempMap.put("EmploymentTypeID", empTypeID);
        tempMap.put("EmploymentTypeName", empTypeName);
        tempMap.put("GrossBaseBonus", grossBaseBonus);
        tempMap.put("HoursPerWeek", hrsPerWeek);
        tempMap.put("IsBudgetedCost", isBudgetedCost);
        tempMap.put("IsPositionMayBePermanent", isPositionMaybePermanent);
        tempMap.put("IsSpecificPerson", isSpecificPeron);
        tempMap.put("IsWithAttachment", isWithAttachment);
        tempMap.put("JobTitle", jobTitle);
        tempMap.put("OfficeOfDeployment", officeDeploymentID);
        tempMap.put("OfficeOfDeploymentName", officeDeploymentName);
        tempMap.put("OrigDoc", "");
        JSONArray jsonBenefits = new JSONArray();
        for(Benefit benefit :benefits)
            jsonBenefits.put(benefit.getJsonObject());
        tempMap.put("OtherBenefits", jsonBenefits);
        tempMap.put("PositionTypeID", posTypeID);
        tempMap.put("PositionTypeName", posTypeName);
        tempMap.put("Reason", reason);
        tempMap.put("RecruitmentNumber", recNum);
        tempMap.put("RecruitmentRequestID", recReqID);
        tempMap.put("RegionalManager", rmID);
        tempMap.put("RegionalManagerEmail", rmEmail);
        tempMap.put("RegionalManagerName", rmName);
        tempMap.put("ReplacementFor", replacementFor);
        tempMap.put("RequesterDepartment", requesterDepartmentName);
        tempMap.put("RequesterID", requesterID);
        tempMap.put("RequesterName", requesterName);
        tempMap.put("RequesterOfficeID", requesterOfficeID);
        tempMap.put("RequesterOfficeName", requesterOfficeName);
        tempMap.put("RequesterPhoneNumber", requesterPhoneNumber);
        tempMap.put("SalaryRangeFrom", salaryRangeFrom);
        tempMap.put("SalaryRangeTo", salaryRangeTo);
        tempMap.put("StatusID", statusID);
        tempMap.put("StatusName", statusName);
        tempMap.put("TargettedStartDate", targetStartDate);
        tempMap.put("TimeBaseTypeID", timebaseTypeID);
        tempMap.put("TimeBaseTypeName", timebaseTypeName);

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

    public class Benefit implements Serializable{
        private float actualCost;
        private int benefitID, benefitRecReqID;
        private String benefitName;

        public Benefit(JSONObject jsonBenefit) throws Exception{
            actualCost = (float)jsonBenefit.getDouble("ActualCost");
            benefitID = jsonBenefit.getInt("BenefitID");
            benefitName = jsonBenefit.getString("BenefitName");
            benefitRecReqID = jsonBenefit.getInt("RecruitmentRequestID");
        }

        public float getActualCost(){ return actualCost; }
        public String getBenefitName(){ return benefitName; }

        public JSONObject getJsonObject() throws Exception{
            JSONObject obj = new JSONObject();
            obj.put("ActualCost", actualCost);
            obj.put("BenefitID", benefitID);
            obj.put("BenefitName", benefitName);
            obj.put("RecruitmentRequestID", benefitRecReqID);

            return obj;
        }
    }
}
