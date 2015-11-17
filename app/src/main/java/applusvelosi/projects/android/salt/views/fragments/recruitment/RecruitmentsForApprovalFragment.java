package applusvelosi.projects.android.salt.views.fragments.recruitment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.RecruitmentForApprovalAdapter;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.fragments.HomeActionbarFragment;

/**
 * Created by Velosi on 10/9/15.
 */
public class RecruitmentsForApprovalFragment extends HomeActionbarFragment implements RootFragment, AdapterView.OnItemClickListener {
    private static RecruitmentsForApprovalFragment instance;

    private RelativeLayout actionbarMenuButton, actionbarRefreshButton;

    private ArrayList<Recruitment> recruitments;
    private ListView lv;
    private RecruitmentForApprovalAdapter adapter;
    private SaltProgressDialog pd;

    private int selectedPos;

    public static RecruitmentsForApprovalFragment getInstance(){
        if(instance == null)
            instance = new RecruitmentsForApprovalFragment();

        return instance;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
        actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
        actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        ((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Recruitments for Approval");

        actionbarMenuButton.setOnClickListener(this);
        actionbarRefreshButton.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview, null);
        lv = (ListView)v.findViewById(R.id.lists_lv);
        recruitments = new ArrayList<Recruitment>();

        adapter = new RecruitmentForApprovalAdapter(activity, recruitments);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        pd = new SaltProgressDialog(getActivity());
        syncToServer();

        return v;
    }

    private void syncToServer(){

        pd.show();;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                    tempResult = app.onlineGateway.getRecruitmentsForApproval();
                }catch (Exception e){
                    e.printStackTrace();
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler((Looper.getMainLooper())).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if(result instanceof  String)
                            app.showMessageDialog(activity, result.toString());
                        else{
                            recruitments.clear();
                            recruitments.addAll((ArrayList<Recruitment>)result);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    public final Recruitment getSelectedRecruitment(RecruitmentForApprovalDetailFragment key){
        return recruitments.get(selectedPos);
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarMenuButton){
            actionbarMenuButton.setEnabled(false); //can only be disabled after slide animation
            activity.toggleSidebar(actionbarMenuButton);
        }else if(v == actionbarRefreshButton){
            syncToServer();
        }
    }

    @Override
    public void enableListButtonOnSidebarAnimationFinished() {
        actionbarMenuButton.setEnabled(true);
    }

    @Override
    public void disableUserInteractionsOnSidebarShown() {
        lv.setEnabled(false);
    }

    @Override
    public void enableUserInteractionsOnSidebarHidden() {
        try{
            lv.setEnabled(true);
        }catch(NullPointerException e){
            System.out.println("Null pointer exception at LeaveListFragment enableUserInteractionOnSidebarHidden()");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedPos = position;
        activity.changeChildPage(new RecruitmentForApprovalDetailFragment());
    }
}
