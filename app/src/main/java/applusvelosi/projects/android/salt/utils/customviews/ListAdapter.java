package applusvelosi.projects.android.salt.utils.customviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;

/**
 * Created by Velosi on 10/26/15.
 */
public class ListAdapter extends BaseAdapter{
    private ListAdapterInterface listAdapterInterface;

    public ListAdapter(ListAdapterInterface listAdapterInterface){
        this.listAdapterInterface = listAdapterInterface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return listAdapterInterface.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return listAdapterInterface.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
