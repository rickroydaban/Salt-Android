package applusvelosi.projects.android.salt.views.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.claims.ItemInputFragment;
import applusvelosi.projects.android.salt.views.fragments.claims.NewClaimItemInputFragment;

/**
 * Created by Velosi on 12/7/15.
 */
public class DialogClaimItemAttendeeList implements ListAdapterInterface, AdapterView.OnItemClickListener {
    private ListView lv;
    private ListAdapter adapter;
    private TextView tvHeader, tvNotificationMessage;
    private LinearLayout container;
    private AlertDialog ad, dialogAddAttendee;
    private ManageClaimItemActivity activity;
    private ItemInputFragment frag;
    private TextView tvDialogName, tvDialogJob, tvDialogNotes;

    public DialogClaimItemAttendeeList(final ItemInputFragment frag){
        this.frag = frag;
        this.activity = (ManageClaimItemActivity)frag.getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_list, null);
        tvNotificationMessage = (TextView)view.findViewById(R.id.tviews_dialogs_list);
        container = (LinearLayout)view.findViewById(R.id.containers_dialogs_list);
        tvHeader = (TextView)view.findViewById(R.id.tviews_dialogs_list_header);
        lv = (ListView)view.findViewById(R.id.lists_dialogs_list);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        tvHeader.setText("Attendees");
        ad = new AlertDialog.Builder(activity).setTitle(null).setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogAddAttendee.show();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        ScrollView builderView = (ScrollView)LayoutInflater.from(activity).inflate(R.layout.dialog_addclaimattendee, null);
        tvDialogName = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_name);
        tvDialogJob = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_job);
        tvDialogNotes = (TextView)builderView.findViewById(R.id.etexts_dialogs_addclaimattendee_note);

        dialogAddAttendee = new AlertDialog.Builder(activity).setTitle("").setView(builderView)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClaimItemAttendee newAttendee = new ClaimItemAttendee(tvDialogName.getText().toString(), tvDialogJob.getText().toString(), tvDialogNotes.getText().toString());
                                activity.claimItem.getAttendees().add(newAttendee);
                                tvHeader.setText("Attendees ("+activity.claimItem.getAttendees().size()+")");
                                frag.tvAttendees.setText((activity.claimItem.getAttendees().size()>1)?activity.claimItem.getAttendees().size()+" Attendees ": "1 Attendee");
                                tvDialogName.setText("");
                                tvDialogJob.setText("");
                                tvDialogNotes.setText("");
                                show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                show();
                            }
                        }).create();

    }

    public void show(){
        ad.show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;

        if(v == null){
            v =  activity.getLayoutInflater().inflate(R.layout.node_claimitemattendee, null);
            holder = new Holder();
            holder.tvName = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_name);
            holder.tvJobTitle = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_jobtitle);
            holder.tvDesc = (TextView)v.findViewById(R.id.tviews_nodes_claimitemattendee_description);

            v.setTag(holder);
        }

        holder = (Holder)v.getTag();
        ClaimItemAttendee attendee = activity.claimItem.getAttendees().get(position);
        holder.tvName.setText(attendee.getName());
        holder.tvJobTitle.setText(attendee.getJobTitle());
        holder.tvDesc.setText(attendee.getNote());

        return v;
    }

    @Override
    public int getCount() {
        return activity.claimItem.getAttendees().size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(activity).setMessage("This will remove "+activity.claimItem.getAttendees().get(position).getName()+" from the list")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.claimItem.getAttendees().remove(position);
                        adapter.notifyDataSetChanged();
                        tvHeader.setText("Attendees ("+activity.claimItem.getAttendees().size()+")");
                        if(activity.claimItem.getAttendees().size() == 0) frag.tvAttendees.setText("No Attendee");
                        else if(activity.claimItem.getAttendees().size() == 1) frag.tvAttendees.setText("1 Attendee");
                        else frag.tvAttendees.setText(activity.claimItem.getAttendees().size()+" 1 Attendee");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private class Holder{
        private TextView tvName, tvJobTitle, tvDesc;
    }
}
