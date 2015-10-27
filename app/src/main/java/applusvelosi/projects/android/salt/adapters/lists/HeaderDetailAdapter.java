package applusvelosi.projects.android.salt.adapters.lists;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.views.HomeActivity;

/**
 * Created by Velosi on 10/11/15.
 */
public class HeaderDetailAdapter extends BaseAdapter{
    public static final String DELIMETER = "~";

    HomeActivity activity;
    ArrayList<String> headerDetails;

    public HeaderDetailAdapter(HomeActivity activity, ArrayList<String> headerDetails){
        this.activity = activity;
        this.headerDetails = headerDetails;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        HeaderDetailHolder holder;

        if (view == null) {
            holder = new HeaderDetailHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_headerdetail, null);
            holder.tvHeader = (TextView) view.findViewById(R.id.tviews_nodes_headerdetail_header);
            holder.tvDetail = (TextView) view.findViewById(R.id.tviews_nodes_headerdetail_detail);

            view.setTag(holder);
        }

        holder = (HeaderDetailHolder) view.getTag();
        String[] headerDetail = headerDetails.get(pos).split(DELIMETER);
        holder.tvHeader.setText(headerDetail[0]);
        holder.tvDetail.setText(headerDetail[1]);

        return view;
    }

    @Override
    public int getCount() {
        return headerDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return headerDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HeaderDetailHolder{
        public TextView tvHeader, tvDetail;
    }
}
