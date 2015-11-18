package applusvelosi.projects.android.salt.views.fragments.capex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexLineItem;
import applusvelosi.projects.android.salt.views.LinearNavFragmentActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexForApprovalLineItemDetailsFragment extends LinearNavActionbarFragment {
    public static String KEY = "capexForApprovalLineItemDetailFragmentKey";

    //actionbar
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private TextView fieldDesc, fieldCategory, fieldQuatity, fieldUnitCost, fieldLocalAmount, fieldUSDAmount;
    private RelativeLayout containerQoutaions;

    private CapexLineItem capexLineItem;

    public static  CapexForApprovalLineItemDetailsFragment newInstance(String hashMapStringObjectLineItemJSON){
        CapexForApprovalLineItemDetailsFragment frag = new CapexForApprovalLineItemDetailsFragment();
        Bundle b = new Bundle();
        b.putString(KEY, hashMapStringObjectLineItemJSON);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
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
        capexLineItem = new CapexLineItem(app.gson.<HashMap<String, Object>>fromJson(getArguments().getString(KEY), app.types.hashmapOfStringObject));
        //initialization
        View view = inflater.inflate(R.layout.fragment_capexforapprovallineitem_detail, null);
        fieldDesc = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_desc);
        fieldCategory = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_category);
        fieldQuatity = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_quantity);
        fieldUnitCost = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_unitcost);
        fieldLocalAmount = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_localamount);
        fieldUSDAmount = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_usdamount);
        containerQoutaions = (RelativeLayout)view.findViewById(R.id.containers_capexforapprovallineitemdetail_qoutations);
        //assignments
        fieldDesc.setText(capexLineItem.getDesc());
        fieldCategory.setText(capexLineItem.getCategoryName());
        fieldQuatity.setText(String.valueOf(capexLineItem.getQuantity()));
        fieldUnitCost.setText(capexLineItem.getUnitCost()+ " "+capexLineItem.getCurrencyThree());
        fieldLocalAmount.setText((capexLineItem.getUnitCost()*capexLineItem.getQuantity())+" "+capexLineItem.getCurrencyThree());
        fieldUSDAmount.setText((capexLineItem.getQuantity() * capexLineItem.getQuantity() * capexLineItem.getUSDExchangeRate()) + " USD");

        containerQoutaions.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == containerQoutaions)
            linearNavFragmentActivity.changePage(CapexLineItemQoutationsFragment.newInstance(capexLineItem.getCapexLineItemID()));
    }
}
