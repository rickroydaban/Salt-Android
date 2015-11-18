package applusvelosi.projects.android.salt.views.fragments.capex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexLineItemQoutation;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/13/15.
 */
public class CapexLineItemQoutationDetailFragment extends LinearNavActionbarFragment {
    public static String KEY = "capexLineItemQoutationDetailFragmentKey";

    //actionbar
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private TextView fieldSupplier, getFieldLocalAmount, fieldAmountInUSD, fieldUSDRate, fieldScheme, fieldTerm, fieldNotes;
    private RelativeLayout containerAttahcments;

    private CapexLineItemQoutation qoutation;

    public static CapexLineItemQoutationDetailFragment newInstance(String hashMapStringObjectLineItemQoutationJSON){
        CapexLineItemQoutationDetailFragment frag = new CapexLineItemQoutationDetailFragment();
        Bundle b = new Bundle();
        b.putString(KEY, hashMapStringObjectLineItemQoutationJSON);
        frag.setArguments(b);

        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Qoutation Detail");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        qoutation = new CapexLineItemQoutation(app.gson.<HashMap<String, Object>>fromJson(getArguments().getString(KEY), app.types.hashmapOfStringObject), app);
        //initialization
        View view = inflater.inflate(R.layout.fragment_capexlineitemqoutation_detail, null);
        fieldSupplier = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_supplier);
        getFieldLocalAmount = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_localamount);
        fieldAmountInUSD = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_amountinusd);
        fieldUSDRate = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_usdrate);
        fieldScheme = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_scheme);
        fieldTerm = (TextView)view.findViewById(R.id.tviews_capexlineitemqoutationdetail_payment);
        fieldNotes = (TextView)view.findViewById(R.id.tviews_capexforapprovallineitemdetail_notes);
        containerAttahcments = (RelativeLayout)view.findViewById(R.id.containers_capexlineitemqoutationdetail_attachments);
        //assignments
        fieldSupplier.setText(qoutation.getSupplierName());
        getFieldLocalAmount.setText(qoutation.getAmount()+" "+qoutation.getCurrencyThree());
        fieldAmountInUSD.setText(qoutation.getAmountInUSD()+" USD");
        fieldUSDRate.setText(String.valueOf(qoutation.getExchangeRate()));
        fieldScheme.setText(qoutation.getFinancingSchemeName());
        fieldTerm.setText(qoutation.getPaymentTerm());
        fieldNotes.setText(qoutation.getNotes());

        containerAttahcments.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
        else if(v == containerAttahcments) {
            linearNavFragmentActivity.changePage(CapexLineItemQoutationAttachmentFragment.newInstance(app.gson.toJson(qoutation.getAttachments(), app.types.arrayListOfHashmapOfStringObject)));
        }
    }
}
