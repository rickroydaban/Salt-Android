package applusvelosi.projects.android.salt.views.fragments.leaves;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.Staff;
import applusvelosi.projects.android.salt.utils.FileManager.AttachmentDownloadListener;
import applusvelosi.projects.android.salt.utils.SaltDatePicker;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.HomeActivity.CameraCaptureListener;
import applusvelosi.projects.android.salt.views.HomeActivity.FileSelectionListener;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class LeaveInputFragment extends ActionbarFragment implements OnItemSelectedListener, TextWatcher, OnFocusChangeListener,
																	 CameraCaptureListener, FileSelectionListener, AttachmentDownloadListener{
	/******************* CONSTANTS *****************/
	private final String HEADERSPINNERTYPE = "Select Leave Type";
	private final String HEADERSPINNERNUMDAYS = "Select Days";
	private final String HALFDAYLEAVE_AM = "0.5 Days AM";
	private final String HALFDAYLEAVE_PM = "0.5 Days PM";
	private final String ONEDAYLEAVE = "1 Day";
	public static final String KEY_LEAVEPOS = "leavekey";
	//action bar buttons
	private TextView actionbarSaveButton, actionbarTitle;
	private RelativeLayout actionbarBackButton;
	
	private Spinner propSpinnerTypes, propSpinnerDays;
	private EditText propFieldStaff, propFieldDateStart, propFieldDateEnd, propFieldDay,  propFieldHolidays, propFieldWorkingDays, propFieldNotes, propFieldRemCredits;
	private TextView propFieldAttachmentName, propButtonTakePicture, propButtonOpenGallery;
//	private TableRow propFieldTrAttachments;
	
	private ArrayList<String> propListDays, propListTypes;
	private HashMap<String, Float> propDictDays;
	private Leave toBeCreatedLeave, toBeEditedLeave;
	private String oldLeaveJSON;
	private SaltProgressDialog pd;
	private File prevSelectedAttachment;
	private File capturedPhoto;
	private SimpleDateFormat sdr;

	public static LeaveInputFragment newInstance(int appLeavePos){
		LeaveInputFragment frag = new LeaveInputFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_LEAVEPOS, appLeavePos);
		frag.setArguments(b);
		
		return frag;
	}
		
	@Override
	protected RelativeLayout setupActionbar() {
		sdr = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss", Locale.getDefault());
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarSaveButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		
		actionbarBackButton.setOnClickListener(this);;
		actionbarSaveButton.setOnClickListener(this);
		actionbarTitle.setText((getArguments()!=null)?"Edit Leave":"New Leave Request");
		actionbarTitle.setOnClickListener(this);
		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.fragment_leave_input, null);

		if(app.getStaffOffice().getHROfficerID()==0 || !app.getStaffOffice().isActive())
			new AlertDialog.Builder(activity).setTitle(null)
											 .setMessage("No HR officer is currently assigned to your office. Kindly contact account manager for assistance.")
											 .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													activity.onBackPressed();
												}
											}).create().show();	
		
		if(app.getStaff().getApprover1ID()==0 || !app.getStaff().isActive())
			new AlertDialog.Builder(activity).setTitle(null)
											 .setMessage("A leave approver should be assigned to your account before you can request for a leave. Kindly contact account manager for assistance.")
											 .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													activity.onBackPressed();
												}
											}).create().show();
			
		
		pd = new SaltProgressDialog(activity);
		propSpinnerTypes = (Spinner)v.findViewById(R.id.choices_dialog_leaverequest_type);
		propFieldStaff = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_staff);
		propSpinnerDays = (Spinner)v.findViewById(R.id.choices_dialog_leaverequest_numdays);
		propFieldDateStart = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_startdate);
		propFieldDateEnd = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_enddate);
		propFieldDay = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_days);
		propFieldHolidays = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_numholidays);
		propFieldWorkingDays = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_workingdays);
		propFieldRemCredits = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_remcredits);
		propFieldNotes = (EditText)v.findViewById(R.id.etexts_dialog_leaverequest_notes);
