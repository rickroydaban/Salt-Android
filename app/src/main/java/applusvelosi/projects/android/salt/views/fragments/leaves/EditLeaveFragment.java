package applusvelosi.projects.android.salt.views.fragments.leaves;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import applusvelosi.projects.android.salt.ParseReceiver;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Holiday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.LeaveListFragment;

/**
 * Created by Velosi on 10/27/15.
 */
public class EditLeaveFragment extends LinearNavActionbarFragment implements ListAdapterInterface{
    private final String NO_SELECTION = "No Selection";
    private enum ITEMTYPE{DEFAULT, NONWORKINGDAY, LEAVEDAY};

    private static final String KEY_APPLEAVEPOS = "appleavepos";
    //actionbar weeks
    private TextView actionbarTitle, actionbarButtonClear;
    private RelativeLayout actionbarButtonBack;

    private RelativeLayout tvHeader;
    private TextView tvLeaveType, tvDurations, tvNumDays, tvRemDays, tvMonthYearHeader;
    private HashMap<Integer, HashMap<Integer, Item>> perWeekItems;
    ArrayList<String> nonworkingDays;
    HashMap<String, Float> leaveDays;
    private ListView lv;
    private ListAdapter adapter;
    private float tempLeaveDuration = 0;
    private float remBalance, tempRemBalance;
    private Calendar startCalendar, endCalendar, leaveStartCalendar, leaveEndCalendar, tempEndLeaveEndCalendar;
    private SimpleDateFormat monthYearFormat;

    private AlertDialog submitDialog;
    private RelativeLayout dialogView;
    private EditText etNotes;

