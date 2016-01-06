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
import applusvelosi.projects.android.salt.models.CountryHoliday;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 10/27/15.
 */
public class EditLeaveFragment_backup extends LinearNavActionbarFragment implements ListAdapterInterface{
    private final String NO_SELECTION = "No Selection";
    private enum ITEMTYPE{DEFAULT, NONWORKINGDAY, LEAVEDAY, LEAVEDAYSELECTED};
    private enum SELECTIONTYPE{WILLDISPLAY_AM, WILLDISPLAY_PM, WILLDISPLAY_ONEDAY, WILLDISPLAY_NONE};

    private static final String KEY_TYPE = "keytpe";
    private static final String KEY_DATESTART = "keydatestart";
    private static final String KEY_DATEEND = "keydateend";
    private static final String KEY_DAYS = "keydays";

    //actionbar weeks
    private TextView actionbarTitle, actionbarButtonClear;
    private RelativeLayout actionbarButtonBack;

    private RelativeLayout tvHeader;
    private TextView tvLeaveType, tvDurations, tvNumDays, tvRemDays;
    private HashMap<Integer, HashMap<Integer, Item>> tempPerWeekItems, defaultPerWeekItems;
    ArrayList<String> nonworkingDays;
    private ListView lv;
    private ListAdapter adapter;
    private SELECTIONTYPE currDurationSelection = SELECTIONTYPE.WILLDISPLAY_ONEDAY;
    private float leaveDuration = 0;
    private float remBalance, tempRemBalance;
    private Calendar startCalendar, endCalendar, leaveStartCalendar, leaveEndCalendar;
    private SimpleDateFormat monthDayFormat;

    private AlertDialog submitDialog;
    private RelativeLayout dialogView;
    private EditText etNotes;

