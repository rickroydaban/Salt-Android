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
import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.adapters.lists.AttachmentAdapter;
import applusvelosi.projects.android.salt.utils.FileManager;
import applusvelosi.projects.android.salt.utils.SaltProgressDialog;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;
import applusvelosi.projects.android.salt.views.fragments.roots.RootFragment;

/**
 * Created by Velosi on 10/12/15.
 */
public class RFADetailAttachmentFragment extends LinearNavActionbarFragment implements AdapterView.OnItemClickListener, FileManager.AttachmentDownloadListener{
    public static String KEY_RECRUITMENTJSON = "RFADetailAttachmentFragmentKeyRecruitmentJSON";

    private RelativeLayout actionbarButtonBack;
    private TextView actionbarTitle;

    private ListView lv;
    private AttachmentAdapter adapter;
    private ArrayList<HashMap<String, Object>> attachments;
    private ArrayList<String> attachmentNames;

    private SaltProgressDialog pd;

    public static RFADetailAttachmentFragment newInstance(String arraylistOfHashMapOfAttachments){
        RFADetailAttachmentFragment frag = new RFADetailAttachmentFragment();
        Bundle b = new Bundle();
        b.putString(KEY_RECRUITMENTJSON, arraylistOfHashMapOfAttachments);
        frag.setArguments(b);
        return frag;
    }

    @Override
    protected RelativeLayout setupActionbar() {
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

        attachments = app.gson.fromJson(getArguments().getString(KEY_RECRUITMENTJSON), app.types.arrayListOfHashmapOfStringObject);

        attachmentNames = new ArrayList<String>();
        for(HashMap<String, Object> attachment :attachments)
            attachmentNames.add(attachment.get("OrigDocName").toString());

//        adapter = new AttachmentAdapter(linearNavFragmentActivity, attachmentNames);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> selectedMap = attachments.get(position);
        int docID = (int)Float.parseFloat(selectedMap.get("DocID").toString());
        int objectTypeID = (int)Float.parseFloat(selectedMap.get("ObjectType").toString());
        int refID = (int)Float.parseFloat(selectedMap.get("RefID").toString());
        String filename = selectedMap.get("DocName").toString();

        try {
            if(pd == null)
                pd = new SaltProgressDialog(linearNavFragmentActivity);
            app.fileManager.downloadDocument(docID, refID, objectTypeID, filename, pd, this);
        }catch(Exception e){
            app.showMessageDialog(linearNavFragmentActivity, e.getMessage());
        }
    }

    @Override
    public void onAttachmentDownloadFinish(File downloadedFile) {
//        app.fileManager.openDocument(activity, downloadedFile);
    }

    @Override
    public void onAttachmentDownloadFailed(String errorMessage) {
//        app.showMessageDialog(activity, errorMessage);
    }

}
