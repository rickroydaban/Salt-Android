package applusvelosi.projects.android.salt.views.fragments.leaves;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.fragments.ActionbarFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class LeaveInputType extends ActionbarFragment implements ListAdapterInterface, AdapterView.OnItemClickListener{
    //actionbar buttons
    private TextView actionbarTitle;
    private RelativeLayout actionbarButtonBack;

    private ArrayList<String> leaveTypes;
    private ListAdapter adapter;
    private ListView lv;


    @Override
    protected RelativeLayout setupActionbar() {
        RelativeLayout actionbarLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Leave Type");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listviewdetail, null);
        leaveTypes = new ArrayList<String>();
        leaveTypes.add(Leave.LEAVETYPEVACATIONDESC);
        leaveTypes.add(Leave.LEAVETYPESICKDESC);
        leaveTypes.add(Leave.LEAVETYPEUNPAIDDESC);
        leaveTypes.add(Leave.LEAVETYPEBUSINESSTRIPDESC);
        leaveTypes.add(Leave.LEAVETYPEHOSPITALIZATIONDESC);
        leaveTypes.add(Leave.LEAVETYPEDOCTORDESC);
        leaveTypes.add(Leave.LEAVETYPEMATERNITYDESC);
        leaveTypes.add(Leave.LEAVETYPEBEREAVEMENTDESC);
        if(app.getStaffOffice().hasBirthdayLeave())
            leaveTypes.add(Leave.LEAVETYPEBIRTHDAYDESC);

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            activity.onBackPressed();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if(view == null){
            holder = new Holder();
            view = activity.getLayoutInflater().inflate(R.layout.node_headeronly, null);
            holder.tvTitle = (TextView)view.findViewById(R.id.tviews_nodes_headeronly_header);
            view.setTag(holder);
        }

        holder = (Holder)view.getTag();
        holder.tvTitle.setText(leaveTypes.get(position));

        return view;
    }

    @Override
    public int getCount() {
        return leaveTypes.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class Holder{
        public TextView tvTitle;
    }
}
