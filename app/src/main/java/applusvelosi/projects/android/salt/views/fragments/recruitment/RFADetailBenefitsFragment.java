package applusvelosi.projects.android.salt.views.fragments.recruitment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.HeaderDetailAdapter;
import applusvelosi.projects.android.salt.adapters.lists.SimpleAdapter;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/12/15.
 */
public class RFADetailBenefitsFragment extends ActionbarFragment implements AdapterView.OnItemClickListener{
    public static String KEY_RECRUITMENTJSON = "RFADetailBenefitsFragmentKeyRecruitmentJSON";

    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private ListView lv;
    private HeaderDetailAdapter adapter;
    private ArrayList<String> headerDetails;
    private ArrayList<HashMap<String, Object>> benefits;

    public static RFADetailBenefitsFragment newInstance(String arraylistOfHashMapOfAttachments){
        RFADetailBenefitsFragment frag = new RFADetailBenefitsFragment();
        Bundle b = new Bundle();
        b.putString(KEY_RECRUITMENTJSON, arraylistOfHashMapOfAttachments);
        frag.setArguments(b);
        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Other Benefits");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.fragment_listview, null);
        lv = (ListView)view.findViewById(R.id.lists_lv);

        benefits = app.gson.fromJson(getArguments().getString(KEY_RECRUITMENTJSON), app.types.arrayListOfHashmapOfStringObject);
        headerDetails = new ArrayList<String>();
        for(HashMap<String, Object> benefit :benefits)
            headerDetails.add(new StringBuilder().append(benefit.get("BenefitName")).append(HeaderDetailAdapter.DELIMETER).append(benefit.get("ActualCost").toString()).toString());

        adapter = new HeaderDetailAdapter(activity, headerDetails);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
