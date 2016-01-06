package applusvelosi.projects.android.salt.models;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.webkit.MimeTypeMap;

import com.google.gson.JsonObject;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.utils.enums.ObjectTypes;

public class Document implements Serializable {

	private boolean isActive;
	private String contentType, dateCreated;
	private int docID, objectType, refID, staffID;
	private float fileSize;
	private String docName, ext, origDocName;

	public Document() throws Exception{
		isActive = true;
		contentType = "";
		dateCreated = "";
		docID = 0;
		docName = "";
		ext = "";
		fileSize = 0;
		objectType = 0;
		origDocName = "";
		refID = 0;
		staffID = 0;
	}

	public Document(JSONObject jsonDoc) throws Exception{
		isActive = jsonDoc.getBoolean("Active");
		contentType = jsonDoc.getString("ContentType");
		dateCreated = jsonDoc.getString("DateCreated");
		docID = jsonDoc.getInt("DocID");
		docName = jsonDoc.getString("DocName");
		ext = jsonDoc.getString("Ext");
		fileSize = (float)jsonDoc.getDouble("FileSize");
		objectType = jsonDoc.getInt("ObjectType");
		origDocName = jsonDoc.getString("OrigDocName");
		refID = jsonDoc.getInt("RefID");
		staffID = jsonDoc.getInt("StaffID");
	}

	public Document(File file, ClaimHeader claimHeader, String dateCreated, String nameDate){
		isActive = true;
        origDocName = file.getName();
        ext = origDocName.substring(origDocName.lastIndexOf(".") + 1, origDocName.length());
		this.contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
		this.dateCreated = dateCreated;
		docID = 0;
		this.docName = claimHeader.getClaimID()+"_ClaimLineItem_"+nameDate;
		fileSize = file.length();
		objectType = ObjectTypes.ClAIMLINEITEM.getID();
		this.staffID = claimHeader.getStaffID();
	}

	public int getDocID(){ return docID; }
	public int getRefID(){ return refID;}
	public int getObjectTypeID(){ return objectType; }
	public String getDocName(){ return docName; }
	public String getOrigDocName() { return origDocName; }

    public void setRefID(int refID){ this.refID = refID; System.out.println("REFID "+this.refID); }

	public JsonObject getJSONObject() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Active", isActive);
		obj.addProperty("ContentType", contentType);
		obj.addProperty("DateCreated", dateCreated);
		obj.addProperty("DocID", docID);
		obj.addProperty("DocName", docName);
		obj.addProperty("Ext", ext);
		obj.addProperty("FileSize", fileSize);
		obj.addProperty("ObjectType", objectType);
		obj.addProperty("OrigDocName", origDocName);
		obj.addProperty("RefID", refID);
		obj.addProperty("StaffID", staffID);

		return obj;
	}
}
