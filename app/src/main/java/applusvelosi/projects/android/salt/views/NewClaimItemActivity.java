package applusvelosi.projects.android.salt.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.ClaimItemAttendee;
import applusvelosi.projects.android.salt.models.Currency;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.interfaces.CameraCaptureInterface;
import applusvelosi.projects.android.salt.utils.interfaces.FileSelectionInterface;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputCategory;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputCurrency;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputProject;

/**
 * Created by Velosi on 11/13/15.
 */
public class NewClaimItemActivity extends LinearNavFragmentActivity {
    public static final String INTENTKEY_CLAIMHEADER  = "claimheaderkey";

    private Currency currency;
    private Category category;
    private Project project;
    private ArrayList<ClaimItemAttendee> attendees;
    private File attachment;
    private CameraCaptureInterface cameraCaptureListener;
    private FileSelectionInterface fileSelectionListener;

    public ClaimHeader claimHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        claimHeader = new ClaimHeader((HashMap<String, Object>)app.gson.fromJson(getIntent().getExtras().getString(INTENTKEY_CLAIMHEADER), app.types.hashmapOfStringObject));
        attendees = new ArrayList<ClaimItemAttendee>();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimItemInputCategory()).commit();
    }

    public void updateCurrency(ClaimItemInputCurrency key, Currency currency){ this.currency = currency; }
    public void updateCategory(ClaimItemInputCategory key, Category category){ this.category = category; }
    public void updateProject(ClaimItemInputProject key, Project project){ this.project = project; }
    public void updateAttachmentUri(File attachmentUri){this.attachment = attachmentUri; }
    public void setCameraListener(CameraCaptureInterface listener){ this.cameraCaptureListener = listener; }
    public void setFileSelectionListener(FileSelectionInterface listener){ this.fileSelectionListener = listener; }
    public ArrayList<ClaimItemAttendee> getAttendees(){ return attendees;}
    public File getAttachment(){ return attachment; }
    public Currency getCurrency(){ return currency; }
    public Category getCategory(){ return category; }
    public Project getProject(){ return project; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case SaltApplication.RESULT_CAMERA:
                if(resultCode == RESULT_OK){
                    cameraCaptureListener.onCameraCaptureSuccess();
                }else {
                    attachment = null;
                    cameraCaptureListener.onCameraCaptureFailed();
                }
                break;

            case SaltApplication.RESULT_BROWSEFILES:
                if(resultCode == RESULT_OK){
                    Cursor cursor = null;
                    try{
                        String [] proj = { MediaStore.Images.Media.DATA};
                        cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        attachment = new File(cursor.getString(column_index));
                        fileSelectionListener.onFileSelectionSuccess(attachment);
                    }finally {
                        if(cursor!=null)
                            cursor.close();
                    }
                }else {
                    attachment = null;
                    fileSelectionListener.onFileSelectionFailed();
                }
                break;
        }
    }


}