//		propFieldTrAttachments = (TableRow)v.findViewById(R.id.trs_dialog_leaverequest_attachment);
		propFieldAttachmentName = (TextView)v.findViewById(R.id.tviews_leaverequest_attachmentname);
		propButtonTakePicture = (TextView)v.findViewById(R.id.buttons_leaverequest_attachment_fromcamera);
		propButtonOpenGallery = (TextView)v.findViewById(R.id.buttons_leaverequest_attachment_fromfiles);

		propListTypes = new ArrayList<String>();
		propListTypes.add(HEADERSPINNERTYPE);
		for(int i=0; i<Leave.getTypeDescriptionList().size(); i++){
			System.out.println(Leave.getTypeDescriptionList().get(i).equals(Leave.LEAVETYPEBIRTHDAYDESC)+" && "+!app.getStaffOffice().hasBirthdayLeave());
			if(Leave.getTypeDescriptionList().get(i).equals(Leave.LEAVETYPEBIRTHDAYDESC) && !app.getStaffOffice().hasBirthdayLeave()){
				//don't add birthday option when birthday is not available in the office
			}else
				propListTypes.add(Leave.getTypeDescriptionList().get(i));
		}
		
		propSpinnerTypes.setAdapter(new SimpleSpinnerAdapter(activity, propListTypes, NodeSize.SIZE_NORMAL));
		propFieldStaff.setText(app.getStaff().getFname()+" "+app.getStaff().getLname());

		propListDays = new ArrayList<String>();
		propDictDays = new HashMap<String, Float>();
		propListDays.add(HEADERSPINNERNUMDAYS);
		propListDays.add(HALFDAYLEAVE_AM);
		propDictDays.put(HALFDAYLEAVE_AM, 0.1f);
		propListDays.add(HALFDAYLEAVE_PM);
		propDictDays.put(HALFDAYLEAVE_PM, 0.2f);
		propListDays.add(ONEDAYLEAVE);
		propDictDays.put(ONEDAYLEAVE, 1.0f);
		for(float i=2; i<31; i++){
			propListDays.add(i+" Days");
			propDictDays.put(i+" Days", i);
		}
		propSpinnerDays.setAdapter(new SimpleSpinnerAdapter(activity, propListDays, NodeSize.SIZE_NORMAL));		
				
		propSpinnerTypes.setTag(-1); //set to -1 so that end date field aftertextchange will work
		propSpinnerDays.setTag(-1);

		if(getArguments() != null){
			toBeEditedLeave = app.getMyLeaves().get(getArguments().getInt(KEY_LEAVEPOS));	
			if(toBeEditedLeave != null){				
				propSpinnerTypes.setSelection(propListTypes.indexOf(toBeEditedLeave.getTypeDescription()));
				propFieldStaff.setText(toBeEditedLeave.getStaffName());
				
				int selection; 
				float days = toBeEditedLeave.getDays();
				if(days == 0.1f) selection = 1;
				else if(days == 0.2f) selection = 2;
				else selection = (int)days+2;
				propSpinnerDays.setTag(selection);
				propSpinnerDays.setSelection(selection);
				
				propFieldDateStart.setText(toBeEditedLeave.getStartDate());
				propFieldDateEnd.setText(toBeEditedLeave.getEndDate());
				propFieldDay.setText(propSpinnerDays.getSelectedItem().toString().split(" ")[0]);
				float holidays = Float.parseFloat(propFieldDay.getText().toString().split(" ")[0])-toBeEditedLeave.getWorkingDays();
				holidays = (holidays>0 && holidays<1)?1:holidays;
				propFieldHolidays.setText(String.valueOf(holidays));
				propFieldWorkingDays.setText(String.valueOf(toBeEditedLeave.getWorkingDays()));
				propFieldNotes.setText(toBeEditedLeave.getNotes());				
			}			
		}else{
			propFieldDateEnd.setEnabled(false); //start date should be set before setting end date
			propSpinnerDays.setEnabled(false);
		}
						
		propSpinnerTypes.setOnItemSelectedListener(this);
		propSpinnerDays.setOnItemSelectedListener(this);
		propFieldDateStart.setOnFocusChangeListener(this);
		propFieldDateStart.setOnClickListener(this);
		propFieldDateStart.addTextChangedListener(this);
		propFieldDateEnd.setOnFocusChangeListener(this);
		propFieldDateEnd.setOnClickListener(this);
		propFieldDateEnd.addTextChangedListener(this); 	
		propFieldRemCredits.addTextChangedListener(this);
		propFieldAttachmentName.setOnClickListener(this);
		propButtonTakePicture.setOnClickListener(this);
		propButtonOpenGallery.setOnClickListener(this);
		activity.setOnCameraCaptureListener(this);
		activity.setOnFileSelectionListener(this);
		
		return v;
	}
		
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		//only triggers when manually inputed
		if(Integer.parseInt(parent.getTag().toString())!=pos){
			if(parent==propSpinnerDays && pos>0){
				try{
					float selectedDays = Float.parseFloat(parent.getSelectedItem().toString().split(" ")[0]);
					long addableDays = (long)(SaltApplication.ONEDAY*((selectedDays<2)?0:selectedDays-1));
					Date startDate = app.dateFormatDefault.parse(propFieldDateStart.getText().toString());
					parent.setTag(pos); //avoid automatic setting for this spinner
					propFieldDateEnd.setText(app.dateFormatDefault.format(new Date(startDate.getTime()+addableDays)));	
					//process for setting other fields is different for manual selection
				}catch(Exception e){
					app.showMessageDialog(activity, e.getMessage());
				}	
			}else if(parent == propSpinnerTypes && pos>0){ //updates 
				if(propFieldDay.length() > 0){
					if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEVACATIONDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingVLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPESICKDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingSLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEBEREAVEMENTDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingBLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEMATERNITYDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingMatPatDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEHOSPITALIZATIONDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingHLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
					else
						propFieldRemCredits.setText("0");
				}else{
					if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEVACATIONDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingVLDays()));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPESICKDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingSLDays()));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEBEREAVEMENTDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingBLDays()));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEMATERNITYDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingMatPatDays()));
					else if(parent.getSelectedItem().toString().equals(Leave.LEAVETYPEHOSPITALIZATIONDESC))
						propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingHLDays()));
					else
						propFieldRemCredits.setText("0");					
				}
				
