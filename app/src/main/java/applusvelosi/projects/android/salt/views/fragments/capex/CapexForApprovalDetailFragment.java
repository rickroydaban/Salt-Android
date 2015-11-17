package applusvelosi.projects.android.salt.views.fragments.capex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalDetailFragment extends HomeActionbarFragment implements FileManager.AttachmentDownloadListener{
    private static String KEY = "capexforapprovaldetailfragmentkey";
    //actionbar
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;
    //controls
    private TextView buttonReturn, buttonApprove, buttonReject;

    private TextView    fieldCapexNumber, fieldInvestmentType, fieldAttachment, fieldStatus, fieldTotal,
                        fieldRequesterName, fieldOffice, fieldDepartment, fieldCostCenter,
                        fieldCountryManagaer, fieldRegionalManager,
                        fieldDateSubmitted, fieldDateProcessedbyCM, fieldDateProcessedbyRM;

    private RelativeLayout  containersLineItems;
    private SaltProgressDialog pd;
    private AlertDialog ad;

    private int capexHeaderID;
    private CapexHeader capexHeader;

    public static CapexForApprovalDetailFragment newInstance(int capexHeaderID){
        CapexForApprovalDetailFragment frag = new CapexForApprovalDetailFragment();
        Bundle b = new Bundle();
        b.putInt(KEY, capexHeaderID);

        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Capex Details");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initialization
        View view = inflater.inflate(R.layout.fragment_capexforapproval_detail, null);
        buttonApprove = (TextView)view.findViewById(R.id.buttons_capexforapprovaldetail_approve);
        buttonReject = (TextView)view.findViewById(R.id.buttons_capexforapprovaldetail_reject);
        buttonReturn = (TextView)view.findViewById(R.id.buttons_capexforapprovaldetail_return);

        fieldCapexNumber = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_capexnumber);
        fieldInvestmentType = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_investment);
        fieldAttachment = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_attachment);
        fieldStatus = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_status);
        fieldTotal = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_total);
        fieldRequesterName = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_requester);
        fieldOffice = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_office);
        fieldDepartment = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_department);
        fieldCostCenter = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_costcenter);
        fieldCountryManagaer = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_cm);
        fieldRegionalManager = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_rm);
        fieldDateSubmitted = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_datesubmitted);
        fieldDateProcessedbyCM = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_processedbycm);
        fieldDateProcessedbyRM = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_processedbyrm);
        containersLineItems = (RelativeLayout)view.findViewById(R.id.containers_capexforapprovaldetail_lineitems);

        //assignment
        pd = new SaltProgressDialog(activity);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                   tempResult = app.onlineGateway.getCapexHeaderDetail(getArguments().getInt(KEY));
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if(result instanceof String)
                            new AlertDialog.Builder(activity).setMessage(result.toString())
                                                     .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {
                                                            activity.onBackPressed();
                                                         }
                                                     }).create().show();
                        else{
                            try{
                                capexHeader = new CapexHeader((JSONObject) result, app.onlineGateway);
                                fieldCapexNumber.setText(capexHeader.getCapexNumber());
                                fieldInvestmentType.setText(capexHeader.getInvestmentTypeName());
                                fieldAttachment.setText(capexHeader.getAttachedCer());
                                fieldStatus.setText(capexHeader.getStatusName());
                                fieldTotal.setText(capexHeader.getTotal()+" USD");
                                fieldRequesterName.setText(capexHeader.getRequesterName());
                                fieldOffice.setText(capexHeader.getOfficeName());
                                fieldDepartment.setText(capexHeader.getDepartmentName());
                                fieldCostCenter.setText(capexHeader.getCostCenterName());
                                fieldCountryManagaer.setText(capexHeader.getCMName());
                                fieldRegionalManager.setText(capexHeader.getRMname());
                                fieldDateSubmitted.setText(capexHeader.getDateSubmitted());
                                fieldDateProcessedbyCM.setText(capexHeader.getDateProcessedByCM());
                                fieldDateProcessedbyRM.setText(capexHeader.getDateProcessedBYRM());

                                if(!capexHeader.getAttachedCer().equals(CapexHeader.NOATTACHMENT)){
                                    fieldAttachment.setOnClickListener(CapexForApprovalDetailFragment.this);
                                    fieldAttachment.setTextColor(activity.getResources().getColor(R.color.orange_velosi));
                                    fieldAttachment.setTypeface(fieldAttachment.getTypeface(), Typeface.BOLD);
                                }
                                containersLineItems.setOnClickListener(CapexForApprovalDetailFragment.this);
                                buttonApprove.setOnClickListener(CapexForApprovalDetailFragment.this);
                                buttonReject.setOnClickListener(CapexForApprovalDetailFragment.this);
                                buttonReturn.setOnClickListener(CapexForApprovalDetailFragment.this);
                            }catch(Exception e){
                                new AlertDialog.Builder(activity).setMessage(e.getMessage())
                                        .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                activity.onBackPressed();
                                            }
                                        }).create().show();
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
        if(v == containersLineItems)
            activity.changeChildPage(CapexForApprovalLineItemsFragment.newInstance(capexHeader.getCapexID()));
        else if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
        else if(v == buttonApprove){
            if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_SUBMITTED) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCM, "DateProcessedByCountryManager");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYCM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYRM, "DateProcessedByRegionalManager");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYRM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCFO, "NA");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYCFO) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCEO, "NA");
        }else if(v == buttonReject){
            if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_SUBMITTED) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCM, "DateProcessedByCountryManager");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYCM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYRM, "DateProcessedByRegionalManager");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYRM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCFO, "NA");
            else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYCFO) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCEO, "NA");
        }else if(v == buttonReturn){
            changeApprovalStatus(CapexHeader.CAPEXHEADERID_OPEN, "NA");
        }else if(v == fieldAttachment){
            try{
                HashMap<String, Object> doc = capexHeader.getDocuments().get(0);
                int docID = Integer.parseInt(doc.get("DocID").toString());
                int objectTypeID = Integer.parseInt(doc.get("ObjectType").toString());
                int refID = Integer.parseInt(doc.get("RefID").toString());
                String filename = doc.get("DocName").toString();
                app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, pd, this);
            }catch(Exception e){
                app.showMessageDialog(activity, e.getMessage());
            }
        }
    }

    private void changeApprovalStatus(final int statusID, final String keyForUpdatableDate){
        if(pd == null)
            pd = new SaltProgressDialog(activity);

        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tempResult;
                try{
                    tempResult = app.onlineGateway.saveCapex(capexHeader.getJSONFromUpdatingCapex(statusID, keyForUpdatableDate, app), capexHeader.jsonize(app));
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final String result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        new AlertDialog.Builder(activity).setMessage((result.equals("OK"))?"Updated Successfully":result)
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        activity.onBackPressed();
                                    }
                                }).show();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onAttachmentDownloadFinish(File downloadedFile) {
        app.fileManager.openDocument(activity, downloadedFile);
    }

    @Override
    public void onAttachmentDownloadFailed(String errorMessage) {
        app.showMessageDialog(activity, errorMessage);
    }
}

