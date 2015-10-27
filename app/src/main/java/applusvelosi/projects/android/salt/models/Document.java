package applusvelosi.projects.android.salt.models;

import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.webkit.MimeTypeMap;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.utils.enums.ObjectTypes;

public class Document {

	private LinkedHashMap<String, Object> map;
	
	public Document(LinkedHashMap<String, Object> map){
		this.map = new LinkedHashMap<String, Object>();
		this.map.putAll(map);
	}
	
	public Document(JSONObject jsonDoc, SaltApplication app) throws Exception{
		map = new LinkedHashMap<String, Object>();
		map.putAll(OnlineGateway.toMap(jsonDoc));
		map.put("DateCreated", app.onlineGateway.dJsonizeDate(map.get("DateCreated").toString()));
	}
	
	public Document(String docName, long fileSize, int staffID, int refLineItemID, ObjectTypes docType){ 
		map = new LinkedHashMap<String, Object>();
		map.put("Active", true);
		map.put("DocID", 0);
		map.put("DocName", "doctest.jpg");
		map.put("FileSize", fileSize);
		map.put("ObjectType", 2);
		map.put("RefID", refLineItemID);
		map.put("StaffID", staffID);
		map.put("OrigDocName", docName);
		String ext = MimeTypeMap.getFileExtensionFromUrl(docName);
		map.put("Ext", "."+ext);
		map.put("ContentType", MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext));
	}
	
	public String jsonize(SaltApplication app) {
		return app.gson.toJson(map, app.types.hashmapOfStringObject);
	}
	
	public LinkedHashMap<String, Object> getMap(){
		return map;
	}
}
