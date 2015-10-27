package applusvelosi.projects.android.salt.views.fragments.capex;

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
import applusvelosi.projects.android.salt.adapters.lists.CapexForApprovalAdapter;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.interfaces.RootFragment;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexesForApprovalFragment extends ActionbarFragment implements RootFragment, AdapterView.OnItemClickListener{
    private static CapexesForApprovalFragment instance;
    //action bar buttons
    private RelativeLayout actionbarMenuButton, actionbarRefreshButton;

    private SaltProgressDialog pd;
    private ListView lv;
    private CapexForApprovalAdapter adapter;
    private ArrayList<CapexHeader> capexes;

    public static CapexesForApprovalFragment getInstance(){
        if(instance == null)
            instance = new CapexesForApprovalFragment();

        return instance;
    }

    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_menurefresh, null);
        actionbarMenuButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_menu);
        actionbarRefreshButton = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_refresh);
        ((TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title)).setText("Capex For Approval");

        actionbarMenuButton.setOnClickListener(this);
        actionbarRefreshButton.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, null);
        lv = (ListView)view.findViewById(R.id.lists_lv);
        capexes = new ArrayList<CapexHeader>();
        adapter = new CapexForApprovalAdapter(activity, capexes);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        syncToServer();
        return view;
    }

    private void syncToServer(){
        if(pd == null)
            pd = new SaltProgressDialog(activity);

        pd.show();;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempCapexForApprovalResult;

                try{
                    tempCapexForApprovalResult = app.onlineGateway.getCapexesForApproval();
                }catch(Exception e){
                    tempCapexForApprovalResult = e.getMessage();
                }

                final Object capexForApprovalResult = tempCapexForApprovalResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();;
                        if(capexForApprovalResult instanceof String)
                            app.showMessageDialog(activity, capexForApprovalResult.toString());
                        else{
                            capexes.clear();
                            capexes.addAll((ArrayList<CapexHeader>)capexForApprovalResult);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
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
        activity.changeChildPage(CapexForApprovalDetailFragment.newInstance(capexes.get(position).getCapexID()));
    }
}
