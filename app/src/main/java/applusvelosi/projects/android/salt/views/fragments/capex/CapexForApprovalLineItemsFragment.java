package applusvelosi.projects.android.salt.views.fragments.capex;

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

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.CapexApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalLineItemsFragment extends LinearNavActionbarFragment implements AdapterView.OnItemClickListener, ListAdapterInterface{
    public static String KEY_CAPEXHEADERID = "CapexForApprovalLineItemsFragmentCapexHeaderID";

    private CapexApprovalDetailActivity activity;
    private RelativeLayout actionbarButtonBack, actionbarButtonRefresh;
    private TextView actionbarTitle;

    private ListView lv;
    private ListAdapter adapter;

    public static CapexForApprovalLineItemsFragment newInstance(int capexHeaderID){
        CapexForApprovalLineItemsFragment frag= new CapexForApprovalLineItemsFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_CAPEXHEADERID, capexHeaderID);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (CapexApprovalDetailActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backrefresh, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarButtonRefresh = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Capex Line Items");

        actionbarButtonBack.setOnClickListener(this);
        actionbarButtonRefresh.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        syncToServer();
        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, null);
        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);

        if(activity.capexLineItems == null)
            syncToServer();
        else{
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(CapexForApprovalLineItemsFragment.this);
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;

        if(v == null){
            v = LayoutInflater.from(activity).inflate(R.layout.node_headerdetail, null);
            holder = new Holder();
            holder.tvHeader = (TextView)v.findViewById(R.id.tviews_nodes_headerdetail_header);
            holder.tvSubHeader = (TextView)v.findViewById(R.id.tviews_nodes_headerdetail_detail);
            v.setTag(holder);
        }

        holder = (Holder)v.getTag();
        CapexLineItem item = activity.capexLineItems.get(position);
        holder.tvHeader.setText(item.getCategoryName());
        holder.tvSubHeader.setText(item.getCapexNumber());
        return v;
    }

    @Override
    public int getCount() {
        return activity.capexLineItems.size();
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == actionbarButtonRefresh)
            syncToServer();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("on item clicked");
//        linearNavFragmentActivity.changePage(CapexForApprovalLineItemDetailsFragment.newInstance(app.gson.toJson(lineItems.get(position).getMap(), app.types.hashmapOfStringObject)));
    }


    private void syncToServer(){
        activity.startLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;

                try {
                    tempResult = app.onlineGateway.getCapexLineItems(getArguments().getInt(KEY_CAPEXHEADERID));
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
                            activity.finishLoading();
                            activity.capexLineItems = new ArrayList<CapexLineItem>();
                            activity.capexLineItems.addAll((ArrayList<CapexLineItem>) result);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(CapexForApprovalLineItemsFragment.this);
                        }
                    }
                });
            }
        }).start();
    }

    private class Holder{
        private TextView tvHeader, tvSubHeader;
    }


}
