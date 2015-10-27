package applusvelosi.projects.android.salt.utils.threads;

import android.os.Handler;
import android.os.Looper;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.interfaces.LoaderInterface;

public class ClaimItemLoader implements Runnable{
	SaltApplication app;
	LoaderInterface li;
	int claimID;
	
	public ClaimItemLoader(SaltApplication app, int claimID, LoaderInterface li){
		this.app = app;
		this.claimID = claimID;
		this.li = li;
	}

	@Override
	public void run() {
		Object tempResult;
		try{
			tempResult = app.onlineGateway.getClaimItemsWithClaimID(claimID);
		}catch(Exception e){
			e.printStackTrace();
			tempResult = e.getMessage();
		}
		
		final Object result = tempResult;
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {				
				if(result instanceof String)
					li.onLoadFailed(result.toString());
				else
					li.onLoadSuccess(result);
			}
		});
	}
}
