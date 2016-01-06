package applusvelosi.projects.android.salt.views.fragments.claims;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;

/**
 * Created by Velosi on 10/26/15.
 */
public class NewMileageItemInputFragment extends ItemInputFragment {

    @Override
    protected View createClaimItemtView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_claimitem_newmileage, null);
    }

    @Override
    protected void saveToServer() {
        try {
            if(tviewsDate.length() > 0){
                if(etextAmount.length() > 0){
                    activity.claimItem.setDateCreated(app.onlineGateway.jsonizeDate(new Date()));
                    activity.startLoading();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String tempResult;
                            try{
                                tempResult = app.onlineGateway.saveClaimLineItem(activity.claimItem.jsonize(app), ClaimItem.getEmptyClaimLineItemJSON(app), attachedFile);
                            }catch(Exception e){
                                e.printStackTrace();
                                tempResult = e.getMessage();
                            }

                            final String result = tempResult;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!result.contains("Okayzx")){
                                        activity.finishLoading(result.toString());
                                    }else{
                                        if(activity.claimItem.hasReceipt()){ //update the claim item attachment
                                            try{
                                                new Thread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try{
                                                            Document attachment = activity.claimItem.getAttachments().get(0);
                                                            attachment.setRefID(Integer.parseInt(result.split(" ")[1]));
                                                            final JSONObject result = new JSONObject(app.onlineGateway.uploadAttachment(attachedFile, attachment)).getJSONObject("UploadFileResult");
                                                            if(result.getJSONArray("SystemErrors").length() > 0){
                                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try{
                                                                            app.showMessageDialog(activity, result.getJSONArray("SystemErrors").getString(0));
                                                                            activity.finish();
                                                                        }catch(Exception e){
                                                                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }else{
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        String tempResult;
                                                                        try{
                                                                            ClaimItem copy = new ClaimItem(activity.claimHeader);
                                                                            copy.setClaimLineItemID(activity.claimItem.getItemID());
                                                                            copy.removeAllAttachments();
                                                                            tempResult = app.onlineGateway.saveClaimLineItem(activity.claimItem.jsonize(app), copy.jsonize(app), attachedFile);
                                                                        }catch(Exception e){
                                                                            e.printStackTrace();
                                                                            tempResult = e.getMessage();
                                                                        }

                                                                        final String result = tempResult;
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                activity.finishLoading();
                                                                                if (!result.contains("Okayzx")) {
                                                                                    activity.finishLoading(result.toString());
                                                                                }else{
                                                                                    activity.finishLoading();
                                                                                    Toast.makeText(activity, "Claim Item Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                                                    activity.finish();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();
                                                            }

                                                        }catch(final Exception e){
                                                            e.printStackTrace();
                                                            System.out.println(e.getMessage());
                                                            activity.finishLoading();
                                                            app.showMessageDialog(activity, e.getMessage());
                                                        }
                                                    }
                                                }).start();
                                            }catch(Exception e){
                                                e.printStackTrace();
                                                activity.finishLoading();
                                                app.showMessageDialog(activity, e.getMessage());
                                            }
                                        }else{
                                            activity.finishLoading();
                                            Toast.makeText(activity, "Claim Item Saved Successfully!", Toast.LENGTH_SHORT).show();
                                            activity.finish();
                                        }

                                    }
                                }
                            });
                        }
                    }).start();
                }else app.showMessageDialog(activity, "Amount is required");
            }else app.showMessageDialog(activity, "Expense date is required");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("SALTX "+e.getMessage());
        }
    }
}
