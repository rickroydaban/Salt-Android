package applusvelosi.projects.android.salt.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Window;
import android.widget.ImageView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.ImageSetter;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class ViewReceiptDialog extends Dialog{
	private ImageView iv;
	
	public ViewReceiptDialog(HomeActivity activity, Uri filePath) {
		super(activity);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		                                                                                                                                                    
		setContentView(R.layout.iv_receipt);
		iv = (ImageView)findViewById(R.id.iviews_itemreceipt);
		new Thread(new ImageSetter(activity, filePath, iv)).start();
	}
	
	public ViewReceiptDialog(Context context, Drawable drawable){
		super(context);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.iv_receipt);
		iv = (ImageView)findViewById(R.id.iviews_itemreceipt);
		iv.setImageDrawable(drawable);
	}

}
