package applusvelosi.projects.android.salt.views.fragments.claims;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.interfaces.CameraCaptureInterface;
import applusvelosi.projects.android.salt.utils.interfaces.FileSelectionInterface;
import applusvelosi.projects.android.salt.utils.interfaces.LoaderInterface;
import applusvelosi.projects.android.salt.utils.threads.ProjectsLoader;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.dialogs.DialogClaimItemAttendeeList;
import applusvelosi.projects.android.salt.views.dialogs.DialogClaimItemCategories;
import applusvelosi.projects.android.salt.views.dialogs.DialogClaimItemCategories.DialogClaimItemCategoryInterface;
import applusvelosi.projects.android.salt.views.dialogs.DialogClaimItemChargeToList;
import applusvelosi.projects.android.salt.views.dialogs.DialogClaimItemProjects;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 12/14/15.
 */
public abstract class ItemInputEditFragment extends ItemInputFragment implements CompoundButton.OnCheckedChangeListener, TextWatcher, CameraCaptureInterface, FileSelectionInterface, DatePickerDialog.OnDateSetListener, FileManager.AttachmentDownloadListener, DialogClaimItemProjects.DialogClaimItemProjectInterface, LoaderInterface, DialogClaimItemCategoryInterface {
    ManageClaimItemActivity activity;

    private TextView actionbarTitle, actionbarDone;
    private RelativeLayout actionbarButtonBack;

    protected TextView tvCategory, tvProject, tvCurrency;

    protected EditText etextAmount, etextLocalAmount, etextForex, etextTax, etextDesc;
    protected TextView tviewsDate, tvCurrLocal, tvAttachment;
    protected CheckBox cboxTaxable, cboxBillable;
    protected ImageView buttonFile, buttonCamera;
    protected TableRow trBillNotes;
    public TextView tvBillTo;
    protected TextView etextBillNotes;
    protected RelativeLayout containerAttendees;
    public TextView tvAttendees;

    protected abstract View createClaimItemtView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void saveToServer();

    private DatePickerDialog datePicker;
    private DialogClaimItemChargeToList dialogChargeTo;
    private DialogClaimItemAttendeeList dialogAttendees;
    private DialogClaimItemProjects dialogProjects;
    private DialogClaimItemCategories dialogCategory;

