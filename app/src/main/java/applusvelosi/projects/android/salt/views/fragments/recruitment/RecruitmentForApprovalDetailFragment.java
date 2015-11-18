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

import org.json.JSONObject;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RecruitmentsForApprovalFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/12/15.
 */
public class RecruitmentForApprovalDetailFragment extends LinearNavActionbarFragment {
    //actionbar
    private SaltProgressDialog pd;
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
    private RelativeLayout  containersAttachments, containersOtherBenefits;

    private TextView tviewDialogRejectReason, tviewDialogReturnReason;
    private LinearLayout dialogViewReject, dialogViewReturn;
    private AlertDialog dialogReject, dialogReturn;

    private Recruitment recruitment;

    @Override
    protected RelativeLayout setupActionbar() {
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
        recruitment = RecruitmentsForApprovalFragment.getInstance().getSelectedRecruitment(this);

        pd = new SaltProgressDialog(linearNavFragmentActivity);
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
        containersOtherBenefits = (RelativeLayout)view.findViewById(R.id.containers_rfadetail_otherbenefits);

        buttonApprove.setOnClickListener(this);
        buttonReject.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
        containersAttachments.setOnClickListener(this);
        containersOtherBenefits.setOnClickListener(this);

        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;

                try {
                    tempResult = app.onlineGateway.getRecruitmentDetail(recruitment.getRecruitmentRequestID());
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();

                        if(result instanceof String)
                            app.showMessageDialog(linearNavFragmentActivity, result.toString());
                        else{
                            try {
                                recruitment = new Recruitment((JSONObject) result, app.onlineGateway);

                                fieldName.setText(recruitment.getRequesterName());
                                fieldEmail.setText(recruitment.getEmail());
                                fieldOffice.setText(recruitment.getRequesterOfficeName());
                                fieldDepartment.setText(recruitment.getRequesterDepartmentName());
                                fieldPhone.setText(recruitment.getRequesterPhoneNumber());
                                fieldCM.setText(recruitment.getCMName());
                                fieldDateRequested.setText(recruitment.getDateRequested());
                                fieldDateProcessedbyCM.setText(recruitment.getDateProcessedByCM());
                                fieldDateProcessedbyRM.setText(recruitment.getDateProcessedByRM());
                                fieldPosition.setText(recruitment.getPositionTypeName());
                                fieldPositionFor.setText(recruitment.getReplacementFor());
                                fieldReasonForVacancy.setText(recruitment.getReason());
                                fieldOfficeOfDeployment.setText(recruitment.getOfficeOfDeploymentName());
                                fieldDepartmentOfDeployment.setText(recruitment.getDepartmentToBeAssignedName());
                                fieldTargettedStartDate.setText(recruitment.getTargettedStartDate());
                                fieldJobTitle.setText(recruitment.getJobTitle());
                                fieldPositionCategory.setText(recruitment.getEmployeeCategoryName());
                                fieldRevenue.setText(recruitment.getAnnualRevenue() + " USD");
                                fieldSalaryRange.setText(recruitment.getSalaryRangeFrom() + " - " + recruitment.getSalaryRangeTo() + " USD");
                                fieldBonus.setText(recruitment.getGrossBaseBonus() + " USD");
                                fieldTimeBase.setText(recruitment.getTimeBaseTypeName());
                                fieldEmployment.setText(recruitment.getEmploymentTymeName());
                                fieldHoursPerWeek.setText(String.valueOf(recruitment.getHorsePerWeek()));
                                cboxBudgettedCost.setChecked(recruitment.isBudgetedCost());
                                cboxSpecificPerson.setChecked(recruitment.isSpecificPerson());
                                cboxPositionMayBePermanent.setChecked(recruitment.isPositionMayBePermanent());

                                dialogViewReject = (LinearLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
                                tviewDialogRejectReason = (EditText)dialogViewReject.getChildAt(0);
                                dialogReject = new AlertDialog.Builder(linearNavFragmentActivity).setTitle("Reject").setView(dialogViewReject)
                                           .setPositiveButton("Reject", new DialogInterface.OnClickListener() {

                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   if(tviewDialogRejectReason.getText().length()>0) {
                                                       if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_SUBMITTED)
                                                           changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYCM, "DateProcessedByCountryManager", tviewDialogRejectReason.getText().toString());
                                                       else if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM)
                                                           changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYRM, "DateProcessedByRegionalManager", tviewDialogRejectReason.getText().toString());
                                                       else if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM)
                                                           changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_REJECTEDBYMR, "NA", tviewDialogRejectReason.getText().toString());
                                                       else if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR)
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

                                dialogViewReturn = (LinearLayout)LayoutInflater.from(linearNavFragmentActivity).inflate(R.layout.dialog_textinput, null);
                                tviewDialogReturnReason = (EditText)dialogViewReturn.getChildAt(0);
                                dialogReturn = new AlertDialog.Builder(linearNavFragmentActivity).setTitle("Return").setView(dialogViewReturn)
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
                            }catch(Exception e){
                                app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
                            }
                        }
                    }
                });
            }
        }).start();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == containersAttachments){
//            linearNavFragmentActivity.changePage(RFADetailAttachmentFragment.newInstance(app.gson.toJson(recruitment.getAttachments(), app.types.arrayListOfHashmapOfStringObject)));
        }else if(v == containersOtherBenefits){
//            linearNavFragmentActivity.changePage(RFADetailBenefitsFragment.newInstance(app.gson.toJson(recruitment.getOtherBenefits(), app.types.arrayListOfHashmapOfStringObject)));
        }else if(v == actionbarButtonBack || v == actionbarTitle){
            linearNavFragmentActivity.onBackPressed();
        }else if(v == buttonApprove){
            if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_SUBMITTED) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM, "DateProcessedByCountryManager", "Approved");
            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM, "DateProcessedByRegionalManager", "Approved");
            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR, "NA", "Approved");
            else if(recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR) changeApprovalStatus(Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCEO, "NA", "Approved");
        }else if(v == buttonReject){
            tviewDialogRejectReason.setText("");
            dialogReject.show();
        }else if(v == buttonReturn){
            tviewDialogReturnReason.setText("");
            dialogReturn.show();
        }
    }

    private void changeApprovalStatus(final int statusID, final String keyForUpdatableDate, final String approverNotes){
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tempResult;
                try{
                    tempResult = app.onlineGateway.saveRecruitment(recruitment.getJSONFromUpdatingRecruitment(statusID, keyForUpdatableDate, approverNotes, app), recruitment.jsonize(app));
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final String result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        new AlertDialog.Builder(linearNavFragmentActivity).setMessage((result.equals("OK"))?"Updated Successfully":result)
                                                         .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialog, int which) {
                                                                 dialog.dismiss();
                                                                 linearNavFragmentActivity.onBackPressed();
                                                             }
                                                         }).show();
                    }
                });
            }
        }).start();
    }
}