    private Leave leave;
    public static EditLeaveFragment newInstance(int appLeavePos){
        EditLeaveFragment frag = new EditLeaveFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_APPLEAVEPOS, appLeavePos);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        leave = app.getMyLeaves().get(getArguments().getInt(KEY_APPLEAVEPOS));
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backedit, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarButtonClear = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Dates");
        actionbarButtonClear.setText("Reset");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);
        actionbarButtonClear.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaveinput_dates, null);
        perWeekItems = new HashMap<Integer, HashMap<Integer, Item>>();
        nonworkingDays = new ArrayList<String>();
        leaveDays = new HashMap<String, Float>();

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);

        tvHeader = (RelativeLayout)view.findViewById(R.id.fragment_leaveinput_dates_header);
        tvLeaveType = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_headerleavetype);
        tvDurations = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_headerdates);
        tvRemDays = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_remdays);
        tvNumDays = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_numdays);
        tvMonthYearHeader = (TextView)view.findViewById(R.id.fragment_leaveinput_monthyearheader);

        monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        tvLeaveType.setText(leave.getTypeDescription());
        tvDurations.setText(NO_SELECTION);
        tvRemDays.setText("");
        tvNumDays.setText("");

        if(leave.getTypeID() == Leave.LEAVETYPEVACATIONKEY || leave.getTypeID() == Leave.LEAVETYPEUNPAIDKEY || leave.getTypeID() == Leave.LEAVETYPEBUSINESSTRIPKEY || leave.getTypeID() == Leave.LEAVETYPEBIRTHDAYKEY){
            endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
            endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH)+11);
            endCalendar.set(Calendar.DAY_OF_MONTH, 31);
        }else{
            //get 3 months of leave data to current date
            startCalendar.set(Calendar.MONTH, endCalendar.get(Calendar.MONTH)-2);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }

        linearNavFragmentActivity.startLoading();
        new Thread(new MyLeavesAndHolidaysUpdater()).start();

        dialogView = (RelativeLayout)inflater.inflate(R.layout.dialog_textinput, null);
        etNotes = (EditText)dialogView.findViewById(R.id.etexts_dialogs_textinput);
        ((TextView)dialogView.findViewById(R.id.tviews_dialogs_textinput)).setText("Notes");
        submitDialog = new AlertDialog.Builder(linearNavFragmentActivity).setTitle("").setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        linearNavFragmentActivity.startLoading();
                        new SubmitNewLeaveRequest().start();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof ImageView){
            manageSelection((ImageView)v);
        }else if(v == actionbarButtonClear){
            reset();
        }else if(v == tvHeader){
            if(!tvDurations.getText().toString().equals(NO_SELECTION) && tempEndLeaveEndCalendar != null){
                if(((leave.getTypeID()==Leave.LEAVETYPEVACATIONKEY || leave.getTypeID()==Leave.LEAVETYPESICKKEY) && tempRemBalance>=0) || !(leave.getTypeID()==Leave.LEAVETYPEVACATIONKEY || leave.getTypeID()==Leave.LEAVETYPESICKKEY))
                    submitDialog.show();
            }else
                Toast.makeText(getActivity(), "Please chose an updated date!", Toast.LENGTH_SHORT).show();
        }else if(v == actionbarButtonBack || v == actionbarTitle){
            linearNavFragmentActivity.onBackPressed();
        }
    }

    private void manageSelection(ImageView iv){ //selection is only applicable for allowing half day leave to be a 1 day leave or adding the next working day
        Item item = (Item)iv.getTag();
        if(tempLeaveDuration < 1) { //half day to one day
            if(tempRemBalance >=.5f){
                if(item.getCalendar().get(Calendar.MONTH)==leaveStartCalendar.get(Calendar.MONTH) && item.getCalendar().get(Calendar.DAY_OF_MONTH)==leaveStartCalendar.get(Calendar.DAY_OF_MONTH) && item.getCalendar().get(Calendar.YEAR)==leaveStartCalendar.get(Calendar.YEAR)) {
                    ItemLeaveDay itemLeaveDay = (ItemLeaveDay) item;
                    iv.setImageResource((itemLeaveDay.getDuration() == Leave.Duration.AM) ? R.drawable.calendarcell_leave_am_whole : R.drawable.calendarcell_leave_pm_whole);
                    tempEndLeaveEndCalendar = Calendar.getInstance();
                    tempEndLeaveEndCalendar.setTime(item.getCalendar().getTime());
                    tempLeaveDuration = 1;
                    tempRemBalance-=0.5f;
                    tvNumDays.setText("1 Day");
                    tvRemDays.setText(tempRemBalance+" Days Remaining");
                }
            }
        }else{ //adding next working day
            if(tempRemBalance >= 1){
                int intervalDays = 0;
                Calendar calendarComparator = Calendar.getInstance();
                if(tempEndLeaveEndCalendar == null){
                    tempEndLeaveEndCalendar = Calendar.getInstance();
                    tempEndLeaveEndCalendar.setTime(leaveEndCalendar.getTime());
                }
                calendarComparator.setTime(tempEndLeaveEndCalendar.getTime());
                calendarComparator.set(Calendar.DAY_OF_MONTH, calendarComparator.get(Calendar.DAY_OF_MONTH)+1);
                for(; calendarComparator.compareTo(item.getCalendar())<=0; calendarComparator.set(Calendar.DAY_OF_MONTH, calendarComparator.get(Calendar.DAY_OF_MONTH)+1)){
                    System.out.println("SALTX at "+app.dateFormatDefault.format(calendarComparator.getTime()));
                    if(!nonworkingDays.contains(app.dateFormatDefault.format(calendarComparator.getTime())) && !leaveDays.containsKey(app.dateFormatDefault.format(calendarComparator.getTime()))) {
                        System.out.println("Not Interval!");
                        intervalDays++;
                    }
                }

                System.out.println("SALTX interval days "+intervalDays);
                if(intervalDays == 1){
                    iv.setImageResource(R.drawable.calendarcell_leave);
                    tempLeaveDuration++;
                    tempEndLeaveEndCalendar.setTime(item.getCalendar().getTime());
                    tempRemBalance--;
                    tvDurations.setText(app.dateFormatDefault.format(startCalendar.getTime())+" - "+app.dateFormatDefault.format(tempEndLeaveEndCalendar.getTime()));
                    tvNumDays.setText(tempLeaveDuration+" Days");
                    tvRemDays.setText(tempRemBalance+" Days Remaining");
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if(view == null){
            holder = new Holder();
            view = linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.node_listviewcalendar, null);

            holder.bgSun = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_0);
            holder.bgMon = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_1);
            holder.bgTue = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_2);
            holder.bgWed = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_3);
            holder.bgThu = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_4);
            holder.bgFri = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_5);
            holder.bgSat = (ImageView)view.findViewById(R.id.iviews_listviewcalendar_nodes_6);
            holder.daySun = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_0);
            holder.dayMon = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_1);
            holder.dayTue = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_2);
            holder.dayWed = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_3);
            holder.dayThu = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_4);
            holder.dayFri = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_5);
            holder.daySat = (TextView)view.findViewById(R.id.tviews_listviewcalendar_nodes_6);

            view.setTag(holder);
        }

        holder = (Holder)view.getTag();

        Item sundayItem = perWeekItems.get(position).get(Calendar.SUNDAY);
        String weekStartDate = monthYearFormat.format(sundayItem.getDate());
        if(!tvMonthYearHeader.getText().equals(weekStartDate))
            tvMonthYearHeader.setText(weekStartDate);
        manageDayView(holder.daySun, holder.bgSun, sundayItem);
        manageDayView(holder.dayMon, holder.bgMon, perWeekItems.get(position).get(Calendar.MONDAY));
        manageDayView(holder.dayTue, holder.bgTue, perWeekItems.get(position).get(Calendar.TUESDAY));
        manageDayView(holder.dayWed, holder.bgWed, perWeekItems.get(position).get(Calendar.WEDNESDAY));
        manageDayView(holder.dayThu, holder.bgThu, perWeekItems.get(position).get(Calendar.THURSDAY));
        manageDayView(holder.dayFri, holder.bgFri, perWeekItems.get(position).get(Calendar.FRIDAY));
        manageDayView(holder.daySat, holder.bgSat, perWeekItems.get(position).get(Calendar.SATURDAY));

        return view;
    }

    private void manageDayView(TextView dayTV, ImageView dayIV, Item item){
        dayTV.setText(item.getDay());

        switch(item.getType()){ //apply styles
            case DEFAULT:{
                dayTV.setTextColor(getResources().getColor(R.color.black));
                dayIV.setImageResource(R.drawable.calendarcell);
                dayIV.setTag(item);
                dayIV.setOnClickListener(this);
            }break;

            case LEAVEDAY:{
                ItemLeaveDay itemLeaveDay = (ItemLeaveDay)item;
                dayTV.setTextColor(getResources().getColor(R.color.black));
                if(itemLeaveDay.getDuration() == Leave.Duration.AM)  dayIV.setImageResource(R.drawable.calendarcell_sel_pm);
                else if(itemLeaveDay.getDuration() == Leave.Duration.PM)  dayIV.setImageResource(R.drawable.calendarcell_sel_am);
                else dayIV.setImageResource(R.drawable.calendarcell_sel);
                dayIV.setTag(itemLeaveDay);
                dayIV.setOnClickListener((itemLeaveDay.getDuration()== Leave.Duration.ONEDAY)?null:this);
            }break;

            case NONWORKINGDAY:{
                dayTV.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
                dayIV.setImageResource(R.drawable.calendarcell_na);
                dayIV.setOnClickListener(null);
            }break;
        }

        //let the selected items be colored blue on reuse
        if(leaveEndCalendar != null && tempEndLeaveEndCalendar!= null) {
            if (item.getType()== ITEMTYPE.DEFAULT && item.getDate().compareTo(leaveEndCalendar.getTime()) >= 0 && item.getDate().compareTo(tempEndLeaveEndCalendar.getTime()) <= 0) {
                dayIV.setImageResource(R.drawable.calendarcell_leave);
            }else if(item.getType() == ITEMTYPE.LEAVEDAY && item.getDate().compareTo(tempEndLeaveEndCalendar.getTime())==0) {
                if (Math.abs(leaveDays.get(item.getStringedDate()) - 0.1) < 0.00001)
                    dayIV.setImageResource(R.drawable.calendarcell_leave_am_whole);
                else dayIV.setImageResource(R.drawable.calendarcell_leave_pm_whole);
            }
        }
    }

    @Override
    public int getCount() {
        return perWeekItems.keySet().size();
    }

    private abstract class Item{
        private Date date;
        private String day;
        private String stringedDate;
        private Calendar calendar;

        public Item(Calendar calendar){ //for default and nonworking days
            this.calendar = calendar;
            date = calendar.getTime();
            stringedDate = app.dateFormatDefault.format(date);
            day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        public String getDay(){ return day; }
        public String getStringedDate(){ return stringedDate; }
        public Date getDate(){ return date; }
        public Calendar getCalendar(){ return calendar; }
        public abstract ITEMTYPE getType();
    }

    public class ItemNonWorking extends Item {
        private boolean shouldBeEmpty;

        public ItemNonWorking(Calendar calendar, boolean shouldBeEmpty) {
            super(calendar);
            this.shouldBeEmpty = shouldBeEmpty;
        }

        @Override
        public ITEMTYPE getType() {
            return ITEMTYPE.NONWORKINGDAY;
        }

        @Override
        public String getDay() {
            return (shouldBeEmpty)?" ":super.getDay();
        }
    }

    public class ItemLeaveDay extends Item{
        private Leave.Duration duration;

        public ItemLeaveDay(Calendar calendar, Leave.Duration duration) {
            super(calendar);
            this.duration = duration;
        }

        @Override
        public ITEMTYPE getType() { return ITEMTYPE.LEAVEDAY; }

        public Leave.Duration getDuration(){ return duration; }

    }

    public class ItemDefault extends Item {

        public ItemDefault(Calendar calendar) {
            super(calendar);
        }

        @Override
        public ITEMTYPE getType() {
            return ITEMTYPE.DEFAULT;
        }
    }

    private class MyLeavesAndHolidaysUpdater implements  Runnable{
        @Override
        public void run() {
            Object tempLeaveResult, tempHolidayResult;
            try{ //TODO Should request a separate web service
                tempLeaveResult = app.onlineGateway.getMyPendingAndApprovedLeaves();
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
                    nonworkingDays.clear();
                    leaveDays.clear();
                    if(leaveResult instanceof String || holidayResult instanceof String){
                        linearNavFragmentActivity.finishLoading(leaveResult.toString()+", "+holidayResult.toString());
                        new AlertDialog.Builder(linearNavFragmentActivity).setTitle("").setMessage((leaveResult instanceof String)?leaveResult.toString():holidayResult.toString())
                                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        linearNavFragmentActivity.onBackPressed();
                                    }
                                })
                                .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        linearNavFragmentActivity.startLoading();
                                        new Thread(new MyLeavesAndHolidaysUpdater()).start();
                                    }
                                }).setCancelable(false).create().show();
                    }else{ //successfully fetched data
                        linearNavFragmentActivity.finishLoading();
                        for(Holiday holiday :(ArrayList<Holiday>)holidayResult) {
                            if(!nonworkingDays.contains(holiday.getStringedDate()))
                                nonworkingDays.add(holiday.getStringedDate());
                        }

                        //get office's non working days for all weeks
                        Calendar comparatorDate = Calendar.getInstance();
                        comparatorDate.setTime(startCalendar.getTime());
                        while(comparatorDate.compareTo(endCalendar) <= 0){
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) && !app.getStaff().hasMonday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY) && !app.getStaff().hasTuesday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY) && !app.getStaff().hasWednesday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY) && !app.getStaff().hasThursday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) && !app.getStaff().hasFriday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) && !app.getStaff().hasSaturday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));
                            if((comparatorDate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) && !app.getStaff().hasSunday()) nonworkingDays.add(app.dateFormatDefault.format(comparatorDate.getTime()));

                            comparatorDate.set(Calendar.DAY_OF_MONTH, comparatorDate.get(Calendar.DAY_OF_MONTH)+1);
                        }

                        if(leave.getTypeID() == Leave.LEAVETYPEVACATIONKEY) remBalance = app.getStaff().getMaxVL();
                        else if(leave.getTypeID() == Leave.LEAVETYPESICKKEY) remBalance = app.getStaff().getMaxSL();
                        for(Leave leave :(ArrayList<Leave>)leaveResult){
                            remBalance = remBalance - ((leave.getDays() < 1)?0.5f:leave.getDays());
                            if(leave.getDays() <= 1){ //handle single day leaves
                                if(leaveDays.containsKey(leave.getStartDate()))
                                   leaveDays.put(leave.getStartDate(), leaveDays.get(leave.getStartDate())+leave.getDays());
                                else
                                    leaveDays.put(leave.getStartDate(), leave.getDays());
                            }else{ //handle consecutive leaves
                                try{
                                    comparatorDate.setTime(app.dateFormatDefault.parse(leave.getStartDate()));
                                    for(float days = leave.getDays(); days > 0;){
                                        if(!(nonworkingDays.contains(app.dateFormatDefault.format(comparatorDate.getTime())))){
                                            leaveDays.put(app.dateFormatDefault.format(comparatorDate.getTime()), 1.0f);
                                            days--;
                                        }

                                        comparatorDate.set(Calendar.DAY_OF_MONTH, comparatorDate.get(Calendar.DAY_OF_MONTH)+1);
                                    }
                                }catch(Exception e){
                                    app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
                                }
                            }
                        }

                        //after populating list data, populate view
                        HashMap<Integer, Item> currMapDays = new HashMap<Integer, Item>();
                        comparatorDate.setTime(startCalendar.getTime());
                        for(int i=1; i<comparatorDate.get(Calendar.DAY_OF_WEEK); i++) {  //prev month days with spaces
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(comparatorDate.getTime());
                            currMapDays.put(i, new ItemNonWorking(calendar, true));
                        }

                        int currWeekOfYear = comparatorDate.get(Calendar.WEEK_OF_YEAR);
                        int currWeekCtr = 0;

                        for(; comparatorDate.compareTo(endCalendar) <=0;comparatorDate.set(Calendar.DAY_OF_MONTH, comparatorDate.get(Calendar.DAY_OF_MONTH)+1)){
                            String currDate = app.dateFormatDefault.format(comparatorDate.getTime());

                            if(currWeekOfYear != comparatorDate.get(Calendar.WEEK_OF_YEAR)) {
                                HashMap<Integer, Item> tem = new HashMap<Integer, Item>();
                                tem.putAll(currMapDays);
                                perWeekItems.put(currWeekCtr, tem);
                                currWeekOfYear = comparatorDate.get(Calendar.WEEK_OF_YEAR);
                                currWeekCtr++;
                                currMapDays.clear();
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(comparatorDate.getTime());
                            if(nonworkingDays.contains(currDate)) {
                                currMapDays.put(comparatorDate.get(Calendar.DAY_OF_WEEK), new ItemNonWorking(calendar, false));
                            }else if(leaveDays.containsKey(currDate)) {
                                Leave.Duration duration;
                                if(Math.abs(leaveDays.get(currDate) - 0.1) < 0.00001) duration = Leave.Duration.AM;
                                else if(Math.abs(leaveDays.get(currDate) - 0.2) < 0.00001) duration = Leave.Duration.PM;
                                else duration = Leave.Duration.ONEDAY;
                                currMapDays.put(comparatorDate.get(Calendar.DAY_OF_WEEK), new ItemLeaveDay(calendar, duration));
                            }else {
                                currMapDays.put(comparatorDate.get(Calendar.DAY_OF_WEEK), new ItemDefault(calendar));
                            }

                        }

                        for(int i=comparatorDate.get(Calendar.DAY_OF_WEEK); i<8; i++){  //prev next days with spaces
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(comparatorDate.getTime());
                            currMapDays.put(i, new ItemNonWorking(calendar, true));
                        }

                        perWeekItems.put(currWeekCtr, currMapDays);
                        reset();

                        tvHeader.setOnClickListener(EditLeaveFragment.this);
                    }
                }
            });
        }
    }

    private class Holder{
        public ImageView bgSun, bgMon, bgTue, bgWed, bgThu, bgFri, bgSat;
        public TextView daySun, dayMon, dayTue, dayWed, dayThu, dayFri, daySat;
    }

    private class SubmitNewLeaveRequest extends Thread{
        Leave newLeave;
        @Override
        public void run() {
            String tempResult;
            String startDateStr = app.dateFormatDefault.format(leaveStartCalendar.getTime());
            String oldEndDateStr = app.dateFormatDefault.format(leaveEndCalendar.getTime());
            String newEndDateStr = app.dateFormatDefault.format(tempEndLeaveEndCalendar.getTime());

            try {
                String oldLeaveJSON = leave.getJSONStringForEditingLeave();
                newLeave = new Leave(leave.getMap());
                newLeave.editLeave( (leave.getTypeID()==Leave.LEAVETYPEVACATIONKEY)?tempRemBalance:app.staffLeaveCounter.getRemainingVLDays(),
                                    (leave.getTypeID()==Leave.LEAVETYPESICKKEY)?tempRemBalance:app.staffLeaveCounter.getRemainingSLDays(),
                                    newEndDateStr,
                                    tempLeaveDuration,
                                    (tvNumDays.getText().toString().equals("AM") || tvNumDays.getText().toString().equals("PM"))?0.5f:Float.parseFloat(tvNumDays.getText().toString().split(" ")[0]),
                                    etNotes.getText().toString(),
                                    app.dateFormatDefault.format(new Date()));
                tempResult = app.onlineGateway.saveLeave(newLeave.getJSONStringForEditingLeave(), oldLeaveJSON);
            }catch(Exception e){
                e.printStackTrace();
                tempResult = e.getMessage();
            }

            final String result = tempResult;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(result == null){
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                String tempFollowUpLeaveResult;

                                try{
                                    tempFollowUpLeaveResult = app.onlineGateway.followUpLeave(newLeave.getJSONStringForEditingLeave());
                                }catch(Exception e){
                                    e.printStackTrace();
                                    tempFollowUpLeaveResult = e.getMessage();
                                }

                                final String followUpLeaveResult = tempFollowUpLeaveResult;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if(followUpLeaveResult != null)
                                            Toast.makeText(linearNavFragmentActivity, "Failed to send email to approver(s): "+followUpLeaveResult, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();

                        ParsePush parsePush = new ParsePush();
                        ParseQuery parseQuery = ParseInstallation.getQuery();
                        parseQuery.whereEqualTo("staffID", app.getStaff().getApprover1ID());
                        parsePush.sendMessageInBackground(ParseReceiver.createLeaveApprovalMessage(newLeave, app), parseQuery);
                        Toast.makeText(getActivity(), "Leave Updated Successfully!", Toast.LENGTH_SHORT).show();
                        LeaveListFragment.getInstance().sync();
                        linearNavFragmentActivity.finishLoading();
                        linearNavFragmentActivity.finish();
                    }else{
                        app.showMessageDialog(getActivity(), result);
                        linearNavFragmentActivity.finishLoading();
                    }
                }
            });
        }
    }

    public void reset(){
        try{
            leaveStartCalendar = Calendar.getInstance();
            leaveStartCalendar.setTime(app.dateFormatDefault.parse(leave.getStartDate()));
            leaveEndCalendar = Calendar.getInstance();
            leaveEndCalendar.setTime(app.dateFormatDefault.parse(leave.getEndDate()));
            tempEndLeaveEndCalendar = null;
            if(Math.abs(leave.getDays() - 0.1) < 0.00001) tvNumDays.setText("AM");
            else if(Math.abs(leave.getDays() - 0.2) < 0.00001) tvNumDays.setText("PM");
            else tvNumDays.setText((int)leave.getDays()+((leave.getDays()>1)?" Days":" Day"));

            tvDurations.setText(app.dateFormatDefault.format(leaveStartCalendar.getTime())+((leave.getDays()>1)?" - "+app.dateFormatDefault.format(leaveEndCalendar.getTime()):""));
            tvRemDays.setText(remBalance+" Days Remaining");

            tempLeaveDuration = leave.getDays();
            tempRemBalance = remBalance;
            adapter.notifyDataSetChanged();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
