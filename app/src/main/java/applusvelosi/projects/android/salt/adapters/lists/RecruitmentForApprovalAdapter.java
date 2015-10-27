package applusvelosi.projects.android.salt.adapters.lists;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.views.HomeActivity;

/**
 * Created by Velosi on 10/11/15.
 */
public class RecruitmentForApprovalAdapter extends BaseAdapter{
    HomeActivity activity;
    ArrayList<Recruitment> recruitments;

    public RecruitmentForApprovalAdapter(HomeActivity activity, ArrayList<Recruitment> recruitments){
        this.activity = activity;
        this.recruitments = recruitments;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        RecruitmentNodeHolder holder;

        if (view == null) {
            holder = new RecruitmentNodeHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_headerdetailstatus, null);
            holder.tvJoTitle = (TextView) view.findViewById(R.id.tviews_nodes_headerdetailstatus_header);
            holder.tvOffice = (TextView) view.findViewById(R.id.tviews_nodes_headerdetailstatus_detail);
            holder.tvStatus = (TextView) view.findViewById(R.id.tviews_nodes_headerdetailstatus_status);

            view.setTag(holder);
        }

        holder = (RecruitmentNodeHolder) view.getTag();
        Recruitment recruitment = recruitments.get(pos);
        String statusName;
        if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYCM)
            statusName = "Approved by CM";
        else if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYRM)
            statusName = "Approved by RM";
        else if (recruitment.getStatusID() == Recruitment.RECRUITMENT_STATUSID_APPROVEDBYMHR)
            statusName = "Approved by MHR";
        else statusName = recruitment.getStatusName();

        holder.tvJoTitle.setText(recruitment.getJobTitle());
        holder.tvOffice.setText(recruitment.getRequesterOfficeName());
        holder.tvStatus.setText(statusName);

        return view;
    }

    @Override
    public int getCount() {
        return recruitments.size();
    }

    @Override
    public Object getItem(int position) {
        return recruitments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class RecruitmentNodeHolder{
        public TextView tvJoTitle, tvOffice, tvStatus;
    }
}
