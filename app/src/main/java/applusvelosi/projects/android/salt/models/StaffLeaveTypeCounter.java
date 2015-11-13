package applusvelosi.projects.android.salt.models;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class StaffLeaveTypeCounter{
	private SaltApplication app;
	private static StaffLeaveTypeCounter instance;
	
	private int approvedVLReqs ,approvedSLReqs, approvedULReqs, approvedMPLReqs, approvedDLReqs, approvedBLReqs, approvedHLReqs, approvedBTLReqs,
	pendingVLReqs, pendingULReqs;

	private float approvedVLDays ,approvedSLDays, approvedULDays, approvedMPLDays, approvedDLDays, approvedBLDays, approvedHLDays, approvedBTLDays,
	pendingVLDays, pendingSLDays, pendingULDays, pendingBLDays, pendingMPLDays, pendingHLDays;	
	
	public static StaffLeaveTypeCounter getInstance(SaltApplication key){ //must only be instantiated in the applicaiton class as possible
		if(instance == null)
			instance = new StaffLeaveTypeCounter(key);
		
		return instance;
	}
	
	private StaffLeaveTypeCounter(SaltApplication app){
		this.app = app;
	}
	
	public void syncToServer(final StaffLeaveSyncListener listener){
		System.out.println("syncing leave types to server");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Object tempMyLeavesResult;
				try{
					app.onlineGateway.updateStaff(app.getStaff().getStaffID(), app.getStaff().getSecurityLevel(), app.getStaff().getOfficeID());
					tempMyLeavesResult = app.onlineGateway.getMyLeaves();
				}catch(Exception e){
					e.printStackTrace();
					tempMyLeavesResult = e.getMessage();
				}
				
				final Object myLeavesResult = tempMyLeavesResult;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if(myLeavesResult instanceof String){
							listener.onSyncFailed(myLeavesResult.toString());
						}else{
							app.updateMyLeaves((ArrayList<Leave>)myLeavesResult);
////							if(app.getStaff().isApprover()){
////								new Thread(new Runnable() {
////									Object tempLeavesForApprovalResult;
////									@Override
////									public void run() {
////										try{
////											tempLeavesForApprovalResult = app.onlineGateway.getLeavesForApproval();
////										}catch(Exception e){
////											e.printStackTrace();
////											tempLeavesForApprovalResult = e.getMessage();
////										}
////
////										final Object leavesForApprovalResult = tempLeavesForApprovalResult;
////										new Handler(Looper.getMainLooper()).post(new Runnable() {
////
////											@Override
////											public void run() {
////												if(leavesForApprovalResult instanceof String){
////													listener.onSyncFailed(leavesForApprovalResult.toString());
////												}else{
////													app.updateLeavesForApproval((ArrayList<Leave>)leavesForApprovalResult);
////													updateValues();
////													listener.onSyncSuccess();
////												}
////											}
////										});
////									}
////								}).start();
//							}else{
								updateValues();
								listener.onSyncSuccess();
//							}
						}
					}
				});
			}
		}).start();				
	}
		
	private void updateValues(){
		approvedVLReqs = approvedSLReqs = approvedULReqs = approvedMPLReqs = approvedDLReqs = approvedBLReqs = approvedHLReqs = approvedBTLReqs = 
		pendingVLReqs = pendingULReqs = 0;
		approvedVLDays = approvedSLDays = approvedULDays = approvedMPLDays = approvedDLDays = approvedBLDays = approvedHLDays = approvedBTLDays =
		pendingVLDays = pendingSLDays = pendingULDays = pendingBLDays = pendingMPLDays = pendingHLDays = 0;			
		
		for(Leave leave :app.getMyLeaves()){
			if(leave.getStatusID() == Leave.LEAVESTATUSAPPROVEDKEY){
				if(leave.getTypeID() == Leave.LEAVETYPEVACATIONKEY) { approvedVLReqs++; approvedVLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPESICKKEY) { approvedSLReqs++; approvedSLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEMATERNITYKEY) { approvedMPLReqs++; approvedMPLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEDOCTORKEY) { approvedDLReqs++; approvedDLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEBEREAVEMENTKEY) { approvedBLReqs++; approvedBLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEHOSPITALIZATIONKEY) { approvedHLReqs++; approvedHLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEBUSINESSTRIPKEY) { approvedBTLReqs++; approvedBTLDays+=leave.getWorkingDays(); }
			}else if(leave.getStatusID() == Leave.LEAVESTATUSPENDINGID){
				if(leave.getTypeID() == Leave.LEAVETYPEVACATIONKEY) { pendingVLReqs++; pendingVLDays+=leave.getWorkingDays(); } 
				else if(leave.getTypeID() == Leave.LEAVETYPEUNPAIDKEY) { pendingULReqs++; pendingULDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPESICKKEY) { pendingSLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEBEREAVEMENTKEY) { pendingBLDays+=leave.getWorkingDays(); }
				else if(leave.getTypeID() == Leave.LEAVETYPEMATERNITYKEY) { pendingMPLDays +=leave.getWorkingDays(); }
			}
		}
	}
	
	public int getApprovedVLRequests(){
		return approvedVLReqs;
	}
	
	public int getApprovedSLRequests(){
		return approvedSLReqs;
	}
	
	public int getApprovedULRequests(){
		return approvedULReqs;
	}
	
	public int getApproveddMPLRequests(){
		return approvedMPLReqs;
	}
	
	public int getApprovedDLRequests(){
		return approvedDLReqs;
	}
	
	public int getApprovedBLRequests(){
		return approvedBLReqs;
	}
	
	public int getApprovedHLRequests(){
		return approvedHLReqs;
	}
	
	public int getApprovedBTLRequests(){
		return approvedBTLReqs;
	}
	
	public int getPendingVLRequests(){
		return pendingVLReqs;
	}
	
	public int getPendingULRequests(){
		return pendingULReqs;
	}
	
	//DAYS GETTER
	public float getApprovedVLDays(){
		return approvedVLDays;
	}
	
	public float getApprovedSLDays(){
		return approvedSLDays;
	}
	
	public float getApprovedULDays(){
		return approvedULDays;
	}
	
	public float getApprovedMPLDays(){
		return approvedMPLDays;
	}
	
	public float getApprovedDLDays(){
		return approvedDLDays;
	}
	
	public float getApprovedBLDays(){
		return approvedBLDays;
	}
	
	public float getApprovedHLDays(){
		return approvedHLDays;
	}
	
	public float getApprovedBTLDays(){
		return approvedBTLDays;
	}
	
	public float getPendingVLDays(){
		return pendingVLDays;
	}
	
	public float getPendingSLDays(){
		return pendingSLDays;
	}
	
	public float getPendingULDays(){
		return pendingULDays;
	}
	
	//REMAINIG DAYS
	public float getRemainingVLDays(){
		return app.getStaff().getMaxVL() - approvedVLDays - pendingVLDays;
	}
	
	public float getRemainingSLDays(){
		return app.getStaff().getMaxSL() - approvedSLDays - pendingSLDays;
	}
	
	public float getRemainingBLDays(){
		return app.getStaffOffice().getBereavementLimit() - approvedBLDays - pendingBLDays;
	}
	
	public float getRemainingMatPatDays(){
		return app.getStaffOffice().getMaternityLimit() - approvedMPLDays - pendingMPLDays;
	}
	
	public float getRemainingHLDays(){
		return app.getStaffOffice().getHospitalizationLimit() - approvedHLDays - pendingHLDays;
	}
	
	public interface StaffLeaveSyncListener{
		
		public void onSyncSuccess();
		public void onSyncFailed(String errorMessage);
	}
}
