package applusvelosi.projects.android.salt.utils.threads;

import android.content.Loader;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.interfaces.LoaderInterface;
import applusvelosi.projects.android.salt.views.ManageClaimItemActivity;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputProject;
import applusvelosi.projects.android.salt.views.fragments.claims.ItemInputFragment;

/**
 * Created by Velosi on 12/22/15.
 */
public class ProjectsLoader extends Thread{
    SaltApplication app;
    ManageClaimItemActivity activity;
    LoaderInterface inf;

    public ProjectsLoader(ManageClaimItemActivity activity, LoaderInterface inf){
        this.inf = inf;
        this.activity = activity;
        app = ((SaltApplication)activity.getApplication());
    }

    @Override
    public void run() {
        Object tempResult;
        try{
            tempResult = app.onlineGateway.getClaimItemProjectsByCostCenter(activity.claimHeader.getCostCenterID());
        }catch(Exception e){
            e.printStackTrace();
            tempResult = e.getMessage();
        }

        final Object result = tempResult;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (result instanceof String)
                    inf.onLoadFailed(result.toString());
                else
                    inf.onLoadSuccess(result);
            }
        });    }

}
