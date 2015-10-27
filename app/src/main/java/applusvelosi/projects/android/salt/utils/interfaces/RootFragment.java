package applusvelosi.projects.android.salt.utils.interfaces;

//implementors pf this fragment will let the system remove all backstack before changing to this view
public interface RootFragment {
	public void enableListButtonOnSidebarAnimationFinished();
	public void disableUserInteractionsOnSidebarShown();
	public void enableUserInteractionsOnSidebarHidden();
}
