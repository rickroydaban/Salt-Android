package applusvelosi.projects.android.salt;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.models.Leave;
import applusvelosi.projects.android.salt.models.capex.CapexHeader;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.recruitments.Recruitment;
import applusvelosi.projects.android.salt.utils.TypeHolder;
import applusvelosi.projects.android.salt.views.CapexApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.ClaimDetailActivity;
import applusvelosi.projects.android.salt.views.HomeActivity;
import applusvelosi.projects.android.salt.views.LeaveApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.LeaveDetailActivity;
import applusvelosi.projects.android.salt.views.RecruitmentApprovalDetailActivity;
import applusvelosi.projects.android.salt.views.SplashActivity;

/**
 * Created by Velosi on 10/23/15.
 */
public class ParseReceiver extends ParsePushBroadcastReceiver{
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    public static final String GCMMESSAGEKEY_TYPE = "PUSHTYPE";
    public static final String GCMMESSAGEKEY_DATA = "PUSHDATA";
    public static final String GCMRECEIVED_LEAVEAPPROVAL = "Leave For Approval";
    public static final String GCMRECEIVED_CLAIMAPPROVAL = "Claim For Approval";
    public static final String GCMRECEIVED_RCMNTAPPROVAL = "Recruitment For Approval";
    public static final String GCMRECEIVED_CAPEXAPPROVAL = "Capex For Approval";
    public static final String GCMRECEIVED_PROCESSEDITEM = "Processed";

    private static HashMap<String, ArrayList<String>> mapNotifTypeJSONData = new HashMap<String, ArrayList<String>>();
    private Gson gson;
    private TypeHolder typeHolder;

    protected void onPushReceive(Context context, Intent intent) {
        gson = new Gson();
        typeHolder = new TypeHolder();

        try {
            JSONObject data = new JSONObject(getDataFromIntent(intent).getString("alert"));
            String dataType = data.getString(GCMMESSAGEKEY_TYPE);
            String dataJSON = data.getString(GCMMESSAGEKEY_DATA);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("Applus Velosi SALT");
            builder.setSmallIcon(R.drawable.noti_icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_notiflarge));
            builder.setAutoCancel(true);
            builder.setLights(0xFFFF0000, 1000, 1000);

            if(mapNotifTypeJSONData.get(dataType) == null)
                mapNotifTypeJSONData.put(dataType, new ArrayList<String>());
            mapNotifTypeJSONData.get(dataType).add(dataJSON);

            System.out.println("Data JSON "+dataJSON);
            if(dataType.equals(GCMRECEIVED_LEAVEAPPROVAL)) openForLeaveApproval(builder, context, dataType, dataJSON);
            else if(dataType.equals(GCMRECEIVED_CLAIMAPPROVAL)) openForClaimApproval(builder, context, dataType, dataJSON);
            else if(dataType.equals(GCMRECEIVED_RCMNTAPPROVAL)) openForRecruitmentApproval(builder, context, dataType, dataJSON);
            else if(dataType.equals(GCMRECEIVED_CAPEXAPPROVAL)) openForCapexApproval(builder, context, dataType, dataJSON);
            else openApp(builder, context, dataType, dataJSON);

            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(dataType, 0, builder.build());
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Should have received push but "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }


    @Override
    protected void onPushOpen(Context context, Intent intent) {
        System.out.println("On Push Open");
    }

    private void openForLeaveApproval(NotificationCompat.Builder builder, Context context, String jsonType, String jsonData){
        Leave leave = new Leave(gson.<HashMap<String, Object>>fromJson(jsonData, typeHolder.hashmapOfStringObject));
        Intent intent;
        System.out.println("type "+jsonType+" count "+mapNotifTypeJSONData.get(jsonType).size());
        if(mapNotifTypeJSONData.get(jsonType).size() > 1){
            builder.setContentText(mapNotifTypeJSONData.get(jsonType).size()+" leaves for approval");
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(SplashActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN, HomeActivity.AUTONAV_POS_LEAVESAPPROVAL);
        }else{
            builder.setContentText("Leave Request from " + leave.getStaffName());
            intent = new Intent(context, LeaveApprovalDetailActivity.class);
            intent.putExtra(LeaveApprovalDetailActivity.INTENTKEY_LEAVEJSON, jsonData);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    private void openForClaimApproval(NotificationCompat.Builder builder, Context context, String jsonType, String jsonData){
        ClaimHeader claimHeader = new ClaimHeader(gson.<HashMap<String, Object>>fromJson(jsonData, typeHolder.hashmapOfStringObject));
        Intent intent;
        if(mapNotifTypeJSONData.size() > 1){
            builder.setContentText(mapNotifTypeJSONData.get(jsonType).size()+" claims for approval");
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(SplashActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN, HomeActivity.AUTONAV_POS_CLAIMSAPPROVAL);
        }else{
            builder.setContentText("Claim Request from "+claimHeader.getStaffName());
            intent = new Intent(context, ClaimDetailActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    private void openForCapexApproval(NotificationCompat.Builder builder, Context context, String jsonType, String jsonData){
        CapexHeader capex = gson.fromJson(jsonData, typeHolder.capex);
        Intent intent;
        if(mapNotifTypeJSONData.size() > 1){
            builder.setContentText(mapNotifTypeJSONData.get(jsonType).size()+" capex for approval");
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(SplashActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN, HomeActivity.AUTONAV_POS_CAPEXAPPROVAL);
        }else{
            builder.setContentText("Capex Request from "+capex.getRequesterName());
            intent = new Intent(context, CapexApprovalDetailActivity.class);
            intent.putExtra(CapexApprovalDetailActivity.INTENTKEY_CAPEXHEADERID, capex.getCapexID());
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    private void openForRecruitmentApproval(NotificationCompat.Builder builder, Context context, String jsonType, String jsonData){
        Recruitment recruitment = gson.fromJson(jsonData, typeHolder.recruitment);
        Intent intent;
        if(mapNotifTypeJSONData.get(jsonType).size() > 1){
            builder.setContentText(mapNotifTypeJSONData.get(jsonType).size()+" recruitments for approval");
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(SplashActivity.KEY_AUTONAV_PENDINGROOTFRAGINDEXTOOPEN, HomeActivity.AUTONAV_POS_RCMNTAPPROVAL);
        }else{
            builder.setContentText("Claim Request from "+recruitment.getRequesterName());
            intent = new Intent(context, CapexApprovalDetailActivity.class);
            intent.putExtra(RecruitmentApprovalDetailActivity.INTENTKEY_RECRUITMENT, recruitment);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    private void openApp(NotificationCompat.Builder builder, Context context, String jsonType, String jsonData){
        builder.setContentText((mapNotifTypeJSONData.get(jsonType).size()>1)?mapNotifTypeJSONData.get(jsonType).size()+"Processed Items":jsonData);
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    public static String createLeaveApprovalMessage(Leave leave, SaltApplication app){
        StringBuilder sb = new StringBuilder("{");
        sb.append("\""+GCMMESSAGEKEY_TYPE+"\":\""+GCMRECEIVED_LEAVEAPPROVAL+"\",");
        sb.append("\""+GCMMESSAGEKEY_DATA + "\":" + leave.jsonize(app));
        sb.append("}");
        return sb.toString();
    }

    public static String createProcessedItemMessage(String message){
        StringBuilder sb = new StringBuilder("{");
        sb.append("\""+GCMMESSAGEKEY_TYPE+"\":\""+GCMRECEIVED_PROCESSEDITEM+"\",");
        sb.append("\""+GCMMESSAGEKEY_DATA+"\":\""+message+"\"");
        sb.append("}");
        return sb.toString();
    }
}
