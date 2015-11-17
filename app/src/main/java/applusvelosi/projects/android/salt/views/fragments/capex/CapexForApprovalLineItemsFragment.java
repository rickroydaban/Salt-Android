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
import applusvelosi.projects.android.salt.adapters.lists.HeaderDetailAdapter;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalLineItemsFragment extends HomeActionbarFragment implements AdapterView.OnItemClickListener{
    public static String KEY_CAPEXHEADERID = "CapexForApprovalLineItemsFragmentCapexHeaderID";

    private SaltProgressDialog pd;
    private RelativeLayout actionbarButtonBack, actionbarButtonRefresh;
    private TextView actionbarTitle;

    private ListView lv;
    private HeaderDetailAdapter adapter;
    private ArrayList<String> headerDetails;
    private ArrayList<CapexLineItem> lineItems;

    public static CapexForApprovalLineItemsFragment newInstance(int capexHeaderID){
        CapexForApprovalLineItemsFragment frag= new CapexForApprovalLineItemsFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_CAPEXHEADERID, capexHeaderID);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backrefresh, null);
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
        lineItems = new ArrayList<CapexLineItem>();
        headerDetails = new ArrayList<String>();

        adapter = new HeaderDetailAdapter(activity, headerDetails);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return view;
    }

    private void syncToServer(){
        if(pd == null)
            pd = new SaltProgressDialog(activity);

        pd.show();
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
                        pd.dismiss();

                        if(result instanceof String)
                            app.showMessageDialog(activity, result.toString());
                        else{
                            lineItems.clear();
                            lineItems.addAll((ArrayList<CapexLineItem>) result);
                            headerDetails.clear();
                            for(CapexLineItem lineItem :lineItems)
                                headerDetails.add(new StringBuilder().append(lineItem.getDesc()).append(HeaderDetailAdapter.DELIMETER).append(lineItem.getAmount()).append(" ").append(lineItem.getBaseCurrencyThree()).toString());

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
        else if(v == actionbarButtonRefresh)
            syncToServer();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("on item clicked");
        activity.changeChildPage(CapexForApprovalLineItemDetailsFragment.newInstance(app.gson.toJson(lineItems.get(position).getMap(), app.types.hashmapOfStringObject)));
    }
}
