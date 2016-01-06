package applusvelosi.projects.android.salt.views.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;

/**
 * Created by Velosi on 12/7/15.
 */
public class DialogClaimItemCategories implements ManageClaimItemActivity.ClaimItemOfficeReloader, ListAdapterInterface, AdapterView.OnItemClickListener {
    private ListView lv;
    private ListAdapter adapter;
    private TextView tvHeader, tvNotificationMessage;
    private LinearLayout container;
    private AlertDialog ad;
    private ManageClaimItemActivity activity;
    private DialogClaimItemCategoryInterface inf;

    public DialogClaimItemCategories(final ManageClaimItemActivity activity, DialogClaimItemCategoryInterface inf){
        this.activity = activity;
        this.inf = inf;
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_list, null);
        tvNotificationMessage = (TextView)view.findViewById(R.id.tviews_dialogs_list);
        container = (LinearLayout)view.findViewById(R.id.containers_dialogs_list);
        tvHeader = (TextView)view.findViewById(R.id.tviews_dialogs_list_header);
        lv = (ListView)view.findViewById(R.id.lists_dialogs_list);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        tvHeader.setText("Select Category");
        ad = new AlertDialog.Builder(activity).setTitle(null).setView(view)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    public void show(){
        ad.show();

    }


    @Override
    public void onSuccess() {
        if(activity.getOffices().size()>0) {
            container.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else
            tvNotificationMessage.setText("No Items");
    }

    @Override
    public void onFailed(String message) {
        tvNotificationMessage.setText(message);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null)
         v = LayoutInflater.from(activity).inflate(R.layout.node_headeronly, null);

        ((TextView)v.findViewById(R.id.tviews_nodes_headeronly_header)).setText(activity.getCategories().get(position).getName());
        return v;
    }

    @Override
    public int getCount() {
        return activity.getCategories().size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ad.dismiss();
        inf.onCategorySelected(activity.getCategories().get(position));
    }

    public interface DialogClaimItemCategoryInterface{
        void onCategorySelected(Category category);
    }
}
