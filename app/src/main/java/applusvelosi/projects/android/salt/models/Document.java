package applusvelosi.projects.android.salt.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.webkit.MimeTypeMap;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.utils.enums.ObjectTypes;

public class Document implements Serializable {

	private boolean isActive;
	private String contentType, dateCreated;
	private int docID, objectType, refID, staffID;
	private float fileSize;
	private String docName, ext, origDocName;

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

	public int getDocID(){ return docID; }
	public int getRefID(){ return refID; }
	public int getObjectTypeID(){ return objectType; }
	public String getDocName(){ return docName; }
	public String getOrigDocName() { return origDocName; }

	public JSONObject getJSONObject() throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("Active", isActive);
		obj.put("ContentType", contentType);
		obj.put("DateCreated", dateCreated);
		obj.put("DocID", docID);
		obj.put("DocName", docName);
		obj.put("Ext", ext);
		obj.put("FileSize", fileSize);
		obj.put("ObjectType", objectType);
		obj.put("OrigDocName", origDocName);
		obj.put("RefID", refID);
		obj.put("StaffID", staffID);

		return obj;
	}
}
