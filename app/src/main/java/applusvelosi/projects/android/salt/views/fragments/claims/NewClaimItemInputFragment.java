package applusvelosi.projects.android.salt.views.fragments.claims;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.CameraCaptureInterface;
import applusvelosi.projects.android.salt.utils.interfaces.FileSelectionInterface;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class NewClaimItemInputFragment extends LinearNavActionbarFragment implements CompoundButton.OnCheckedChangeListener, TextWatcher, CameraCaptureInterface, FileSelectionInterface {
    private NewClaimItemActivity activity;

    //actionbar buttons
    private TextView actionbarTitle, actionbarDone;
    private RelativeLayout actionbarButtonBack;

    private EditText etextAmount, etextLocalAmount, etextForex, etextTax, etextProject, etextBillTo, etextDesc;
    private TextView tviewsDate, tviewCurr, tvAttachment, tvAttendees;
    private CheckBox cboxTaxable, cboxBillable;
    private ImageView buttonFile, buttonCamera;
    private TableRow trBillNotes;
    private RelativeLayout containerAttendees;

    private DatePickerDialog datePicker;
    private Calendar currCalendar;
    private float forex;

    private File attachedFile;


    @Override
    protected RelativeLayout setupActionbar() {
        activity = (NewClaimItemActivity)getActivity();

        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarDone = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText(activity.getCategory().getName());

        actionbarDone.setOnClickListener(this);
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

        etextProject.setText(activity.getProject().getName());
        etextAmount.addTextChangedListener(this);
        etextForex.addTextChangedListener(this);
        etextTax.addTextChangedListener(this);

        tviewCurr = (TextView)view.findViewById(R.id.tviews_claimiteminput_fc_curr);

        cboxTaxable = (CheckBox)view.findViewById(R.id.cboxs_claimiteminput_tax);
        cboxBillable = (CheckBox)view.findViewById(R.id.cboxs_claimiteminput_billable);

        trBillNotes = (TableRow)view.findViewById(R.id.trs_claimiteminput_clienttobill);

        cboxBillable.setOnCheckedChangeListener(this);
        cboxTaxable.setOnCheckedChangeListener(this);

        containerAttendees = (RelativeLayout)view.findViewById(R.id.containers_claimiteminput_attendees);
        tvAttendees = (TextView)view.findViewById(R.id.tviews_claimiteminput_attendees);
        if(activity.getAttendees().size() == 0) tvAttendees.setText("No Attendee");
        else if(activity.getAttendees().size() == 1) tvAttendees.setText("1 Attendee");
        else tvAttendees.setText(activity.getAttendees().size() +" Attendees");
        containerAttendees.setOnClickListener(this);

        buttonFile = (ImageView)view.findViewById(R.id.buttons_claimiteminput_files);
        buttonCamera = (ImageView)view.findViewById(R.id.buttons_claimiteminput_camera);
        tvAttachment = (TextView)view.findViewById(R.id.tviews_claimiteminput_attachment);
        if(activity.getAttachment() != null)
            updateAttachment(activity.getAttachment());
        buttonFile.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        tvAttachment.setOnClickListener(this);


        tviewCurr.setText(activity.getCurrency().getCurrencySymbol());

        currCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(linearNavFragmentActivity, new DateSetListener(), currCalendar.get(Calendar.YEAR), currCalendar.get(Calendar.MONTH), currCalendar.get(Calendar.DAY_OF_MONTH));
        tviewsDate.setOnClickListener(this);

        if(app.getStaffOffice().getBaseCurrencyThree().equals(activity.getCurrency().getCurrencySymbol())){
            forex = 1;
            etextForex.setText("1.00");
        }else{
            activity.startLoading();
            new ForexGetter().start();
        }

        activity.setCameraListener(this);
        activity.setFileSelectionListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == tviewsDate)
            datePicker.show();
        else if(v == containerAttendees)
            linearNavFragmentActivity.changePage(new ClaimItemAttendeeListFragment());
        else if(v == buttonCamera){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            try {
                attachedFile = File.createTempFile("attachment", ".jpg", new File(app.fileManager.getDirForCapturedAttachments()));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(attachedFile));
                activity.startActivityForResult(intent, SaltApplication.RESULT_CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
                app.showMessageDialog(activity, e.getMessage());
            }
        }else if (v == buttonFile) {
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("gagt/sdf");
            activity.startActivityForResult(fileintent, SaltApplication.RESULT_BROWSEFILES);
        }else if(v == tvAttachment){
            app.fileManager.openDocument(activity, activity.getAttachment());
        }else if(v == actionbarDone){
            activity.startLoading();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        if(v == cboxTaxable){
            if(isChecked){
                etextTax.setEnabled(true);
                etextTax.setText(String.valueOf(app.getStaffOffice().getDefaultTax()/100));
            }else{
                etextTax.setEnabled(false);
                etextTax.setText("(Tax Not Applicable)");
            }
        }else if(v == cboxBillable){
            etextBillTo.setEnabled(isChecked);
            trBillNotes.setVisibility((isChecked)?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        try{
            float totalLC = Float.parseFloat(etextAmount.getText().toString()) * (Float.parseFloat(etextForex.getText().toString()));
            if(cboxTaxable.isChecked()) totalLC = totalLC-(totalLC*=Float.parseFloat(etextTax.getText().toString()));
            etextLocalAmount.setText(String.valueOf(totalLC));
        }catch(Exception e){
            e.printStackTrace();
            etextLocalAmount.setText("0.00");
        }
    }

    @Override
    public void onCameraCaptureSuccess() {
        updateAttachment(attachedFile);
    }

    @Override
    public void onCameraCaptureFailed() {
        updateAttachment(null);
    }

    @Override
    public void onFileSelectionSuccess(File file) {
        updateAttachment(file);
    }

    @Override
    public void onFileSelectionFailed() {
        updateAttachment(null);
    }

    private void updateAttachment(File file){
        System.out.println("SALTX file updated with file "+file);
        attachedFile = file;
        if(file == null){
            tvAttachment.setText("No Selection");
            tvAttachment.setTypeface(tvAttachment.getTypeface(), Typeface.NORMAL);
            tvAttachment.setTextColor(Color.parseColor("#909090"));
        }else{
            activity.updateAttachmentUri(file);
            tvAttachment.setText(file.getName());
            tvAttachment.setTypeface(tvAttachment.getTypeface(), Typeface.BOLD);
            tvAttachment.setTextColor(getResources().getColor(R.color.orange_velosi));
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
                tempResult = app.onlineGateway.getForexRate(activity.getCurrency().getCurrencySymbol(), app.getStaffOffice().getBaseCurrencyThree());
            }catch(Exception e){
                e.printStackTrace();
                tempResult = e.getMessage();
            }

            final Object result = tempResult;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                activity.finishLoading();
                if(result instanceof String){
                    new AlertDialog.Builder(linearNavFragmentActivity).setTitle("").setMessage(result.toString())
                            .setPositiveButton("Reload", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    activity.startLoading();
                                     new ForexGetter().start();
                                }
                            }).setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    linearNavFragmentActivity.finish();
                                }
                    }).create().show();
                }else{
                    forex = Float.parseFloat(result.toString());
                    etextForex.setText(String.valueOf(forex));
                }
                }
            });
        }
    }
}
