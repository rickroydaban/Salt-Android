package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class ClaimHeaderInputType extends LinearNavActionbarFragment implements ListAdapterInterface, AdapterView.OnItemClickListener{
    //actionbar buttons
    private TextView actionbarTitle;
    private RelativeLayout actionbarButtonBack;

    private ArrayList<String> claimHeaderTypes;
    private ListAdapter adapter;
    private ListView lv;


    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Header Type");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listviewdetail, null);
        claimHeaderTypes = new ArrayList<String>();
        claimHeaderTypes.add(ClaimHeader.TYPEDESC_CLAIMS);
        claimHeaderTypes.add(ClaimHeader.TYPEDESC_ADVANCES);
        claimHeaderTypes.add(ClaimHeader.TYPEDESC_LIQUIDATION);

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if(view == null){
            holder = new Holder();
            view = linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.node_headeronly, null);
            holder.tvTitle = (TextView)view.findViewById(R.id.tviews_nodes_headeronly_header);
            view.setTag(holder);
        }

        holder = (Holder)view.getTag();
        holder.tvTitle.setText(claimHeaderTypes.get(position));

        return view;
    }

    @Override
    public int getCount() {
        return claimHeaderTypes.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
           linearNavFragmentActivity.changePage(new ClaimInputOverviewClaimFragment());
        else if(position == 1) {
            boolean canStillAddBA = true;
            for(ClaimHeader claimHeader :app.getMyClaims()){
                if(claimHeader.getTypeID() == ClaimHeader.TYPEKEY_ADVANCES && (claimHeader.getStatusID() != ClaimHeader.STATUSKEY_LIQUIDATED || claimHeader.getStatusID() != ClaimHeader.STATUSKEY_CANCELLED)){
                    canStillAddBA = false;
                    break;
                }
            }

            if(canStillAddBA)
                linearNavFragmentActivity.changePage(new ClaimInputOverviewAdvancesFragment());
            else
                app.showMessageDialog(getActivity(), "Cannot create another unliquidated Business Advance.");
        }else
            linearNavFragmentActivity.changePage(new ClaimInputOverviewLiquidationFragment());
    }

    private class Holder{
        public TextView tvTitle;
    }
}
