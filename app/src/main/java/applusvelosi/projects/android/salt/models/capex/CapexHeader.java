package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexHeader {
    public static final String NOATTACHMENT = "No Attachment";

    private static String CAPEXHEADERTYPE_OPEN = "40-Open";
    private static String CAPEXHEADERTYPE_SUBMITTED = "41-Submitted";
    private static String CAPEXHEADERTYPE_APPROVEDBYCM = "42-Approved by Country Manager";
    private static String CAPEXHEADERTYPE_REJECTEDBYCM = "43-Rejected by Country Manager";
    private static String CAPEXHEADERTYPE_APPROVEDBYRM = "44-Approved by Regional Manager";
    private static String CAPEXHEADERTYPE_REJECTEDBYRM = "45-Rejected by Regional Manager";
    private static String CAPEXHEADERTYPE_RETURNED = "46-Return";
    private static String CAPEXHEADERTYPE_CANCELLED = "47-Cancelled";
    private static String CAPEXHEADERTYPE_APPROVEDBYCFO = "48-Approved by CFO";
    private static String CAPEXHEADERTYPE_REJECTEDBYCFO = "49-Approved by CFO";
    private static String CAPEXHEADERTYPE_APPROVEDBYCEO = "50-Approved by CEO";
    private static String CAPEXHEADERTYPE_REJECTEDBYCEO = "51-Rejected by CEO";

    public static int CAPEXHEADERID_OPEN, CAPEXHEADERID_SUBMITTED, CAPEXHEADERID_APPROVEDBYCM, CAPEXHEADERID_REJECTEDBYCM,
            CAPEXHEADERID_APPROVEDBYRM, CAPEXHEADERID_REJECTEDBYRM, CAPEXHEADERID_RETURN, CAPEXHEADERID_CANCEL,
            CAPEXHEADERID_APPROVEDBYCFO, CAPEXHEADERID_REJECTEDBYCFO, CAPEXHEADERID_APPROVEDBYCEO, CAPEXHEADERID_REJECTEDBYCEO;

    private static String CAPEXHEADERDESC_OPEN, CAPEXHEADERDESC_SUBMITTED, CAPEXHEADERDESC_APPROVEDBYCM, CAPEXHEADERDESC_REJECTEDBYCM,
            CAPEXHEADERDESC_APPROVEDBYRM, CAPEXHEADERDESC_REJECTEDBYRM, CAPEXHEADERDESC_RETURN, CAPEXHEADERDESC_CANCEL,
            CAPEXHEADERDESC_APPROVEDBYCFO, CAPEXHEADERDESC_REJECTEDBYCFO, CAPEXHEADERDESC_APPROVEDBYCEO, CAPEXHEADERDESC_REJECTEDBYCEO;

    private static ArrayList<String> statusDescriptionList;

    static {
        CAPEXHEADERID_OPEN = Integer.parseInt(CAPEXHEADERTYPE_OPEN.split("-")[0]);
        CAPEXHEADERID_SUBMITTED = Integer.parseInt(CAPEXHEADERTYPE_SUBMITTED.split("-")[0]);
        CAPEXHEADERID_APPROVEDBYCM = Integer.parseInt(CAPEXHEADERTYPE_APPROVEDBYCM.split("-")[0]);
        CAPEXHEADERID_REJECTEDBYCM = Integer.parseInt(CAPEXHEADERTYPE_REJECTEDBYCM.split("-")[0]);
        CAPEXHEADERID_APPROVEDBYRM = Integer.parseInt(CAPEXHEADERTYPE_APPROVEDBYRM.split("-")[0]);
        CAPEXHEADERID_REJECTEDBYRM = Integer.parseInt(CAPEXHEADERTYPE_REJECTEDBYRM.split("-")[0]);
        CAPEXHEADERID_RETURN = Integer.parseInt(CAPEXHEADERTYPE_RETURNED.split("-")[0]);
        CAPEXHEADERID_CANCEL = Integer.parseInt(CAPEXHEADERTYPE_CANCELLED.split("-")[0]);
        CAPEXHEADERID_APPROVEDBYCFO = Integer.parseInt(CAPEXHEADERTYPE_APPROVEDBYCFO.split("-")[0]);
        CAPEXHEADERID_REJECTEDBYCFO = Integer.parseInt(CAPEXHEADERTYPE_REJECTEDBYCFO.split("-")[0]);
        CAPEXHEADERID_APPROVEDBYCEO = Integer.parseInt(CAPEXHEADERTYPE_APPROVEDBYCEO.split("-")[0]);
        CAPEXHEADERID_REJECTEDBYCEO = Integer.parseInt(CAPEXHEADERTYPE_REJECTEDBYCEO.split("-")[0]);

        CAPEXHEADERDESC_OPEN = CAPEXHEADERTYPE_OPEN.split("-")[1];
        CAPEXHEADERDESC_SUBMITTED = CAPEXHEADERTYPE_SUBMITTED.split("-")[1];
        CAPEXHEADERDESC_APPROVEDBYCM = CAPEXHEADERTYPE_APPROVEDBYCM.split("-")[1];
        CAPEXHEADERDESC_REJECTEDBYCM = CAPEXHEADERTYPE_REJECTEDBYCM.split("-")[1];
        CAPEXHEADERDESC_APPROVEDBYRM = CAPEXHEADERTYPE_APPROVEDBYRM.split("-")[1];
        CAPEXHEADERDESC_REJECTEDBYRM = CAPEXHEADERTYPE_REJECTEDBYRM.split("-")[1];
        CAPEXHEADERDESC_RETURN = CAPEXHEADERTYPE_RETURNED.split("-")[1];
        CAPEXHEADERDESC_CANCEL = CAPEXHEADERTYPE_CANCELLED.split("-")[1];
        CAPEXHEADERDESC_APPROVEDBYCFO = CAPEXHEADERTYPE_APPROVEDBYCFO.split("-")[1];
        CAPEXHEADERDESC_REJECTEDBYCFO = CAPEXHEADERTYPE_REJECTEDBYCFO.split("-")[1];
        CAPEXHEADERDESC_APPROVEDBYCEO = CAPEXHEADERTYPE_APPROVEDBYCEO.split("-")[1];
        CAPEXHEADERDESC_REJECTEDBYCEO = CAPEXHEADERTYPE_REJECTEDBYCEO.split("-")[1];

        statusDescriptionList = new ArrayList<String>();
        statusDescriptionList.add(CAPEXHEADERDESC_OPEN);
        statusDescriptionList.add(CAPEXHEADERDESC_SUBMITTED);
        statusDescriptionList.add(CAPEXHEADERDESC_APPROVEDBYCM);
        statusDescriptionList.add(CAPEXHEADERDESC_REJECTEDBYCM);
        statusDescriptionList.add(CAPEXHEADERDESC_APPROVEDBYRM);
        statusDescriptionList.add(CAPEXHEADERDESC_REJECTEDBYRM);
        statusDescriptionList.add(CAPEXHEADERDESC_RETURN);
        statusDescriptionList.add(CAPEXHEADERDESC_CANCEL);
        statusDescriptionList.add(CAPEXHEADERDESC_APPROVEDBYCFO);
        statusDescriptionList.add(CAPEXHEADERDESC_REJECTEDBYCFO);
        statusDescriptionList.add(CAPEXHEADERDESC_APPROVEDBYCEO);
        statusDescriptionList.add(CAPEXHEADERDESC_REJECTEDBYCEO);
    }

    private HashMap<String, Object> mapCapexHeader;
    private ArrayList<HashMap<String, Object>> mapDocuments;

    public CapexHeader(JSONObject jsonCapexHeader, OnlineGateway onlineGateway) throws Exception{
        mapCapexHeader = new HashMap<String, Object>();
        mapCapexHeader.putAll(OnlineGateway.toMap(jsonCapexHeader));
        mapCapexHeader.put("CapexID",Integer.parseInt(mapCapexHeader.get("CapexID").toString()));
        mapCapexHeader.put("CostCenterID", Integer.parseInt(mapCapexHeader.get("CostCenterID").toString()));
        mapCapexHeader.put("CountryManager", Integer.parseInt(mapCapexHeader.get("CountryManager").toString()));
        mapCapexHeader.put("CreatedBy", Integer.parseInt(mapCapexHeader.get("CreatedBy").toString()));
        mapCapexHeader.put("DepartmentID", Integer.parseInt(mapCapexHeader.get("DepartmentID").toString()));
        mapCapexHeader.put("InvestmentTypeID", Integer.parseInt(mapCapexHeader.get("InvestmentTypeID").toString()));
        mapCapexHeader.put("LastModifiedBy", Integer.parseInt(mapCapexHeader.get("LastModifiedBy").toString()));
        mapCapexHeader.put("OfficeID", Integer.parseInt(mapCapexHeader.get("OfficeID").toString()));
        mapCapexHeader.put("RegionalManager", Integer.parseInt(mapCapexHeader.get("RegionalManager").toString()));
        mapCapexHeader.put("RequesterID", Integer.parseInt(mapCapexHeader.get("RequesterID").toString()));
        mapCapexHeader.put("StatusID", Integer.parseInt(mapCapexHeader.get("StatusID").toString()));
        //dates
        mapCapexHeader.put("DateCreated", onlineGateway.dJsonizeDate(mapCapexHeader.get("DateCreated").toString()));
        mapCapexHeader.put("DateProcessedByCountryManager", onlineGateway.dJsonizeDate(mapCapexHeader.get("DateProcessedByCountryManager").toString()));
        mapCapexHeader.put("DateProcessedByRegionalManager", onlineGateway.dJsonizeDate(mapCapexHeader.get("DateProcessedByRegionalManager").toString()));
        mapCapexHeader.put("DateSubmitted", onlineGateway.dJsonizeDate(mapCapexHeader.get("DateSubmitted").toString()));

        mapDocuments = (ArrayList<HashMap<String, Object>>)OnlineGateway.toList(jsonCapexHeader.getJSONArray("Documents"));
    }

    public String getAttachedCer(){
        String cer = mapCapexHeader.get("AttachedCER").toString();
        return (cer == null || cer.length()<1 || cer.equals(""))?NOATTACHMENT:cer;
    }

    public int getCapexID(){
        return Integer.parseInt(mapCapexHeader.get("CapexID").toString());
    }

    public String getCapexNumber(){
        return mapCapexHeader.get("CapexNumber").toString();
    }

    public int getCostCenterID(){
        return Integer.parseInt(mapCapexHeader.get("CostCenterID").toString());
    }

    public String getCostCenterName(){
        return mapCapexHeader.get("CostCenterName").toString();
    }

    public int getCMID(){
        return Integer.parseInt(mapCapexHeader.get("CountryManager").toString());
    }

    public String getCMEmail(){
        return mapCapexHeader.get("CountryManagerEmail").toString();
    }

    public String getCMName(){
        return mapCapexHeader.get("CountryManagerName").toString();
    }

    public String getDateCreated(){
        return mapCapexHeader.get("DateCreated").toString();
    }

    public String getDateProcessedByCM(){
        return mapCapexHeader.get("DateProcessedByCountryManager").toString();
    }

    public String getDateProcessedBYRM(){
        return mapCapexHeader.get("DateProcessedByRegionalManager").toString();
    }

    public String getDateSubmitted(){
        return mapCapexHeader.get("DateSubmitted").toString();
    }

    public int getDepartmentID(){
        return Integer.parseInt(mapCapexHeader.get("DepartmentID").toString());
    }

    public String getDepartmentName(){
        return mapCapexHeader.get("DepartmentName").toString();
    }

    public int getInvestmentTypeID(){
        return Integer.parseInt(mapCapexHeader.get("InvestmentTypeID").toString());
    }

    public String getInvestmentTypeName(){
        return mapCapexHeader.get("InvestmentTypeName").toString();
    }

    public String getNotes(){
        return mapCapexHeader.get("Note").toString();
    }

    public int getOfficeID(){
        return  Integer.parseInt(mapCapexHeader.get("OfficeID").toString());
    }

    public String getOfficeName(){
        return mapCapexHeader.get("OfficeName").toString();
    }

    public int getRMID(){
        return Integer.parseInt(mapCapexHeader.get("RegionalManager").toString());
    }

    public String getRMEmail(){
        return mapCapexHeader.get("RegionalManagerEmail").toString();
    }

    public String getRMname(){
        return mapCapexHeader.get("RegionalManagerName").toString();
    }

    public String getRequesterEmail(){
        return mapCapexHeader.get("RequesterEmail").toString();
    }

    public int getRequesterID(){
        return Integer.parseInt(mapCapexHeader.get("RequesterID").toString());
    }

    public String getRequesterName(){
        return mapCapexHeader.get("RequesterName").toString();
    }

    public int getStatusID(){
        return Integer.parseInt(mapCapexHeader.get("StatusID").toString());
    }

    public String getStatusName(){
        return mapCapexHeader.get("StatusName").toString();
    }

    public float getTotal(){
        return Float.parseFloat(mapCapexHeader.get("Total").toString());
    }

    public float getTotalAmountInUSD(){
        return Float.parseFloat(mapCapexHeader.get("TotalAmountInUSD").toString());
    }

    public ArrayList<HashMap<String, Object>> getDocuments(){ return mapDocuments; }

    public String getJSONFromUpdatingCapex(int statusID, String keyForUpdatableDate, SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.putAll(mapCapexHeader);

        tempMap.put("StatusID", statusID);
        tempMap.put("DateCreated", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateCreated").toString())));
        tempMap.put("DateProcessedByRegionalManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByRegionalManager").toString())));
        tempMap.put("DateSubmitted", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateSubmitted").toString())));
        tempMap.put("DateProcessedByCountryManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByCountryManager").toString())));
        tempMap.put("CapexLineItems", app.gson.fromJson(tempMap.get("CapexLineItems").toString(), app.types.arrayListOfHashmapOfStringObject));
        tempMap.put("Documents", app.gson.fromJson(tempMap.get("Documents").toString(), app.types.arrayListOfHashmapOfStringObject));

        if(!keyForUpdatableDate.equals("NA"))
            tempMap.put(keyForUpdatableDate, app.onlineGateway.jsonizeDate(new Date()));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

    public String jsonize(SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.putAll(mapCapexHeader);

        tempMap.put("DateCreated", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateCreated").toString())));
        tempMap.put("DateProcessedByRegionalManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByRegionalManager").toString())));
        tempMap.put("DateSubmitted", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateSubmitted").toString())));
        tempMap.put("DateProcessedByCountryManager", app.onlineGateway.jsonizeDate(app.dateFormatDefault.parse(tempMap.get("DateProcessedByCountryManager").toString())));
        tempMap.put("CapexLineItems", app.gson.fromJson(tempMap.get("CapexLineItems").toString(), app.types.arrayListOfHashmapOfStringObject));
        tempMap.put("Documents", app.gson.fromJson(tempMap.get("Documents").toString(), app.types.arrayListOfHashmapOfStringObject));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

}