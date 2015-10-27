package applusvelosi.projects.android.salt.adapters.lists;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.views.HomeActivity;

/**
 * Created by Velosi on 10/11/15.
 */
public class AttachmentAdapter extends BaseAdapter{

    HomeActivity activity;
    ArrayList<String> headers;

    public AttachmentAdapter(HomeActivity activity, ArrayList<String> headers){
        this.activity = activity;
        this.headers = headers;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        HeaderDetailHolder holder;

        if (view == null) {
            holder = new HeaderDetailHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_attachment, null);
            holder.tvHeader = (TextView) view.findViewById(R.id.tviews_nodes_attachment_name);

            view.setTag(holder);
        }

        holder = (HeaderDetailHolder) view.getTag();
        holder.tvHeader.setText(headers.get(pos));

        return view;
    }

    @Override
    public int getCount() {
        return headers.size();
    }

    @Override
    public Object getItem(int position) {
        return headers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HeaderDetailHolder{
        public TextView tvHeader;
    }
}
