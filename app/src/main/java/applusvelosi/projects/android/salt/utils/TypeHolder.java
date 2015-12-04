package applusvelosi.projects.android.salt.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import applusvelosi.projects.android.salt.models.ClaimTrail;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.LocalHoliday;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;

import com.google.gson.reflect.TypeToken;

public class TypeHolder {

	public Type staff = new TypeToken<Staff>(){}.getType();
	public Type office = new TypeToken<Office>(){}.getType();
	public Type leave = new TypeToken<Leave>(){}.getType();
	public Type claim = new TypeToken<ClaimHeader>(){}.getType();
	public Type capex = new TypeToken<CapexHeader>(){}.getType();
	public Type recruitment = new TypeToken<Recruitment>(){}.getType();
	public Type claimItem = new TypeToken<ClaimItem>(){}.getType();
	public Type arrayListOfString = new TypeToken<ArrayList<String>>(){}.getType();
	public Type arrayListOfHolidays = new TypeToken<ArrayList<Holiday>>(){}.getType();
	public Type arrayListOfLocalHolidays = new TypeToken<ArrayList<LocalHoliday>>(){}.getType();
	public Type arrayListOfLeaves = new TypeToken<ArrayList<Leave>>(){}.getType();
	public Type arrayListOfClaims = new TypeToken<ArrayList<ClaimHeader>>(){}.getType();
	public Type arrayListOfClaimItems = new TypeToken<ArrayList<ClaimItem>>(){}.getType();
	public Type arrayListOfClaimTrails = new TypeToken<ArrayList<ClaimTrail>>(){}.getType();
	public Type hashmapOfStringObject = new TypeToken<HashMap<String, Object>>(){}.getType();
	public Type hashmapOfStringString = new TypeToken<HashMap<String, String>>(){}.getType();
	public Type arrayListOfHashmapOfStringObject = new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType();
}
