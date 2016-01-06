package applusvelosi.projects.android.salt.views.fragments.capex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.CapexApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalDetailFragment extends LinearNavActionbarFragment implements FileManager.AttachmentDownloadListener{
    private CapexApprovalDetailActivity activity;
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

    private LinearLayout  containersLineItems;
    private ImageView ivLineItemLoader;
    private TextView tvLineItemHeader;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (CapexApprovalDetailActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
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
        final View view = inflater.inflate(R.layout.fragment_capexforapproval_detail, null);
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
        containersLineItems = (LinearLayout)view.findViewById(R.id.containers_capexforapprovaldetail_lineitems);
        ivLineItemLoader = (ImageView)view.findViewById(R.id.iviews_loader);
        tvLineItemHeader = (TextView)view.findViewById(R.id.tviews_capexforapprovaldetail_lineitemheader);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(activity.capexHeader == null){
            activity.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object tempResult;
                    try{
                        tempResult = app.onlineGateway.getCapexHeaderDetail(activity.capexHeaderID);
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
                                try{
                                    activity.capexHeader = new CapexHeader((JSONObject) result);
                                    System.out.println("SALTX capexheader "+activity.capexHeader);
                                    updateViews();
                                    activity.finishLoading();
                                    ((AnimationDrawable)ivLineItemLoader.getDrawable()).start();
                                    syncLineItems();
                                }catch(Exception e){
                                    e.printStackTrace();
                                    System.out.println("SALTX "+e.getMessage());
                                    ivLineItemLoader.setVisibility(View.GONE);
                                    tvLineItemHeader.setText("Asset Detail Line Items");
                                    activity.finishLoading(e.getMessage());
                                }

                            }
                        }
                    });
                }
            }).start();
        }else {
            updateViews();

            tvLineItemHeader.setText("Asset Detail Line Items");
            ivLineItemLoader.setVisibility(View.GONE);
            for(int i=0; i<activity.capexLineItems.size(); i++){
                final int pos = i;
                CapexLineItem item = activity.capexLineItems.get(i);
                View v = LayoutInflater.from(activity).inflate(R.layout.node_tvwithsepartorabove, null);
                v.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        linearNavFragmentActivity.changePage(CapexForApprovalLineItemDetailsFragment.newInstance(pos));
                    }
                });

                ((TextView)v.findViewById(R.id.tviews_node_tvwithseparator)).setText(item.getCapexNumber());
                containersLineItems.addView(v);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == buttonApprove){
            if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_SUBMITTED) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCM, "DateProcessedByCountryManager");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYCM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYRM, "DateProcessedByRegionalManager");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYRM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCFO, "NA");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYCFO) changeApprovalStatus(CapexHeader.CAPEXHEADERID_APPROVEDBYCEO, "NA");
        }else if(v == buttonReject){
            if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_SUBMITTED) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCM, "DateProcessedByCountryManager");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYCM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYRM, "DateProcessedByRegionalManager");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYRM) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCFO, "NA");
            else if(activity.capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYCFO) changeApprovalStatus(CapexHeader.CAPEXHEADERID_REJECTEDBYCEO, "NA");
        }else if(v == buttonReturn){
            changeApprovalStatus(CapexHeader.CAPEXHEADERID_OPEN, "NA");
        }else if(v == fieldAttachment){
            if(!activity.capexHeader.getAttachedCer().equals(CapexHeader.NOATTACHMENT)) {
                try {
                    System.out.println("SALTX will click attachment");
                    Document doc = activity.capexHeader.getDocuments().get(0);
                    int docID = doc.getDocID();
                    int objectTypeID = doc.getObjectTypeID();
                    int refID = doc.getRefID();
                    String filename = doc.getDocName();
                    activity.startLoading();
                    app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.finishLoading();
                    app.showMessageDialog(activity, e.getMessage());
                }
            }
        }
    }

    private void changeApprovalStatus(final int statusID, final String keyForUpdatableDate){
        activity.startLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tempResult;
                try{
                    tempResult = app.onlineGateway.saveCapex(activity.capexHeader.getJSONFromUpdatingCapex(statusID, keyForUpdatableDate, app), activity.capexHeader.jsonize(app));
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
                        }else
                            activity.finishLoading();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onAttachmentDownloadFinish(File downloadedFile) {
        activity.finishLoading();
        app.fileManager.openDocument(linearNavFragmentActivity, downloadedFile);
    }

    @Override
    public void onAttachmentDownloadFailed(String errorMessage) {
        activity.finishLoading();
        app.showMessageDialog(linearNavFragmentActivity, errorMessage);
    }

    private void updateViews(){
        fieldCapexNumber.setText(activity.capexHeader.getCapexNumber());
        fieldInvestmentType.setText(activity.capexHeader.getInvestmentTypeName());
        fieldAttachment.setText(activity.capexHeader.getAttachedCer());
        fieldStatus.setText(activity.capexHeader.getStatusName());
        fieldTotal.setText(activity.capexHeader.getTotal()+" USD");
        fieldRequesterName.setText(activity.capexHeader.getRequesterName());
        fieldOffice.setText(activity.capexHeader.getOfficeName());
        fieldDepartment.setText(activity.capexHeader.getDepartmentName());
        fieldCostCenter.setText(activity.capexHeader.getCostCenterName());
        fieldCountryManagaer.setText(activity.capexHeader.getCMName());
        fieldRegionalManager.setText(activity.capexHeader.getRMname());
        fieldDateSubmitted.setText(activity.capexHeader.getDateSubmitted(app));
        fieldDateProcessedbyCM.setText(activity.capexHeader.getDateProcessedByCM(app));
        fieldDateProcessedbyRM.setText(activity.capexHeader.getDateProcessedBYRM(app));

        if(!activity.capexHeader.getAttachedCer().equals(CapexHeader.NOATTACHMENT)){
            fieldAttachment.setOnClickListener(CapexForApprovalDetailFragment.this);
            fieldAttachment.setTextColor(linearNavFragmentActivity.getResources().getColor(R.color.orange_velosi));
        }
        buttonApprove.setOnClickListener(CapexForApprovalDetailFragment.this);
        buttonReject.setOnClickListener(CapexForApprovalDetailFragment.this);
        buttonReturn.setOnClickListener(CapexForApprovalDetailFragment.this);
    }

    private void syncLineItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;

                try {
                    tempResult = app.onlineGateway.getCapexLineItems(activity.capexHeader.getCapexID());
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tvLineItemHeader.setText("Asset Detail Line Items");
                        ivLineItemLoader.setVisibility(View.GONE);
                        if(result instanceof String)
                            Toast.makeText(activity, "Unable to load line items", Toast.LENGTH_LONG).show();
                        else{
                            activity.capexLineItems = new ArrayList<CapexLineItem>();
                            activity.capexLineItems.addAll((ArrayList<CapexLineItem>) result);
                            for(int i=0; i<activity.capexLineItems.size(); i++){
                                final int pos = i;
                                CapexLineItem item = activity.capexLineItems.get(i);
                                View v = LayoutInflater.from(activity).inflate(R.layout.node_tvwithsepartorabove, null);
                                v.setOnClickListener(new View.OnClickListener(){

                                    @Override
                                    public void onClick(View v) {
                                        linearNavFragmentActivity.changePage(CapexForApprovalLineItemDetailsFragment.newInstance(pos));
                                    }
                                });

                                ((TextView)v.findViewById(R.id.tviews_node_tvwithseparator)).setText(item.getCapexNumber());
                                containersLineItems.addView(v);
                            }
                        }
                    }
                });
            }
        }).start();
    }
}

