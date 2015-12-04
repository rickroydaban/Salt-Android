package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexHeader implements Serializable{
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

    private boolean isActive;
    private String attachedCER;
    private int capexID;
    private ArrayList<CapexLineItem> capexLineItems;
    private String capexNumber;
    private int costCenterID;
    private String costCenterName;
    private int cmID;
    private String cmEmail, cmName;
    private int createdBy;
    private String dateCreated, dateProcessedByCM, dateProcessedByRM, dateSubmitted;
    private int departmentID;
    private String departmentName;
    private String docName;
    private ArrayList<Document> documents;
    private int investmentTypeID;
    private String investmentTypeName;
    private int lastModifiedBy;
    private String note;
    private int officeID;
    private String officeName;
    private int rmID;
    private String rmEmail, rmName, requesterEmail;
    private int requesterID;
    private String requesterName;
    private int statusID;
    private String statusName;
    private float total, totalAmountInUSD;

    public CapexHeader(JSONObject jsonCapexHeader) throws Exception{
        isActive = jsonCapexHeader.getBoolean("Active");
        attachedCER = jsonCapexHeader.getString("AttachedCER");
        capexID = jsonCapexHeader.getInt("CapexID");
        capexNumber = jsonCapexHeader.getString("CapexNumber");
        costCenterID = jsonCapexHeader.getInt("CostCenterID");
        costCenterName = jsonCapexHeader.getString("CostCenterName");
        cmID = jsonCapexHeader.getInt("CountryManager");
        cmEmail = jsonCapexHeader.getString("CountryManagerEmail");
        cmName = jsonCapexHeader.getString("CountryManagerName");
        createdBy = jsonCapexHeader.getInt("CreatedBy");
        dateCreated = jsonCapexHeader.getString("DateCreated");
        dateProcessedByCM = jsonCapexHeader.getString("DateProcessedByCountryManager");
        dateProcessedByRM = jsonCapexHeader.getString("DateProcessedByRegionalManager");
        dateSubmitted = jsonCapexHeader.getString("DateSubmitted");
        departmentID = jsonCapexHeader.getInt("DepartmentID");
        departmentName = jsonCapexHeader.getString("DepartmentName");
        docName = jsonCapexHeader.getString("DocName");
        investmentTypeID = jsonCapexHeader.getInt("InvestmentTypeID");
        investmentTypeName = jsonCapexHeader.getString("InvestmentTypeName");
        lastModifiedBy = jsonCapexHeader.getInt("LastModifiedBy");
        note = jsonCapexHeader.getString("Note");
        officeID = jsonCapexHeader.getInt("OfficeID");
        officeName = jsonCapexHeader.getString("OfficeName");
        rmID = jsonCapexHeader.getInt("RegionalManager");
        rmEmail = jsonCapexHeader.getString("RegionalManagerEmail");
        rmName = jsonCapexHeader.getString("RegionalManagerName");
        requesterEmail = jsonCapexHeader.getString("RequesterEmail");
        requesterID = jsonCapexHeader.getInt("RequesterID");
        requesterName = jsonCapexHeader.getString("RequesterName");
        statusID = jsonCapexHeader.getInt("StatusID");
        statusName = jsonCapexHeader.getString("StatusName");
        total = (float)jsonCapexHeader.getDouble("Total");
        totalAmountInUSD = (float)jsonCapexHeader.getDouble("TotalAmountInUSD");

        JSONArray jsonLineItems = jsonCapexHeader.getJSONArray("CapexLineItems");
        capexLineItems = new ArrayList<CapexLineItem>();
        for(int i=0; i<jsonLineItems.length(); i++)
            capexLineItems.add(new CapexLineItem(jsonLineItems.getJSONObject(i)));

        JSONArray jsonDocs = jsonCapexHeader.getJSONArray("Documents");
        documents = new ArrayList<Document>();
        for(int i=0; i<jsonDocs.length(); i++)
            documents.add(new Document(jsonDocs.getJSONObject(i)));
    }

    public String getAttachedCer(){ return (attachedCER == null || attachedCER.length()<1 || attachedCER.equals(""))?NOATTACHMENT:attachedCER; }
    public int getCapexID(){ return capexID; }
    public String getCapexNumber(){ return capexNumber; }
    public int getCostCenterID(){ return costCenterID; }
    public String getCostCenterName(){ return costCenterName; }
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
    public String getDateSubmitted(SaltApplication app){
        String tempDateSubmitted = app.onlineGateway.dJsonizeDate(dateSubmitted);
        return (tempDateSubmitted.contains("-Jan-1900"))?"-":tempDateSubmitted;
    }
    public int getDepartmentID(){ return departmentID; }
    public String getDepartmentName(){ return (departmentName.equals("null"))?"-":departmentName; }
    public int getInvestmentTypeID(){ return investmentTypeID; }
    public String getInvestmentTypeName(){ return (departmentName.equals("null"))?"-":investmentTypeName; }
    public String getNotes(){ return note; }
    public int getOfficeID(){ return officeID; }
    public String getOfficeName(){ return officeName; }
    public int getRMID(){ return rmID; }
    public String getRMEmail(){ return rmEmail; }
    public String getRMname(){
        return rmName;
    }
    public String getRequesterEmail(){ return requesterEmail; }
    public int getRequesterID(){ return requesterID; }
    public String getRequesterName(){ return requesterName; }
    public int getStatusID(){
        return statusID;
    }
    public String getStatusName(){
        return statusName;
    }
    public float getTotal(){ return total; }
    public float getTotalAmountInUSD(){ return totalAmountInUSD;}
    public ArrayList<Document> getDocuments(){ return documents; }

    public String getJSONFromUpdatingCapex(int statusID, String keyForUpdatableDate, SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("Active", isActive);
        tempMap.put("AttachedCER", attachedCER);
        tempMap.put("CapexID", capexID);
        tempMap.put("CapexNumber", capexNumber);
        tempMap.put("CostCenterID", costCenterID);
        tempMap.put("CostCenterName", costCenterName);
        tempMap.put("CountryManager", cmID);
        tempMap.put("CountryManagerEmail", cmEmail);
        tempMap.put("CountryManagerName", cmName);
        tempMap.put("CreatedBy", createdBy);
        tempMap.put("DateCreated", dateCreated);
        tempMap.put("DateProcessedByCountryManager", dateProcessedByCM);
        tempMap.put("DateProcessedByRegionalManager", dateProcessedByCM);
        tempMap.put("DateSubmitted", dateSubmitted);
        tempMap.put("DepartmentID", departmentID);
        tempMap.put("DepartmentName", departmentName);
        tempMap.put("DocName", docName);
        tempMap.put("InvestmentTypeID", investmentTypeID);
        tempMap.put("InvestmentTypeName", investmentTypeName);
        tempMap.put("LastModifiedBy", lastModifiedBy);
        tempMap.put("Note", note);
        tempMap.put("OfficeID", officeID);
        tempMap.put("OfficeName", officeName);
        tempMap.put("RegionalManager", rmID);
        tempMap.put("RegionalManagerEmail", rmEmail);
        tempMap.put("RegionalManagerName", rmName);
        tempMap.put("RequesterEmail", requesterEmail) ;
        tempMap.put("RequesterID", requesterID);
        tempMap.put("RequesterName", requesterName);
        tempMap.put("StatusID", statusID);
        tempMap.put("StatusName", statusName);
        tempMap.put("Total", total);
        tempMap.put("TotalAmountInUSD", totalAmountInUSD);
        tempMap.put("CapexLineItems", new JSONArray());

        if(!keyForUpdatableDate.equals("NA"))
            tempMap.put(keyForUpdatableDate, app.onlineGateway.jsonizeDate(new Date()));

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

    public String jsonize(SaltApplication app) throws Exception{
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("Active", isActive);
        tempMap.put("AttachedCER", attachedCER);
        tempMap.put("CapexID", capexID);
        tempMap.put("CapexNumber", capexNumber);
        tempMap.put("CostCenterID", costCenterID);
        tempMap.put("CostCenterName", costCenterName);
        tempMap.put("CountryManager", cmID);
        tempMap.put("CountryManagerEmail", cmEmail);
        tempMap.put("CountryManagerName", cmName);
        tempMap.put("CreatedBy", createdBy);
        tempMap.put("DateCreated", dateCreated);
        tempMap.put("DateProcessedByCountryManager", dateProcessedByCM);
        tempMap.put("DateProcessedByRegionalManager", dateProcessedByCM);
        tempMap.put("DateSubmitted", dateSubmitted);
        tempMap.put("DepartmentID", departmentID);
        tempMap.put("DepartmentName", departmentName);
        tempMap.put("DocName", docName);
        tempMap.put("InvestmentTypeID", investmentTypeID);
        tempMap.put("InvestmentTypeName", investmentTypeName);
        tempMap.put("LastModifiedBy", lastModifiedBy);
        tempMap.put("Note", note);
        tempMap.put("OfficeID", officeID);
        tempMap.put("OfficeName", officeName);
        tempMap.put("RegionalManager", rmID);
        tempMap.put("RegionalManagerEmail", rmEmail);
        tempMap.put("RegionalManagerName", rmName);
        tempMap.put("RequesterEmail", requesterEmail) ;
        tempMap.put("RequesterID", requesterID);
        tempMap.put("RequesterName", requesterName);
        tempMap.put("StatusID", statusID);
        tempMap.put("StatusName", statusName);
        tempMap.put("Total", total);
        tempMap.put("TotalAmountInUSD", totalAmountInUSD);
        tempMap.put("CapexLineItems", new JSONArray());

        return app.gson.toJson(tempMap, app.types.hashmapOfStringObject);
    }

}