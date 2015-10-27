package applusvelosi.projects.android.salt.views.fragments.homepages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.StaffLeaveTypeCounter;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.fragments.HomeFragment;

public class OverviewLeavesFragment extends Fragment implements OnClickListener{
	private static OverviewLeavesFragment instance;
	
	private SaltApplication app;
//	private StaffLeaveTypeCounter leaveCounter;
	private TextView TVLeavesForApprovalLink,
					 TVconsumedVLReqs, TVconsumedSLReqs, TVconsumedULReqs, TVconsumedMPLReqs, TVconsumedDLReqs, TVconsumedBLReqs, TVconsumedHLReqs, TVconsumedBTLReqs,
					 TVpendingVLReqs, TVpendingULReqs,
					 TVconsumedVLDays, TVconsumedSLDays, TVconsumedULDays, TVconsumedMPLDays, TVconsumedDLDays, TVconsumedBLDays, TVconsumedHLDays, TVconsumedBTLDays,
					 TVpendingVLDays, TVpendingULDays,
					 TVbalanceVL, TVbalanceSL,
					 TVleavesForApproval;
	
	SaltProgressDialog pd;
	
	public static OverviewLeavesFragment getInstance(){
		if(instance == null)
			instance = new OverviewLeavesFragment();
		
		return instance;
	}
	
	public static void removeInstance(){
		if(instance != null)
			instance = null;
	}	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView scrollView = (ScrollView)inflater.inflate(R.layout.fragment_overviewleaves, null);
		TableLayout tableLayout = (TableLayout)scrollView.getChildAt(0);

		app = (SaltApplication)getActivity().getApplication();
		
		//consumed leaves
		TVconsumedVLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedVactionLeaves_reqs);
		TVconsumedVLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedVactionLeaves_days);
		TVconsumedSLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedSickLeaves_reqs);
		TVconsumedSLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedSickLeaves_days);
		TVconsumedULReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedUnpaidLeaves_reqs);
		TVconsumedULDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedUnpaidLeaves_days);
		TVconsumedMPLReqs =(TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedMatPatLeaves_reqs);
		TVconsumedMPLDays =(TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedMatPatLeaves_days);
		TVconsumedDLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedDoctorLeaves_reqs);
		TVconsumedDLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedDoctorLeaves_days);
		TVconsumedBLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedBereavementLeaves_reqs);
		TVconsumedBLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedBereavementLeaves_days);
		TVconsumedHLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedHospitalizationLeaves_reqs);
		TVconsumedHLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedHospitalizationLeaves_days);
		TVconsumedBTLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedBusinessTripLeaves_reqs);
		TVconsumedBTLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_consumedBusinessTripLeaves_days);
		//balance
		TVbalanceVL = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_balanceVacationLeaves_days);
		TVbalanceSL = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_balanceSickLeaves_days);
		//pending leaves
		TVpendingVLReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_pendingVacationLeaves_reqs);
		TVpendingVLDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_pendingVacationLeaves_days);
		TVpendingULReqs = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_pendingUnpaidLeaves_reqs);
		TVpendingULDays = (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_pendingUnpaidLeaves_days);
		//leaves for approval
		if(app.getStaff().isUser()) //user privileges only
			tableLayout.removeViews(tableLayout.getChildCount()-3, 2);
		else{
			TVLeavesForApprovalLink = (TextView)tableLayout.findViewById(R.id.tviews_overviewleaves_labels_leavesforapproval);
			TVLeavesForApprovalLink.setOnClickListener(this);
			TVleavesForApproval =  (TextView)tableLayout.findViewById(R.id.labels_overviewleaves_values_leavesForApproval);
		}
				
		return scrollView;
	}
	
	public void refresh(HomeFragment key, StaffLeaveTypeCounter leaveCounter){
		//consumed leaves reqs
		TVconsumedVLReqs.setText(String.valueOf(leaveCounter.getApprovedVLRequests()));
		TVconsumedSLReqs.setText(String.valueOf(leaveCounter.getApprovedSLRequests()));
		TVconsumedULReqs.setText(String.valueOf(leaveCounter.getApprovedULRequests()));
		TVconsumedMPLReqs.setText(String.valueOf(leaveCounter.getApproveddMPLRequests()));
		TVconsumedDLReqs.setText(String.valueOf(leaveCounter.getApprovedDLRequests()));
		TVconsumedBLReqs.setText(String.valueOf(leaveCounter.getApprovedBLRequests()));
		TVconsumedHLReqs.setText(String.valueOf(leaveCounter.getApprovedHLRequests()));
		TVconsumedBTLReqs.setText(String.valueOf(leaveCounter.getApprovedBTLRequests()));
		//consumed leaves day
		TVconsumedVLDays.setText(getString(leaveCounter.getApprovedVLDays()));
		TVconsumedSLDays.setText(getString(leaveCounter.getApprovedSLDays()));
		TVconsumedULDays.setText(getString(leaveCounter.getApprovedULDays()));
		TVconsumedMPLDays.setText(getString(leaveCounter.getApprovedMPLDays()));
		TVconsumedDLDays.setText(getString(leaveCounter.getApprovedDLDays()));
		TVconsumedBLDays.setText(getString(leaveCounter.getApprovedBLDays()));
		TVconsumedHLDays.setText(getString(leaveCounter.getApprovedHLDays()));
		TVconsumedBTLDays.setText(getString(leaveCounter.getApprovedBTLDays()));

		//balance
		TVbalanceVL.setText(getString(leaveCounter.getRemainingVLDays()));
		TVbalanceSL.setText(getString(leaveCounter.getRemainingSLDays()));
		
		//pending leaves
		TVpendingVLReqs.setText(String.valueOf(leaveCounter.getPendingVLRequests()));
		TVpendingULReqs.setText(String.valueOf(leaveCounter.getPendingULRequests()));
		TVpendingVLDays.setText(String.valueOf(leaveCounter.getPendingVLDays()));
		TVpendingULDays.setText(String.valueOf(leaveCounter.getPendingULDays()));
		
		//for approval
		if(!app.getStaff().isUser()){ //user privileges only
			TVleavesForApproval.setText((app.getStaff().isApprover())?String.valueOf(app.getLeavesForApproval().size()):"");
			int leavesForApprovalCtr = 0;
			for (Leave leave : app.getLeavesForApproval()) {
				if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID && leave.isApprover(app.getStaff().getStaffID()))
					leavesForApprovalCtr++;
			}
			TVleavesForApproval.setText(String.valueOf(leavesForApprovalCtr));
		}		
	}
	
	private String getString(float val){
		return (val == 0)?"0":String.valueOf(val);
	}

	@Override
	public void onClick(View v) {
		if(v == TVLeavesForApprovalLink){
			((HomeActivity)getActivity()).linkToLeavesForApproval(this);
		}
	}
}
