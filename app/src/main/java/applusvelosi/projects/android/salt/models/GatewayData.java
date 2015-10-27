package applusvelosi.projects.android.salt.models;

import applusvelosi.projects.android.salt.utils.interfaces.GatewayInterface;

public class GatewayData {
	private GatewayInterface type;
	private String data;
	
	public GatewayData(GatewayInterface type, String data){
		this.type = type;
		this.data = data;
	}
	
	public GatewayInterface getType(){
		return type;
	}
	
	public String getData(){
		return data;
	}
}
