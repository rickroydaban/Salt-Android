package applusvelosi.projects.android.salt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import applusvelosi.projects.android.salt.views.SplashActivity;

/**
 * Created by Velosi on 10/23/15.
 */
public class ParseReceiver extends ParsePushBroadcastReceiver{
    public static final String PARSE_DATA_KEY = "com.parse.Data";

    protected void onPushReceive(Context context, Intent intent) {
        JSONObject data = getDataFromIntent(intent);
        System.out.println("Intent "+intent.getExtras());
        // Do something with the data. To create a notification do:

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Approval Request");
        try {
            builder.setContentText(data.getString("alert"));
        }catch(Exception e){
            builder.setContentText("Default Content Text");
        }
        builder.setSmallIcon(R.drawable.noti_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.noti_largeicon));
        builder.setAutoCancel(true);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class), 0));
//        builder.setContentIntent(PendingIntent.getActivity(this, 0, ,PendingIntent.FLAG_CANCEL_CURRENT));
        notificationManager.notify("MyTag", 0, builder.build());
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
}
