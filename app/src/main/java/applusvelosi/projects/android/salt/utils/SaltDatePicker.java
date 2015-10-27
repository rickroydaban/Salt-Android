package applusvelosi.projects.android.salt.utils;

import java.text.ParseException;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.EditText;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.enums.MonthAbbrs;

public class SaltDatePicker implements OnDateSetListener{
	private Calendar calendar;
	private EditText dateField;
	private int year, monthOfYear, dayOfMonth;
	private DatePickerDialog datePickerDialog;
	
	public SaltDatePicker(FragmentActivity fa, EditText dateField){
		this.dateField = dateField;
		calendar = Calendar.getInstance();
		if(dateField.length() > 0)
			try {
				calendar.setTime(((SaltApplication)fa.getApplication()).dateFormatDefault.parse(dateField.getText().toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		year = calendar.get(Calendar.YEAR);
		monthOfYear = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		
		datePickerDialog = new DatePickerDialog(fa, this, year, monthOfYear, dayOfMonth);
		datePickerDialog.show();
	}
	
	protected DatePicker getDatePicker(){
		return datePickerDialog.getDatePicker();
	}
	
	@Override
	public void onDateSet(android.widget.DatePicker datePicker, int year, int month,int day) {
		dateField.setText(day+"-"+MonthAbbrs.values()[month].toString()+"-"+year);
	}
}
