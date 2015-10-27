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

import java.lang.reflect.Array;
import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.CapexLineItemQoutationAdapter;
import applusvelosi.projects.android.salt.adapters.lists.SimpleAdapter;
import applusvelosi.projects.android.salt.models.capex.CapexLineItemQoutation;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexLineItemQoutationsFragment extends ActionbarFragment implements AdapterView.OnItemClickListener{
    private static final String KEY = "CapexLineItemQoutationFragmentKey";

    private RelativeLayout actionbarButtonBack, actionbarButtonRefresh;
    private TextView actionbarTitle;

    private ArrayList<CapexLineItemQoutation> qoutations;
    private ListView lv;
    private CapexLineItemQoutationAdapter adapter;
    private SaltProgressDialog pd;

    public static CapexLineItemQoutationsFragment newInstance(int capexLineItemID){
        CapexLineItemQoutationsFragment frag = new CapexLineItemQoutationsFragment();
        Bundle b = new Bundle();
        b.putInt(KEY, capexLineItemID);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.actionbar_backrefresh, null);
        actionbarButtonBack = (RelativeLayout) actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarButtonRefresh = (RelativeLayout) actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        actionbarTitle = (TextView) actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Sales Qoutations");

        actionbarButtonBack.setOnClickListener(this);
        actionbarButtonRefresh.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview, null);
        lv = (ListView)v.findViewById(R.id.lists_lv);
        qoutations = new ArrayList<CapexLineItemQoutation>();

        adapter = new CapexLineItemQoutationAdapter(activity, qoutations);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        syncToServer();

        return v;
    }

    private void syncToServer(){
        if(pd == null)
            pd = new SaltProgressDialog(getActivity());

        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                    tempResult = app.onlineGateway.getCapexLineItemQoutations(getArguments().getInt(KEY));
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
                            qoutations.clear();
                            qoutations.addAll((ArrayList<CapexLineItemQoutation>) result);
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
        activity.changeChildPage(CapexLineItemQoutationDetailFragment.newInstance(app.gson.toJson(qoutations.get(position).getMap(), app.types.hashmapOfStringObject)));
    }
}
