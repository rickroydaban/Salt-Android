package applusvelosi.projects.android.salt.utils.enums;

public enum ObjectTypes {

	CLAIM(1, "Claim"),
	LEAVE(2, "Leave"),
	ClAIMLINEITEM(3, "ClaimLineItem"),
	STAFF(4, "Staff"),
	RECRUITMENTREQUEST(5, "RecruitmentRequest"),
	TRAVEL(6, "Travel"),
	CAPEX(7, "Capex"),
	CAPEXLINEITEMQOUTATION(8, "CapexLineItemQoutation");
	
	private int id;
	private String name;
	private ObjectTypes(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getID(){
		return id;
	}
	
	public String toString(){
		return name;
	}
}
