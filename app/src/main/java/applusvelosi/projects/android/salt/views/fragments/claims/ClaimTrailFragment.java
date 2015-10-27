package applusvelosi.projects.android.salt.views.fragments.claims;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.ClaimTrailAdapter;
import applusvelosi.projects.android.salt.models.ClaimTrail;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

public class ClaimTrailFragment extends ActionbarFragment implements OnItemSelectedListener{
	private static final String CLAIMTRAILKEY_CLAIMTRAILS = "claimTrailKey_claimtrails";
	private static final String CLAIMTRAILKEY_CLAIMID = "claimTrailKey_claimID";
	private RelativeLayout buttonActionbarBack, buttonActionbarAdd;
	private TextView tviewActionbarTitle;
	
	private ListView lv;
	private ClaimTrailAdapter adapter;
	private ArrayList<ClaimTrail> claimTrails;
	private AlertDialog addTrailBuilder;
	private TextView tviewDialogAddComment;
	
	public static ClaimTrailFragment newInstance(int claimID, String jsonizedItems){
		ClaimTrailFragment frag  = new ClaimTrailFragment();
		Bundle b = new Bundle();
		b.putInt(CLAIMTRAILKEY_CLAIMID, claimID);
		b.putString(CLAIMTRAILKEY_CLAIMTRAILS, jsonizedItems);
		frag.setArguments(b);
		
		return frag;
	}
	
	@Override
	protected RelativeLayout setupActionbar() {
		RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backnew, null);
		buttonActionbarBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
		buttonActionbarAdd = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_add);
		tviewActionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
		tviewActionbarTitle.setText("Claim Trails");

		buttonActionbarBack.setOnClickListener(this);
		buttonActionbarAdd.setOnClickListener(this);
		tviewActionbarTitle.setOnClickListener(this);
		
		LinearLayout builderView = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.dialog_addclaimtrail, null);
		tviewDialogAddComment = (TextView)builderView.getChildAt(0);
		addTrailBuilder = new Builder(activity).setTitle("Add Trail").setView(builderView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface di, int id) {
				claimTrails.add(new ClaimTrail(app.getStaff().getFname()+" "+app.getStaff().getLname(), app.dateTimeFormat.format(new Date()), tviewDialogAddComment.getText().toString()));
				System.out.println("added "+tviewDialogAddComment.getText());
				adapter.notifyDataSetChanged();
				di.dismiss();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface di, int id) {
				di.dismiss();
			}
		}).create();
		
		
		return actionbarLayout;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = activity.getLayoutInflater().inflate(R.layout.fragment_claim_trails, null);
		lv = (ListView)v.findViewById(R.id.lists_claimtrails);
		claimTrails = new ArrayList<ClaimTrail>();
		adapter = new ClaimTrailAdapter(this, claimTrails);
		lv.setAdapter(adapter);
		
		return v;
	}

	@Override
	public void onClick(View view) {
		if(view == buttonActionbarBack || view == tviewActionbarTitle)
			activity.onBackPressed();
		else if(view == buttonActionbarAdd){
			addTrailBuilder.show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
