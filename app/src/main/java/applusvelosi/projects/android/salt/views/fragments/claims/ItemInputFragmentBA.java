package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.spinners.ClaimItemSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;

public class ItemInputFragmentBA extends ItemInputFragment {
	
	private ArrayList<String> categoryNames;
	
	public static ItemInputFragmentBA newInstance(int posClaim, int posClaimItem){
		ItemInputFragmentBA frag = new ItemInputFragmentBA();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, posClaim);
		b.putInt(KEY_CLAIMITEMPOS, posClaimItem);
		frag.setArguments(b);

		return frag;
	}

	@Override
	public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_claimitem_input, null);
//		claimHeader = app.getMyClaims().get(getArguments().getInt(KEY_CLAIMPOS));
//		spinnerCategoryNames = (Spinner) v.findViewById(R.id.spinners_claimiteminput_choices);
//		etextDate = (EditText) v.findViewById(R.id.etexts_claimiteminput_expenseDate);
//		spinnerForeignCurrencies = (Spinner) v.findViewById(R.id.spinners_claimiteminput_currency_fc);
//		etextAmountForeign = (EditText) v.findViewById(R.id.etexts_claimiteminput_amount_fc);
//		etextCurrencyLocal = (EditText) v.findViewById(R.id.etexts_claimiteminput_currency_lc);
//		etextAmountLocal = (EditText) v.findViewById(R.id.etexts_claimiteminput_amount_lc);
//		etextExchangeRate = (EditText) v.findViewById(R.id.etexts_claimiteminput_exrate);
//		etextTaxRate = (EditText) v.findViewById(R.id.etexts_claimiteminput_taxrate);
//		etextDesc = (EditText) v.findViewById(R.id.etexts_claimiteminput_description);
//		etextNotesClientToBill = (EditText) v.findViewById(R.id.etexts_claimiteminput_notesclienttobill);
//		cboxApplyTaxRate = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_istaxapplied);
//		cboxApplyTaxRate.setOnCheckedChangeListener(this);
//		spinnerProject = (Spinner)v.findViewById(R.id.spinners_claimiteminput_project);
//		spinnerOffices = (Spinner)v.findViewById(R.id.spinners_claimiteminput_billToOffices);
//		cboxBillable = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_isBillable);
//		cboxBillable.setOnCheckedChangeListener(this);
//		//UNLIKE CLAIMS BUSINESS ADVANCE SHOULD NOT HAVE FIELDS OF TYPE MILEAGE
//		//attachment
//		cboxAttachment = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_attachment_header);
//		cboxAttachment.setOnCheckedChangeListener(this);
//		trAttachmentPreview = (TableRow)v.findViewById(R.id.trs_claimiteminput_attachment_preview);
//		trAttachmentAction = (TableRow)v.findViewById(R.id.trs_claimiteminput_attachment_actions);
//		tvAttachment = (TextView)v.findViewById(R.id.tviews_claimiteminput_attachment);
//		buttonTakePicture = (TextView)v.findViewById(R.id.buttons_claimiteminput_attachment_fromcamera);
//		buttonChooseFromFile = (TextView)v.findViewById(R.id.buttons_claimiteminput_attachment_fromfiles);
//
//		categoryNames = new ArrayList<String>();
//		categoryNames.add(ClaimHeader.TYPEDESC_ADVANCES);
//
//		spinnerCategoryNames.setAdapter(new SimpleSpinnerAdapter(activity, categoryNames, NodeSize.SIZE_NORMAL));
//		spinnerCategoryNames.setOnItemSelectedListener(ItemInputFragmentBA.this);
//		spinnerCategoryNames.setTag("-1");
//
//		if(pd == null)
//			pd = new SaltProgressDialog(activity);
//		pd.show();
//		new Thread(new ClaimItemProjectListGetter()).start();
//
//		foreignCurrencies = new ArrayList<String>();
//		for(int i=0; i<app.getCurrencies().size(); i++)
//			foreignCurrencies.add(app.getCurrencies().get(i).getCurrencySymbol());
//		spinnerForeignCurrencies.setAdapter(new ClaimItemSpinnerAdapter(activity, foreignCurrencies));
//
//		if(getArguments().getInt(KEY_CLAIMITEMPOS) >= 0){
//			newClaimItem = claimHeader.getClaimItems(app).get(getArguments().getInt(KEY_CLAIMITEMPOS));
//			etextDate.setText(newClaimItem.getExpenseDate());
//			etextAmountForeign.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, newClaimItem.getForeignAmount()));
//			etextAmountLocal.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, newClaimItem.getLocalAmount()));
//			prevSelectedForeignCurr = foreignCurrencies.indexOf(newClaimItem.getForeignCurrencyName());
//			spinnerForeignCurrencies.setSelection(prevSelectedForeignCurr);
//			etextTaxRate.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, newClaimItem.getTaxAmount()));
//			cboxApplyTaxRate.setChecked(newClaimItem.isTaxApplied());
//			if(newClaimItem.isBillable()){
//				cboxBillable.setOnCheckedChangeListener(null);
//				cboxBillable.setChecked(true);
//				cboxBillable.setOnCheckedChangeListener(this);
//				cboxBillable.setText(newClaimItem.getBillableCompanyName());
//			}
//
//			etextDesc.setText(newClaimItem.getDescription());
//			cboxAttachment.setChecked(newClaimItem.hasReceipt());
//		}else{
//			prevSelectedForeignCurr = foreignCurrencies.indexOf(app.getStaffOffice().getBaseCurrencyThree());
//			spinnerForeignCurrencies.setSelection(prevSelectedForeignCurr);
//		}
//
//		spinnerForeignCurrencies.setOnItemSelectedListener(this);
//		spinnerForeignCurrencies.setTag("-1");
//		String currLC = app.getStaffOffice().getBaseCurrencyName(); //parse office staff currency
//		etextCurrencyLocal.setText(currLC.substring(currLC.indexOf("(")+1, currLC.indexOf(")")));
//		etextDate.setOnClickListener(this);
//		etextAmountForeign.addTextChangedListener(this);
//		etextExchangeRate.addTextChangedListener(this);
//
//
//		buttonTakePicture.setOnClickListener(this);
//		buttonChooseFromFile.setOnClickListener(this);
//		tvAttachment.setOnClickListener(this);

		return v;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		super.onItemSelected(parent, view, pos, id);
		//NOTE OTHER SPINNER CHANGE LISTENERS ARE ALREADY DEFINED IN THE PARENT CLASS
		if(parent == spinnerCategoryNames && pos>0){
		}
	}

	//override to manage negative values for business advances
	@Override
	public void afterTextChanged(Editable e) {
		if(e.hashCode() == etextAmountForeign.getEditableText().hashCode() || e.hashCode() == etextExchangeRate.getEditableText().hashCode()){				
			if(!e.toString().equals("-")){ //avoid parsing a negative character
				if(e.toString().equals("") || e.toString().equals(".")){
					if(e.hashCode() == etextAmountForeign.getEditableText().hashCode()){
						etextAmountForeign.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, (float)-1));
						etextAmountLocal.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, (float)-1));						
						etextAmountForeign.setSelection(etextAmountForeign.length());
					}else{
						etextExchangeRate.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, (float)1));				
						etextExchangeRate.setSelection(etextExchangeRate.length());
					}
				}else{
					float foreignAmt = Float.parseFloat(etextAmountForeign.getText().toString());
					if(foreignAmt > 0)
						foreignAmt = -foreignAmt;
					
					float exRate = Float.parseFloat(etextExchangeRate.getText().toString());
					etextAmountLocal.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, foreignAmt*exRate));								
				}						
			}
		}			
	}
}
