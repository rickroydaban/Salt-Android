package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.CostCenter;
import applusvelosi.projects.android.salt.models.claimheaders.BusinessAdvance;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

/**
 * Created by Velosi on 11/5/15.
 */
public class ClaimInputOverviewAdvancesFragment extends HomeActionbarFragment {
    public static final String KEY_CLAIMPOS = "claimkey";
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle, actionbarButtonSave;

    private TextView tvType, tvStaff, tvApprover;
    private LinearLayout containerCostCenter;

    private ArrayList<CostCenter> costCenters;
    private ArrayList<LinearLayout> costCenterNodes;
    private SaltProgressDialog pd;
    private int selectedCostCenterPos = 0;

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarButtonSave = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("New Business Advance");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);
        actionbarButtonSave.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_claimheaderinputoverview_advances, null);

        tvType = (TextView) v.findViewById(R.id.tviews_claiminputoverview_type);
        tvStaff = (TextView) v.findViewById(R.id.tviews_claiminputoverview_staff);
        tvApprover = (TextView) v.findViewById(R.id.tviews_claiminputoverview_approver);

        containerCostCenter = (LinearLayout) v.findViewById(R.id.containers_claiminputoverview_costcenter);

        tvType.setText(ClaimHeader.TYPEDESC_ADVANCES);
        tvStaff.setText(app.getStaff().getFname()+" "+app.getStaff().getLname());
        tvApprover.setText(app.getStaff().getExpenseApproverName());

        costCenters = new ArrayList<CostCenter>();
        pd = new SaltProgressDialog(activity);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                    tempResult = app.onlineGateway.getCostCentersByOfficeID();
                }catch(Exception e){
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if(result instanceof String){
                            app.showMessageDialog(activity, result.toString());
                            activity.onBackPressed();
                        }else{
                            costCenters.addAll((ArrayList<CostCenter>)result);
                            costCenterNodes = new ArrayList<LinearLayout>();
                            for (int i=0; i<costCenters.size(); i++) {
                                LinearLayout node = (LinearLayout) inflater.inflate(R.layout.node_textonly, null);
                                ((TextView) node.findViewById(R.id.tviews_tviews_textonly)).setText(costCenters.get(i).getCostCenterName());
                                node.setOnClickListener(ClaimInputOverviewAdvancesFragment.this);
                                node.setTag(i);
                                costCenterNodes.add(node);
                                containerCostCenter.addView(node);
                            }

                            if (costCenters.size() == 1){ //auto selected if it is the only cost center available
                                ((TextView) (costCenterNodes.get(0).findViewById(R.id.tviews_tviews_textonly))).setTextColor(getResources().getColor(R.color.orange_velosi));
                                costCenterNodes.get(0).setOnClickListener(null);
                            }
                        }
                    }
                });
            }
        }).start();

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle){
            activity.onBackPressed();
        }else if(v == actionbarButtonSave){
            if(selectedCostCenterPos > 0){
                final String oldClaimHeaderJSON;
                final BusinessAdvance newClaimHeader;

                CostCenter selectedCostCenter = costCenters.get(selectedCostCenterPos);
                if(getArguments() == null){
                    try{
                        oldClaimHeaderJSON = BusinessAdvance.getEmptyJSON(app);
                        newClaimHeader = new BusinessAdvance(app, selectedCostCenter.getCostCenterID(), selectedCostCenter.getCostCenterName());

                        pd.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String tempResult;
                                try{
                                    tempResult = app.onlineGateway.saveClaim(newClaimHeader.jsonize(app), oldClaimHeaderJSON);
                                }catch(Exception e){
                                    e.printStackTrace();
                                    tempResult = e.getMessage();
                                }

                                final String result = tempResult;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        if(result == null){
                                            if(getArguments()!=null){
                                                int claimPos = getArguments().getInt(KEY_CLAIMPOS);
                                                app.getMyClaims().add(claimPos+1, new BusinessAdvance(newClaimHeader.getMap()));
                                                app.getMyClaims().remove(claimPos);
                                                app.offlineGateway.serializeMyClaims(app.getMyClaims());
                                                Toast.makeText(activity, "Business Advance Updated Successfully", Toast.LENGTH_SHORT).show();
                                            }else
                                                Toast.makeText(activity, "Business Advance Created Successfully", Toast.LENGTH_SHORT).show();
                                            activity.onBackPressed();
                                        }else{
                                            app.showMessageDialog(activity, result);
                                        }

                                    }
                                });
                            }
                        }).start();
                    }catch(Exception e){
                        e.printStackTrace();
                        app.showMessageDialog(activity, e.getMessage());
                    }
                }
            }else{
                app.showMessageDialog(activity, "Please select a cost center");
            }
        }else{
            if(v.getTag() != null){ //cost center nodes each has defined tags
                if(containerCostCenter.getChildCount() > 1) { //user has selected a cost center
                    int selectedPos = Integer.parseInt(v.getTag().toString());
                    selectedCostCenterPos = selectedPos;
                    System.out.println("selected pos "+selectedCostCenterPos);
                    for(int i=0, maxsize=costCenterNodes.size(); i<maxsize;){
                        if(i != selectedPos) {
                            containerCostCenter.removeViewAt(i);
                            maxsize--;
                            selectedPos--;
                        }else {
                            ((TextView)containerCostCenter.getChildAt(i).findViewById(R.id.tviews_tviews_textonly)).setTextColor(getResources().getColor(R.color.orange_velosi));
                            i++;
                        }
                    }
                }else { //user has deselected the selected cost center
                    selectedCostCenterPos = 0;
                    containerCostCenter.removeAllViews();
                    for(int i=0; i<costCenterNodes.size(); i++){
                        containerCostCenter.addView(costCenterNodes.get(i));
                        ((TextView)costCenterNodes.get(i).findViewById(R.id.tviews_tviews_textonly)).setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        }
    }
}
