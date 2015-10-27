package applusvelosi.projects.android.salt.utils.interfaces;

import android.app.Activity;

public interface DataGetterInterface {

	public Activity getSaltActivity();
	public void updateDataSource() throws Exception;
	public void onSuccess();
}
