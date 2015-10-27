package applusvelosi.projects.android.salt.utils.enums;

public enum SecurityLevels {

	USER("User",1),
	MANAGER("Manager",2),
	ACCOUNTS("Accounts",3),
	ADMIN("Admin",4),
	COUNTRY_MANAGER("CountryManager",5),
	ACCOUNT_MANAGER("AccountManager",6);
	
	private String name;
	private int id;
	
	private SecurityLevels(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public int getKey(){
		return id;
	}
}
