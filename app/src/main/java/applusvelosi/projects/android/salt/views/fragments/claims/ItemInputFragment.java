package applusvelosi.projects.android.salt.views.fragments.claims;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter;
import applusvelosi.projects.android.salt.adapters.spinners.SimpleSpinnerAdapter.NodeSize;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.FileManager.AttachmentDownloadListener;
import applusvelosi.projects.android.salt.utils.SaltDatePicker;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.CameraCaptureInterface;
import applusvelosi.projects.android.salt.utils.interfaces.FileSelectionInterface;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

public abstract class ItemInputFragment extends LinearNavActionbarFragment implements AttachmentDownloadListener,
																				OnItemSelectedListener, OnCheckedChangeListener, TextWatcher, OnFocusChangeListener {
	public static final String KEY_CLAIMITEMPOS = "claimitemkey";
	public static final String KEY_CLAIMPOS = "claimposkey";
	public static final String KEY_CLAIMITEMMAP = "claimItemMapkey";
	protected final String HEADER_CATEGORY = " -Category- ";
	protected final String HEADER_BILLTO = " -Company Code-";
	// action bar buttons
	protected TextView actionbarSaveButton, actionbarTitleTextview;
	private RelativeLayout actionbarBackButton;

	protected RelativeLayout buttonAttendees;
	protected TextView 	tviewAttendeeCnt, buttonTakePicture, buttonChooseFromFile;
	protected Spinner 	spinnerCategoryNames, spinnerForeignCurrencies, spinnerProject, spinnerOffices;
	protected EditText 	etextDate, etextCurrencyLocal, etextAmountLocal, etextAmountForeign, etextExchangeRate, etextTaxRate, etextDesc, etextNotesClientToBill;
	protected CheckBox 	cboxApplyTaxRate, cboxAttachment, cboxBillable;
	protected TableRow 	trAttachmentHeader, trAttachmentPreview, trAttachmentAction;
	protected SaltProgressDialog pd;
	
	protected ClaimItemAttendeeListFragment attendeeFragment;
	protected ArrayList<Office> offices;
	protected ArrayList<String> foreignCurrencies;
	protected ArrayList<String> projectNames, officeNames;
	protected HashMap<String, Integer> projectNameIDs;
	protected ClaimHeader claimHeader;
	protected TextView tvAttachment; 
	private File prevSelectedAttachment;
	private File capturedPhoto;
	private SimpleDateFormat sdr;
	
	//web service properties
	protected ClaimItem newClaimItem;
	protected String oldClaimItemJSON;
	
	protected int prevSelectedForeignCurr = 0; //IMPORTANT! need to take note of the prev selected currency so that it can switch back in case of lost internet connection
	@Override
	protected RelativeLayout setupActionbar() {
		sdr = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		RelativeLayout actionbarLayout = (RelativeLayout) linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backdone, null);
		actionbarBackButton = (RelativeLayout) actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarSaveButton = (TextView) actionbarLayout.findViewById(R.id.buttons_actionbar_done);
		actionbarTitleTextview = (TextView) actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarTitleTextview.setText((getArguments() == null) ? "New Claim Item" : "Manage Claim Item");
		actionbarBackButton.setOnClickListener(this);
		actionbarSaveButton.setOnClickListener(this);
		actionbarTitleTextview.setOnClickListener(this);

//		activity.setOnCameraCaptureListener(this);
//		activity.setOnFileSelectionListener(this);
		return actionbarLayout;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(parent.getTag()!=null && Integer.parseInt(parent.getTag().toString()) != pos){
			if(parent == spinnerForeignCurrencies){
				if(!etextCurrencyLocal.getText().toString().equals(spinnerForeignCurrencies.getSelectedItem().toString())){
					if(pd == null) pd = new SaltProgressDialog(linearNavFragmentActivity);
					pd.show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							Object tempResult;
							try{
								tempResult = app.onlineGateway.getForexRate(spinnerForeignCurrencies.getSelectedItem().toString(), etextCurrencyLocal.getText().toString());
							}catch(Exception e){
								tempResult = e.getMessage();
							}
							
							final Object result = tempResult;
							new Handler(Looper.getMainLooper()).post(new Runnable() {
								
								@Override
								public void run() {
									pd.dismiss();
//									try{
//										float rate = Float.parseFloat(result); //make sure rate is a float before updating the rate field
//										etextExchangeRate.setText(String.valueOf(rate));
//										cboxApplyTaxRate.setChecked(false);
//										cboxApplyTaxRate.setEnabled(false);
//									}catch(NumberFormatException e){
//										System.out.println("prev "+prevSelectedForeignCurr);
//										spinnerForeignCurrencies.setSelection(prevSelectedForeignCurr);
//										app.showMessageDialog(activity, result);
//									}
								}
							});
						}
					}).start();
				}else{
					cboxApplyTaxRate.setChecked(true);
					cboxApplyTaxRate.setEnabled(true);
					etextExchangeRate.setText("1.00");
				}
			}else if(parent == spinnerOffices){
				spinnerOffices.setVisibility(View.GONE);
				cboxBillable.setVisibility(View.VISIBLE);
				if(pos == 1){
					cboxBillable.setChecked(false);
					cboxBillable.setText("");
				}else{
					cboxBillable.setChecked(true);
					cboxBillable.setText(officeNames.get(pos));
				}
				cboxBillable.setEnabled(true);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View v) {
		if (v == actionbarBackButton || v == actionbarTitleTextview) {
			linearNavFragmentActivity.onBackPressed();
		} else if (v == etextDate) {
			new SaltDatePicker(linearNavFragmentActivity, etextDate);
		} else if (v == tvAttachment) {
//			try{
//				System.out.println(tvAttachment.getTag());
//				if(tvAttachment.getTag() != null){
//					File attachment = (File)tvAttachment.getTag();
//					String ext = "."+attachment.getName().substring(attachment.getName().lastIndexOf('.')+1, attachment.getName().length());
//					app.fileManager.openAttachment(activity, ext, attachment);
//				}else{
//					System.out.println("is null");
//					app.fileManager.openAttachment(activity, newClaimItem.getAttachmentExtension(), prevSelectedAttachment);
//				}
//			}catch(Exception e){
//				app.showMessageDialog(activity, e.getMessage());
//			}
		} else if (v == buttonTakePicture) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			try {
				capturedPhoto = File.createTempFile("saltcapturedattachment_"+sdr.format(new Date())+"_", ".jpg", new File(app.fileManager.getDirForCapturedAttachments()));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturedPhoto));
				prevSelectedAttachment = (File)tvAttachment.getTag();
				tvAttachment.setTag(capturedPhoto);
