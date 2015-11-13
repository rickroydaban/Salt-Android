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

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class ClaimItemInputCurrency extends ActionbarFragment implements ListAdapterInterface, AdapterView.OnItemClickListener{
    public static final String KEY_CATEGORY = "categorykey";
    //actionbar buttons
    private TextView actionbarTitle;
    private RelativeLayout actionbarButtonBack;

    private ListAdapter adapter;
    private ListView lv;
    private int thisOfficeCurr;

    private Category category;

    public static ClaimItemInputCurrency newInstance(Category category){
        ClaimItemInputCurrency frag = new ClaimItemInputCurrency();
        Bundle b = new Bundle();
        b.putSerializable(KEY_CATEGORY, category);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Currency");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listviewdetail, null);

        category = (Category)getArguments().getSerializable(KEY_CATEGORY);
        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        for(int i=0; i < app.getCurrencies().size(); i++){
            Currency curr = app.getCurrencies().get(i);
            if(curr.getCurrencySymbol().equals(app.getStaffOffice().getBaseCurrencyThree())){
                lv.setSelection(i);
                break;
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if(view == null){
            holder = new Holder();
            view = activity.getLayoutInflater().inflate(R.layout.node_headeronly, null);
            holder.tvTitle = (TextView)view.findViewById(R.id.tviews_nodes_headeronly_header);
            view.setTag(holder);
        }

        holder = (Holder)view.getTag();
        Currency currency = app.getCurrencies().get(position);
        holder.tvTitle.setText(currency.getCurrencySymbol() + " (" + currency.getCurrencyName() + ")");

        return view;
    }

    @Override
    public int getCount() {
        return app.getCurrencies().size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.changeChildPage(ClaimItemInputGeneric.newInstance(category, app.getCurrencies().get(position)));
    }

    private class Holder{
        public TextView tvTitle;
    }
}
