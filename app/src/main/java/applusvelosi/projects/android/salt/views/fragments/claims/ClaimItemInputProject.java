package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 11/16/15.
 */
public class ClaimItemInputProject extends LinearNavActionbarFragment implements ListAdapterInterface, AdapterView.OnItemClickListener{
    private NewClaimItemActivity activity;
    private TextView actionbarTitle;
    private RelativeLayout actionbarBack;

    private ListAdapter adapter;
    private ListView lv;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (NewClaimItemActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Project");

        actionbarBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listviewdetail, null);

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);

        if(activity.getProjects() == null){

            activity.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object tempResult;
                    try{
                        tempResult = app.onlineGateway.getClaimItemProjectsByCostCenter(activity.claimHeader.getCostCenterID());
                    }catch(Exception e){
                        e.printStackTrace();
                        tempResult = e.getMessage();
                    }

                    final Object result = tempResult;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(result instanceof String)
                                activity.finishLoading(result.toString());
                            else{
                                activity.finishLoading();
                                activity.updateProjectList(ClaimItemInputProject.this, (ArrayList<Project>) result);
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(ClaimItemInputProject.this);
                            }
                        }
                    });
                }
            }).start();
        }else{
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;

        if(v == null){
            holder = new Holder();
            v = linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.node_headeronly, null);
            holder.tvName = (TextView)v.findViewById(R.id.tviews_nodes_headeronly_header);
            v.setTag(holder);
        }

        holder = (Holder)v.getTag();
        holder.tvName.setText(activity.getProjects().get(position).getName());

        return v;
    }

    @Override
    public int getCount() {
        return activity.getProjects().size();
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarBack || v == actionbarTitle){
            activity.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.updateProject(this, position);
        linearNavFragmentActivity.changePage(new ClaimItemInputCurrency());
    }

    private class Holder{
        private TextView tvName;
    }
}
