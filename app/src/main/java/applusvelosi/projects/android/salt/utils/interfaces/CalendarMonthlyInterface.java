package applusvelosi.projects.android.salt.utils.interfaces;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;

public interface CalendarMonthlyInterface {

	public FragmentActivity getActivity();
	public void reloadEventList(ArrayList<String> eventLabels);
}
