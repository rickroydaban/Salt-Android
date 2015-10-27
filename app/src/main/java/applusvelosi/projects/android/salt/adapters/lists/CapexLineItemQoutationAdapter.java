package applusvelosi.projects.android.salt.adapters.lists;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.capex.CapexLineItemQoutation;
import applusvelosi.projects.android.salt.views.HomeActivity;

/**
 * Created by Velosi on 10/11/15.
 */
public class CapexLineItemQoutationAdapter extends BaseAdapter{
    HomeActivity activity;
    ArrayList<CapexLineItemQoutation> qoutations;

    public CapexLineItemQoutationAdapter(HomeActivity activity, ArrayList<CapexLineItemQoutation> qoutations){
        this.activity = activity;
        this.qoutations = qoutations;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        HeaderDetailHolder holder;

        if (view == null) {
            holder = new HeaderDetailHolder();
            view = activity.getLayoutInflater().inflate(R.layout.node_capexlineitemqoutation, null);
            holder.tvP = (TextView) view.findViewById(R.id.tviews_nodes_capexlineitemqoutation_p);
            holder.tvSupplier = (TextView) view.findViewById(R.id.tviews_nodes_capexlineitemqoutation_supplier);

            view.setTag(holder);
        }

        holder = (HeaderDetailHolder) view.getTag();
        CapexLineItemQoutation qoutation = qoutations.get(pos);

        holder.tvP.setTextColor(activity.getResources().getColor((qoutation.isPrimary())?R.color.orange_velosi:R.color.light_gray));
        holder.tvSupplier.setText(qoutation.getSupplierName());

        return view;
    }

    @Override
    public int getCount() {
        return qoutations.size();
    }

    @Override
    public Object getItem(int position) {
        return qoutations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HeaderDetailHolder{
        public TextView tvP, tvSupplier;
    }
}