//				activity.startActivityForResult(intent, HomeActivity.RESULT_CAMERA);
			} catch (Exception e) {
				e.printStackTrace();
				app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
			}
		} else if (v == buttonChooseFromFile) {
			Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
			fileintent.setType("gagt/sdf");
//			activity.startActivityForResult(fileintent, HomeActivity.RESULT_BROWSEFILES);
		}else if(v == buttonAttendees){
//			activity.changeChildPage(attendeeFragment);
		}
	}

	@Override
	public void onFocusChange(View v, boolean isFocused) {
		if(isFocused){
			if(v == etextDate)
				new SaltDatePicker(linearNavFragmentActivity, etextDate);
		}
	}
	
//	@Override
//	public void onCameraCaptureSuccess() {
//		//uploading of attachment should be done before sending a request to create a claim item
//		updateAttachment(capturedPhoto);
//	}
	
//	@Override
//	public void onCameraCaptureFailed() {
//		tvAttachment.setTag(prevSelectedAttachment);
//		tvAttachment.setText((prevSelectedAttachment!=null)?prevSelectedAttachment.getName():"No File Chosen");
//	}
	
//	@Override
//	public void onFileSelectionSuccess(File file) {
//		//uploading of attachment should be done before sending a request to create a claim item
//		updateAttachment(file);
//	}
	