    protected File attachedFile;
    private float forex;
    private Calendar currCalendar;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (ManageClaimItemActivity)getActivity();

        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarDone = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText(activity.claimItem.getCategoryName());

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);
        actionbarDone.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createClaimItemtView(inflater, container, savedInstanceState);

        tviewsDate = (TextView)v.findViewById(R.id.tviews_claimiteminput_expenseDate);
        etextAmount = (EditText)v.findViewById(R.id.etexts_claimiteminput_fc_amount);
        etextLocalAmount = (EditText)v.findViewById(R.id.etexts_claimiteminput_lc_amount);
        tvCurrency = (TextView)v.findViewById(R.id.tviews_claimiteminput_fc_curr);
        tvCurrLocal = (TextView)v.findViewById(R.id.tviews_claimiteminput_lc_curr);
        etextForex = (EditText)v.findViewById(R.id.etexts_claimiteminput_forex);
        etextTax = (EditText)v.findViewById(R.id.etexts_claimiteminput_tax);
        cboxTaxable = (CheckBox)v.findViewById(R.id.cboxs_claimiteminput_tax);

        tvAttachment = (TextView)v.findViewById(R.id.tviews_claimiteminput_attachment);
        buttonCamera = (ImageView)v.findViewById(R.id.buttons_claimiteminput_camera);
        buttonFile = (ImageView)v.findViewById(R.id.buttons_claimiteminput_files);

        tvProject = (TextView)v.findViewById(R.id.tviews_claimiteminput_project);
        tvBillTo = (TextView)v.findViewById(R.id.tviews_claimiteminput_billTo);
        cboxBillable = (CheckBox)v.findViewById(R.id.cboxs_claimiteminput_billable);
        trBillNotes = (TableRow)v.findViewById(R.id.trs_claimiteminput_clienttobill);
        etextBillNotes = (EditText)v.findViewById(R.id.etexts_claimiteminput_clienttobill);

        containerAttendees =  (RelativeLayout)v.findViewById(R.id.containers_claimiteminput_attendees);
        tvAttendees = (TextView)v.findViewById(R.id.tviews_claimiteminput_attendees);

        etextDesc = (EditText)v.findViewById(R.id.etexts_claimiteminput_desc);

        tvCategory = (TextView)v.findViewById(R.id.tviews_claimiteminput_category);
        tvProject = (TextView)v.findViewById(R.id.tviews_claimiteminput_project);


        tvCategory.setText(activity.claimItem.getCategoryName());
        tvProject.setText(activity.claimItem.getProjectName());

        tvCategory.setOnClickListener(this);
        tvProject.setOnClickListener(this);

        tvProject.setOnClickListener(this);
        tviewsDate.setOnClickListener(this);
        etextAmount.addTextChangedListener(this);
        etextForex.addTextChangedListener(this);
        etextTax.addTextChangedListener(this);
        cboxTaxable.setOnCheckedChangeListener(this);
        tvAttachment.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        buttonFile.setOnClickListener(this);

        tvBillTo.setOnClickListener(this);
        cboxBillable.setOnCheckedChangeListener(this);

        containerAttendees.setOnClickListener(this);

        etextDesc.addTextChangedListener(this);

        tvCurrency.setText(activity.claimItem.getForeignCurrencyName());
        tvCurrLocal.setText(app.getStaffOffice().getBaseCurrencyThree());
        activity.claimItem.setLocalCurrency(new Currency(app.getStaffOffice().getBaseCurrencyID(), app.getStaffOffice().getBaseCurrencyName(), app.getStaffOffice().getBaseCurrencyThree()));

        currCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(linearNavFragmentActivity, this, currCalendar.get(Calendar.YEAR), currCalendar.get(Calendar.MONTH), currCalendar.get(Calendar.DAY_OF_MONTH));

        if(app.getStaffOffice().getBaseCurrencyThree().equals(activity.claimItem.getForeignCurrencyName())){
            forex = 1;
            etextForex.setText("1.00");
        }else{
            activity.startLoading();
            new ForexGetter().start();
        }

        dialogChargeTo = new DialogClaimItemChargeToList(this);
        dialogAttendees = new DialogClaimItemAttendeeList(this);
        if(activity.getAttachment() != null)
            updateAttachment(activity.getAttachment());

        activity.setCameraListener(this);
        activity.setFileSelectionListener(this);

        tvProject.setText(activity.claimItem.getProjectName());
        tvCurrency.setText(activity.claimItem.getForeignCurrencyName());


        if(activity.claimItem.getAttendees().size() == 0) tvAttendees.setText("No Attendee");
        else if(activity.claimItem.getAttendees().size() == 1) tvAttendees.setText("1 Attendee");
        else tvAttendees.setText(activity.claimItem.getAttendees().size() +" Attendees");


        tviewsDate.setText(activity.claimItem.getExpenseDate(app));
        etextAmount.setText(String.valueOf(activity.claimItem.getForeignAmount()));
        etextLocalAmount.setText(String.valueOf(activity.claimItem.getLocalAmount()));
        etextForex.setText(String.valueOf(activity.claimItem.getForex()));
        cboxTaxable.setChecked(activity.claimItem.isTaxApplied());
        if(activity.claimItem.isTaxApplied()) {
            etextTax.setText(String.valueOf(activity.claimItem.getTaxAmount()));
        }

        if(activity.claimItem.hasReceipt()) {
            tvAttachment.setText(activity.claimItem.getAttachments().get(0).getDocName());
            tvAttachment.setTextColor(getResources().getColor(R.color.orange_velosi));
        }
        cboxBillable.setChecked(activity.claimItem.isBillable());
        if(activity.claimItem.isBillable()) {
            tvBillTo.setText(activity.claimItem.getBillableCompanyName());
            etextBillNotes.setText(activity.claimItem.getNotes());
        }

        int attendeeCnt = activity.claimItem.getAttendees().size();
        if(attendeeCnt == 0) tvAttendees.setText("No Attendee");
        else if(attendeeCnt == 1) tvAttendees.setText("1 Attendee");
        else tvAttendees.setText(attendeeCnt+" Attendees");

        etextDesc.setText(activity.claimItem.getDescription());

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == tviewsDate)
            datePicker.show();
        else if(v == tvCategory){
            if(activity.getCategories() == null) {
                activity.startLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Object tempResult;
                        try{
                            tempResult = app.onlineGateway.getClaimItemCategoryByOffice(app.getStaffOffice().getBaseCurrencyID());
                        }catch(Exception e){
                            e.printStackTrace();
                            tempResult = e.getMessage();
                        }

                        final Object result = tempResult;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                activity.finishLoading();
                                if (result instanceof String) app.showMessageDialog(activity, result.toString());
                                else{
                                    ArrayList<Category> tempCateories = (ArrayList<Category>) result;
                                    ArrayList<Category> categories = new ArrayList<Category>();
                                    for(Category category :tempCateories) {
                                        if(!category.getName().contains("Mileage")) //limited for editing to non mileage type only
                                            categories.add(category);
                                    }
                                    activity.updateCategoryList(activity, categories);
                                    dialogCategory = new DialogClaimItemCategories(activity, ItemInputEditFragment.this);
                                    dialogCategory.show();
                                }
                            }
                        });
                    }
                }).start();
            }else {
                if(dialogCategory == null)
                    dialogCategory = new DialogClaimItemCategories(activity, ItemInputEditFragment.this);
                dialogProjects.show();
            }
        }else if(v == tvProject) {
            if(activity.getProjects() == null) {
                activity.startLoading();
                new ProjectsLoader(activity, this).start();
            }else {
                if(dialogProjects == null)
                    dialogProjects = new DialogClaimItemProjects(activity ,this);
                dialogProjects.show();
            }
        }else if(v == containerAttendees)
            dialogAttendees.show();
        else if(v == tvBillTo)
            dialogChargeTo.show();
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
        }else if(v == tvAttachment) {
            if(attachedFile == null) {
                try{
                    Document document = activity.claimItem.getAttachments().get(0);
                    app.fileManager.downloadDocument(document.getDocID(), document.getRefID(), document.getObjectTypeID(), document.getDocName(), this);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(activity, "Cannot download document "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }else if(v == actionbarDone)
            saveToServer();

    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        if(v == cboxTaxable){
            activity.claimItem.setIsTaxRated(isChecked);
            if(isChecked){
                etextTax.setEnabled(true);
                etextTax.setText(String.valueOf(app.getStaffOffice().getDefaultTax()/100));
            }else{
                etextTax.setEnabled(false);
                etextTax.setText("(Tax Not Applicable)");
            }
        }else if(v == cboxBillable){
            activity.claimItem.setIsRechargable(isChecked);
            tvBillTo.setEnabled(isChecked);
            if(isChecked){
                trBillNotes.setVisibility(View.VISIBLE);
                tvBillTo.setOnClickListener(this);
            }else{
                trBillNotes.setVisibility(View.GONE);
                tvBillTo.setOnClickListener(null);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        try{
            float totalLC = Float.parseFloat(etextAmount.getText().toString()) * (Float.parseFloat(etextForex.getText().toString()));
            if(cboxTaxable.isChecked()) totalLC = totalLC-(totalLC*=Float.parseFloat(etextTax.getText().toString()));
            activity.claimItem.setAmountLC(totalLC);
            etextLocalAmount.setText(String.valueOf(totalLC));

            if(s.hashCode() == etextAmount.getText().hashCode()) activity.claimItem.setAmount(Float.parseFloat(etextAmount.getText().toString()));
            else if(s.hashCode() == etextForex.getText().hashCode()) activity.claimItem.setForex(Float.parseFloat(etextForex.getText().toString()));
            else if(s.hashCode() == etextTax.getText().hashCode()) activity.claimItem.setTaxAmount(Float.parseFloat(etextTax.getText().toString()));
        }catch(Exception e){
            e.printStackTrace();
            etextLocalAmount.setText("0.00");
        }

        if(s.hashCode() == etextBillNotes.getText().hashCode()) activity.claimItem.setNotes(etextBillNotes.getText().toString());
        else if (s.hashCode() == etextDesc.getText().hashCode()) activity.claimItem.setDescription(etextDesc.getText().toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        activity.claimItem.setDateExpensed(calendar.getTime(), app);
        tviewsDate.setText(app.dateFormatDefault.format(calendar.getTime()));
    }

    @Override
    public void onCameraCaptureSuccess() { updateAttachment(attachedFile); }

    @Override
    public void onCameraCaptureFailed() { updateAttachment(null); }

    @Override
    public void onFileSelectionSuccess(File file) { updateAttachment(file); }

    @Override
    public void onFileSelectionFailed() { updateAttachment(null); }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    private void updateAttachment(File file){
        attachedFile = file;
        activity.claimItem.getAttachments().clear();
        if(file == null){
            resetAttachment();
        }else{
            if(file.length() <= 10485760){
                if(SaltApplication.ACCEPTED_FILETYPES.contains(file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().length()))){
                    activity.claimItem.addAttachment(new Document(file, activity.claimHeader, app.onlineGateway.jsonizeDate(new Date()), app.dateFormatClaimItemAttachment.format(new Date())));
                    activity.claimItem.setHasReceipt(true);
                    activity.updateAttachmentUri(file);
                    tvAttachment.setText(file.getName());
                    tvAttachment.setTextColor(getResources().getColor(android.R.color.black));
                }else {
                    resetAttachment();
                    app.showMessageDialog(activity, "Invalid File Type");
                }
            }else {
                resetAttachment();
                app.showMessageDialog(activity, "File must not exceed 10mb");
            }
        }
    }

    private void resetAttachment(){
        activity.claimItem.setHasReceipt(false);
        tvAttachment.setText("No Selection");
        tvAttachment.setTextColor(Color.parseColor("#909090"));
    }

    @Override
    public void onProjectSelected(Project project) {
        activity.claimItem.setProject(project);
        tvProject.setText(project.getName());
    }

    @Override
    public void onCategorySelected(Category category) {
        activity.claimItem.setCategory(category);
        tvCategory.setText(category.getName());
    }

    public class ForexGetter extends  Thread{

        @Override
        public void run() {
            Object tempResult;
            try{
                tempResult = app.onlineGateway.getForexRate(activity.claimItem.getForeignCurrencyName(), app.getStaffOffice().getBaseCurrencyThree());
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

    @Override
    public void onAttachmentDownloadFinish(File downloadedFile) {
        attachedFile = downloadedFile;
        app.fileManager.openDocument(activity, attachedFile);
    }

    @Override
    public void onAttachmentDownloadFailed(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadSuccess(Object result) {
        activity.finishLoading();
        activity.updateProjectList(activity, (ArrayList<Project>) result);
        dialogProjects = new DialogClaimItemProjects(activity ,this);
        dialogProjects.show();
    }

    @Override
    public void onLoadFailed(String failureMessage) {
        activity.finishLoading();
        app.showMessageDialog(activity, failureMessage);
    }
}
