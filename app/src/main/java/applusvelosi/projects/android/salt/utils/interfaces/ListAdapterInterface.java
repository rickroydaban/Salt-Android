package applusvelosi.projects.android.salt.utils.interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Velosi on 10/26/15.
 */
public interface ListAdapterInterface {

    View getView(int position, View convertView, ViewGroup parent);
    int getCount();
}
