package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimPaidByCC;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimInputFragment extends ActionbarFragment implements OnItemSelectedListener{
	private final String HEADER_OFFICE = " -Office- ";
	private final String HEADER_CHARGETO = " -Cost Center- ";
	private final String HEADER_APPROVER = " -Approver- ";
	private final String HEADER_BUSINESSADVANCE = " -Business Advance- ";
	
	public static final String KEY_CLAIMPOS = "claimkey";
	//action bar buttons
	private TextView actionbarSaveButton, actionbarTitle;
	private RelativeLayout actionbarBackButton;
	private TableRow trBusinessAdvance;
	private Spinner propSpinnerTypes, propSpinnerProxyNames, propSpinnerOffices, propSpinnerCostCenters, propSpinnerApprovers, accountSpinner, propSpinnerAdvances;
	private ArrayList<String> types, claimStaffs, offices, costCenters, approvers, advances;
	private HashMap<String, Integer> advanceNumberIDs;
	private CheckBox paidByCC;
	private ClaimHeader newClaimHeader;
	private String oldClaimHeaderJSON;
	
	private ProgressDialog pd;
	
	public static ClaimInputFragment newInstance(int appClaimPos){
		ClaimInputFragment frag = new ClaimInputFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, appClaimPos);
		frag.setArguments(b);

		return frag;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarSaveButton = (TextView)actionbarLayout.findViewById(R.id.buttons_actionbar_done);
		actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitle.setText((getArguments()!=null)?"Edit Claim Header":"Create Claim Header");
		
		actionbarBackButton.setOnClickListener(this);;
		actionbarSaveButton.setOnClickListener(this);
		actionbarTitle.setOnClickListener(this);
		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_claim_input, null);
					
		trBusinessAdvance = (TableRow)v.findViewById(R.id.trs_claiminput_businessadvance);
		propSpinnerTypes = (Spinner)v.findViewById(R.id.choices_claiminput_type);
		propSpinnerProxyNames = (Spinner)v.findViewById(R.id.choices_claiminput_staff);
		propSpinnerOffices = (Spinner)v.findViewById(R.id.choices_claiminput_office);
		propSpinnerCostCenters = (Spinner)v.findViewById(R.id.choices_claiminput_chargeTo);
		propSpinnerApprovers = (Spinner)v.findViewById(R.id.choices_claiminput_approver);
		paidByCC = (CheckBox)v.findViewById(R.id.switchs_claiminput_paidbycc);
		propSpinnerAdvances = (Spinner)v.findViewById(R.id.choices_claiminput_businessadvance);
		
		types = new ArrayList<String>();
		types.add(ClaimHeader.TYPEDESC_CLAIMS);
		types.add(ClaimHeader.TYPEDESC_ADVANCES);
		types.add(ClaimHeader.TYPEDESC_LIQUIDATION);
		
//		appProxies = app.getStaff().getAllProxies(app);
		claimStaffs = new ArrayList<String>();	
