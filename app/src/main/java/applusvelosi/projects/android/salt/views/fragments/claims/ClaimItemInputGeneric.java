package applusvelosi.projects.android.salt.views.fragments.claims;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class ClaimItemInputGeneric extends ActionbarFragment implements CompoundButton.OnCheckedChangeListener{
    public static final String KEY_CATEGORY = "categorykey";
    public static final String KEY_CURRENCY = "currencykey";

    //actionbar buttons
    private TextView actionbarTitle, actionbarDone;
    private RelativeLayout actionbarButtonBack;

    private Category category;
    private Currency currency;

    private EditText etextAmount, etextLocalAmount, etextForex, etextTax, etextProject, etextBillTo, etextDesc;
    private TextView tviewsDate, tviewCurr, tvAttachment;
    private CheckBox cboxTaxable, cboxBillable;
    private ImageView buttonFile, buttonCamera;
    private TableRow trBillNotes;

    private DatePickerDialog datePicker;
    private Calendar currCalendar;
    private float forex;

    private SaltProgressDialog pd;


    public static ClaimItemInputGeneric newInstance(Category category, Currency currency){
        ClaimItemInputGeneric frag = new ClaimItemInputGeneric();
        Bundle b = new Bundle();
        b.putSerializable(KEY_CATEGORY, category);
        b.putSerializable(KEY_CURRENCY, currency);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        category = (Category)getArguments().getSerializable(KEY_CATEGORY);
        currency = (Currency)getArguments().getSerializable(KEY_CURRENCY);

        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarDone = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText(category.getName());

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claimitem_input, null);

        tviewsDate = (TextView)view.findViewById(R.id.tviews_claimiteminput_expenseDate);
        etextAmount = (EditText)view.findViewById(R.id.etexts_claimiteminput_fc_amount);
        etextLocalAmount = (EditText)view.findViewById(R.id.etexts_claimiteminput_lc_amount);
        etextForex = (EditText)view.findViewById(R.id.etexts_claimiteminput_forex);
        etextTax = (EditText)view.findViewById(R.id.etexts_claimiteminput_tax);
        etextProject = (EditText)view.findViewById(R.id.etexts_claimiteminput_project);
        etextBillTo = (EditText)view.findViewById(R.id.etexts_claimiteminput_billTo);
        etextDesc = (EditText)view.findViewById(R.id.etexts_claimiteminput_desc);

        tviewCurr = (TextView)view.findViewById(R.id.tviews_claimiteminput_fc_curr);

        cboxTaxable = (CheckBox)view.findViewById(R.id.cboxs_claimiteminput_tax);
        cboxBillable = (CheckBox)view.findViewById(R.id.cboxs_claimiteminput_billable);

        trBillNotes = (TableRow)view.findViewById(R.id.trs_claimiteminput_clienttobill);

        cboxBillable.setOnCheckedChangeListener(this);
        cboxTaxable.setOnCheckedChangeListener(this);


        buttonFile = (ImageView)view.findViewById(R.id.buttons_claimiteminput_files);
        buttonCamera = (ImageView)view.findViewById(R.id.buttons_claimiteminput_camera);
        tvAttachment = (TextView)view.findViewById(R.id.tviews_claimiteminput_attachment);

        tviewCurr.setText(currency.getCurrencySymbol());

        currCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(activity, new DateSetListener(), currCalendar.get(Calendar.YEAR), currCalendar.get(Calendar.MONTH), currCalendar.get(Calendar.DAY_OF_MONTH));
        tviewsDate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
        else if(v == tviewsDate)
            datePicker.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        if(v == cboxTaxable){
            etextTax.setEnabled(isChecked);
        }else if(v == cboxBillable){
            etextBillTo.setEnabled(isChecked);
            trBillNotes.setVisibility((isChecked)?View.VISIBLE:View.GONE);
        }
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tviewsDate.setText(app.dateFormatDefault.format(calendar.getTime()));
        }
    }

    public class ForexGetter extends  Thread{

        @Override
        public void run() {
            Object tempResult;
            try{
                tempResult = app.onlineGateway.getForexRate(app.getStaffOffice().getBaseCurrencyThree(), currency.getCurrencySymbol());
            }catch(Exception e){
                e.printStackTrace();
                tempResult = e.getMessage();
            }

            final Object result = tempResult;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    if(result instanceof String){
                        new AlertDialog.Builder(activity).setTitle("").setMessage(result.toString())
                                .setPositiveButton("Reload", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        pd.show();
                                        new ForexGetter().start();
                                    }
                                }).setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        activity.finish();
                                    }
                        }).create().show();
                    }else{
                        forex = (float)forex;
                        etextForex.setText(String.valueOf(forex));
                    }
                }
            });
        }
    }
}
