package applusvelosi.projects.android.salt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.LocalHoliday;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimNotPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.models.claimheaders.LiquidationOfBA;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.OfflineGateway;
import applusvelosi.projects.android.salt.utils.OnlineGateway;
import applusvelosi.projects.android.salt.utils.TypeHolder;
import applusvelosi.projects.android.salt.utils.enums.Months;
import applusvelosi.projects.android.salt.views.LoginActivity;
import applusvelosi.projects.android.salt.views.SplashActivity;
import applusvelosi.projects.android.salt.views.fragments.HolidaysLocalFragment;
import applusvelosi.projects.android.salt.views.fragments.HolidaysMonthlyFragment;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;

/* The entrance point of the app
 * after the app has been opened, the system
 * will display the splash activity which will then trigger the initapplication method in this class
 */

public class SaltApplication extends Application {
	private Tracker mTracker;

//	/**
//	 * Gets the default {@link Tracker} for this {@link Application}.
//	 * @return tracker
//	 */
//	synchronized public Tracker getDefaultTracker() {
//		if (mTracker == null) {
//			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//			mTracker = analytics.newTracker(R.xml.global_tracker);
//		}
//		return mTracker;
//	}

	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-68161281-1";

	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 *
	 * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	 * storing them all in Application object helps ensure that they are created only once per
	 * application instance.
	 */
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER // Tracker used by all the apps from a company. eg: roll-up tracking.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID): analytics.newTracker(R.xml.global_tracker);
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

    public static final long ONEDAY = 24*60*60*1000;
	public static final String DEFAULT_FLOAT_FORMAT = "%.2f";
	public static final int MAINHRID = 198;
	public FileManager fileManager;
	public OfflineGateway offlineGateway;
	public OnlineGateway onlineGateway;
	
	public Gson gson;
	public TypeHolder types;
	public Calendar calendar;
	public SimpleDateFormat dateTimeFormat, dateFormatDefault, dateFormatClaimItemAttachment;

	private String savedMonthlyCalendarMonth;
	private ArrayList<Holiday> nationalHolidays;
	private ArrayList<LocalHoliday> localHolidays;
	private ArrayList<Leave> myLeaves;
	private ArrayList<ClaimHeader> myClaimHeaders;
	private boolean hasLoadedMonthlyHolidays, hasLoadedLocalHolidays;
	private ArrayList<Currency> currencies;
	private Staff staff; 
	private Office office;
	
	public ArrayList<String> dropDownYears, dropDownMonths;
	public StaffLeaveTypeCounter staffLeaveCounter;
	public int thisYear;
	