//		for(StaffProxy proxy :appProxies)
//			claimStaffs.add(proxy.getName());		

		offices = new ArrayList<String>();
		offices.add((app.getStaff().getOfficeName().length()>0)?app.getStaff().getOfficeName():HEADER_OFFICE);
				
		costCenters = new ArrayList<String>();
		costCenters.add((app.getStaff().getCostCenterName().length()>0)?app.getStaff().getCostCenterName():HEADER_CHARGETO);

		approvers = new ArrayList<String>();
		approvers.add((app.getStaff().getExpenseApproverName().length()>0)?app.getStaff().getExpenseApproverName():HEADER_APPROVER);
		
		advances = new ArrayList<String>();
		advanceNumberIDs = new HashMap<String, Integer>();
		advances.add(HEADER_BUSINESSADVANCE);
				
		propSpinnerTypes.setAdapter(new SimpleSpinnerAdapter(activity, types, NodeSize.SIZE_NORMAL));
		propSpinnerProxyNames.setAdapter(new SimpleSpinnerAdapter(activity, claimStaffs, NodeSize.SIZE_NORMAL));
		propSpinnerOffices.setAdapter(new SimpleSpinnerAdapter(activity, offices, NodeSize.SIZE_NORMAL));
		propSpinnerCostCenters.setAdapter(new SimpleSpinnerAdapter(activity, costCenters, NodeSize.SIZE_NORMAL));
		propSpinnerApprovers.setAdapter(new SimpleSpinnerAdapter(activity, approvers, NodeSize.SIZE_NORMAL));
		propSpinnerAdvances.setAdapter(new SimpleSpinnerAdapter(activity, advances, NodeSize.SIZE_NORMAL));

		propSpinnerOffices.setEnabled(false);
		propSpinnerCostCenters.setEnabled(false);
		propSpinnerApprovers.setEnabled(false);
		
		//get available selection for business advance spinner
		for(ClaimHeader claimHeader :app.getMyClaims()){
			if(claimHeader.getTypeID()==ClaimHeader.TYPEKEY_ADVANCES && claimHeader.getStatusID()==ClaimHeader.STATUSKEY_PAID){
				advanceNumberIDs.put(claimHeader.getClaimNumber(), claimHeader.getClaimID());
				advances.add(claimHeader.getClaimNumber());
				propSpinnerAdvances.setAdapter(new SimpleSpinnerAdapter(activity, advances, NodeSize.SIZE_NORMAL));
				break;
			}
		}
		
		propSpinnerTypes.setTag(-1);
		propSpinnerProxyNames.setTag(-1);
		propSpinnerOffices.setTag(-1);
		propSpinnerCostCenters.setTag(-1);
		propSpinnerApprovers.setTag(-1);
		propSpinnerAdvances.setTag(-1);
		
		if(getArguments() != null){
			newClaimHeader = app.getMyClaims().get(getArguments().getInt(KEY_CLAIMPOS));
			if(newClaimHeader != null){
				String claimTypeDesc = ClaimHeader.getTypeDescriptionForKey(newClaimHeader.getTypeID());
				propSpinnerTypes.setSelection(types.indexOf(claimTypeDesc));
				
				if(claimTypeDesc.equals(ClaimHeader.TYPEDESC_LIQUIDATION)){
					trBusinessAdvance.setVisibility(View.VISIBLE);
					if(paidByCC.getVisibility() == CheckBox.VISIBLE)
						paidByCC.setVisibility(CheckBox.GONE);
				}else if(claimTypeDesc.equals(ClaimHeader.TYPEDESC_ADVANCES)){
					if(paidByCC.getVisibility() == CheckBox.VISIBLE)
						paidByCC.setVisibility(CheckBox.GONE);

					if(trBusinessAdvance.getVisibility() == CheckBox.VISIBLE)
						trBusinessAdvance.setVisibility(CheckBox.GONE);					
				}else{
					if(paidByCC.getVisibility() == CheckBox.GONE)
						paidByCC.setVisibility(CheckBox.VISIBLE);
					paidByCC.setChecked((Boolean.parseBoolean(newClaimHeader.getMap().get(ClaimPaidByCC.KEY_ISPAIDBYCOMPANYCARD).toString()))?true:false);

					
					if(trBusinessAdvance.getVisibility() == Spinner.VISIBLE)
						trBusinessAdvance.setVisibility(Spinner.GONE);	
					
					propSpinnerTypes.setEnabled(false);
					propSpinnerProxyNames.setEnabled(false);
				}
				
				propSpinnerProxyNames.setSelection(claimStaffs.indexOf(newClaimHeader.getStaffName()));
			}
		}else{ //of type claim by default
			if(paidByCC.getVisibility() == CheckBox.GONE)
				paidByCC.setVisibility(CheckBox.VISIBLE);
			
			if(trBusinessAdvance.getVisibility() == Spinner.VISIBLE)
				trBusinessAdvance.setVisibility(Spinner.GONE);				
			
		}
		
		propSpinnerTypes.setOnItemSelectedListener(this);
		
		return v;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		if(parent == propSpinnerTypes){			
			if(types.get(pos).equals(ClaimHeader.TYPEDESC_CLAIMS)){
				if(paidByCC.getVisibility() == CheckBox.GONE)
					paidByCC.setVisibility(CheckBox.VISIBLE);
				
				if(trBusinessAdvance.getVisibility() == Spinner.VISIBLE)
					trBusinessAdvance.setVisibility(Spinner.GONE);				
			}else if(types.get(pos).equals(ClaimHeader.TYPEDESC_LIQUIDATION)){
				if(paidByCC.getVisibility() == CheckBox.VISIBLE)
					paidByCC.setVisibility(CheckBox.GONE);
				
				if(trBusinessAdvance.getVisibility() == Spinner.GONE)
					trBusinessAdvance.setVisibility(Spinner.VISIBLE);				
			}else if(types.get(pos).equals(ClaimHeader.TYPEDESC_ADVANCES)){
				if(paidByCC.getVisibility() == CheckBox.VISIBLE)
					paidByCC.setVisibility(CheckBox.GONE);

				if(trBusinessAdvance.getVisibility() == CheckBox.VISIBLE)
					trBusinessAdvance.setVisibility(CheckBox.GONE);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitle){
			activity.onBackPressed();
		}else if(v == actionbarSaveButton){
			String selType = propSpinnerTypes.getSelectedItem().toString();
			String selOffice = propSpinnerOffices.getSelectedItem().toString();
			String selCostCenter = propSpinnerCostCenters.getSelectedItem().toString();
			String selApprover = propSpinnerApprovers.getSelectedItem().toString();
			String selBA = propSpinnerAdvances.getSelectedItem().toString();

			//check if required fields have values			
			if(selOffice.equals(HEADER_OFFICE) || selCostCenter.equals(HEADER_CHARGETO) || selApprover.equals(HEADER_APPROVER) || (selType.equals(ClaimHeader.TYPEDESC_LIQUIDATION)&&selBA.equals(HEADER_BUSINESSADVANCE))){ 
				if(selOffice.equals(HEADER_OFFICE))
					propSpinnerOffices.performClick();
				else if(selCostCenter.equals(HEADER_CHARGETO))
					propSpinnerCostCenters.performClick();
				else if(selApprover.equals(HEADER_APPROVER))
					propSpinnerApprovers.performClick();
				else if(selType.equals(ClaimHeader.TYPEDESC_LIQUIDATION) && selBA.equals(HEADER_BUSINESSADVANCE))
					propSpinnerAdvances.performClick();
			}
			else{
//				try{
//					final StaffProxy selectedLeavee = appProxies.get(propSpinnerProxyNames.getSelectedItemPosition());
					
//					if(getArguments() == null){ //new claim header
//						if(selType.equals(ClaimHeader.TYPEDESC_CLAIMS)){
//							oldClaimHeaderJSON = ClaimPaidByCC.getEmptyJSON(app);
//							System.out.println(paidByCC.isChecked());
//							if(paidByCC.isChecked())
//								newClaimHeader = new ClaimPaidByCC(	app.getStaff().getStaffID(), app.getStaff().getFname()+" "+app.getStaff().getLname(), app.getStaff().getEmail(),
//																	app.getStaff().getOfficeID(), app.getStaff().getOfficeName(), "",
//																	app.getStaff().getCostCenterID(), app.getStaff().getCostCenterName(),
//																	app.getStaff().getExpenseApproverID(), app.getStaff().getExpenseApproverName(), app.getStaff().getExpenseApproverEmail(),
//																	app.getStaff().getAccountID(), app.getStaff().getAccountName(), app.getStaff().getAccountEmail(),
//																	app);
//							else
//								newClaimHeader = new ClaimNotPaidByCC(	app.getStaff().getStaffID(), app.getStaff().getFname()+" "+app.getStaff().getLname(), app.getStaff().getEmail(),
//																		app.getStaff().getOfficeID(), app.getStaff().getOfficeName(), "",
//																		app.getStaff().getCostCenterID(), app.getStaff().getCostCenterName(),
//																		app.getStaff().getExpenseApproverID(), app.getStaff().getExpenseApproverName(), app.getStaff().getExpenseApproverEmail(),
//																		app.getStaff().getAccountID(), app.getStaff().getAccountName(), app.getStaff().getAccountEmail(),
//																		app);
//						}else if(selType.equals(ClaimHeader.TYPEDESC_ADVANCES)){
//							oldClaimHeaderJSON = BusinessAdvance.getEmptyJSON(app);
//							newClaimHeader = new BusinessAdvance(	app.getStaff().getStaffID(), app.getStaff().getFname()+" "+app.getStaff().getLname(), app.getStaff().getEmail(),
//																	app.getStaff().getOfficeID(), app.getStaff().getOfficeName(), "",
//																	app.getStaff().getCostCenterID(), app.getStaff().getCostCenterName(),
//																	app.getStaff().getExpenseApproverID(), app.getStaff().getExpenseApproverName(), app.getStaff().getExpenseApproverEmail(),
//																	app.getStaff().getAccountID(), app.getStaff().getAccountName(), app.getStaff().getAccountEmail(),
//																	app);
//						}else if(selType.equals(ClaimHeader.TYPEDESC_LIQUIDATION)){
//							String selClaimNum = propSpinnerAdvances.getSelectedItem().toString();
//							oldClaimHeaderJSON = LiquidationOfBA.getEmptyJSON(app);
//							newClaimHeader = new LiquidationOfBA(	app.getStaff().getStaffID(), app.getStaff().getFname()+" "+app.getStaff().getLname(), app.getStaff().getEmail(),
//																	app.getStaff().getOfficeID(), app.getStaff().getOfficeName(), "",
//																	app.getStaff().getCostCenterID(), app.getStaff().getCostCenterName(),
//																	app.getStaff().getExpenseApproverID(), app.getStaff().getExpenseApproverName(), app.getStaff().getExpenseApproverEmail(),
//																	app.getStaff().getAccountID(), app.getStaff().getAccountName(), app.getStaff().getAccountEmail(),
//																	advanceNumberIDs.get(selClaimNum), selClaimNum,
//																	app);
//						}
//					}else{ //edit claim header
//						oldClaimHeaderJSON = newClaimHeader.jsonize(app);
//						newClaimHeader.editClaimHeader(paidByCC.isChecked());
//					}

//					if(pd == null)
//						pd = new SaltProgressDialog(activity);
//					pd.show();
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							String tempResult;
//							try{
//								tempResult = app.onlineGateway.saveClaim(newClaimHeader.jsonize(app), oldClaimHeaderJSON);
//							}catch(Exception e){
//								e.printStackTrace();
//								tempResult = e.getMessage();
//							}
//
//							final String result = tempResult;
//							new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//								@Override
//								public void run() {
//									pd.dismiss();
//									if(result == null){
//										if(getArguments()!=null && Integer.parseInt(newClaimHeader.getMap().get(ClaimHeader.KEY_TYPEID).toString()) == ClaimHeader.TYPEKEY_CLAIMS){
//											int claimPos = getArguments().getInt(KEY_CLAIMPOS);
//											app.getMyClaims().add(claimPos+1, (paidByCC.isChecked())?new ClaimPaidByCC(newClaimHeader.getMap()):new ClaimNotPaidByCC(newClaimHeader.getMap()));
//											app.getMyClaims().remove(claimPos);
//											app.offlineGateway.serializeClaimHeaders(app.getMyClaims(), SerializableClaimTypes.MY_CLAIM);
//										}
//										app.showMessageDialog(activity, "Claim Created Successfully!");
//									}else{
//										//revert to its original status
//										newClaimHeader.editClaimHeader(!paidByCC.isChecked());
//										app.showMessageDialog(activity, result);
//									}
//								}
//							});
//						}
//					}).start();
//				}catch(Exception e){
//					e.printStackTrace();
//					app.showMessageDialog(activity, "asd "+e.getMessage());
//				}
			}
		}
	}
}
