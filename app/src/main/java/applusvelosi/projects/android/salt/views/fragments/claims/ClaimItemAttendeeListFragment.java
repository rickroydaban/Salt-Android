	package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.ClaimItemAttendeeAdapter;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

	/*
     * To update attendees of existing claim items, please specify a claim item position on the second parameter of the newInstance()
     * otherwise, give it a value of -1 to signify that claim item is non-existent yet but is subject for creation
     *
     * Please make sure :
     * 		1. That you SHOULD NOT changeChildPage() right after calling this.newInstance() if second parameter is negative 1
     * 		2. That you SHOULD CALL this.setTempClaimItem() first before invoking changeToChildPage() if second parameter is negative 1;
     *
     */
public class ClaimItemAttendeeListFragment extends ActionbarFragment implements OnItemClickListener{
	private static String KEY_CLAIMPOS = "claimitemattendeeelistframgnetclaimpos";
	private static String KEY_CLAIITEMMPOS = "claimitemattendeeelistframgnetclaimitempos";
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarNewButton;
	private TextView actionbarTitleTextview;
	
	private SaltProgressDialog pd;
	private ListView lv;
	private TextView tvDialogName, tvDialogJob, tvDialogNotes;
	private ClaimItemAttendeeAdapter adapter;
	private AlertDialog dialogAddAttendee;
	private ClaimItem claimItem; 
	private ArrayList<ClaimItemAttendee> attendees;
	
	//for editing existing claim item
	public static ClaimItemAttendeeListFragment newInstance(int claimPosition, int claimItemPosition){
		ClaimItemAttendeeListFragment fragment = new ClaimItemAttendeeListFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, claimPosition);
		b.putInt(KEY_CLAIITEMMPOS, claimItemPosition);
		fragment.setArguments(b);
		return fragment;
	}
			
	public static ClaimItemAttendeeListFragment newInstance(int claimPosition){
		ClaimItemAttendeeListFragment fragment = new ClaimItemAttendeeListFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_CLAIMPOS, claimPosition);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backnew, null);
		actionbarBackButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		actionbarTitleTextview = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		actionbarNewButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_add);		
					
		actionbarBackButton.setOnClickListener(this);
		actionbarTitleTextview.setOnClickListener(this);
		actionbarNewButton.setOnClickListener(this);
		return actionbarLayout;
	}
	
	@Override
	protected View createView(LayoutInflater li, ViewGroup vg, Bundle b) {
		View view = li.inflate(R.layout.fragment_listview, null);
		lv = (ListView)view.findViewById(R.id.lists_lv);	
		
		if(getArguments().containsKey(KEY_CLAIITEMMPOS))
			claimItem = app.getMyClaims().get(getArguments().getInt(KEY_CLAIMPOS)).getPreparedClaimItemForEdit();
		else 
			claimItem = app.getMyClaims().get(getArguments().getInt(KEY_CLAIMPOS)).getPreparedClaimItemForCreation();
		
		attendees = claimItem.getAttendees();
		adapter = new ClaimItemAttendeeAdapter(activity, attendees);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		actionbarTitleTextview.setText("Claim Attendees ("+attendees.size()+")");
		
		LinearLayout builderView = (LinearLayout)LayoutInflater.from(activity).inflate(R.layout.dialog_addclaimattendee, null);
		tvDialogName = (TextView)builderView.findViewById(R.id.tviews_dialogs_addclaimattendee_name);
		tvDialogJob = (TextView)builderView.findViewById(R.id.tviews_dialogs_addclaimattendee_job);
		tvDialogNotes = (TextView)builderView.findViewById(R.id.tviews_dialogs_addclaimattendee_note);
		
		dialogAddAttendee = new AlertDialog.Builder(activity).setTitle("New Attendee").setView(builderView)
							.setPositiveButton("Add", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									ClaimItemAttendee newAttendee = new ClaimItemAttendee(tvDialogName.getText().toString(), tvDialogJob.getText().toString(), tvDialogNotes.getText().toString());
									attendees.add(newAttendee);
									adapter.notifyDataSetChanged();
									claimItem.updateAttendees(app, attendees);
									actionbarTitleTextview.setText("Claim Attendees ("+attendees.size()+")");
								}
							}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create();

		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v == actionbarBackButton || v == actionbarTitleTextview){
			activity.onBackPressed();
		}else if(v == actionbarNewButton){
			dialogAddAttendee.show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, final int pos, long id) {
		new AlertDialog.Builder(activity).setMessage("This will delete "+attendees.get(pos).getName()+" in the list")
										 .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												attendees.remove(pos);
												adapter.notifyDataSetChanged();
												claimItem.updateAttendees(app, attendees);
												actionbarTitleTextview.setText("Claim Attendees ("+attendees.size()+")");
											}
										})
										.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										}).create().show();
	}
}
