package applusvelosi.projects.android.salt.adapters.lists;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.views.HomeActivity;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexForApprovalAdapter extends BaseAdapter{
    HomeActivity activity;
    ArrayList<CapexHeader> capexHeaders;

    public CapexForApprovalAdapter(HomeActivity activity, ArrayList<CapexHeader> capexHeaders){
        this.activity = activity;
        this.capexHeaders = capexHeaders;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        CapexNodeHolder holder;

        if(view == null){
            holder = new CapexNodeHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_headerdetailstatus, null);
            holder.tvOffice = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_header);
            holder.tvCostCenter = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_detail);
            holder.tvStatus = (TextView)view.findViewById(R.id.tviews_nodes_headerdetailstatus_status);

            view.setTag(holder);
        }

        holder = (CapexNodeHolder)view.getTag();
        CapexHeader capexHeader = capexHeaders.get(pos);
        String statusName;
        if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYCM) statusName = "Approved by CM";
        else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYCM) statusName = "Rejected by CM";
        else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_APPROVEDBYRM) statusName = "Approved by RM";
        else if(capexHeader.getStatusID() == CapexHeader.CAPEXHEADERID_REJECTEDBYRM) statusName = "Rejected by RM";
        else statusName = capexHeader.getStatusName();

        holder.tvOffice.setText(capexHeader.getOfficeName());
        holder.tvCostCenter.setText(capexHeader.getCapexNumber());
        holder.tvStatus.setText(statusName);

        return view;
    }

    @Override
    public int getCount() {
        return capexHeaders.size();
    }

    @Override
    public Object getItem(int position) {
        return capexHeaders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class CapexNodeHolder{
        public TextView tvCostCenter, tvOffice, tvStatus;
    }
}
