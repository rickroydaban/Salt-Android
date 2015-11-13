package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.adapters.spinners.ClaimItemSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimitems.MilageClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;

public class ItemInputFragmentClaims extends ItemInputFragment {	
	private TableRow trMileageHeader, trMileageFrom, trMileageTo, trMileageReturn, trMileageMileage, trMileageRate;
	private EditText etextMileageFrom, etextMileageTo, etextMileageMileage, etextMileageRate;
	private Spinner spinnerMileageType;
	private CheckBox cboxMileageReturn;

	protected ArrayList<Category> categories;
	protected  ArrayList<String> categoryNames;
	
	/*for editing existing claim items*/
	public static ItemInputFragmentClaims newInstanceForEditingClaimItem(int posClaim, int posClaimItem){
		ItemInputFragmentClaims frag = new ItemInputFragmentClaims();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, posClaim);
		b.putInt(KEY_CLAIMITEMPOS, posClaimItem);
		frag.setArguments(b);

		return frag;
	}

	/*for creating new claim items
	 * 
	 * Please take note that every time we switch back to this fragment it will always invoke onCreate(),
	 * so we can safely assume that every time we switch to this fragment the above variable values will be reseted.
	 * And this will be a problem when creating new claim items, since we will be switching to other fragments like adding attendees.
	 * 
	 * As a solution for the problem, we should have a new claim item instance in the our application class
	 * which will change only if the posClaim passed in the newInstance() is different from the previously passed posClaim
	*/
	public static ItemInputFragmentClaims newInstanceForCreatingNewClaimItem(int posClaim){ //SHOULD SET A NEW CLAIM IN THE APPLICATION CLASS!!!
		ItemInputFragmentClaims frag = new ItemInputFragmentClaims();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, posClaim);
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
//		buttonAttendees = (RelativeLayout) v.findViewById(R.id.containers_claimitemdetail_attendees);
//		tviewAttendeeCnt = (TextView) v.findViewById(R.id.tviews_claimitemdetail_attendeecount);
//		etextTaxRate = (EditText) v.findViewById(R.id.etexts_claimiteminput_taxrate);
//		etextDesc = (EditText) v.findViewById(R.id.etexts_claimiteminput_description);
//		etextNotesClientToBill = (EditText) v.findViewById(R.id.etexts_claimiteminput_notesclienttobill);
//		cboxApplyTaxRate = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_istaxapplied);
//		cboxApplyTaxRate.setOnCheckedChangeListener(this);
//		spinnerProject = (Spinner)v.findViewById(R.id.spinners_claimiteminput_project);
//		spinnerOffices = (Spinner)v.findViewById(R.id.spinners_claimiteminput_billToOffices);
//		cboxBillable = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_isBillable);
//		cboxBillable.setOnCheckedChangeListener(this);
//		//mileage
//		trMileageHeader = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_header);
//		trMileageFrom = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_from);
//		trMileageTo = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_to);
//		trMileageReturn = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_return);
//		trMileageMileage = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_mileage);
//		trMileageRate = (TableRow)v.findViewById(R.id.trs_claimiteminput_mileage_rate);
//		etextMileageFrom = (EditText)v.findViewById(R.id.etexts_claimiteminput_mileage_from);
//		etextMileageTo = (EditText)v.findViewById(R.id.etexts_claimiteminput_mileage_to);
//		cboxMileageReturn = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_mileage_isReturn);
//		etextMileageMileage = (EditText)v.findViewById(R.id.etexts_claimiteminput_mileage_mileage);
//		spinnerMileageType = (Spinner)v.findViewById(R.id.spinner_claimiteminput_mileage_mileage);
//		etextMileageRate = (EditText)v.findViewById(R.id.etexts_claimiteminput_mileage_rate);
//		//attachment
//		cboxAttachment = (CheckBox)v.findViewById(R.id.cbox_claimiteminput_attachment_header);
//		cboxAttachment.setOnCheckedChangeListener(this);
//		trAttachmentPreview = (TableRow)v.findViewById(R.id.trs_claimiteminput_attachment_preview);
//		trAttachmentAction = (TableRow)v.findViewById(R.id.trs_claimiteminput_attachment_actions);
//		tvAttachment = (TextView)v.findViewById(R.id.tviews_claimiteminput_attachment);
//		buttonTakePicture = (TextView)v.findViewById(R.id.buttons_claimiteminput_attachment_fromcamera);
//		buttonChooseFromFile = (TextView)v.findViewById(R.id.buttons_claimiteminput_attachment_fromfiles);
//
//		ArrayList<String> mileageTypes = new ArrayList<String>();
//		mileageTypes.add(MilageClaimItem.MILEAGETYPE_VAL_KILOMETER);
//		mileageTypes.add(MilageClaimItem.MILEAGETYPE_VAL_MILE);
//		spinnerMileageType.setAdapter(new SimpleSpinnerAdapter(activity, mileageTypes, NodeSize.SIZE_NORMAL));
//		//initialization of categories should be done in the thread
//		if(pd == null)
//			pd = new SaltProgressDialog(activity);
//		pd.show();
//		new Thread(new ClaimItemCategoryListGetter()).start();
//		pd.show();
//		new Thread(new ClaimItemProjectListGetter()).start();
//
//		foreignCurrencies = new ArrayList<String>();
//		for(int i=0; i<app.getCurrencies().size(); i++)
//			foreignCurrencies.add(app.getCurrencies().get(i).getCurrencySymbol());
//		spinnerForeignCurrencies.setAdapter(new ClaimItemSpinnerAdapter(activity, foreignCurrencies));
//
//		if(getArguments().containsKey(KEY_CLAIMITEMPOS)){
//			newClaimItem = claimHeader.getPreparedClaimItemForEdit();
//			attendeeFragment = ClaimItemAttendeeListFragment.newInstance(getArguments().getInt(KEY_CLAIMPOS), getArguments().getInt(KEY_CLAIMITEMPOS));
//			etextDate.setText(newClaimItem.getExpenseDate());
//			prevSelectedForeignCurr = foreignCurrencies.indexOf(newClaimItem.getForeignCurrencyName());
//			spinnerForeignCurrencies.setSelection(prevSelectedForeignCurr);
//			etextAmountForeign.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, newClaimItem.getForeignAmount()));
//			etextAmountLocal.setText(String.format(SaltApplication.DEFAULT_FLOAT_FORMAT, newClaimItem.getLocalAmount()));
//			etextTaxRate.setText(String.valueOf(newClaimItem.getTaxAmount()));
//			cboxApplyTaxRate.setChecked(newClaimItem.isTaxApplied());
//			if(newClaimItem.isBillable()){
//				cboxBillable.setOnCheckedChangeListener(null);
//				cboxBillable.setChecked(true);
//				cboxBillable.setOnCheckedChangeListener(this);
//				cboxBillable.setText(newClaimItem.getBillableCompanyName());
//			}
//
//			etextDesc.setText(newClaimItem.getDescription());
//			if(newClaimItem.hasReceipt()){
//				cboxAttachment.setChecked(true);
//				tvAttachment.setText(newClaimItem.getAttachmentName());
//			}else
//				cboxAttachment.setChecked(false);
//
//			cboxAttachment.setChecked(newClaimItem.hasReceipt());
//
//			if(newClaimItem.getCategoryTypeID() == Category.TYPE_MILEAGE){
//				MilageClaimItem mileageItem = new MilageClaimItem(newClaimItem.getMap(), app);
//				etextMileageFrom.setText(mileageItem.getMileageFrom());
//				etextMileageTo.setText(mileageItem.getMileageTo());
//				cboxMileageReturn.setChecked(mileageItem.isMileageReturn());
//				etextMileageMileage.setText(String.valueOf(mileageItem.getMileage()));
//				spinnerMileageType.setSelection(mileageTypes.indexOf(mileageItem.getMilageTypeName()));
//				etextMileageRate.setText(String.valueOf(mileageItem.getMilageRate()));
//			}
//		}else{
//			newClaimItem = claimHeader.getPreparedClaimItemForCreation();
//			attendeeFragment = ClaimItemAttendeeListFragment.newInstance(getArguments().getInt(KEY_CLAIMPOS));
//			prevSelectedForeignCurr = foreignCurrencies.indexOf(app.getStaffOffice().getBaseCurrencyThree());
//			spinnerForeignCurrencies.setSelection(prevSelectedForeignCurr);
//		}
//
//		int cnt = newClaimItem.getAttendees().size();
//		tviewAttendeeCnt.setText(String.valueOf(cnt));
//
//		spinnerForeignCurrencies.setOnItemSelectedListener(this);
//		spinnerForeignCurrencies.setTag("-1");
//		etextCurrencyLocal.setText(app.getStaffOffice().getBaseCurrencyThree());
//		etextDate.setOnFocusChangeListener(this);
//		etextDate.setOnClickListener(this);
//		etextAmountForeign.addTextChangedListener(this);
//		etextExchangeRate.addTextChangedListener(this);
//		buttonAttendees.setOnClickListener(this);
//
//		buttonTakePicture.setOnClickListener(this);
//		buttonChooseFromFile.setOnClickListener(this);
//		tvAttachment.setOnClickListener(this);

		return v;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == actionbarSaveButton) {
			Category selCat = categories.get(spinnerCategoryNames.getSelectedItemPosition());
			
			//check if required fields have appropriate values
			if(selCat.equals(HEADER_CATEGORY)) 
				spinnerCategoryNames.performClick();
			else if(cboxBillable.isChecked() && spinnerOffices.getSelectedItem().toString().equals(HEADER_BILLTO) && etextNotesClientToBill.length()==0) 
				spinnerOffices.performClick();
			else if(Float.parseFloat(etextAmountForeign.getText().toString()) > categories.get(spinnerCategoryNames.getSelectedItemPosition()).getSpendLimit())
				app.showMessageDialog(activity, "The amount requested is higher than the category's spend limit");
			else if(etextDate.length() < 1)
				app.showMessageDialog(activity, "Please select expense date");
			else if(etextDesc.length() < 1)
				app.showMessageDialog(activity, "Please input description of this item");
			else if(categories.get(spinnerCategoryNames.getSelectedItemPosition()-1).getCategoryTypeID()==1 &&
					etextMileageFrom.equals("") && etextMileageTo.equals("") && etextMileageMileage.equals("")){//type mileage
				if(etextMileageFrom.equals("")) app.showMessageDialog(activity, "Please input start location of mileage");
				else if(etextMileageTo.equals("")) app.showMessageDialog(activity, "Please input destination of mileage");
				else if(etextMileageMileage.equals("")) app.showMessageDialog(activity, "Please input mileage value");
			}else{
				int chargeToID;
				String chargeToName;
//TODO get office data from selected item in officeToChargeSpinner				 
				
//				if(cboxBillable.isChecked()){
//					Office selOffice = offices.get(spinnerOffices.getSelectedItemPosition()+1);
//					chargeToID  = selOffice.get
//				}
				
				try{
					if(getArguments().containsKey(KEY_CLAIMITEMPOS)){ //for editing claim item
//						if(categories.get(spinnerCategoryNames.getSelectedItemPosition()-1).getCategoryTypeID()==1){ //type mileage
//							newClaimItem = new ClaimItemBuilder(app, claimHeader).setAttendees(att)
//																				 .
//						}
						System.out.println("submit to edit");
					}else{
//						oldClaimItemJSON = ClaimItem.getEmptyJSON(app);
//						ClaimItem tempClaimItem = claimHeader.getPreparedClaimItemForCreation();
//						
//						newClaimItem = new ClaimItem(claimHeader, app, 
//													 Float.parseFloat(etextAmountForeign.getText().toString()), Float.parseFloat(etextAmountLocal.getText().toString()), 
//													 tempClaimItem.getAttachment(app), 
//													 tempClaimItem.getAttendees(), 
//													 selCat.getCategoryTypeID(), selCat.getName(), 
//													 companyChargeToID, companyChargeToName, currencyID, currencyName, desc, exchangeRate, expenseDate, isRechargeable, isTaxRate, hasReceipt, localCurrencyID, localCurrencyName, notes, projectID, projectName, standardExchangeRate, taxAmount)
//						System.out.println("old "+oldClaimItemJSON);
//						System.out.println("new "+claimHeader.getPreparedClaimItemForCreation().jsonize(app));						
					}
				}catch(Exception e){
					e.printStackTrace();
					app.showMessageDialog(activity, e.getMessage());
				}
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		//NOTE OTHER SPINNER CHANGE LISTENERS ARE ALREADY DEFINED IN THE PARENT CLASS
		super.onItemSelected(parent, view, pos, id);
		if(parent.getTag()!=null && Integer.parseInt(parent.getTag().toString()) != pos){
			if(parent == spinnerCategoryNames && pos>0){
				if(categories.get(pos-1).getCategoryTypeID() == 1){//type mileage
					trMileageHeader.setVisibility(View.VISIBLE);
					trMileageFrom.setVisibility(View.VISIBLE);
					trMileageTo.setVisibility(View.VISIBLE);
					trMileageReturn.setVisibility(View.VISIBLE);
					trMileageMileage.setVisibility(View.VISIBLE);
					trMileageRate.setVisibility(View.VISIBLE);
				}else{
					trMileageHeader.setVisibility(View.GONE);
					trMileageFrom.setVisibility(View.GONE);
					trMileageTo.setVisibility(View.GONE);
					trMileageReturn.setVisibility(View.GONE);
					trMileageMileage.setVisibility(View.GONE);
					trMileageRate.setVisibility(View.GONE);					
				}
			}
		}
	}

	@Override
	public void afterTextChanged(Editable e) {
		if(e.length()>0){
			if(e.hashCode() == etextAmountForeign.getEditableText().hashCode() || e.hashCode() == etextExchangeRate.getEditableText().hashCode()){
				if(e.toString().equals(".")){
					if(e.hashCode() == etextAmountForeign.getEditableText().hashCode()){
						etextAmountForeign.setText("0.0");
						etextAmountForeign.setSelection(etextAmountForeign.length());
					}else{
						etextExchangeRate.setText("0.0");
						etextExchangeRate.setSelection(etextExchangeRate.length());
					}
				}
					
				float foreignAmt = Float.parseFloat(etextAmountForeign.getText().toString());
				float exRate = Float.parseFloat(etextExchangeRate.getText().toString());
				etextAmountLocal.setText(String.valueOf(foreignAmt*exRate));			
			}			
		}
	}
	
	private class ClaimItemCategoryListGetter implements Runnable{

		@Override
		public void run() {
			final Object result;
			Object tempResult;
			try{
				tempResult = app.onlineGateway.getClaimItemCategoryByOffice();
			}catch(Exception e){
				tempResult = e.getMessage();
			}
			
			result=tempResult;
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					pd.dismiss();
					if(result instanceof String)
						app.showMessageDialog(activity, "Failed to load categories. "+result);
					else{
						categories = new ArrayList<Category>();
						categories.addAll((ArrayList<Category>)result);
						categoryNames = new ArrayList<String>();
						categoryNames.add(HEADER_CATEGORY);
						for(Category category :categories)
							categoryNames.add(category.getName());
						
						spinnerCategoryNames.setAdapter(new SimpleSpinnerAdapter(activity, categoryNames, NodeSize.SIZE_SMALL));							
						if(getArguments().containsKey(KEY_CLAIMITEMPOS))
							spinnerCategoryNames.setSelection(categoryNames.indexOf(newClaimItem.getCategoryName()));
						spinnerCategoryNames.setOnItemSelectedListener(ItemInputFragmentClaims.this);
						spinnerCategoryNames.setTag("-1");							
					}
				}
			});
		}		
	}	
	
}