	//initializes all objects that are needed by different objects living within the app
	public void initializeApp(SplashActivity key){
		fileManager = new FileManager();
		onlineGateway = new OnlineGateway(this);
		offlineGateway = new OfflineGateway(this);
		
		hasLoadedMonthlyHolidays = false;
		hasLoadedLocalHolidays = false;
		dateTimeFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
		dateFormatDefault = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		calendar = Calendar.getInstance();
		gson = new Gson();
		types = new TypeHolder();
		thisYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date()));
				
		dropDownYears = new ArrayList<String>();
		int currYear = calendar.get(Calendar.YEAR);
		for(int i=currYear-4; i<=currYear+1; i++)
			dropDownYears.add(String.valueOf(i));
		dropDownMonths = new ArrayList<String>();
		for(int i=0; i<Months.values().length; i++)
			dropDownMonths.add(Months.values()[i].toString());
		staffLeaveCounter = StaffLeaveTypeCounter.getInstance(this);
		myClaimHeaders = new ArrayList<ClaimHeader>();

		if(offlineGateway.isLoggedIn()){
			staff = offlineGateway.deserializeStaff();
			office = offlineGateway.deserializeStaffOffice();
			myLeaves = offlineGateway.deserializeMyLeaves();
			currencies = offlineGateway.deserializeCurrencies();
            downCastClaim(myClaimHeaders, offlineGateway.deserializeMyClaims());
			nationalHolidays = offlineGateway.deserializeNationalHolidays();	
			localHolidays = offlineGateway.deserializeLocalHolidays();

			try{
				onlineGateway.updateStaff(staff.getStaffID(), staff.getSecurityLevel(), staff.getOfficeID());
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			myLeaves = new ArrayList<Leave>();
			myClaimHeaders = new ArrayList<ClaimHeader>();
			nationalHolidays = new ArrayList<Holiday>();
			localHolidays = new ArrayList<LocalHoliday>();
		}
	}

    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "Lsnfv65XD5V1RlgoYnWX8DEB06EW9BCTOdwBDWRb", "CiYC18jb5FXSdjJgvJbixG0wqoC252dnR7YAwgBd");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

	public boolean hasLoadedMonthlyHolidays(){
		return hasLoadedMonthlyHolidays;
	}
	
	public boolean hasLoadedLocalHolidays(){
		return hasLoadedLocalHolidays;
	}
	
	public void setMonthlyHolidaysLoaded(HolidaysMonthlyFragment key){
		hasLoadedMonthlyHolidays = true;
	}
	
	public void setLocalHolidaysLoaded(HolidaysLocalFragment key){
		hasLoadedLocalHolidays = true;
	}
	
	private void downCastClaim(ArrayList<ClaimHeader> claimHeaders, ArrayList<HashMap<String, Object>> maps){
		for(HashMap<String, Object> map :maps){
			if(Integer.parseInt(map.get(ClaimHeader.KEY_TYPEID).toString()) == ClaimHeader.TYPEKEY_CLAIMS){
				if(Boolean.parseBoolean(map.get(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD).toString()))
					claimHeaders.add(new ClaimPaidByCC(map));
				else
					claimHeaders.add(new ClaimNotPaidByCC(map));
			}else if(Integer.parseInt(map.get(ClaimHeader.KEY_TYPEID).toString()) == ClaimHeader.TYPEKEY_ADVANCES){
				claimHeaders.add(new BusinessAdvance(map));
			}else if(Integer.parseInt(map.get(ClaimHeader.KEY_TYPEID).toString()) == ClaimHeader.TYPEKEY_LIQUIDATION){
				claimHeaders.add(new LiquidationOfBA(map));
			}
		}
	}
	
	public boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
		
	public void showMessageDialog(Context context, String message){
		new AlertDialog.Builder(context).setMessage(message)
										.setPositiveButton("Ok", new OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int arg1) {
                                                dialog.dismiss();
                                            }
                                        })
										.create().show();
	}
	
	public void showNetworkErrorMessage(Context context, String message){
		new AlertDialog.Builder(context).setMessage(message)
		.setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		})
		.create().show();		
	}
	
	public boolean hasSavedData(){
		return (this.offlineGateway!=null && this.offlineGateway.isLoggedIn())?true:false;
	}
	
	public void setStaffData(OnlineGateway key, Staff staff, Office office){
		this.staff = staff;
		this.office = office;

		if(office.getCMID() == staff.getStaffID())
			staff.setUserPosition((office.isHeadQuarter())?Staff.USERPOSITION.USERPOSITION_CFO:Staff.USERPOSITION.USERPOSITION_CM);
		else if(office.getRMID() == staff.getStaffID())
			staff.setUserPosition((office.isHeadQuarter())?Staff.USERPOSITION.USERPOSITION_CEO:Staff.USERPOSITION.USERPOSITION_RM);
		else
			staff.setUserPosition(Staff.USERPOSITION.USERPOSITION_DEFAULT);

		this.offlineGateway.serializeStaffData(staff, office);

        ParseObject dataObject = new ParseObject("Usage");
        dataObject.put("name", staff.getFname()+" "+staff.getLname());
        dataObject.put("office", office.getName());
        dataObject.saveInBackground();
    }
	
	public void setStaff(LoginActivity key, Staff staff){ //temporary data for staff that must be only called by loginactivity class
		this.staff = staff;
	}
	
	public Staff getStaff(){
		return staff;
	}
	
	public Office getStaffOffice(){
		return office;
	}
		
	public void updateMyLeaves(ArrayList<Leave> myLeaves){
		this.myLeaves.clear();
		this.myLeaves.addAll(myLeaves);
		offlineGateway.serializeMyLeaves(myLeaves);
	}

	public void updateMyClaims(ArrayList<ClaimHeader> myClaimHeaders){
		this.myClaimHeaders.clear();
		this.myClaimHeaders.addAll(myClaimHeaders);
		offlineGateway.serializeMyClaims(myClaimHeaders);
	}

	public ArrayList<Leave> getMyLeaves(){
		return myLeaves;
	}

	public ArrayList<ClaimHeader> getMyClaims(){
		return myClaimHeaders;
	}
							
	public ArrayList<Holiday> getNationalHolidays(){
		return nationalHolidays;
	}
	
	public ArrayList<LocalHoliday> getLocalHolidays(){
		return localHolidays;
	}
			
	public int getHolidaysCountForIntervals(Date startDate, Date endDate) {
		int count=0;

		if(startDate.compareTo(endDate) == 0){ //lesser comparisons when start and end dates are the same
			for(int i=0;i<nationalHolidays.size();i++){
				if(nationalHolidays.get(i).getDate().compareTo(startDate) == 0)
					count++;
			}
		}else{
			for(int i=0;i<nationalHolidays.size(); i++){
				Date currDate = nationalHolidays.get(i).getDate();
				if(currDate.compareTo(startDate)==0 || currDate.compareTo(endDate)==0 || (currDate.compareTo(startDate)>0 && currDate.compareTo(endDate)<0))
					count++;
			}			
		}
		
		return count;
	}
	
	public String getSavedMonthlyCalendarMonth(){
		return savedMonthlyCalendarMonth;
	}
		
	public void updateNationalHolidays(ArrayList<Holiday> nationalHolidays){
		this.nationalHolidays.clear();
		this.nationalHolidays.addAll(nationalHolidays);
		this.offlineGateway.serializeNationHolidays(this, nationalHolidays);
	}
	
	public void updateLocalHolidays(ArrayList<LocalHoliday> localHolidays){
		this.localHolidays.clear();
		this.localHolidays.addAll(localHolidays);
		this.offlineGateway.serializeLocalHolidays(this, localHolidays);
	}
	
	public void setCurrencies(ArrayList<Currency> currencies){
		this.currencies = currencies;
		offlineGateway.serializeCurrencies(currencies);
	}
	
	public ArrayList<Currency> getCurrencies(){
		return currencies;
	}
		
	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface myFont(Context context) {
    	String name = "fonts/Tahoma.ttf";
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
