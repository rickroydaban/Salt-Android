package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.utils.customviews.ListAdapter;
import applusvelosi.projects.android.salt.utils.interfaces.ListAdapterInterface;
import applusvelosi.projects.android.salt.views.NewClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 10/26/15.
 */
public class ClaimItemInputCategory extends LinearNavActionbarFragment implements ListAdapterInterface, AdapterView.OnItemClickListener{
    private NewClaimItemActivity activity;
    //actionbar buttons
    private TextView actionbarTitle;
    private RelativeLayout actionbarButtonBack;

    private ArrayList<Category> categories;
    private ListAdapter adapter;
    private ListView lv;

    @Override
    protected RelativeLayout setupActionbar() {
        activity = (NewClaimItemActivity)getActivity();
        RelativeLayout actionbarLayout = (RelativeLayout)linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.actionbar_backonly, null);
        actionbarButtonBack = (RelativeLayout)actionbarLayout.findViewById(R.id.buttons_actionbar_back);
        actionbarTitle = (TextView)actionbarLayout.findViewById(R.id.tviews_actionbar_title);
        actionbarTitle.setText("Select Category");

        actionbarButtonBack.setOnClickListener(this);
        actionbarTitle.setOnClickListener(this);

        return actionbarLayout;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listviewdetail, null);
        categories = new ArrayList<Category>();

        lv = (ListView)view.findViewById(R.id.lists_lv);
        adapter = new ListAdapter(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        activity.startLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object tempResult;
                try{
                    tempResult = app.onlineGateway.getClaimItemCategoryByOffice();
                }catch(Exception e){
                    e.printStackTrace();
                    tempResult = e.getMessage();
                }

                final Object result = tempResult;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(result instanceof String){
                            activity.finishLoading(result.toString());
                        }else{
                            activity.finishLoading();
                            categories.clear();
                            categories.addAll((ArrayList<Category>) result);
                            Collections.sort(categories, new Comparator<Category>() {
                                @Override
                                public int compare(Category lhs, Category rhs) {
                                    return lhs.getName().compareToIgnoreCase(rhs.getName().toString());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == actionbarButtonBack || v == actionbarTitle)
            linearNavFragmentActivity.onBackPressed();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if(view == null){
            holder = new Holder();
            view = linearNavFragmentActivity.getLayoutInflater().inflate(R.layout.node_headeronly, null);
            holder.tvTitle = (TextView)view.findViewById(R.id.tviews_nodes_headeronly_header);
            view.setTag(holder);
        }

        holder = (Holder)view.getTag();
        holder.tvTitle.setText(categories.get(position).getName());

        return view;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.updateCategory(this, categories.get(position));
        linearNavFragmentActivity.changePage(new ClaimItemInputProject());
    }

    private class Holder{
        public TextView tvTitle;
    }
}
