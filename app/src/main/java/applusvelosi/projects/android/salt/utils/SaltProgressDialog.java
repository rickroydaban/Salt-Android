package applusvelosi.projects.android.salt.utils;

import android.app.ProgressDialog;
import android.content.Context;
import applusvelosi.projects.android.salt.R;

public class SaltProgressDialog extends ProgressDialog{
	private int pdCnt;
	
	public SaltProgressDialog(Context context) {
		super(context);

		super.setMessage("Loading");
		super.setCancelable(false);
		super.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		super.setIndeterminate(true);
		super.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.anim_loadingdialog));
		pdCnt = 0;
	}
		
	@Override
	public void show() {
		try{
			pdCnt++;
			if(!super.isShowing())
				super.show();			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void dismiss() {
		pdCnt--;
		if(pdCnt == 0)
			super.dismiss();
	}
	
}
