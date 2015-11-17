package applusvelosi.projects.android.salt.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.parse.ParseObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.CostCenter;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.LocalHoliday;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.models.capex.CapexLineItemQoutation;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.LiquidationOfBA;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.models.claimitems.MilageClaimItem;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.utils.enums.ObjectTypes;
import applusvelosi.projects.android.salt.views.LoginActivity;

public class OnlineGateway {
	private final String AUTHKEY = "ak0ayMa+apAn6";
	public final String apiUrl;
	public SaltApplication app;
	
	public OnlineGateway(SaltApplication app){
		this.apiUrl = "https://salttestapi.velosi.com/SALTService.svc/";
//		this.apiUrl = "https://saltapi.velosi.com/SALTService.svc/";
		this.app = app;
	}

//TODO UTILITY METHODS
	private String getHttpGetResult(String url) throws Exception {
		System.out.println("GET URL: " + url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGETRequest = new HttpGet(url);
		httpGETRequest.setHeader("AuthKey", AUTHKEY);
	    HttpResponse response = httpclient.execute(httpGETRequest);
	    System.gc();
	    return EntityUtils.toString(response.getEntity());		    
	}	
	
	private String getHttpPostResult(String url, String postBodyJSON) throws Exception{
		System.out.println("POST URL: "+url);
		System.out.println(postBodyJSON);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPostRequest = new HttpPost(url);
		httpPostRequest.setHeader("Content-Type", "application/json");
		httpPostRequest.setHeader("Accept", "application/json");
		httpPostRequest.setHeader("AuthKey", AUTHKEY);
		httpPostRequest.setEntity(new StringEntity(postBodyJSON));
		
	    HttpResponse response = httpClient.execute(httpPostRequest);
	    String ret = EntityUtils.toString(response.getEntity());
//	    System.out.println("Return POST");
//	    System.out.println(ret);
	    return ret;			
	}

	public String getURLEncodedString(String string) throws Exception{
		return URLEncoder.encode(string, "UTF-8");
	}
	
	public String now() throws Exception{
		return getURLEncodedString(app.dateTimeFormat.format(app.calendar.getTime()));
	}

	public String getDefaultDate() throws Exception{
		return getURLEncodedString("01-01-1900");		
	}
	
	//converts epoch date format returned by web services to the readable default date format
	public String dJsonizeDate(String d){
		int idx1 = d.indexOf("(");
	    int idx2 = d.indexOf(")") - 5;
	    String s = d.substring(idx1+1, idx2);

//	    return app.dateFormatDefault.format(new Date(Long.valueOf(s) + SaltApplication.ONEDAY));
		return app.dateFormatDefault.format(new Date(Long.valueOf(s)));
	}

	public String jsonizeDate(Date date) {
		return "/Date("+date.getTime()+"+0000)/";
	}

	//TODO STAFF METHODS
	public String authentication(String username, String password) throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"AuthenticateLogin?userName="+username+"&password="+password+"&datetime="+now())).getJSONObject("AuthenticateLoginResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		else 
			return result.getInt("StaffID")+"-"+result.getInt("SecurityLevel")+"-"+result.getInt("OfficeID");
	}
	
	public void updateStaff(int staffID, int securityLevel, int officeID) throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetStaffByID?staffID="+staffID+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetStaffByIDResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			throw new Exception(result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message"));
		
		JSONObject officeResult = new JSONObject(getHttpGetResult(apiUrl+"GetOfficeByID?officeID="+officeID+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetOfficeByIDResult");
		if(officeResult.getJSONArray("SystemErrors").length() > 0)
			throw new Exception(officeResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message"));
		
		app.setStaffData(this, new Staff(this, result.getJSONObject("Staff")), new Office(officeResult.getJSONObject("Office"), true));
	}

//TODO LEAVE METHODS
	public Object getMyLeaves() throws Exception{
		try{
			int staffID = app.getStaff().getStaffID();
			String filter = getURLEncodedString("WHERE year(DateFrom)=Year(getdate()) AND StaffID=" + staffID + " ORDER BY DateSubmitted DESC");
			JSONObject myLeaveResult = new JSONObject(getHttpGetResult(apiUrl+"GetAllLeave?filter="+filter+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetAllLeaveResult");

			if(myLeaveResult.getJSONArray("SystemErrors").length() > 0)
				return myLeaveResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
			
			ArrayList<Leave> myLeaves = new ArrayList<Leave>();
			JSONArray jsonMyLeaves = myLeaveResult.getJSONArray("Leaves");
			for(int i=0; i<jsonMyLeaves.length(); i++)
				myLeaves.add(new Leave(jsonMyLeaves.getJSONObject(i), this));
			
			return myLeaves;
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public Object getMyPendingAndApprovedLeaves() throws Exception{
		try{
			int staffID = app.getStaff().getStaffID();
			String filter = getURLEncodedString("WHERE year(DateFrom)=Year(getdate()) AND StaffID=" + staffID + " AND ((LeaveStatus=18)OR(LeaveStatus=20)) ORDER BY DateSubmitted DESC");
			JSONObject myLeaveResult = new JSONObject(getHttpGetResult(apiUrl+"GetAllLeave?filter="+filter+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetAllLeaveResult");

			if(myLeaveResult.getJSONArray("SystemErrors").length() > 0)
				return myLeaveResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			ArrayList<Leave> myLeaves = new ArrayList<Leave>();
			JSONArray jsonMyLeaves = myLeaveResult.getJSONArray("Leaves");
			for(int i=0; i<jsonMyLeaves.length(); i++)
				myLeaves.add(new Leave(jsonMyLeaves.getJSONObject(i), this));

			return myLeaves;
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public Object getLeavesForApproval() throws Exception{
		JSONObject leavesForApprovalResult;
		try{
			int staffID = app.getStaff().getStaffID();
			String filter = getURLEncodedString("WHERE year(DateSubmitted)=Year(getdate()) AND LeaveApprover1=" + staffID + " ORDER BY DateSubmitted DESC");
			leavesForApprovalResult = new JSONObject(getHttpGetResult(apiUrl+"GetAllLeave?filter="+filter+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetAllLeaveResult");
			
			if(leavesForApprovalResult.getJSONArray("SystemErrors").length() > 0)
				return leavesForApprovalResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
			
			ArrayList<Leave> leavesForApproval = new ArrayList<Leave>();
			JSONArray jsonLeaves = leavesForApprovalResult.getJSONArray("Leaves");
			for(int i=0; i<jsonLeaves.length(); i++)
				leavesForApproval.add(new Leave(jsonLeaves.getJSONObject(i), this));				
			
			return leavesForApproval;
		}catch(Exception e){
			return e.getMessage();
		}	
	}
		
//TODO CLAIMS METHODS	
	public Object getMyClaims() throws Exception{
		JSONObject staffClaimResult = new JSONObject(getHttpGetResult(apiUrl+"GetClaimsByStaff?staffID="+app.getStaff().getStaffID()+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetClaimsByStaffResult");
		if(staffClaimResult.getJSONArray("SystemErrors").length() > 0)
			return staffClaimResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		ArrayList<ClaimHeader> myClaims = new ArrayList<ClaimHeader>();		
		JSONArray jsonClaims = staffClaimResult.getJSONArray("Claims");
		for(int i=0; i<jsonClaims.length(); i++){
			JSONObject jsonClaim = jsonClaims.getJSONObject(i);
			int type = jsonClaim.getInt(ClaimHeader.KEY_TYPEID);
			if(type == ClaimHeader.TYPEKEY_CLAIMS)
				myClaims.add((jsonClaim.getBoolean(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD))?new ClaimPaidByCC(jsonClaim, app):new ClaimNotPaidByCC(jsonClaim, app));
			else if(type == ClaimHeader.TYPEKEY_ADVANCES)
				myClaims.add(new BusinessAdvance(jsonClaim, app));
			else if(type == ClaimHeader.TYPEKEY_LIQUIDATION)
				myClaims.add(new LiquidationOfBA(jsonClaim, app));
			else
				throw new Exception("Invalid Claim Type");
		}
		
		return myClaims;
	}
	
	public Object getClaimsForApprovalAndPayment() throws Exception{
		HashMap<String, ArrayList<ClaimHeader>> approvalMaps = new HashMap<String, ArrayList<ClaimHeader>>();
		JSONObject claimsForApprovalResult;
		try{
			//choose leave retrieve type
			if(app.getStaff().isAdmin() || app.getStaff().isCorporateApprover() || app.getStaff().isRegional())
				claimsForApprovalResult = new JSONObject(getHttpGetResult(apiUrl+"GetAllClaims?requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetAllClaimsResult");
			else if(!app.getStaff().isCorporateApprover() && (app.getStaff().isCM() || app.getStaff().isAM()))
				claimsForApprovalResult = new JSONObject(getHttpGetResult(apiUrl+"GetClaimsByOffice?officeID="+app.getStaff().getOfficeID()+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetLeaveByOfficeResult");
			else { //return an empty list for approvals and payments
				approvalMaps.put("ForApproval", new ArrayList<ClaimHeader>());
				approvalMaps.put("ForPayment", new ArrayList<ClaimHeader>());
				return approvalMaps;
			}
			
			if(claimsForApprovalResult.getJSONArray("SystemErrors").length() > 0)
				return claimsForApprovalResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
			
			ArrayList<ClaimHeader> claimsForApproval = new ArrayList<ClaimHeader>();
			ArrayList<ClaimHeader> claimsForPayment = new ArrayList<ClaimHeader>();
			JSONArray jsonClaims = claimsForApprovalResult.getJSONArray("Claims");
			for(int i=0; i<jsonClaims.length(); i++){
				JSONObject jsonClaim = jsonClaims.getJSONObject(i);
				int type = jsonClaim.getInt(ClaimHeader.KEY_TYPEID);
				int status = jsonClaim.getInt("ClaimStatus");
				//for payment
				if(app.getStaff().isAdmin() || (app.getStaff().getStaffID()==jsonClaim.getInt("AccountsID")&&status==ClaimHeader.STATUSKEY_APPROVEDBYACCOUNTS)){

					if(type == ClaimHeader.TYPEKEY_CLAIMS)
						claimsForApproval.add((jsonClaim.getBoolean(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD))?new ClaimPaidByCC(jsonClaim, app):new ClaimNotPaidByCC(jsonClaim, app));
					else if(type == ClaimHeader.TYPEKEY_ADVANCES)
						claimsForApproval.add(new BusinessAdvance(jsonClaim, app));
					else if(type == ClaimHeader.TYPEKEY_LIQUIDATION)
						claimsForApproval.add(new LiquidationOfBA(jsonClaim, app));
					else
						throw new Exception("Invalid Claim Type");					
					
				}else if( app.getStaff().isAdmin() || 
						  jsonClaim.getInt("ApproverID")==app.getStaff().getStaffID() ||
						  (app.getStaff().isCM()&&app.getStaff().getOfficeID()==jsonClaim.getInt("OfficeID")&&status==ClaimHeader.STATUSKEY_APPROVEDBYAPPROVER&&type==ClaimHeader.TYPEKEY_ADVANCES) ||
						  (jsonClaim.getInt("AccountsID")==app.getStaff().getStaffID()&&(status==ClaimHeader.STATUSKEY_APPROVEDBYAPPROVER||status==ClaimHeader.STATUSKEY_APPROVEDBYCOUNTRYMANAGER))){

					if(type == ClaimHeader.TYPEKEY_CLAIMS)
						claimsForApproval.add((jsonClaim.getBoolean(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD))?new ClaimPaidByCC(jsonClaim, app):new ClaimNotPaidByCC(jsonClaim, app));
					else if(type == ClaimHeader.TYPEKEY_ADVANCES)
						claimsForApproval.add(new BusinessAdvance(jsonClaim, app));
					else if(type == ClaimHeader.TYPEKEY_LIQUIDATION)
						claimsForApproval.add(new LiquidationOfBA(jsonClaim, app));
					else
						throw new Exception("Invalid Claim Type");					
					
				}
			}
			
			approvalMaps.put("ForApproval", claimsForApproval);
			approvalMaps.put("ForPayment", claimsForPayment);
			return approvalMaps;
		}catch(Exception e){
			return e.getMessage();
		}	
	}

	public Object getCapexesForApproval() throws Exception{
		try{
			int staffID = app.getStaff().getStaffID();
			StringBuilder filter = new StringBuilder();

			if(app.getStaff().isCM()){
				if(app.getStaff().isCorporateApprover()){
					if(app.getStaff().getUserPosition()==Staff.USERPOSITION.USERPOSITION_CFO || app.getStaff().getUserPosition()==Staff.USERPOSITION.USERPOSITION_CEO) {
						int statusID = (app.getStaff().getUserPosition() == Staff.USERPOSITION.USERPOSITION_CFO) ? CapexHeader.CAPEXHEADERID_APPROVEDBYRM : CapexHeader.CAPEXHEADERID_APPROVEDBYCFO;
						filter.append("WHERE ((CountryManager=" + staffID + " AND StatusID=" + CapexHeader.CAPEXHEADERID_SUBMITTED + ")OR(RegionalManager=" + staffID + " AND StatusID=" + CapexHeader.CAPEXHEADERID_APPROVEDBYCM + "))OR(TotalAmountInUSD>3000 AND StatusID="+statusID+")");
					}else
						filter.append("WHERE (CountryManager="+staffID+" AND StatusID="+CapexHeader.CAPEXHEADERID_SUBMITTED+")OR(RegionalManager="+staffID+" AND StatusID="+CapexHeader.CAPEXHEADERID_APPROVEDBYCM+")");
				}else
					filter.append("WHERE CountryManager="+staffID+" AND StatusID="+ CapexHeader.CAPEXHEADERID_SUBMITTED);
			}else
				filter.append("WHERE CapexID=0");

			filter.append(" ORDER BY DateCreated DESC");
			String encodedFilter = getURLEncodedString(filter.toString());
			JSONObject capexForApprovalResult = new JSONObject(getHttpGetResult(apiUrl+"GetFilterCapex?filter="+encodedFilter+"&requestingPerson="+staffID+"&datetime="+now())).getJSONObject("GetFilterCapexResult");

			if(capexForApprovalResult.getJSONArray("SystemErrors").length() > 0)
				return capexForApprovalResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			ArrayList<CapexHeader> capexesForApproval = new ArrayList<CapexHeader>();
			JSONArray jsonCapexesForApproval = capexForApprovalResult.getJSONArray("Capexes");
			for(int i=0; i<jsonCapexesForApproval.length(); i++)
				capexesForApproval.add(new CapexHeader(jsonCapexesForApproval.getJSONObject(i), this));

			return capexesForApproval;
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public Object getRecruitmentsForApproval() throws Exception{
		try{
			int staffID = app.getStaff().getStaffID();
			StringBuilder filter = new StringBuilder();

			if(app.getStaff().isCM()){
				if(app.getStaff().isCorporateApprover()){
					if(app.getStaff().getUserPosition() == Staff.USERPOSITION.USERPOSITION_CEO) {
						filter.append("WHERE StatusID=" + Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR + "OR((CountryManager=" + app.getStaff().getStaffID() + " AND StatusID=" + Recruitment.RECRUITMENT_STATUSID_SUBMITTED + ")OR(RegionalManager=" + app.getStaff().getStaffID() + " AND StatusID=" + Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM + "))");
					}else
						filter.append("WHERE (CountryManager="+app.getStaff().getStaffID()+" AND StatusID="+Recruitment.RECRUITMENT_STATUSID_SUBMITTED+")OR(RegionalManager="+app.getStaff().getStaffID()+" AND StatusID="+Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM+")");
				}else
					filter.append("WHERE CountryManager="+app.getStaff().getStaffID()+" AND StatusID="+Recruitment.RECRUITMENT_STATUSID_SUBMITTED);
			}else if(app.getStaff().getStaffID() == SaltApplication.MAINHRID){
				filter.append("WHERE StatusID="+Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM);
			}else{
				filter.append("WHERE RecruitmentRequestID=0");
			}

			filter.append(" ORDER BY DateRequested DESC");
			String encodedFilter = getURLEncodedString(filter.toString());
			JSONObject recruitmentForApprovalResult = new JSONObject(getHttpGetResult(apiUrl+"GetFilterRecruitment?filter="+encodedFilter+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetFilterRecruitmentResult");

			if(recruitmentForApprovalResult.getJSONArray("SystemErrors").length() > 0)
				return recruitmentForApprovalResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			ArrayList<Recruitment> recruitmentsForApproval = new ArrayList<Recruitment>();
			JSONArray jsonRecruitmentsForApproval = recruitmentForApprovalResult.getJSONArray("RecruitmentRequests");
			for(int i=0; i<jsonRecruitmentsForApproval.length(); i++) {
				recruitmentsForApproval.add(new Recruitment(jsonRecruitmentsForApproval.getJSONObject(i), this));
			}

			return recruitmentsForApproval;
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public Object getCapexLineItems(int capexID) throws Exception{
		try{
			JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetCapexLineItems?CapexID="+capexID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetCapexLineItemsResult");
			if(result.getJSONArray("SystemErrors").length() > 0)
				return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			ArrayList<CapexLineItem> capexLineItems = new ArrayList<CapexLineItem>();
			JSONArray jsonCapexLineItems = result.getJSONArray("CapexLineItems");
			for(int i=0; i<jsonCapexLineItems.length(); i++)
				capexLineItems.add(new CapexLineItem(jsonCapexLineItems.getJSONObject(i), this));

			return capexLineItems;
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public Object getCapexLineItemQoutations(int capexLineItemID) throws Exception {
		try {
			JSONObject result = new JSONObject((getHttpGetResult(apiUrl + "GetCapexLinteItemQuotation?capexLineItemID="+capexLineItemID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now()))).getJSONObject("GetCapexLinteItemQuotationResult");
			if(result.getJSONArray("SystemErrors").length() > 0)
				return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			ArrayList<CapexLineItemQoutation> capexLineItemQoutations = new ArrayList<CapexLineItemQoutation>();
			JSONArray jsonCapexLineItemQoutations = result.getJSONArray("CapexLineItemQuotations");
			for(int i=0; i<jsonCapexLineItemQoutations.length(); i++)
				capexLineItemQoutations.add(new CapexLineItemQoutation(jsonCapexLineItemQoutations.getJSONObject(i), this));

			return capexLineItemQoutations;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public Object getRecruitmentDetail(int recruitmentID) throws Exception{
		try{
			JSONObject result = new JSONObject((getHttpGetResult(apiUrl+"GetRecruitmentByID?RecruitmentID="+recruitmentID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now()))).getJSONObject("GetRecruitmentByIDResult");
			if(result.getJSONArray("SystemErrors").length() > 0)
				return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			return result.getJSONArray("RecruitmentRequests").get(0);
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public Object getCapexHeaderDetail(int capexHeaderID) throws Exception{
		try{
			JSONObject result = new JSONObject((getHttpGetResult(apiUrl+"GetByCapexID?CapexID="+capexHeaderID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now()))).getJSONObject("GetByCapexIDResult");
			if(result.getJSONArray("SystemErrors").length() > 0)
				return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

			return result.getJSONObject("Capex");
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public Object getClaimItemsWithClaimID(int claimID) throws Exception{
		JSONObject claimItemResult = new JSONObject(getHttpGetResult(apiUrl+"GetClaimLineItems?claimID="+claimID+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetClaimLineItemsResult");
		if(claimItemResult.getJSONArray("SystemErrors").length() > 0)
			return claimItemResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<ClaimItem> claimItems = new ArrayList<ClaimItem>();
		JSONArray jsonClaimItems = claimItemResult.getJSONArray("ClaimLineItems");
		for(int i=0; i<jsonClaimItems.length(); i++){
			JSONObject jsonClaimItem = jsonClaimItems.getJSONObject(i);
			JSONObject resultCategory = new JSONObject(getHttpGetResult(apiUrl+"GetByCategoryID?categoryID="+jsonClaimItem.getString(ClaimItem.KEY_CATEGORYID)+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetByCategoryIDResult");
			if(resultCategory.getJSONArray("SystemErrors").length() > 0)
				return resultCategory.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
			
			JSONObject jsonCategory = resultCategory.getJSONObject("Category");
			int categoryType = Integer.parseInt(jsonCategory.getString(ClaimItem.KEY_CATEGORYTYPEID));
			if(categoryType != Category.TYPE_ASSET){ //exclude claim items of type asset
				if(categoryType == Category.TYPE_MILEAGE) //of category type mileage
					claimItems.add(new MilageClaimItem(jsonClaimItem, jsonCategory,  app));
				else
					claimItems.add(new ClaimItem(jsonClaimItem, jsonCategory, app));
			}
		}
		
		return claimItems;
	}
	
	public Object getClaimItemCategoryByOffice() throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetCategoryByOffice?officeID="+app.getStaff().getOfficeID()+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetCategoryByOfficeResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<Category> categories = new ArrayList<Category>();
		JSONArray jsonCategories = result.getJSONArray("Categories");
		for(int i=0; i<jsonCategories.length(); i++){ //exclude of type assets since they are only for capex
			JSONObject jsonCategory = jsonCategories.getJSONObject(i);
			if(jsonCategory.getInt(ClaimItem.KEY_CATEGORYTYPEID) != Category.TYPE_ASSET)
				categories.add(new Category(jsonCategory));
		}
		
		return categories;
	}

	public Object getCostCentersByOfficeID() throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetCostCenterByOFfice?officeID="+app.getStaff().getOfficeID()+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetCostCenterByOfficeResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<CostCenter> costCenters = new ArrayList<CostCenter>();
		JSONArray jsonCostCenters = result.getJSONArray("CostCenters");
		for(int i = 0; i<jsonCostCenters.length(); i++){
			JSONObject jsonCostCenter = jsonCostCenters.getJSONObject(i);
			costCenters.add(new CostCenter(jsonCostCenter.getInt("CostCenterID"), jsonCostCenter.getString("Description")));
		}

		return costCenters;
	}

	public Object getClaimItemProjectsByCostCenter(int costCenterID) throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetProjectByCostCenter?costCenterID="+costCenterID+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetProjectByCostCenterResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<Project> projects = new ArrayList<Project>();
		JSONArray jsonProjects = result.getJSONArray("Projects");
		for(int i=0; i<jsonProjects.length(); i++){ //exclude of type assets since they are only for capex
			JSONObject jsonProject = jsonProjects.getJSONObject(i);
			projects.add(new Project(jsonProject.getInt("ProjectID"), jsonProject.getString("ProjectName")));
		}
		
		return projects;
	}
	
	public Object getAllOffices() throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetAllOffice?requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetAllOfficeResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<Office> offices = new ArrayList<Office>();
		JSONArray jsonOffices = result.getJSONArray("Offices");
		for(int i=0; i<jsonOffices.length(); i++)
			offices.add(new Office(jsonOffices.getJSONObject(i), false));
		
		return offices;		
	}

	public Object getCurrencies() throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetAllCurrencies?requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetAllCurrenciesResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ArrayList<Currency> currencies = new ArrayList<Currency>();
		JSONArray jsonCurrencies = result.getJSONArray("Currencies");
		for(int i=0; i<jsonCurrencies.length(); i++)
			currencies.add(new Currency(jsonCurrencies.getJSONObject(i)));

		return currencies;
	}


	public Object getForexRate(String currFrom, String currTo) throws Exception{
		String html = getHttpGetResult("http://www.google.com/finance/converter?a=1&from=" + currFrom + "&to=" + currTo);
		int parentDivStartPos = html.indexOf("<div id=currency_converter_result>"); 
		int parentDivEndPos = html.indexOf("<input type=submit value=\"Convert\">\n</div>", parentDivStartPos);
		if(parentDivStartPos!=-1 && parentDivEndPos!=-1){
			String parentDivSubstring = html.substring(parentDivStartPos, parentDivEndPos);
			//check if div has a child, if it does not have then there is something wrong with the parameters
			if(!parentDivSubstring.contains("><input")){
				//get the span that contains the text of the rate
				int spanStartPos = parentDivSubstring.indexOf("<span class=bld>");
				int spanEndPos = parentDivSubstring.indexOf("</span>");				
				if(spanStartPos!=-1 && spanEndPos!=-1){
					StringBuilder tempSpanSubstring = new StringBuilder(parentDivSubstring.substring(spanStartPos, spanEndPos));
					return Float.parseFloat(tempSpanSubstring.substring(tempSpanSubstring.indexOf(">")+1, tempSpanSubstring.length()).split(" ")[0]);
				}else
					return "HTML Parsing error. Target source code of container span for rate string might have been changed. Please contact developer";				
			}else
				return "Currency Not Found";
		}else
			return "HTML Parsing error. Target source code of container div for rate string might have been changed. Please contact developer";
	}
	
	public String saveClaim(String newClaimJson, String oldClaimJson) throws Exception{		
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"newClaim\":"+newClaimJson);
		sb.append(",\"oldClaim\":"+oldClaimJson);
		sb.append(",\"requestingPerson\":"+String.valueOf(app.getStaff().getStaffID()));
		sb.append(",\"datetime\":\""+app.dateTimeFormat.format(app.calendar.getTime())+"\"");
		sb.append("}");
		JSONObject result = new JSONObject(getHttpPostResult(apiUrl+"SaveClaim", sb.toString())).getJSONObject("SaveClaimResult");
		System.out.println("Processed "+result.getInt("ProcessedClaimID"));
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		if(result.getInt("ProcessedClaimID") == -1)
			return "No Changes were made";


		ParseObject dataObject = new ParseObject("Requests");
		dataObject.put("requester", app.getStaff().getFname() + " " + app.getStaff().getLname());
		dataObject.put("type", "Claim");
		dataObject.saveInBackground();

		return null;
	}

	public String saveCapex(String newCapexHeaderJSON, String oldCapexHeaderJSON) throws Exception{
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"newCapex\":"+newCapexHeaderJSON);
		sb.append(",\"oldCapex\":"+oldCapexHeaderJSON);
		sb.append(",\"requestingPerson\":"+String.valueOf(app.getStaff().getStaffID()));
		sb.append(",\"datetime\":\""+app.dateTimeFormat.format(app.calendar.getTime())+"\"");
		sb.append("}");
		JSONObject result = new JSONObject(getHttpPostResult(apiUrl+"SaveCapex", sb.toString())).getJSONObject("SaveCapexResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		if(result.getInt("ProcessedCapexID") < 0)
			return "No changes were made";

		ParseObject dataObject = new ParseObject("Approvals");
		dataObject.put("approver", app.getStaff().getFname()+" "+app.getStaff().getLname());
		dataObject.put("type", "Capex");
		dataObject.saveInBackground();

		return "OK";
	}

	public String saveRecruitment(String newRecruitmentJSON, String oldRecruitmentJSON) throws Exception{
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"newRecruitment\":"+newRecruitmentJSON);
		sb.append(",\"oldRecruitment\":"+oldRecruitmentJSON);
		sb.append(",\"requestingPerson\":"+String.valueOf(app.getStaff().getStaffID()));
		sb.append(",\"datetime\":\""+app.dateTimeFormat.format(app.calendar.getTime())+"\"");
		sb.append("}");
		JSONObject result = new JSONObject(getHttpPostResult(apiUrl+"SaveRecruitment", sb.toString())).getJSONObject("SaveRecruitmentResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		if(result.getInt("ProcessedRecruitmentRequestID") < 0)
			return "No changes were made";

		ParseObject dataObject = new ParseObject("Approvals");
		dataObject.put("approver", app.getStaff().getFname()+" "+app.getStaff().getLname());
		dataObject.put("type", "Recruitment");
		dataObject.saveInBackground();

		return "OK";
	}

	public Object uploadAttachment(File file) throws Exception{
//		String documentJSON = new Document(file.getName(), file.length(), app.getStaff().getStaffID(), 0, ObjectTypes.LEAVE).jsonize(app);
		StringBuilder sb = new StringBuilder("{");
//		sb.append("\"document\":"+documentJSON);
//		sb.append("\"document\": \"test.jpg\"");
		sb.append("\"document\": 1");
		sb.append(",\"base64File\": [");

		String base64Encoded = encodeToBase64(file);
		int cSharpMaxStringLength = 30000;
		for(int ctr=0; ctr<base64Encoded.length(); ctr+=cSharpMaxStringLength) {
			sb.append("\"" + base64Encoded.substring(ctr, Math.min(ctr + cSharpMaxStringLength, base64Encoded.length())) + "\"");
			if(ctr+cSharpMaxStringLength < base64Encoded.length())
				sb.append(","+System.lineSeparator());
		}
		sb.append("]");

		sb.append(",\"requestingPerson\":"+String.valueOf(app.getStaff().getStaffID()));
		sb.append(",\"datetime\":\""+app.dateTimeFormat.format(app.calendar.getTime())+"\"");
		sb.append("}");

//		StringBuilder cSharbSB = new StringBuilder("string[] test = new string{");
//		String base64Encoded = encodeToBase64(file);
//		int cSharpMaxStringLength = 30000;
//		for(int ctr=0; ctr<base64Encoded.length(); ctr+=cSharpMaxStringLength) {
//			cSharbSB.append("\"" + base64Encoded.substring(ctr, Math.min(ctr + cSharpMaxStringLength, base64Encoded.length())) + "\"");
//			if(ctr+cSharpMaxStringLength < base64Encoded.length())
//				cSharbSB.append(","+System.lineSeparator());
//		}
//
//		cSharbSB.append("};");
		app.fileManager.saveToTextFile(sb.toString());
		System.out.println("Saved to text file!");
//		JSONObject result = new JSONObject(getHttpPostResult(apiUrl+"UploadFile", sb.toString())).getJSONObject("UploadFile");
		System.out.println("SALT testing");
		System.out.println("");
		System.out.println("");
//		System.out.println("SALT jsonobjectresult "+result);
		System.out.println("");
		System.out.println("result "+getHttpPostResult(apiUrl+"UploadFile", sb.toString()));
		System.out.println("");
//		System.out.println("test2");
//		JSONObject result = new JSONObject(httpPost(apiUrl+"UploadFile", sb.toString()));
//		System.out.println(result);
//		if(result.getJSONArray("SystemErrors").length() > 0)
//			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
//		return result;
		return "hey";
	}
	
	public Object saveClaimLineItem(String newClaimLineItemJSON, String oldClaimLineItemJSON) throws Exception{
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"newClaimLineItem\":"+newClaimLineItemJSON);
		sb.append(",\"oldClaimLineItem\":"+oldClaimLineItemJSON);
		sb.append(",\"requestingPerson\":"+String.valueOf(app.getStaff().getStaffID()));
		sb.append(",\"datetime\":\""+app.dateTimeFormat.format(app.calendar.getTime())+"\"");
		sb.append("}");
		JSONObject result = new JSONObject(getHttpPostResult(apiUrl+"SaveClaimLineItem", sb.toString())).getJSONObject("SaveClaimLineItemResult");
		System.out.println("result "+result);
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		return result;		
	}	
	
//TODO HOLIDAY METHODS
	public Object getAllHolidayArrayListOrErrorMessage(boolean shouldLoadFlag) throws Exception{
//		HashMap<String, Bitmap> flagBitmaps = new HashMap<String, Bitmap>();
		
		JSONObject holidayResult = new JSONObject(getHttpGetResult(apiUrl+"GetAllNationalHoliday?requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetAllNationalHolidayResult");
		if(holidayResult.getJSONArray("SystemErrors").length() > 0)
			return holidayResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		ArrayList<Holiday> nationalHolidays = new ArrayList<Holiday>();
		JSONArray holidayArray = holidayResult.getJSONArray("NationalHoliday");
		for(int i=0; i<holidayArray.length(); i++){			
			JSONObject jsonHoliday = holidayArray.getJSONObject(i);

			if(jsonHoliday.getBoolean("Active") == true){
				String dateStr = dJsonizeDate(jsonHoliday.getString("Date"));
				Holiday holiday = new Holiday(jsonHoliday.getString("Name"), jsonHoliday.getString("Country"), dateStr, app.dateFormatDefault.parse(dateStr));
//				String flagUrl = jsonHoliday.getString("ImageURL");
//				if(shouldLoadFlag){ //reuse bitmaps to greatly lessen loading time
//					if(!flagBitmaps.containsKey(flagUrl)){
//						try{
//							flagBitmaps.put(flagUrl, getFlag(flagUrl));
//						}catch(Exception e){
//							System.out.println(flagUrl);
//							flagBitmaps.put(flagUrl, null);
//							e.printStackTrace();
//						}
//					}
//					holiday.setFlag(flagBitmaps.get(flagUrl));
//				}else
//					holiday.setFlag(null);
				
				JSONArray holidayOffices = jsonHoliday.getJSONArray("OfficesApplied");
				for(int j=0; j<holidayOffices.length(); j++)
					holiday.addOffice(holidayOffices.getJSONObject(j).getInt("OfficeID"), holidayOffices.getJSONObject(j).getString("OfficeName"));				
				
				nationalHolidays.add(holiday);
			}				
		}
		
		return nationalHolidays;
	}
	
	public Object getOfficeHolidaysOrErrorMessage(int officeID) throws Exception{
		JSONObject holidayResult = new JSONObject(getHttpGetResult(apiUrl+"GetNationalHolidaysByOffice?officeID="+officeID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetNationalHolidaysByOfficeResult");
		if(holidayResult.getJSONArray("SystemErrors").length() > 0)
			return holidayResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		ArrayList<Holiday> officeHolidays = new ArrayList<Holiday>();
		JSONArray holidayArray = holidayResult.getJSONArray("NationalHolidays");
		for(int i=0; i<holidayArray.length(); i++){
			JSONObject jsonHoliday = holidayArray.getJSONObject(i);
			if(jsonHoliday.getBoolean("Active") == true){
				JSONArray offices = jsonHoliday.getJSONArray("OfficesApplied");
				for(int officeCTR=0; officeCTR<offices.length(); officeCTR++){
					if(offices.getJSONObject(officeCTR).getInt("OfficeID")==app.getStaff().getOfficeID()){
						String dateStr = dJsonizeDate(jsonHoliday.getString("Date"));
						officeHolidays.add(new Holiday(jsonHoliday.getString("Name"), jsonHoliday.getString("Country"), dateStr, app.dateFormatDefault.parse(dateStr)));
						break;
					}
				}
			}
		}
		
		return officeHolidays;
	}
	
	public Object getLocalHolidayArrayListOrErrorMessage() throws Exception{
		SimpleDateFormat formatDayName = new SimpleDateFormat("EEEE",Locale.UK);
		SimpleDateFormat formatMonthName = new SimpleDateFormat("LLLL",Locale.UK);
		JSONObject holidayResult = new JSONObject(getHttpGetResult(apiUrl+"GetNationalHolidaysByOffice?officeID="+app.getStaff().getOfficeID()+"&requestingperson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetNationalHolidaysByOfficeResult");
		if(holidayResult.getJSONArray("SystemErrors").length() > 0)
			return holidayResult.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		ArrayList<LocalHoliday> localHolidays = new ArrayList<LocalHoliday>();
		JSONArray holidayArray = holidayResult.getJSONArray("NationalHolidays");
		for(int i=0; i<holidayArray.length(); i++){			
			JSONObject jsonHoliday = holidayArray.getJSONObject(i);
			if(jsonHoliday.getBoolean("Active") == true){
				JSONArray offices = jsonHoliday.getJSONArray("OfficesApplied"); 
				for(int officeCTR=0; officeCTR<offices.length(); officeCTR++){ //not all offices within the nation might not observe some holiday so must check if the office observes the holiday or not
					if(offices.getJSONObject(officeCTR).getInt("OfficeID")==app.getStaff().getOfficeID()){
						String dateStr = dJsonizeDate(jsonHoliday.getString("Date"));
						if(Integer.parseInt(dateStr.split("-")[2]) == app.thisYear){
							Date date = app.dateFormatDefault.parse(dateStr);
							localHolidays.add(new LocalHoliday(jsonHoliday.getString("Name"), dateStr, formatDayName.format(date), formatMonthName.format(date)));
						} 				
						break;
					}
				}

			}				
		}
		
		return localHolidays;		
	}
		
	public String saveLeave(String newLeaveJson, String oldLeaveJson) throws Exception{		
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"SaveLeave?newLeave="+newLeaveJson+"&oldLeave="+oldLeaveJson+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("SaveLeaveResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		ParseObject dataObject = new ParseObject("Requests");
		dataObject.put("requester", app.getStaff().getFname() + " " + app.getStaff().getLname());
		dataObject.put("type", "Leave");
		dataObject.saveInBackground();

		return null;
	}
	
	public String changeLeaveStatus(String leaveJson, int statusID) throws Exception{ 
		System.out.println("Will change to "+Leave.getLeaveStatusDescForKey(statusID));
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"ApproveLeave?leave="+leaveJson+"&status="+statusID+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("ApproveLeaveResult");
		System.out.println("change leave status result " + result);
//		app.fileManager.saveToTextFile(result.toString());
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");

		if(statusID == Leave.LEAVESTATUSAPPROVEDKEY || statusID == Leave.LEAVESTATUSREJECTEDKEY){
			ParseObject dataObject = new ParseObject("Approvals");
			dataObject.put("approver", app.getStaff().getFname()+" "+app.getStaff().getLname());
			dataObject.put("type", "Leave");
			dataObject.saveInBackground();
		}
		return null;		
	}
		
	public String followUpLeave(String leaveJson) throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"EmailLeave?leave="+leaveJson+"&requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("EmailLeaveResult");
		
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		return null;		
	}
	
	// Calendar
	public Object getWeeklyHolidays() throws Exception{
		JSONObject result = new JSONObject(getHttpGetResult(apiUrl+"GetNationalHolidaysForTheWeek?requestingPerson="+app.getStaff().getStaffID()+"&datetime="+now())).getJSONObject("GetNationalHolidaysForTheWeekResult");
		if(result.getJSONArray("SystemErrors").length() > 0)
			return result.getJSONArray("SystemErrors").getJSONObject(0).getString("Message");
		
		JSONArray jsonHolidays = result.getJSONArray("NationalHoliday");
		ArrayList<Holiday> holidays = new ArrayList<Holiday>();
		for(int i=0; i<jsonHolidays.length(); i++){
			JSONObject jsonHoliday = jsonHolidays.getJSONObject(i);
			String dateStr = dJsonizeDate(jsonHoliday.getString("Date"));
			Holiday holiday = new Holiday(jsonHoliday.getString("Name"), jsonHoliday.getString("Country"), dateStr, app.dateFormatDefault.parse(dateStr));

			JSONArray jsonOffices = jsonHoliday.getJSONArray("OfficesApplied");
			for(int j=0; j<jsonOffices.length(); j++)
				holiday.addOffice(jsonOffices.getJSONObject(j).getInt("OfficeID"), jsonOffices.getJSONObject(j).getString("OfficeName"));
			
			holidays.add(holiday);
		}
		
		return holidays;
	}
	
	public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
	    HashMap<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key).toString();

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}	
	
	private static String encodeToBase64(File fileToEncode) throws Exception{
		int fileSize = (int) fileToEncode.length();
		//convert file to byte array
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(fileToEncode));
		byte[] bytes = new byte[fileSize];
		reader.read(bytes, 0, fileSize);
		reader.close();
		//convert byte array to base64 string
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
		
	}
		
	//PRIVATE METHODS
	public Bitmap getBitmapFromUrl(String src) throws IOException{
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
	}

}
