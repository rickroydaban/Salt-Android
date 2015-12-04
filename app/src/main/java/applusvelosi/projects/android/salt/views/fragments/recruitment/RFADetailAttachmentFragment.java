package applusvelosi.projects.android.salt.views.fragments.recruitment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.RecruitmentApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 10/12/15.
 */
public class RFADetailAttachmentFragment extends LinearNavActionbarFragment implements AdapterView.OnItemClickListener, FileManager.AttachmentDownloadListener, ListAdapterInterface{
    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private RecruitmentApprovalDetailActivity activity;
    private ListView lv;
    private ListAdapter adapter;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (RecruitmentApprovalDetailActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Attachments");

        actionbarTitle.setOnClickListener(this);
        actionbarButtonBack.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.fragment_listview, null);
        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;

        if(v == null){
            holder = new Holder();
            v = linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.node_attachment, null);
            holder.tvHeader = (TextView)v.findViewById(R.id.tviews_nodes_attachment_name);
            v.setTag(holder);
        }

        holder = (Holder) v.getTag();
        holder.tvHeader.setText(activity.recruitment.getDocuments().get(position).getDocName());
        return  v;
    }

    @Override
    public int getCount() {
        return activity.recruitment.getDocuments().size();
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Document doc = activity.recruitment.getDocuments().get(position);
        int docID = doc.getDocID();
        int objectTypeID = doc.getObjectTypeID();
        int refID = doc.getRefID();
        String filename = doc.getDocName();

        activity.startLoading();
        try {
            app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, this);
        }catch(Exception e){
            e.printStackTrace();
            app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
            activity.finishLoading(e.getMessage());
        }
    }

    @Override
    public void onAttachmentDownloadFinish(File downloadedFile) {
        app.fileManager.openDocument(activity, downloadedFile);
        activity.finishLoading();
    }

    @Override
    public void onAttachmentDownloadFailed(String errorMessage) {
        activity.finishLoading(errorMessage);
    }

    private class Holder{
        public TextView tvHeader;
    }
}
