package applusvelosi.projects.android.salt.views;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.views.fragments.LinearNavActionbarFragment;

/**
 * Created by Velosi on 11/13/15.
 */
public class LinearNavFragmentActivity extends FragmentActivity{

    protected SaltApplication app;
    private RelativeLayout actionbar, mainFragment;
    private RelativeLayout containersLoader;
    private TextView tviewsLoader;
    private ImageView ivLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        containersLoader = (RelativeLayout)findViewById(R.id.containers_loader);
        tviewsLoader = (TextView)findViewById(R.id.tviews_loader);
        ivLoader = (ImageView) findViewById(R.id.iviews_loader);
        ((AnimationDrawable)ivLoader.getDrawable()).start();

        app = (SaltApplication)getApplication();
        actionbar = (RelativeLayout)findViewById(R.id.actionbar_top);
        mainFragment = (RelativeLayout)findViewById(R.id.activity_view);
    }

    public void setupActionbar(RelativeLayout actionbarContentViews){
        if(actionbar!=null && actionbarContentViews!=null){
            actionbar.removeAllViews();
            actionbar.addView(actionbarContentViews);
        }else{
            startActivity(new Intent(this, LinearNavFragmentActivity.class));
            finish();
        }
    }

    public void changePage(LinearNavActionbarFragment fragment){
        getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.activity_view, fragment).commit();
    }

    public void startLoading(){
        containersLoader.setVisibility(View.VISIBLE);
        ivLoader.setVisibility(View.VISIBLE);
        tviewsLoader.setText("Loading");
        tviewsLoader.setTextColor(getResources().getColor(R.color.black));

    }

    public void finishLoading(){
        containersLoader.setVisibility(View.GONE);
    }

    public void finishLoading(String error){
        if(error != null){
            ivLoader.setVisibility(View.GONE);
            if(error.contains("No address associated with hostname"))
                error = "Server Connection Failed";
            tviewsLoader.setText(error);
            tviewsLoader.setTextColor(getResources().getColor(R.color.red));
        }else
            containersLoader.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(ivLoader.getVisibility() == View.GONE || containersLoader.getVisibility() == View.GONE) {
            if(containersLoader.getVisibility() == View.VISIBLE)
                containersLoader.setVisibility(View.GONE);
            super.onBackPressed();
        }
    }
}