//				String type = parent.getSelectedItem().toString();
//				propFieldTrAttachments.setVisibility((type.equals(Leave.LEAVETYPESICKDESC) || type.equals(Leave.LEAVETYPEMATERNITYDESC) || type.equals(Leave.LEAVETYPEDOCTORDESC) || type.equals(Leave.LEAVETYPEHOSPITALIZATIONDESC))?View.VISIBLE:View.GONE);
			}
		}		
	}						

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void afterTextChanged(final Editable editable) {
		//check if the selected date is not a holiday or already has filed a leave on the selected date
		if(editable.length()>0 && (propFieldDateStart.getText().hashCode()==editable.hashCode() || propFieldDateEnd.getText().hashCode()==editable.hashCode())){			
			pd.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					Object tempLeaveResult, tempHolidayResult;
					try{
						tempLeaveResult = app.onlineGateway.getMyLeaves();
						tempHolidayResult = app.onlineGateway.getOfficeHolidaysOrErrorMessage(app.getStaff().getOfficeID());
					}catch(Exception e){
						tempLeaveResult = e.getMessage();
						tempHolidayResult = e.getMessage();
					}
					
					final Object leaveResult = tempLeaveResult;
					final Object holidayResult = tempHolidayResult;
					
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {
							if(leaveResult instanceof String){ //there is an error so reset fields
								if(editable.hashCode() == propFieldDateStart.getEditableText().hashCode()){ //if start date will be reseted, then disable corresponding fields
									propFieldDateStart.getText().clear();
									propSpinnerDays.setTag(0);
									propSpinnerDays.setSelection(0);
									propFieldDateEnd.setEnabled(false);
									propSpinnerDays.setEnabled(false);
								}
								app.showMessageDialog(activity, leaveResult.toString());
								propFieldDateEnd.getText().clear();
							}else{	//successfully fetch data								
								if(propFieldDateEnd.length()<1){ //no updating of values yet. Just enable the disabled fields
									try{ //single day checking for leaves and holidays
										boolean hasAlreadyFiledALeaveOnStartDate = false;
										boolean hasHolidayOnStartDate = false;
										String holidayName = "Unspecified";
										Date date = app.dateFormatDefault.parse(propFieldDateStart.getText().toString());
										//no checking for duplication of leave request when end date is not set yet. Only holidays will be checked
										for(Holiday holiday :(ArrayList<Holiday>)holidayResult){
											if(holiday.getDate().compareTo(date) == 0){
												hasHolidayOnStartDate = true;
												holidayName = holiday.getName();
												break;
											}
										}
										
										if(hasHolidayOnStartDate){ //can actually proceed filing leave even if holiday specially for special case scenarios
											app.showMessageDialog(activity, "Date selected is a holiday. ("+holidayName+")");
										}
										
										propFieldDateEnd.setEnabled(true);
										propSpinnerDays.setEnabled(true);
									}catch(Exception e){ 
										propFieldDateStart.getText().clear();
										app.showMessageDialog(activity, e.getMessage()); 
									}
								}else{ //update fields
									try { //interval day checking for leaves and holidays
										boolean hasAlreadyFiledALeaveOnStartDate = false;
										int holidayCtr = 0;
										Calendar startCalendar = Calendar.getInstance();
										Calendar endCalendar = Calendar.getInstance();
										startCalendar.setTime(app.dateFormatDefault.parse(propFieldDateStart.getText().toString()));
										endCalendar.setTime(app.dateFormatDefault.parse(propFieldDateEnd.getText().toString()));
										for(Leave leave :(ArrayList<Leave>)leaveResult){
											//make sure to disable checking when the data being edited is the data itself
											if(!(toBeEditedLeave!=null && toBeEditedLeave.getLeaveID()==leave.getLeaveID())){
												Date leaveStartDate = app.dateFormatDefault.parse(leave.getStartDate());
												Date leaveEndDate = app.dateFormatDefault.parse(leave.getEndDate());
												if(startCalendar.compareTo(endCalendar)==0 && startCalendar.compareTo(Calendar.getInstance())!=0){
													//make sure that we can still file 2 half days of two different leave types
													if(leaveStartDate.compareTo(startCalendar.getTime())==0 &&
													   (propSpinnerDays.getSelectedItem().toString().equals(ONEDAYLEAVE) ||
													    (leave.getDays()==0.1f&&propSpinnerDays.getSelectedItem().toString().equals(HALFDAYLEAVE_AM)) ||
													    (leave.getDays()==0.2f&&propSpinnerDays.getSelectedItem().toString().equals(HALFDAYLEAVE_PM))) ){
														
														if(leave.getStatusID()!=Leave.LEAVESTATUSREJECTEDKEY || leave.getStatusID()!=Leave.LEAVESTATUSCANCELLEDKEY){
															hasAlreadyFiledALeaveOnStartDate = true;
															break;															
														}
													}
												}else{
													//dates within an interval
													if((startCalendar.getTime().compareTo(leaveStartDate)==0 || endCalendar.getTime().compareTo(leaveEndDate)==0 || (startCalendar.getTime().compareTo(leaveStartDate)>0&&endCalendar.getTime().compareTo(leaveEndDate)<0))&&leave.getStatusID()!=Leave.LEAVESTATUSCANCELLEDKEY&&leave.getStatusID()!=Leave.LEAVESTATUSREJECTEDKEY){
														hasAlreadyFiledALeaveOnStartDate = true;
														break;
													}												
												}												
											}
										}
										
										for(Holiday holiday :(ArrayList<Holiday>)holidayResult){
											if(holiday.getDate().compareTo(startCalendar.getTime())==0 || holiday.getDate().compareTo(endCalendar.getTime())==0 || (holiday.getDate().compareTo(startCalendar.getTime())>0 && holiday.getDate().compareTo(endCalendar.getTime())<0))
												holidayCtr++;
										}
										
										if(hasAlreadyFiledALeaveOnStartDate)
											handleAutoCompleteFieldErrors(editable, "Already requested leave during dates/time specified");
										else{
											if(holidayCtr > 0) //can actually proceed filing leave even if holiday specially for special case scenarios
												app.showMessageDialog(activity, "Date interval has holiday(s)");

//											int interval = (int)((endDate.getTime()-startDate.getTime())/SaltApplication.ONEDAY)+3; //add one for the spinner header and half days
											int interval = (int)((endCalendar.getTimeInMillis()-startCalendar.getTimeInMillis())/SaltApplication.ONEDAY)+3;
											if(propSpinnerDays.getSelectedItemPosition() != Integer.parseInt(propSpinnerDays.getTag().toString())){
												propSpinnerDays.setTag(interval);
												propSpinnerDays.setSelection(interval);																								
											}
																						
											int nonWorkingDays = 0;
											Calendar comparableWorkingCalendar = startCalendar;
											while(comparableWorkingCalendar.compareTo(endCalendar) <= 0){
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) && !app.getStaff().hasMonday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY) && !app.getStaff().hasTuesday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY) && !app.getStaff().hasWednesday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY) && !app.getStaff().hasThursday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) && !app.getStaff().hasFriday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) && !app.getStaff().hasSaturday()) nonWorkingDays++;
												if((comparableWorkingCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) && !app.getStaff().hasSunday()) nonWorkingDays++;
												
												comparableWorkingCalendar.set(Calendar.DAY_OF_MONTH, comparableWorkingCalendar.get(Calendar.DAY_OF_MONTH)+1);
											}
											
											float selectedDays = Float.parseFloat(propSpinnerDays.getSelectedItem().toString().split(" ")[0]);
											propFieldDay.setText(String.valueOf(selectedDays));
											propFieldHolidays.setText(String.valueOf(holidayCtr));
											//avoid negative result
											float workingDays = selectedDays - holidayCtr - nonWorkingDays;
											workingDays = (workingDays<0)?0:workingDays;
											
											if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPEVACATIONDESC) && workingDays > app.getStaff().getMaxConsecutiveLeave())
												handleAutoCompleteFieldErrors(editable, "Your leave cannot be processed as it is beyond the limits set.");
											else{
												propFieldWorkingDays.setText(String.valueOf(workingDays));
												if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPEVACATIONDESC))
													propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingVLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
												else if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPESICKDESC))
													propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingSLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
												else if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPEBEREAVEMENTDESC))
													propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingBLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
												else if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPEMATERNITYDESC))
													propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingMatPatDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
												else if(propSpinnerTypes.getSelectedItem().toString().equals(Leave.LEAVETYPEHOSPITALIZATIONDESC))
													propFieldRemCredits.setText(String.valueOf(app.staffLeaveCounter.getRemainingHLDays()-Float.parseFloat(propFieldWorkingDays.getText().toString().split(" ")[0])));
												else
													propFieldRemCredits.setText("0");												
											}
										}
									}catch(Exception e){ handleAutoCompleteFieldErrors(editable, e.getMessage()); }
								}
							}	
							
							pd.dismiss();
						}
					});
				}
			}).start();			
		}else if(editable.hashCode() == propFieldRemCredits.getEditableText().hashCode()){
			if(propFieldRemCredits.length()>0 && Float.parseFloat(propFieldRemCredits.getText().toString()) < 0)
				handleAutoCompleteFieldErrors(editable, "You do not have sufficient holiday this year left to book the selected leave. Please amend the details you entered and try again or you may use unpaid leave type instead");
		}
	}
	
	private void handleAutoCompleteFieldErrors(Editable editable, String errorMessage){
		if(editable.hashCode() == propFieldDateEnd.getEditableText().hashCode()){
			System.out.println("clearing startdate");
			propFieldDateStart.getText().clear();
			propSpinnerDays.setEnabled(false);
			propFieldDateEnd.setEnabled(false);
		}

		propFieldDateEnd.getText().clear();
		propSpinnerDays.setTag(0);
		propSpinnerDays.setSelection(0);
		propFieldDay.getText().clear();
		propFieldHolidays.getText().clear();
		propFieldRemCredits.getText().clear();
		propFieldWorkingDays.getText().clear();												
		
		app.showMessageDialog(activity, errorMessage);
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	
	@Override
	public void onFocusChange(View v, boolean isFocused) {
		if(isFocused){
			if(v == propFieldDateStart){
				System.out.println("TEST focused propfielddatestart");
				new StartDatePickerDialog(activity, propFieldDateStart);
			}else if(v == propFieldDateEnd){
				System.out.println("TEST focused propfielddateend");
				new EndDatePickerDialog(activity, propFieldDateEnd);
			}				
		}
	}

	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitle){
			activity.onBackPressed();
		}else if(v == actionbarSaveButton){
			if(propSpinnerTypes.getSelectedItemPosition() > 0){
				if(propFieldDateStart.getText().length() > 0){
					try {
						if(toBeEditedLeave == null){ //new leave request
							oldLeaveJSON = Leave.createEmptyJSON();
							toBeCreatedLeave = new Leave(	app.getStaff(),
															app.staffLeaveCounter.getRemainingVLDays(),
															app.staffLeaveCounter.getRemainingSLDays(),
															Leave.getLeaveTypeIDForDesc(propSpinnerTypes.getSelectedItem().toString()), 
															Leave.LEAVESTATUSPENDINGID,
															propFieldDateStart.getText().toString(),  //start date
															propFieldDateEnd.getText().toString(),  //end date 
															propDictDays.get(propSpinnerDays.getSelectedItem()),  //days
															Float.parseFloat(propFieldWorkingDays.getText().toString()), //working days 
															propFieldNotes.getText().toString(),  //notes
															app.dateFormatDefault.format(new Date()));
						}else{ //edit leave
							oldLeaveJSON = toBeEditedLeave.getJSONStringForEditingLeave();
							toBeEditedLeave.editLeave(	Leave.getLeaveTypeIDForDesc(propSpinnerTypes.getSelectedItem().toString()), 
														app.getStaff().getMaxVL(), 
														app.getStaff().getMaxSL(), 
														propFieldDateStart.getText().toString(),  //start date
														propFieldDateEnd.getText().toString(),  //end date 
														propDictDays.get(propSpinnerDays.getSelectedItem()),  //days
														Float.parseFloat(propSpinnerDays.getSelectedItem().toString().split(" ")[0]), //working days 
														propFieldNotes.getText().toString(),  //notes
														app.dateFormatDefault.format(new Date()));
						}
						
						actionbarSaveButton.setEnabled(false);
						pd.show();
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								String tempSaveLeaveResult;
								
								try {						
									tempSaveLeaveResult = app.onlineGateway.saveLeave((toBeCreatedLeave!=null)?toBeCreatedLeave.getJSONStringForEditingLeave():toBeEditedLeave.getJSONStringForEditingLeave() , oldLeaveJSON);
								} catch (Exception e) {
									e.printStackTrace();
									tempSaveLeaveResult = e.getMessage();
								}
	
								final Object saveLeaveResult = tempSaveLeaveResult;
								new Handler(Looper.getMainLooper()).post(new Runnable() {
									@Override
									public void run(){
										actionbarSaveButton.setEnabled(true);
										pd.dismiss();
										((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
										if(saveLeaveResult != null)
											app.showMessageDialog(activity, "Failed to save leave "+saveLeaveResult);
										else{
											new AlertDialog.Builder(activity).setTitle(null)
																			 .setMessage((getArguments()==null)?"Leave Submitted!":"Leave Saved Successsfully")
																			 .setCancelable(false)
																			 .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
																				
																				@Override
																				public void onClick(DialogInterface dialog, int which) {
																					activity.changeChildPage(LeaveListFragment.getInstance());
																					new Thread(new Runnable() {
																						
																						@Override
																						public void run() {
																							String tempFollowUpLeaveResult;
																							
																							try{
																								tempFollowUpLeaveResult = app.onlineGateway.followUpLeave((toBeCreatedLeave!=null)?toBeCreatedLeave.getJSONStringForProcessingLeave():toBeEditedLeave.getJSONStringForProcessingLeave());
																							}catch(Exception e){
																								e.printStackTrace();
																								tempFollowUpLeaveResult = e.getMessage();
																							}
																							
																							final String followUpLeaveResult = tempFollowUpLeaveResult;
																							new Handler(Looper.getMainLooper()).post(new Runnable() {
																								
																								@Override
																								public void run() {
																									if(followUpLeaveResult != null)
																										app.showMessageDialog(activity, "Failed to send email to approver(s): "+followUpLeaveResult);
																								}
																							});
																						}
																					}).start();	
																				}
																			}).create().show();

										}										
									}
								});
							}
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
					}				
				}else
					app.showMessageDialog(activity, "All fields are required");
			}else{
				app.showMessageDialog(activity, "Please select a leave type");
				propSpinnerTypes.performClick();
			}
		}else if(v == propFieldDateStart){
			new StartDatePickerDialog(activity, propFieldDateStart);
		}else if(v == propFieldDateEnd){
			new EndDatePickerDialog(activity, propFieldDateEnd);
//			new EDatePickerDialog(activity, propFieldDateEnd);
		}else if(v == propFieldAttachmentName){
			try{
				if(propFieldAttachmentName.getTag() != null){
					File attachment = (File)propFieldAttachmentName.getTag();  
					String ext = "."+attachment.getName().substring(attachment.getName().lastIndexOf('.')+1, attachment.getName().length());
					app.fileManager.openAttachment(activity, ext, attachment);
				}else{
					app.showMessageDialog(activity, "Let Leave Models have attachmetn");
//					app.fileManager.openAttachment(activity, newLeave.getAttachmentExtension(), prevSelectedAttachment);
				}
			}catch(Exception e){
				app.showMessageDialog(activity, e.getMessage());
			}			
		}else if(v == propButtonTakePicture){
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			try {
				capturedPhoto = File.createTempFile("saltcapturedattachment_"+sdr.format(new Date())+"_", ".jpg", new File(app.fileManager.getDirForCapturedAttachments()));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturedPhoto));
				prevSelectedAttachment = (File)propFieldAttachmentName.getTag();
				propFieldAttachmentName.setTag(capturedPhoto);
				activity.startActivityForResult(intent, HomeActivity.RESULT_CAMERA);
			} catch (Exception e) {
				e.printStackTrace();
				app.showMessageDialog(activity, e.getMessage());
			}
		}else if(v == propButtonOpenGallery){
			Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
			fileintent.setType("gagt/sdf");
			activity.startActivityForResult(fileintent, HomeActivity.RESULT_BROWSEFILES);
		}

	}

	private class StartDatePickerDialog extends SaltDatePicker{

		public StartDatePickerDialog(FragmentActivity fa, EditText dateField) {
			super(fa, dateField);
			try{
				Calendar today = Calendar.getInstance();
				today.set(Calendar.MILLISECOND, today.get(Calendar.SECOND)+1);
				if(propSpinnerTypes.getSelectedItem().equals(Leave.LEAVETYPEVACATIONDESC) && app.getStaff().getSecurityLevel()<Staff.SECURITYLEVEL_ACCOUNTMANAGER)
					getDatePicker().setMinDate(today.getTimeInMillis());
				else if(propSpinnerTypes.getSelectedItem().equals(Leave.LEAVETYPESICKDESC) && app.getStaff().getSecurityLevel()<Staff.SECURITYLEVEL_ACCOUNTMANAGER)
					getDatePicker().setMaxDate(today.getTimeInMillis());				
			}catch(Exception e){
				e.printStackTrace();
				app.showMessageDialog(activity, e.getMessage());
			}
		}		
	}
	
	private class EndDatePickerDialog extends SaltDatePicker{

		public EndDatePickerDialog(FragmentActivity fa, EditText dateField) {
			super(fa, dateField);
			try{
				Date today = Calendar.getInstance().getTime();
				if(propSpinnerTypes.getSelectedItem().equals(Leave.LEAVETYPEVACATIONDESC) && app.getStaff().getSecurityLevel()<Staff.SECURITYLEVEL_ACCOUNTMANAGER)
					getDatePicker().setMinDate(today.getTime());
				else if(propSpinnerTypes.getSelectedItem().equals(Leave.LEAVETYPESICKDESC) && app.getStaff().getSecurityLevel()<Staff.SECURITYLEVEL_ACCOUNTMANAGER)
					getDatePicker().setMaxDate(today.getTime());				
			}catch(Exception e){
				e.printStackTrace();
				app.showMessageDialog(activity, e.getMessage());
			}
		}		
	}
	
	@Override
	public void onCameraCaptureSuccess() {
		//uploading of attachment should be done before sending a request to create a claim item
		updateAttachment(capturedPhoto);
	}
	
	@Override
	public void onCameraCaptureFailed() {
		propFieldAttachmentName.setTag(prevSelectedAttachment);
		propFieldAttachmentName.setText((prevSelectedAttachment!=null)?prevSelectedAttachment.getName():"No File Chosen");
	}
	
	@Override
	public void onFileSelectionSuccess(File file) {
		//uploading of attachment should be done before sending a request to create a claim item
		updateAttachment(file);
	}
	
	@Override
	public void onFileSelectionFailed() {
		propFieldAttachmentName.setTag(prevSelectedAttachment);
		propFieldAttachmentName.setText((prevSelectedAttachment!=null)?prevSelectedAttachment.getName():"No File Chosen");
	}

	private void updateAttachment(File file){
		if(propFieldAttachmentName.getTag() != null)
			prevSelectedAttachment = (File)propFieldAttachmentName.getTag();
		propFieldAttachmentName.setTag(file);
		propFieldAttachmentName.setText(file.getName());
		
//		try {
//			app.fileManager.generateBase64OnSD(OnlineGateway.encodeToBase64(file));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					app.onlineGateway.uploadAttachment((File)propFieldAttachmentName.getTag());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	@Override
	public void onAttachmentDownloadFinish(File file) {
		prevSelectedAttachment = file;
		propFieldAttachmentName.setTag(prevSelectedAttachment);
	}
	
	@Override
	public void onAttachmentDownloadFailed(String errorMessage) {
		app.showMessageDialog(activity, errorMessage);
	}
	
}
