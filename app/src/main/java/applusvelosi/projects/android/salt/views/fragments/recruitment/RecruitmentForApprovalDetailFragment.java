package applusvelosi.projects.android.salt.views.fragments.recruitment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.RecruitmentApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RecruitmentsForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/12/15.
 */
public class RecruitmentForApprovalDetailFragment extends LinearNavActionbarFragment {
    //actionbar
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;
    //controls
    private TextView buttonReturn, buttonApprove, buttonReject;
    //details
    private TextView    fieldName, fieldEmail, fieldOffice, fieldDepartment, fieldPhone, fieldCM, fieldDateRequested,
                        fieldDateProcessedbyCM, fieldDateProcessedbyRM, fieldPosition, fieldPositionFor, fieldReasonForVacancy,
                        fieldOfficeOfDeployment, fieldDepartmentOfDeployment, fieldTargettedStartDate, fieldJobTitle, fieldPositionCategory,
                        fieldRevenue, fieldSalaryRange, fieldBonus, fieldTimeBase, fieldEmployment, fieldHoursPerWeek;
    private CheckBox    cboxBudgettedCost, cboxSpecificPerson, cboxPositionMayBePermanent;
    private RelativeLayout  containersAttachments;
    private LinearLayout containerBenefits;

    private RecruitmentApprovalDetailActivity activity;
    private TextView tviewDialogRejectReason, tviewDialogReturnReason;
    private RelativeLayout dialogViewReject, dialogViewReturn;
    private AlertDialog dialogReject, dialogReturn;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (RecruitmentApprovalDetailActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Recruitment Detail");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rfa_detail, null);
        buttonApprove = (TextView)view.findViewById(R.id.buttons_rfadetail_approve);
        buttonReject = (TextView)view.findViewById(R.id.buttons_rfadetail_reject);
        buttonReturn = (TextView)view.findViewById(R.id.buttons_rfadetail_return);

        fieldName = (TextView)view.findViewById(R.id.tviews_rfadetail_name);
        fieldEmail = (TextView)view.findViewById(R.id.tviews_rfadetail_email);
        fieldOffice = (TextView)view.findViewById(R.id.tviews_rfadetail_office);
        fieldDepartment = (TextView)view.findViewById(R.id.tviews_rfadetail_department);
        fieldPhone = (TextView)view.findViewById(R.id.tviews_rfadetail_phoneNumber);
        fieldCM = (TextView)view.findViewById(R.id.tviews_rfadetail_countryManager);
        fieldDateRequested = (TextView)view.findViewById(R.id.tviews_rfadetail_dateRequested);
        fieldDateProcessedbyCM = (TextView)view.findViewById(R.id.tviews_rfadetail_dateprocessedbycm);
        fieldDateProcessedbyRM = (TextView)view.findViewById(R.id.tviews_rfadetail_dateprocessedbyrm);
        fieldPosition = (TextView)view.findViewById(R.id.tviews_rfadetail_positionType);
        fieldPositionFor = (TextView)view.findViewById(R.id.tviews_rfadetail_replacementfor);
        fieldReasonForVacancy = (TextView)view.findViewById(R.id.tviews_rfadetail_reasonforreplacement);
        fieldOfficeOfDeployment = (TextView)view.findViewById(R.id.tviews_rfadetail_officeofdeployment);
        fieldDepartmentOfDeployment = (TextView)view.findViewById(R.id.tviews_rfadetail_departmentofdeployment);
        fieldTargettedStartDate = (TextView)view.findViewById(R.id.tviews_rfadetail_targettedstartdate);
        fieldJobTitle = (TextView)view.findViewById(R.id.tviews_rfadetail_jobTitle);
        fieldPositionCategory = (TextView)view.findViewById(R.id.tviews_rfadetail_employeecategory);
        fieldRevenue = (TextView)view.findViewById(R.id.tviews_rfadetail_annualrevenue);
        fieldSalaryRange = (TextView)view.findViewById(R.id.tviews_rfadetail_salaryrange);
        fieldBonus = (TextView)view.findViewById(R.id.tviews_rfadetail_grossbasebunos);
        fieldTimeBase = (TextView)view.findViewById(R.id.tviews_rfadetail_timebase);
        fieldEmployment = (TextView)view.findViewById(R.id.tviews_rfadetail_employmenttype);
        fieldHoursPerWeek = (TextView)view.findViewById(R.id.tviews_rfadetail_hoursperweek);

        cboxBudgettedCost = (CheckBox)view.findViewById(R.id.cbox_rfadetail_budgetedcost);
        cboxSpecificPerson = (CheckBox)view.findViewById(R.id.cbox_rfadetail_specificperson);
        cboxPositionMayBePermanent = (CheckBox)view.findViewById(R.id.cbox_rfadetail_positionmaybecomepermanent);

        containersAttachments = (RelativeLayout)view.findViewById(R.id.containers_rfadetail_attachments);
        containerBenefits = (LinearLayout)view.findViewById(R.id.containers_rfadetail_benefits);

        buttonApprove.setOnClickListener(this);
        buttonReject.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
        containersAttachments.setOnClickListener(this);

        if(activity.recruitment == null){
            activity.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object tempResult;

                    try {
                        tempResult = app.onlineGateway.getRecruitmentDetail(activity.tempRecruitment.getRecruitmentRequestID());
                    }catch(Exception e){
                        tempResult = e.getMessage();
                    }

                    final Object result = tempResult;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(result instanceof String)
                                activity.finishLoading(result.toString());
                            else{
                                try {
                                    activity.recruitment = new Recruitment((JSONObject) result);
                                    updateView();
                                    activity.finishLoading();
                                }catch(Exception e){
                                    e.printStackTrace();
                                    activity.finishLoading(e.getMessage());
                                }
                            }
                        }
                    });
                }
            }).start();
        }else
            updateView();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == containersAttachments){
            linearNavFragmentActivity.changePage(new RFADetailAttachmentFragment());
        }else if(v == actionbarButtonBack || v == actionbarTitle){
            linearNavFragmentActivity.onBackPressed();
//        }else if(v == buttonApprove){
//            if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_SUBMITTED) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM, "DateProcessedByCountryManager", "Approved");
//            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM, "DateProcessedByRegionalManager", "Approved");
//            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR, "NA", "Approved");
//            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCEO, "NA", "Approved");
        }else if(v == buttonReject){
            tviewDialogRejectReason.setText("");
            dialogReject.show();
        }else if(v == buttonReturn){
            tviewDialogReturnReason.setText("");
            dialogReturn.show();
        }
    }

    private void changeApprovalStatus(final int statusID, final String keyForUpdatableDate, final String approverNotes){
        activity.startLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tempResult;
                try{
                    tempResult = app.onlineGateway.saveRecruitment(activity.recruitment.getJSONFromUpdatingRecruitment(statusID, keyForUpdatableDate, approverNotes, app), activity.recruitment.jsonize(app));
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final String result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(result.equals("OK")){
                            activity.finishLoading();
                            Toast.makeText(activity, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }else{
                            activity.finishLoading(result);
                        }
                    }
                });
            }
        }).start();
    }

    private void updateView(){
        fieldName.setText(activity.recruitment.getRequesterName());
        fieldEmail.setText(activity.recruitment.getEmail());
        fieldOffice.setText(activity.recruitment.getRequesterOfficeName());
        fieldDepartment.setText(activity.recruitment.getRequesterDepartmentName());
        fieldPhone.setText(activity.recruitment.getRequesterPhoneNumber());
        fieldCM.setText(activity.recruitment.getCMName());
        fieldDateRequested.setText(activity.recruitment.getDateRequested(app));
        fieldDateProcessedbyCM.setText(activity.recruitment.getDateProcessedByCM(app));
        fieldDateProcessedbyRM.setText(activity.recruitment.getDateProcessedBYRM(app));
        fieldPosition.setText(activity.recruitment.getPositionTypeName());
        fieldPositionFor.setText(activity.recruitment.getReplacementFor());
        fieldReasonForVacancy.setText(activity.recruitment.getReason());
        fieldOfficeOfDeployment.setText(activity.recruitment.getOfficeOfDeploymentName());
        fieldDepartmentOfDeployment.setText(activity.recruitment.getDepartmentToBeAssignedName());
        fieldTargettedStartDate.setText(activity.recruitment.getTargettedStartDate(app));
        fieldJobTitle.setText(activity.recruitment.getJobTitle());
        fieldPositionCategory.setText(activity.recruitment.getEmployeeCategoryName());
        fieldRevenue.setText(activity.recruitment.getAnnualRevenue() + " USD");
        fieldSalaryRange.setText(activity.recruitment.getSalaryRangeFrom() + " - " + activity.recruitment.getSalaryRangeTo() + " USD");
        fieldBonus.setText(activity.recruitment.getGrossBaseBonus() + " USD");
        fieldTimeBase.setText(activity.recruitment.getTimeBaseTypeName());
        fieldEmployment.setText(activity.recruitment.getEmploymentTymeName());
        fieldHoursPerWeek.setText(String.valueOf(activity.recruitment.getHorsePerWeek()));
        cboxBudgettedCost.setChecked(activity.recruitment.isBudgetedCost());
        cboxSpecificPerson.setChecked(activity.recruitment.isSpecificPerson());
        cboxPositionMayBePermanent.setChecked(activity.recruitment.isPositionMayBePermanent());

        for(Recruitment.Benefit benefit :activity.recruitment.getBenefits()){
            TextView tv = (TextView) activity.getLayoutInflater().inflate(R.layout.node_fragmentdetaillistitem, null);
            tv.setText(benefit.getBenefitName());
            tv.setTypeface(SaltApplication.myFont(activity));
            containerBenefits.addView(tv);
        }

        dialogViewReject = (RelativeLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
        tviewDialogRejectReason = (EditText)dialogViewReject.findViewById(R.id.etexts_dialogs_textinput);
        ((TextView)dialogViewReject.findViewById(R.id.tviews_dialogs_textinput)).setText("Reason for Rejection");
        dialogReject = new AlertDialog.Builder(linearNavFragmentActivity).setTitle(null).setView(dialogViewReject)
                   .setPositiveButton("Reject", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           if(tviewDialogRejectReason.getText().length()>0) {
                               if (activity.recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_SUBMITTED)
                                   changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYCM, "DateProcessedByCountryManager", tviewDialogRejectReason.getText().toString());
                               else if (activity.recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM)
                                   changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYRM, "DateProcessedByRegionalManager", tviewDialogRejectReason.getText().toString());
                               else if (activity.recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM)
                                   changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYMR, "NA", tviewDialogRejectReason.getText().toString());
                               else if (activity.recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR)
                                   changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYCEO, "NA", tviewDialogRejectReason.getText().toString());
                           }else
                               app.showMessageDialog(linearNavFragmentActivity, "Please input a reason for rejection");
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   }).create();

        dialogViewReturn = (RelativeLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
        tviewDialogReturnReason = (EditText)dialogViewReturn.findViewById(R.id.etexts_dialogs_textinput);
        ((TextView)dialogViewReturn.findViewById(R.id.tviews_dialogs_textinput)).setText("Reason for Returning");
        dialogReturn = new AlertDialog.Builder(linearNavFragmentActivity).setTitle(null).setView(dialogViewReturn)
                .setPositiveButton("Return", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(tviewDialogReturnReason.getText().length()>0) {
                            changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_OPEN, "DateProcessedByCountryManager", tviewDialogReturnReason.getText().toString());
                        }else
                            app.showMessageDialog(linearNavFragmentActivity, "Please input a reason for rejection");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }
}