//	@Override
//	public void onFileSelectionFailed() {
//		tvAttachment.setTag(prevSelectedAttachment);
//		tvAttachment.setText((prevSelectedAttachment!=null)?prevSelectedAttachment.getName():"No File Chosen");
//	}
	
	private void updateAttachment(File file){
//		pd.show();
//		new Thread(new ClaimAttachmentUploader(file)).start();
		if(tvAttachment.getTag() != null)
			prevSelectedAttachment = (File)tvAttachment.getTag();
		tvAttachment.setTag(file);
		tvAttachment.setText(file.getName());
	}
		
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView == cboxAttachment){
//			trAttachmentPreview.setVisibility((isChecked)?View.VISIBLE:View.GONE);
//			trAttachmentAction.setVisibility((isChecked)?View.VISIBLE:View.GONE);
//			if(getArguments().containsKey(KEY_CLAIMITEMPOS)){
//				try {
//					app.fileManager.downloadAttachment(activity, newClaimItem, pd, this);
//				} catch (Exception e) {
//					e.printStackTrace();
//					app.showMessageDialog(activity, e.getMessage());
//				}
//			}
		}else if(buttonView == cboxApplyTaxRate){
			etextTaxRate.setEnabled((isChecked)?true:false);
		}else if (buttonView == cboxBillable){
			if(isChecked){
				cboxBillable.setEnabled(false);
				cboxBillable.setVisibility(View.GONE);
				spinnerOffices.setVisibility(View.VISIBLE);
				if(offices == null){
					pd.show();
					new Thread(new AllOfficeGetter()).start();					
				}
			}else{
				cboxBillable.setText("");
			}
		}
	}

	@Override
	public void onAttachmentDownloadFinish(File file) {
		prevSelectedAttachment = file;
		tvAttachment.setTag(prevSelectedAttachment);
	}
	
	@Override
	public void onAttachmentDownloadFailed(String errorMessage) {
		app.showMessageDialog(linearNavFragmentActivity, errorMessage);
	}
	
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			
	protected class ClaimItemProjectListGetter implements Runnable{

		@Override
		public void run() {
			final Object result;
			Object tempResult;
			try{
				tempResult = app.onlineGateway.getClaimItemProjectsByCostCenter(claimHeader.getCostCenterID());
			}catch(Exception e){
				tempResult = e.getMessage();
			}
			
			result=tempResult;
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					pd.dismiss();					
					if(result instanceof String){
						new AlertDialog.Builder(linearNavFragmentActivity).setMessage("Cannot load projects. "+result)
														 .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
															
															@Override
															public void onClick(DialogInterface dialog, int which) {
																linearNavFragmentActivity.onBackPressed();
															}
														}).create().show();
					}else{
						projectNameIDs = new HashMap<String, Integer>();
						projectNameIDs.putAll((HashMap<String, Integer>)result);
						projectNames = new ArrayList<String>();
						for(String projectName: projectNameIDs.keySet())
							projectNames.add(projectName);
						
						spinnerProject.setAdapter(new SimpleSpinnerAdapter(linearNavFragmentActivity, projectNames, NodeSize.SIZE_SMALL));
						if(getArguments().getInt(KEY_CLAIMITEMPOS) >= 0)
							spinnerProject.setSelection(projectNames.indexOf(newClaimItem.getProjectName()));
						spinnerProject.setOnItemSelectedListener(ItemInputFragment.this);
						spinnerProject.setTag("-1");							
					}
				}
			});
		}		
	}	

	private class AllOfficeGetter implements Runnable{

		@Override
		public void run() {
			final Object result;
			Object tempResult;
			try{
				tempResult = app.onlineGateway.getAllOffices();
			}catch(Exception e){
				tempResult = e.getMessage();
			}
			
			result=tempResult;
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					pd.dismiss();
					spinnerOffices.setVisibility(View.VISIBLE);
					cboxBillable.setVisibility(View.GONE);
					if(result instanceof String)
						app.showMessageDialog(linearNavFragmentActivity, "Cannot load offices. "+result);
					else{
						offices = new ArrayList<Office>();
						officeNames = new ArrayList<String>();
						officeNames.add(HEADER_BILLTO);
						officeNames.add("SELECT TO CANCEL");
						offices.addAll((ArrayList<Office>)result);
						for(Office office :offices)
							officeNames.add(office.getName());
						
						spinnerOffices.setAdapter(new SimpleSpinnerAdapter(linearNavFragmentActivity, officeNames, NodeSize.SIZE_SMALL));
						spinnerOffices.setOnItemSelectedListener(ItemInputFragment.this);
						spinnerOffices.setTag("0");
					}
				}
			});
		}		
	}		
	
//	private class ClaimAttachmentUploader implements Runnable{
//		private File fileToUpload;
//		
//		public ClaimAttachmentUploader(File fileToUpload){
//			this.fileToUpload = fileToUpload;
//		}
//		
//		@Override
//		public void run() {
//			Object tempResult;
//			try{
//				Document document = new Document(fileToUpload.getName(), fileToUpload.length(), app.getStaff().getStaffID(), 0);
//				tempResult = app.onlineGateway.uploadAttachment(document.jsonize(app), fileToUpload);
//			}catch(Exception e){
//				e.printStackTrace();
//				tempResult = e.getMessage();
//			}
//			final Object result = tempResult;
//			new Handler(Looper.getMainLooper()).post(new Runnable(){
//				
//				@Override
//				public void run(){
//					pd.dismiss();
//					app.showMessageDialog(activity, result.toString());
//				}
//			});
//		}
//		
//	}
}