    private int startWeekNum;
    private int leaveTypeID;
    private String dateStart, dateEnd;
    public static EditLeaveFragment_backup newInstance(int leaveTypeID, String dateStart, String dateEnd, float leavedays){
        EditLeaveFragment_backup frag = new EditLeaveFragment_backup();
        Bundle b = new Bundle();
        b.putInt(KEY_TYPE, leaveTypeID);
        b.putString(KEY_DATESTART, dateStart);
        b.putString(KEY_DATEEND, dateEnd);
        b.putFloat(KEY_DAYS, leavedays);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backedit, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarButtonClear = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_edit);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Edit Leave");
        actionbarButtonClear.setText("Reset");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);
        actionbarButtonClear.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaveinput_dates, null);
        tempPerWeekItems = new HashMap<Integer, HashMap<Integer, Item>>();
        defaultPerWeekItems = new HashMap<Integer, HashMap<Integer, Item>>();
        nonworkingDays = new ArrayList<String>();

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);

        tvHeader = (RelativeLayout)view.findViewById(R.id.fragment_leaveinput_dates_header);
        tvLeaveType = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_headerleavetype);
        tvDurations = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_headerdates);
        tvRemDays = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_remdays);
        tvNumDays = (TextView)view.findViewById(R.id.fragment_leaveinput_dates_numdays);

        monthDayFormat = new SimpleDateFormat("MMM dd");
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        leaveTypeID = getArguments().getInt(KEY_TYPE);
        dateStart = getArguments().getString(KEY_DATESTART);
        dateEnd = getArguments().getString(KEY_DATEEND);
        leaveDuration = getArguments().getFloat(KEY_DAYS);
        tvLeaveType.setText(Leave.getLeaveTypeDescForKey(leaveTypeID));
        tvDurations.setText(NO_SELECTION);
        tvRemDays.setText("");
        tvNumDays.setText("");

        if(leaveTypeID == Leave.LEAVETYPEVACATIONKEY || leaveTypeID == Leave.LEAVETYPEUNPAIDKEY || leaveTypeID == Leave.LEAVETYPEBUSINESSTRIPKEY || leaveTypeID == Leave.LEAVETYPEBIRTHDAYKEY){
            endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH)+12);
            endCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH));
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
            if(!tvDurations.getText().toString().equals(NO_SELECTION)){
                if(((leaveTypeID==Leave.LEAVETYPEVACATIONKEY || leaveTypeID==Leave.LEAVETYPESICKKEY) && tempRemBalance>=0) || !(leaveTypeID==Leave.LEAVETYPEVACATIONKEY || leaveTypeID==Leave.LEAVETYPESICKKEY))
                    submitDialog.show();
            }
        }else if(v == actionbarButtonBack || v == actionbarTitle){
            linearNavFragmentActivity.onBackPressed();
        }
    }

    private void reset(){
        try{
            leaveStartCalendar = Calendar.getInstance();
            leaveStartCalendar.setTime(app.dateFormatDefault.parse(dateStart));
            leaveEndCalendar = Calendar.getInstance();
            leaveEndCalendar.setTime(app.dateFormatDefault.parse(dateEnd));
            if(Math.abs(leaveDuration - 0.1) < 0.00001) {
                currDurationSelection = SELECTIONTYPE.WILLDISPLAY_PM;
                tvNumDays.setText("AM");
            }else if(Math.abs(leaveDuration - 0.2) < 0.00001){
                currDurationSelection = SELECTIONTYPE.WILLDISPLAY_ONEDAY;
                tvNumDays.setText("PM");
            }else{
                currDurationSelection = SELECTIONTYPE.WILLDISPLAY_NONE;
                tvNumDays.setText((int)leaveDuration+((leaveDuration>1)?" Days":" Day"));
            }
            currDurationSelection = SELECTIONTYPE.WILLDISPLAY_ONEDAY;
            tvDurations.setText(monthDayFormat.format(leaveStartCalendar.getTime())+((leaveDuration>1)?monthDayFormat.format(leaveEndCalendar.getTime()):""));
            tvRemDays.setText(String.valueOf(remBalance));

            ArrayList<Integer> weeksSelected = new ArrayList<Integer>();
            if(leaveDuration > 1){
                Calendar comparator = Calendar.getInstance();
                comparator.setTime(leaveStartCalendar.getTime());
                for(; comparator.before(leaveEndCalendar); comparator.set(Calendar.DAY_OF_MONTH, comparator.get(Calendar.DAY_OF_MONTH)+1)){
                    if(!weeksSelected.contains(comparator.get(Calendar.WEEK_OF_YEAR)))
                        weeksSelected.add(comparator.get(Calendar.WEEK_OF_YEAR));
                }
            }else
                weeksSelected.add(leaveStartCalendar.get(Calendar.WEEK_OF_YEAR));

            tempPerWeekItems.clear();
            tempPerWeekItems.putAll(defaultPerWeekItems);
//            for(int weekNumber :weeksSelected){
//                Leave.Duration thisWeekSunLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.SUNDAY);
//                Leave.Duration thisWeekMonLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.MONDAY);
//                Leave.Duration thisWeekTueLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.TUESDAY);
//                Leave.Duration thisWeekWedLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.WEDNESDAY);
//                Leave.Duration thisWeekThuLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.THURSDAY);
//                Leave.Duration thisWeekFriLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.FRIDAY);
//                Leave.Duration thisWeekSatLeaveDuration = managePreSelectedLeaveDays(weekNumber, Calendar.SATURDAY);
//                if(thisWeekSunLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.SUNDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.SUNDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekMonLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.MONDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.MONDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekTueLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.TUESDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.TUESDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekWedLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.WEDNESDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.WEDNESDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekThuLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.THURSDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.THURSDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekFriLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.FRIDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.FRIDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//                if(thisWeekSatLeaveDuration != null){
//                    ItemLeaveDay item = (ItemLeaveDay)defaultPerWeekItems.get(weekNumber).get(Calendar.SATURDAY);
//                    tempPerWeekItems.get(weekNumber).put(Calendar.SATURDAY, new ItemLeaveDaySelected(item.getCalendar(), item.getDuration(), (leaveDuration < 1)));
//                }
//
//            }

            adapter.notifyDataSetChanged();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Leave.Duration managePreSelectedLeaveDays(int weekOfYear, int dayOfWeek){
        System.out.println("defaultperweekitemkeys "+defaultPerWeekItems.keySet());
        System.out.println("currweekitems "+defaultPerWeekItems.get(weekOfYear));
        System.out.println("weekofyear "+weekOfYear);
        System.out.println("dayofweek "+dayOfWeek);
        Item item = defaultPerWeekItems.get(weekOfYear).get(dayOfWeek);
        return (item instanceof ItemLeaveDay)?((ItemLeaveDay)item).getDuration():null;
    }

    private void manageSelection(ImageView iv){
        Item item = (Item)iv.getTag();
        if(leaveStartCalendar == null || (leaveDuration < 2 && leaveStartCalendar.getTime().equals(item.getDate()))){ //can change duration if 1 day
            leaveStartCalendar = Calendar.getInstance();
            leaveStartCalendar.setTime(item.getDate());
            int drawableRes;

            if(item.getType() == ITEMTYPE.LEAVEDAY){ //limited selection
                //please note that this can only be called when leave duration is half day as it is set on managefunction
                ItemLeaveDay itemLeaveDay = (ItemLeaveDay)item;
                if(currDurationSelection == SELECTIONTYPE.WILLDISPLAY_AM){
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_NONE;
                    drawableRes = (itemLeaveDay.getDuration() == Leave.Duration.AM)?R.drawable.calendarcell_leave_am_whole :R.drawable.calendarcell_leave_pm_whole;
                    leaveStartCalendar = Calendar.getInstance();
                    leaveStartCalendar.setTime(item.getDate());
                    leaveEndCalendar = Calendar.getInstance();
                    leaveEndCalendar.setTime(item.getDate());
                    leaveDuration = (itemLeaveDay.getDuration() == Leave.Duration.AM)?0.1f:0.2f;
                    String text = (itemLeaveDay.getDuration() == Leave.Duration.AM)?"AM":"PM";
                    tvDurations.setText(((Item)iv.getTag()).getStringedDate());
                    tempRemBalance = remBalance-.5f;
                    tvRemDays.setText((leaveTypeID==Leave.LEAVETYPEVACATIONKEY||leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance+" Days Remaining":"");
                    tvNumDays.setText(text);
                }else{
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_AM;
                    drawableRes = (itemLeaveDay.getDuration() == Leave.Duration.AM)?R.drawable.calendarcell_sel_pm:R.drawable.calendarcell_sel_am;
                    leaveDuration = 0.0f;
                    leaveStartCalendar = null;
                    leaveEndCalendar = null;
                    tvDurations.setText(NO_SELECTION);
                    tvRemDays.setText("");
                    tvNumDays.setText("");
                }
            }else{
                if(currDurationSelection == SELECTIONTYPE.WILLDISPLAY_AM){
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_PM;
                    drawableRes = R.drawable.calendarcell_leave_am;
                    leaveStartCalendar = Calendar.getInstance();
                    leaveStartCalendar.setTime(item.getDate());
                    leaveEndCalendar = Calendar.getInstance();
                    leaveEndCalendar.setTime(item.getDate());
                    leaveDuration = 0.1f;
                    tvDurations.setText(monthDayFormat.format(leaveStartCalendar.getTime()));
                    tempRemBalance = remBalance-.5f;
                    tvRemDays.setText((leaveTypeID==Leave.LEAVETYPEVACATIONKEY||leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance+" Days Remaining":"");
                    tvNumDays.setText("AM");
                }else if(currDurationSelection == SELECTIONTYPE.WILLDISPLAY_PM){
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_ONEDAY;
                    drawableRes = R.drawable.calendarcell_leave_pm;
                    leaveStartCalendar = Calendar.getInstance();
                    leaveStartCalendar.setTime(item.getDate());
                    leaveEndCalendar = Calendar.getInstance();
                    leaveEndCalendar.setTime(item.getDate());
                    leaveDuration = 0.2f;
                    tvDurations.setText(monthDayFormat.format(leaveStartCalendar.getTime()));
                    tempRemBalance = remBalance-.5f;
                    tvRemDays.setText((leaveTypeID==Leave.LEAVETYPEVACATIONKEY||leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance+" Days Remaining":"");
                    tvNumDays.setText("PM");
                }else if(currDurationSelection == SELECTIONTYPE.WILLDISPLAY_ONEDAY){
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_NONE;
                    drawableRes = R.drawable.calendarcell_leave;
                    leaveDuration = 1.0f;
                    leaveEndCalendar = Calendar.getInstance();
                    leaveEndCalendar.setTime(item.getDate());
                    tvDurations.setText(monthDayFormat.format(leaveStartCalendar.getTime()));
                    tempRemBalance = remBalance - 1;
                    tvRemDays.setText((leaveTypeID==Leave.LEAVETYPEVACATIONKEY||leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance+" Days Remaining":"");
                    tvNumDays.setText("1 Day");
                }else{ //Cancel
                    currDurationSelection = SELECTIONTYPE.WILLDISPLAY_AM;
                    drawableRes = R.drawable.calendarcell;
                    leaveDuration = 0.0f;
                    leaveStartCalendar = null;
                    leaveEndCalendar = null;
                    tvDurations.setText(NO_SELECTION);
                    tempRemBalance = remBalance;
                    tvRemDays.setText("");
                    tvNumDays.setText("");
                }
            }

            iv.setImageResource(drawableRes);
        }else{
            if(leaveDuration >= 1.0f){ //it should be at least a day not half day
                //check how many nonworkingdays are in between the selected dates
                int nonWorkingDaysCtr = 0;
                Calendar checkStartCalendar = Calendar.getInstance();
                checkStartCalendar.setTime(leaveEndCalendar.getTime());
                checkStartCalendar.set(Calendar.DAY_OF_MONTH, checkStartCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar checkEndCalendar = Calendar.getInstance();
                checkEndCalendar.setTime(((Item)iv.getTag()).getDate());
                Calendar comparator = Calendar.getInstance();
                comparator.setTime(checkStartCalendar.getTime());
                for(; comparator.compareTo(checkEndCalendar) <= 0; comparator.set(Calendar.DAY_OF_MONTH, comparator.get(Calendar.DAY_OF_MONTH)+1)){
                    if(nonworkingDays.contains(app.dateFormatDefault.format(comparator.getTime())))
                        nonWorkingDaysCtr++;
                }

                if(checkEndCalendar.get(Calendar.DAY_OF_YEAR)-nonWorkingDaysCtr-checkStartCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
                    iv.setImageResource(R.drawable.calendarcell_leave);
                    leaveDuration++;
                    leaveEndCalendar.setTime(item.getDate());
                    tvDurations.setText(monthDayFormat.format(leaveStartCalendar.getTime()) + " - " + monthDayFormat.format(leaveEndCalendar.getTime()));
                    tempRemBalance = remBalance-leaveDuration;
                    tvRemDays.setText((leaveTypeID==Leave.LEAVETYPEVACATIONKEY||leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance+" Days Remaining":"");
                    tvNumDays.setText((int) leaveDuration + " Days");
                }
            }
        }

    }

    @Override
    public View getView(int weekNumber, View convertView, ViewGroup parent) {
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
        manageDayView(holder.daySun, holder.bgSun, tempPerWeekItems.get(weekNumber).get(Calendar.SUNDAY));
        manageDayView(holder.dayMon, holder.bgMon, tempPerWeekItems.get(weekNumber).get(Calendar.MONDAY));
        manageDayView(holder.dayTue, holder.bgTue, tempPerWeekItems.get(weekNumber).get(Calendar.TUESDAY));
        manageDayView(holder.dayWed, holder.bgWed, tempPerWeekItems.get(weekNumber).get(Calendar.WEDNESDAY));
        manageDayView(holder.dayThu, holder.bgThu, tempPerWeekItems.get(weekNumber).get(Calendar.THURSDAY));
        manageDayView(holder.dayFri, holder.bgFri, tempPerWeekItems.get(weekNumber).get(Calendar.FRIDAY));
        manageDayView(holder.daySat, holder.bgSat, tempPerWeekItems.get(weekNumber).get(Calendar.SATURDAY));

        return view;
    }

    private void manageDayView(TextView dayTV, ImageView dayIV, Item item){
        System.out.println("daytv "+dayTV+" item "+item);
        System.out.println("managing for "+item.getDay()+" "+item.getStringedDate());
        dayTV.setText("0");

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

            case LEAVEDAYSELECTED:{
                ItemLeaveDaySelected itemLeaveDaySelected = (ItemLeaveDaySelected)item;
                dayTV.setTextColor(getResources().getColor(R.color.black));
                if(itemLeaveDaySelected.getDuration() == Leave.Duration.AM) dayIV.setImageResource((itemLeaveDaySelected.isWholeDay())?R.drawable.calendarcell_leave_am_whole:R.drawable.calendarcell_leave_am);
                else if(itemLeaveDaySelected.getDuration() == Leave.Duration.PM) dayIV.setImageResource((itemLeaveDaySelected.isWholeDay())?R.drawable.calendarcell_leave_pm_whole:R.drawable.calendarcell_leave_pm);
                else dayIV.setImageResource(R.drawable.calendarcell_leave);
                dayIV.setTag(itemLeaveDaySelected);
                dayIV.setOnClickListener((itemLeaveDaySelected.getDuration()== Leave.Duration.ONEDAY)?null:this);
            }break;

            case NONWORKINGDAY:{
                dayTV.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
                dayIV.setImageResource(R.drawable.calendarcell_na);
                dayIV.setOnClickListener(null);
            }break;
        }

        if(leaveStartCalendar != null && leaveEndCalendar != null) {
            if (item.getType()== ITEMTYPE.DEFAULT && item.getDate().compareTo(leaveStartCalendar.getTime()) >= 0 && item.getDate().compareTo(leaveEndCalendar.getTime()) <= 0) {
                dayTV.setTextColor(getResources().getColor(R.color.black));
                if (leaveDuration < 1) {
                    if (leaveDuration == 0.1f)
                        dayIV.setImageResource(R.drawable.calendarcell_leave_am);
                    else dayIV.setImageResource(R.drawable.calendarcell_leave_pm);
                } else dayIV.setImageResource(R.drawable.calendarcell_leave);
            }
        }
    }

    @Override
    public int getCount() {
        return tempPerWeekItems.keySet().size();
    }

    private abstract class Item{
        private Date date;
        private String day;
        private String stringedDate;

        public Item(Calendar calendar){ //for default and nonworking days
            date = calendar.getTime();
            stringedDate = monthDayFormat.format(date);
            day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        public String getDay(){ return day; }
        public String getStringedDate(){ return stringedDate; }
        public Date getDate(){ return date; }
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
        private Calendar cal;
        public ItemLeaveDay(Calendar calendar, Leave.Duration duration) {
            super(calendar);
            this.cal = calendar;
            this.duration = duration;
        }

        @Override
        public ITEMTYPE getType() { return ITEMTYPE.LEAVEDAY; }

        public Leave.Duration getDuration(){ return duration; }
        public Calendar getCalendar(){ return cal; }
    }

    public class ItemLeaveDaySelected extends ItemLeaveDay{
        private boolean isWholeDay;

        public ItemLeaveDaySelected(Calendar calendar, Leave.Duration duration, boolean isWholeDay) {
            super(calendar, duration);
            this.isWholeDay = isWholeDay;
        }

        @Override
        public ITEMTYPE getType() { return ITEMTYPE.LEAVEDAYSELECTED; }

        @Override
        public Leave.Duration getDuration() {
            if(super.getDuration() == Leave.Duration.AM) return Leave.Duration.PM;
            else if(super.getDuration() == Leave.Duration.PM) return Leave.Duration.AM;
            else return super.getDuration();
        }

        public boolean isWholeDay(){ return isWholeDay; }
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
                    HashMap<String, Float> leaveDays = new HashMap<String, Float>();
                    nonworkingDays.clear();
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
                        for(CountryHoliday holiday :(ArrayList<CountryHoliday>)holidayResult) {
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

                        if(leaveTypeID == Leave.LEAVETYPEVACATIONKEY) remBalance = app.getStaff().getVacationLeaveAllowance();
                        else if(leaveTypeID == Leave.LEAVETYPESICKKEY) remBalance = app.getStaff().getSickLeaveAllowance();
                        for(Leave leave :(ArrayList<Leave>)leaveResult){
                            remBalance-=leave.getWorkingDays();
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

                        startWeekNum = comparatorDate.get(Calendar.WEEK_OF_YEAR);
                        int currWeekOfYear = 0;

                        //check every single day from start to end calendar
                        for(; comparatorDate.compareTo(endCalendar) <=0;comparatorDate.set(Calendar.DAY_OF_MONTH, comparatorDate.get(Calendar.DAY_OF_MONTH)+1)){
                            String currDate = app.dateFormatDefault.format(comparatorDate.getTime());

                            if(currWeekOfYear != comparatorDate.get(Calendar.WEEK_OF_YEAR)) { //put all the stocked weekdays and weekends for this week in the map
                                HashMap<Integer, Item> tem = new HashMap<Integer, Item>();
                                tem.putAll(currMapDays);
                                defaultPerWeekItems.put(currWeekOfYear, tem);
                                currWeekOfYear = comparatorDate.get(Calendar.WEEK_OF_YEAR);
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

                        defaultPerWeekItems.put(currWeekOfYear++, currMapDays);
                        reset();

                        tvHeader.setOnClickListener(EditLeaveFragment_backup.this);
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
        String oldLeaveJSON;
        Leave newLeave;

        @Override
        public void run() {
            String tempResult;
            String startDateStr = app.dateFormatDefault.format(leaveStartCalendar.getTime());
            String endDateStr = app.dateFormatDefault.format(leaveEndCalendar.getTime());

            try {
                oldLeaveJSON = Leave.createEmptyJSON();
                newLeave = new Leave( app.getStaff(),
                                            (leaveTypeID==Leave.LEAVETYPEVACATIONKEY)?tempRemBalance:app.staffLeaveCounter.getRemainingVLDays(),
                                            (leaveTypeID==Leave.LEAVETYPESICKKEY)?tempRemBalance:app.staffLeaveCounter.getRemainingSLDays(),
                                            leaveTypeID,
                                            Leave.LEAVESTATUSPENDINGID,
                                            startDateStr,
                                            endDateStr,
                                            leaveDuration,
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
                        parseQuery.whereEqualTo("staffID", app.getStaff().getLeaveApprover1ID());
                        parsePush.sendMessageInBackground(ParseReceiver.createLeaveApprovalMessage(newLeave, app), parseQuery);
                        Toast.makeText(getActivity(), "Leave Submitted Successfully!", Toast.LENGTH_SHORT).show();
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
}
