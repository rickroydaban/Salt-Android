package applusvelosi.projects.android.salt.views.fragments.capex;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.models.capex.CapexLineItemQoutation;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.CustomViewPager;
import applusvelosi.projects.android.salt.views.CapexApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.LinearNavFragmentActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalLineItemDetailsFragment extends LinearNavActionbarFragment implements ViewPager.OnPageChangeListener{
    public static String KEY = "capexForApprovalLineItemDetailFragmentKey";
    private CapexApprovalDetailActivity activity;

    //actionbar
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private TextView fieldDesc, fieldCategory, fieldQuatity, fieldUnitCost, fieldLocalAmount, fieldUSDAmount;

    private LinearLayout containerqoutationindicators;
    private RelativeLayout containerLoader;
    private ImageView ivLineItemLoader;
    private TextView tvLineItemHeader;

    private CapexLineItem capexLineItem;
    private int prevSelectedPage = 0;
    private ArrayList<QoutationFragment> qoutationFragments;
    private QoutationPagerAdapter adapter;
    private CustomViewPager pagerQuotations;
    private static ArrayList<CapexLineItemQoutation> qoutations;

    public static CapexForApprovalLineItemDetailsFragment newInstance(int pos){
        CapexForApprovalLineItemDetailsFragment frag = new CapexForApprovalLineItemDetailsFragment();
        Bundle b = new Bundle();
        b.putInt(KEY, pos);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (CapexApprovalDetailActivity)getActivity();
        capexLineItem = activity.capexLineItems.get(getArguments().getInt(KEY));
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Line Item Detail");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initialization
        View view = inflater.inflate(R.layout.fragment_capexforapprovallineitem_detail, null);
        fieldDesc = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_desc);
        fieldCategory = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_category);
        fieldQuatity = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_quantity);
        fieldUnitCost = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_unitcost);
        fieldLocalAmount = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_localamount);
        fieldUSDAmount = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_usdamount);
        containerqoutationindicators = (LinearLayout)view.findViewById(R.id.containers_capexforapprovallineitemdetail_indicators);
        containerLoader = (RelativeLayout)view.findViewById(R.id.containers_loader);
        ivLineItemLoader = (ImageView)view.findViewById(R.id.iviews_loader);
        tvLineItemHeader = (TextView)view.findViewById(R.id.tviews_loader);
        //assignments
        fieldDesc.setText(capexLineItem.getDesc());
        fieldCategory.setText(capexLineItem.getCategoryName());
        fieldQuatity.setText(SaltApplication.decimalFormat.format(capexLineItem.getQuantity()));
        fieldUnitCost.setText(capexLineItem.getUnitCost()+ " "+capexLineItem.getCurrencySymbol());
        fieldLocalAmount.setText(SaltApplication.decimalFormat.format(capexLineItem.getUnitCost()*capexLineItem.getQuantity())+" "+capexLineItem.getCurrencySymbol());
        fieldUSDAmount.setText(SaltApplication.decimalFormat.format(capexLineItem.getQuantity() * capexLineItem.getQuantity() * capexLineItem.getUsdExchangeRate()) + " USD");
        qoutationFragments = new ArrayList<QoutationFragment>();
        pagerQuotations = (CustomViewPager)view.findViewById(R.id.pager);
        adapter = new QoutationPagerAdapter(activity.getSupportFragmentManager());
        pagerQuotations.setAdapter(adapter);
        pagerQuotations.setOnPageChangeListener(this);
        ((AnimationDrawable)ivLineItemLoader.getDrawable()).start();
        syncToServer();

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
    }

    private void syncToServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try {
                    tempResult = app.onlineGateway.getCapexLineItemQoutations(capexLineItem.getCapexLineItemID());
                } catch (Exception e) {
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (result instanceof String)
                            Toast.makeText(activity, "Unable to load line items", Toast.LENGTH_LONG).show();
                        else {
                            qoutations = (ArrayList<CapexLineItemQoutation>) result;
                            if(qoutations.size() > 0){
                                containerLoader.setVisibility(View.GONE);
                                containerqoutationindicators.removeAllViews();
                                for(int i=0; i<qoutations.size(); i++){
                                    qoutationFragments.add(QoutationFragment.newInstance(i));
                                    LayoutInflater.from(activity).inflate(R.layout.pageindicator_blur, containerqoutationindicators);
                                }

                                adapter.notifyDataSetChanged();
                                ((ImageView)containerqoutationindicators.getChildAt(0)).setImageResource(R.drawable.bg_pageindicator_focus);
                            }else{
                                tvLineItemHeader.setText("No Qoutations");
                                ivLineItemLoader.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((ImageView)containerqoutationindicators.getChildAt(prevSelectedPage)).setImageResource(R.drawable.bg_pageindicator_blur);
        ((ImageView)containerqoutationindicators.getChildAt(position)).setImageResource(R.drawable.bg_pageindicator_focus);
        prevSelectedPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class QoutationPagerAdapter extends FragmentPagerAdapter {

        public QoutationPagerAdapter(FragmentManager fragmentManager){ super(fragmentManager); }

        @Override
        public Fragment getItem(int position) {
            return qoutationFragments.get(position);
        }

        @Override
        public int getCount() {
            return qoutationFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position >= getCount()) {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }

        @Override
        public int getItemPosition (Object object) {
            int index = qoutationFragments.indexOf (object);
            System.out.println("path position "+index);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }
    }

    public static class QoutationFragment extends Fragment implements FileManager.AttachmentDownloadListener{
        private CapexApprovalDetailActivity activity;
        private SaltApplication app;
        public static final String BUNDLEKEY_POS = "qoutationpos";

        private TextView tvPrimary, tvSupplier, tvInUSD, tvLocalAmount, tvFinancialScheme, tvPaymentTerm, tvNotes;
        private LinearLayout containerAttachments;
        private CapexLineItemQoutation qoutation;

        public static QoutationFragment newInstance(int pos){
            QoutationFragment frag = new QoutationFragment();
            Bundle b = new Bundle();
            b.putInt(BUNDLEKEY_POS, pos);
            frag.setArguments(b);

            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            activity = (CapexApprovalDetailActivity)getActivity();
            app = (SaltApplication)activity.getApplication();
            qoutation = qoutations.get(getArguments().getInt(BUNDLEKEY_POS));
            View v = inflater.inflate(R.layout.fragment_claimitemqoutation, null);
            tvPrimary = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_isprimary);
            tvSupplier = (TextView)v.findViewById(R.id.tviews_claimitemquotation_supplier);
            tvInUSD = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_inusd);
            tvLocalAmount = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_localamount);
            tvFinancialScheme = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_financialscheme);
            tvPaymentTerm = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_paymentterm);
            tvNotes = (TextView)v.findViewById(R.id.tviews_claimitemqoutation_notes);

            tvPrimary.setVisibility(qoutation.isPrimary() ? View.VISIBLE : View.GONE);
            tvSupplier.setText(qoutation.getSupplierName());

            tvInUSD.setText(SaltApplication.decimalFormat.format(qoutation.getAmountInUSD()) + " USD");
            tvLocalAmount.setText(SaltApplication.decimalFormat.format(qoutation.getAmount()) + " " + qoutation.getCurrencyName());
            tvFinancialScheme.setText(qoutation.getFinancingSchemeName());
            tvPaymentTerm.setText(qoutation.getPaymentTerm());
            tvNotes.setText(qoutation.getNotes());
            containerAttachments = (LinearLayout)v.findViewById(R.id.containers_qoutationattachments);

            ArrayList<Document> documents = qoutation.getDocuments();
            for(int i=0; i<documents.size(); i++){
                Document doc = documents.get(i);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.node_attachmentwithseparatorbelow, null);
                ((TextView)view.findViewById(R.id.tviews_node_tvwithseparator)).setText(doc.getDocName());
                final int docID = doc.getDocID();
                final int objectTypeID = doc.getObjectTypeID();
                final int refID = doc.getRefID();
                final String filename = doc.getDocName();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            activity.startLoading();
                            app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, QoutationFragment.this);
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            activity.finishLoading();
                        }
                    }
                });
                containerAttachments.addView(view);
            }

            return v;
        }

        @Override
        public void onAttachmentDownloadFinish(File downloadedFile) {
            app.fileManager.openDocument(activity, downloadedFile);
            activity.finishLoading();
        }

        @Override
        public void onAttachmentDownloadFailed(String errorMessage) {
            activity.finishLoading();
            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
