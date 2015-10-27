package applusvelosi.projects.android.salt.utils.interfaces;

import android.graphics.Bitmap;
import applusvelosi.projects.android.salt.models.GatewayData;

public interface GatewayInterface {

	public GatewayData getApprovees() throws Exception;
	public GatewayData getWeeklyCalendar() throws Exception;
	public GatewayData getMonthlyCalendar() throws Exception;
	public GatewayData getLeaves() throws Exception;
	public GatewayData getClaims() throws Exception;
	public Bitmap getFlag(String flagPath) throws Exception;
}
