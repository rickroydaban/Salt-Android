package applusvelosi.projects.android.salt.models.recruitments;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class Recruitment {
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

    private HashMap<String, Object> mapRecruitment;
    //property maps
    private ArrayList<HashMap<String, Object>> mapBenefits, mapAttachments;

    public Recruitment(JSONObject jsonRecruitment, OnlineGateway onlineGateway) throws Exception{
        mapRecruitment = new HashMap<String, Object>();
        mapRecruitment.putAll(OnlineGateway.toMap(jsonRecruitment));
        mapRecruitment.put("CountryManager", Integer.parseInt(mapRecruitment.get("CountryManager").toString()));
        mapRecruitment.put("DepartmentToBeAssigned", Integer.parseInt(mapRecruitment.get("DepartmentToBeAssigned").toString()));
        mapRecruitment.put("EmployeeCategoryID", Integer.parseInt(mapRecruitment.get("EmployeeCategoryID").toString()));
        mapRecruitment.put("EmploymentTypeID", Integer.parseInt((mapRecruitment.get("EmploymentTypeID").toString())));
        mapRecruitment.put("OfficeOfDeployment", Integer.parseInt(mapRecruitment.get("OfficeOfDeployment").toString()));
        mapRecruitment.put("PositionTypeID", Integer.parseInt(mapRecruitment.get("PositionTypeID").toString()));
        mapRecruitment.put("RecruitmentRequestID", Integer.parseInt(mapRecruitment.get("RecruitmentRequestID").toString()));
        mapRecruitment.put("RegionalManager", Integer.parseInt(mapRecruitment.get("RegionalManager").toString()));
        mapRecruitment.put("RequesterID", Integer.parseInt(mapRecruitment.get("RequesterID").toString()));
        mapRecruitment.put("RequesterOfficeID", Integer.parseInt(mapRecruitment.get("RequesterOfficeID").toString()));
        mapRecruitment.put("TimeBaseTypeID", Integer.parseInt(mapRecruitment.get("TimeBaseTypeID").toString()));
        mapRecruitment.put("StatusID", Integer.parseInt(mapRecruitment.get("StatusID").toString()));
        //floats
        mapRecruitment.put("GrossBaseBonus", Float.parseFloat(mapRecruitment.get("GrossBaseBonus").toString()));
        mapRecruitment.put("SalaryRangeTo", Float.parseFloat(mapRecruitment.get("SalaryRangeTo").toString()));
        mapRecruitment.put("HoursPerWeek", Float.parseFloat(mapRecruitment.get("HoursPerWeek").toString()));
        mapRecruitment.put("AnnualRevenue", Float.parseFloat(mapRecruitment.get("AnnualRevenue").toString()));
        mapRecruitment.put("SalaryRangeFrom", Float.parseFloat(mapRecruitment.get("SalaryRangeFrom").toString()));
        //booleans
        mapRecruitment.put("Active", Boolean.parseBoolean(mapRecruitment.get("Active").toString()));
        mapRecruitment.put("IsWithAttachment", Boolean.parseBoolean(mapRecruitment.get("IsWithAttachment").toString()));
        mapRecruitment.put("IsBudgetedCost", Boolean.parseBoolean(mapRecruitment.get("IsBudgetedCost").toString()));
        mapRecruitment.put("IsPositionMayBePermanent", Boolean.parseBoolean(mapRecruitment.get("IsPositionMayBePermanent").toString()));
        mapRecruitment.put("IsSpecificPerson", Boolean.parseBoolean(mapRecruitment.get("IsSpecificPerson").toString()));
        //dates
        mapRecruitment.put("DateProcessedByCountryManager", onlineGateway.dJsonizeDate(mapRecruitment.get("DateProcessedByCountryManager").toString()));
        mapRecruitment.put("DateProcessedByRegionalManager", onlineGateway.dJsonizeDate(mapRecruitment.get("DateProcessedByRegionalManager").toString()));
        mapRecruitment.put("DateRejected", onlineGateway.dJsonizeDate(mapRecruitment.get("DateRejected").toString()));
        mapRecruitment.put("DateRequested", onlineGateway.dJsonizeDate(mapRecruitment.get("DateRequested").toString()));
        mapRecruitment.put("TargettedStartDate", onlineGateway.dJsonizeDate(mapRecruitment.get("TargettedStartDate").toString()));

        mapAttachments = (ArrayList<HashMap<String, Object>>)OnlineGateway.toList(jsonRecruitment.getJSONArray("Documents"));
        mapBenefits = (ArrayList<HashMap<String, Object>>)OnlineGateway.toList(jsonRecruitment.getJSONArray("OtherBenefits"));
    }

    public float getAnnualRevenue(){ return Float.parseFloat(mapRecruitment.get("AnnualRevenue").toString()); }

    public String getApproverNotes(){ return mapRecruitment.get("ApproverNote").toString(); }

    public int getCMID(){ return Integer.parseInt(mapRecruitment.get("CountryManager").toString()); }

    public String getCMEmail(){ return mapRecruitment.get("CountryManagerEmail").toString(); }

    public String getCMName(){ return mapRecruitment.get("CountryManagerName").toString(); }

    public String getDateProcessedByCM(){ return mapRecruitment.get("DateProcessedByCountryManager").toString(); }

    public String getDateProcessedByRM(){ return mapRecruitment.get("DateProcessedByRegionalManager").toString(); }

    public String getDateRejected(){ return mapRecruitment.get("DateRejected").toString(); }

    public String getDateRequested(){ return mapRecruitment.get("DateRequested").toString(); }

    public int getDepartmentToBeAssignedID(){ return Integer.parseInt(mapRecruitment.get("DepartmentToBeAssigned").toString()); }

    public String getDepartmentToBeAssignedName(){ return mapRecruitment.get("DepartmentToBeAssignedName").toString(); }

    public String getEmail(){ return mapRecruitment.get("Email").toString(); };

    public int getEmloyeeCategoryID(){ return Integer.parseInt(mapRecruitment.get("EmployeeCategoryID").toString()); }

    public String getEmployeeCategoryName(){ return mapRecruitment.get("EmployeeCategoryName").toString(); }

    public int getEmploymentTypeID(){ return Integer.parseInt(mapRecruitment.get("EmploymentTypeID").toString()); }

    public String getEmploymentTymeName(){ return mapRecruitment.get("EmploymentTypeName").toString(); }

    public float getGrossBaseBonus(){ return Float.parseFloat(mapRecruitment.get("GrossBaseBonus").toString()); }

    public float getHorsePerWeek(){ return Float.parseFloat(mapRecruitment.get("HoursPerWeek").toString()); }

    public boolean isBudgetedCost(){ return Boolean.parseBoolean(mapRecruitment.get("IsBudgetedCost").toString()); }

    public boolean isPositionMayBePermanent(){ return Boolean.parseBoolean(mapRecruitment.get("IsPositionMayBePermanent").toString()); }

    public boolean isSpecificPerson(){ return Boolean.parseBoolean(mapRecruitment.get("IsSpecificPerson").toString()); }

    public boolean isWithAttachment(){ return Boolean.parseBoolean(mapRecruitment.get("IsWithAttachment").toString()); }

    public String getJobTitle(){ return mapRecruitment.get("JobTitle").toString(); }

    public int getOfficeOfDeploymentID(){ return Integer.parseInt(mapRecruitment.get("OfficeOfDeployment").toString()); }

    public String getOfficeOfDeploymentName(){ return mapRecruitment.get("OfficeOfDeploymentName").toString(); }

    public HashMap<String, Object> getBenefits() { return (HashMap<String, Object>) mapRecruitment.get("OtherBenefits"); }

    public HashMap<String, Object> getDocuents(){ return (HashMap<String, Object>) mapRecruitment.get("Documents"); }

    public int getPositionTypeID(){ return Integer.parseInt(mapRecruitment.get("PositionTypeID").toString()); }

    public String getPositionTypeName(){ return mapRecruitment.get("PositionTypeName").toString(); }

    public String getReason(){ return mapRecruitment.get("Reason").toString(); }

    public String getRecruitmentNumber(){ return mapRecruitment.get("RecruitmentNumber").toString(); }

    public int getRecruitmentRequestID(){ return Integer.parseInt(mapRecruitment.get("RecruitmentRequestID").toString()); }

    public int getRMID(){ return Integer.parseInt(mapRecruitment.get("RegionalManager").toString()); }

    public String getRMEmail(){ return mapRecruitment.get("RegionalManagerEmail").toString(); }

    public String getRMName(){ return mapRecruitment.get("RegionalManagerName").toString(); }

    public String getReplacementFor(){ return mapRecruitment.get("ReplacementFor").toString(); }

    public String getRequesterDepartmentName(){ return mapRecruitment.get("RequesterDepartment").toString(); }

    public int getRequesterID(){ return Integer.parseInt(mapRecruitment.get("RequesterID").toString()); }

    public String getRequesterName(){ return mapRecruitment.get("RequesterName").toString(); }

    public int getRequesterOfficeID(){ return Integer.parseInt(mapRecruitment.get("RequesterOfficeID").toString()); }

    public  String getRequesterOfficeName(){ return mapRecruitment.get("RequesterOfficeName").toString(); }

    public String getRequesterPhoneNumber(){ return mapRecruitment.get("RequesterPhoneNumber").toString(); }

    public float getSalaryRangeFrom(){ return Float.parseFloat(mapRecruitment.get("SalaryRangeFrom").toString()); }

    public float getSalaryRangeTo(){ return Float.parseFloat(mapRecruitment.get("SalaryRangeTo").toString()); }

    public int getStatusID(){ return Integer.parseInt(mapRecruitment.get("StatusID").toString()); }

    public String getStatusName(){ return mapRecruitment.get("StatusName").toString(); }

    public String getTargettedStartDate(){ return mapRecruitment.get("TargettedStartDate").toString(); }

    public int getTimeBaseTypeID(){ return Integer.parseInt(mapRecruitment.get("TimeBaseTypeID").toString()); }

    public String getTimeBaseTypeName(){ return mapRecruitment.get("TimeBaseTypeName").toString(); }

    public ArrayList<HashMap<String, Object>> getAttachments(){ return mapAttachments; }

    public ArrayList<HashMap<String, Object>> getOtherBenefits(){ return mapBenefits; }

    public String getJSONFromUpdatingRecruitment(int statusID, String keyForUpdatableDate, String approverNote, SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.putAll(mapRecruitment);

        tempMap.put("StatusID", statusID);
        tempMap.put("DateProcessedByCountryManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByCountryManager").toString())));
        tempMap.put("DateProcessedByRegionalManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByRegionalManager").toString())));
        tempMap.put("DateRejected", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateRejected").toString())));
        tempMap.put("DateRequested", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateRequested").toString())));
        tempMap.put("TargettedStartDate", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("TargettedStartDate").toString())));
        tempMap.put("Documents", app.gson.fromJson(tempMap.get("Documents").toString(), app.types.arrayListOfHashmapOfStringObject));
        tempMap.put("OtherBenefits", app.gson.fromJson(tempMap.get("OtherBenefits").toString(), app.types.arrayListOfHashmapOfStringObject));
        tempMap.put("ApproverNote", approverNote);

        if(!keyForUpdatableDate.equals("NA"))
            tempMap.put(keyForUpdatableDate, app.onlineGateway.jsonizeDate(new Date()));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

    public String jsonize(SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.putAll(mapRecruitment);

        tempMap.put("DateProcessedByCountryManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByCountryManager").toString())));
        tempMap.put("DateProcessedByRegionalManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByRegionalManager").toString())));
        tempMap.put("DateRejected", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateRejected").toString())));
        tempMap.put("DateRequested", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateRequested").toString())));
        tempMap.put("TargettedStartDate", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("TargettedStartDate").toString())));
        tempMap.put("Documents", app.gson.fromJson(tempMap.get("Documents").toString(), app.types.arrayListOfHashmapOfStringObject));
        tempMap.put("OtherBenefits", app.gson.fromJson(tempMap.get("OtherBenefits").toString(), app.types.arrayListOfHashmapOfStringObject));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }
}
