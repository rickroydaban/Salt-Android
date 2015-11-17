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
import android.widget.ScrollView;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

public class ClaimItemAttendeeListFragment extends LinearNavActionbarFragment implements OnItemClickListener, ListAdapterInterface{
	private static final String BUNDLEKEY_ATTENDEES = "keyattendeenames";
	//action bar buttons
	private RelativeLayout actionbarBackButton, actionbarNewButton;
	private TextView actionbarTitleTextview;
	
	private SaltProgressDialog pd;
	private ListView lv;
	private ListAdapter adapter;
	private TextView tvDialogName, tvDialogJob, tvDialogNotes;
	private AlertDialog dialogAddAttendee;
	private NewClaimItemActivity activity;

	@Override
	protected RelativeLayout setupActionbar() {
        activity = (NewClaimItemActivity)getActivity();
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

		adapter = new ListAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		actionbarTitleTextview.setText("Claim Attendees ("+activity.getAttendees().size()+")");

		ScrollView builderView = (ScrollView)LayoutInflater.from(activity).inflate(R.layout.dialog_addclaimattendee, null);
		tvDialogName = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_name);
		tvDialogJob = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_job);
		tvDialogNotes = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_note);
		
		dialogAddAttendee = new AlertDialog.Builder(activity).setTitle("").setView(builderView)
							.setPositiveButton("Add", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									ClaimItemAttendee newAttendee = new ClaimItemAttendee(tvDialogName.getText().toString(), tvDialogJob.getText().toString(), tvDialogNotes.getText().toString());
                                    activity.getAttendees().add(newAttendee);
									adapter.notifyDataSetChanged();
									actionbarTitleTextview.setText("Claim Attendees ("+activity.getAttendees().size()+")");
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
		new AlertDialog.Builder(activity).setMessage("This will delete "+activity.getAttendees().get(pos).getName()+" in the list")
										 .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												activity.getAttendees().remove(pos);
												adapter.notifyDataSetChanged();
												actionbarTitleTextview.setText("Claim Attendees ("+activity.getAttendees().size()+")");
											}
										})
										.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										}).create().show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder;

		if(v == null){
			v =  activity.getLayoutInflater().inflate(R.layout.node_claimitemattendee, null);
			holder = new Holder();
			holder.tvName = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_name);
			holder.tvJobTitle = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_jobtitle);
			holder.tvDesc = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_description);

			v.setTag(holder);
		}

		holder = (Holder)v.getTag();
		ClaimItemAttendee attendee = activity.getAttendees().get(position);
		holder.tvName.setText(attendee.getName());
		holder.tvJobTitle.setText(attendee.getJobTitle());
		holder.tvDesc.setText(attendee.getNote());

		return v;
	}

	@Override
	public int getCount() {
		return activity.getAttendees().size();
	}

	private class Holder{
		public TextView tvName, tvJobTitle, tvDesc;
	}
}
