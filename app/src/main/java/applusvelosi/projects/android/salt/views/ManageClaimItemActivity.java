package applusvelosi.projects.android.salt.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Category;
import applusvelosi.projects.android.salt.models.Office;
import applusvelosi.projects.android.salt.models.claimheaders.ClaimHeader;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.models.claimitems.Project;
import applusvelosi.projects.android.salt.utils.interfaces.CameraCaptureInterface;
import applusvelosi.projects.android.salt.utils.interfaces.FileSelectionInterface;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputCategory;
import applusvelosi.projects.android.salt.views.fragments.claims.ClaimItemInputProject;
import applusvelosi.projects.android.salt.views.fragments.claims.ItemInputFragmentClaims;
import applusvelosi.projects.android.salt.views.fragments.claims.ItemInputFragmentMileage;

/**
 * Created by Velosi on 11/13/15.
 */
public class ManageClaimItemActivity extends LinearNavFragmentActivity {
    public static final String INTENTKEY_CLAIMHEADERPOS = "claimheaderkey";
    public static final String INTENTKEY_CLAIMITEM = "claimitemkey";
    public static final String INTENTKEY_CLAIMITEMATTACHMENT = "claimitemattachment";

    public ClaimHeader claimHeader;
    public ClaimItem claimItem;
    private File attachment;
    private CameraCaptureInterface cameraCaptureListener;
    private FileSelectionInterface fileSelectionListener;

    private ArrayList<Project> projects;
    private ArrayList<Category> categories;
    private ArrayList<Office> offices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra(INTENTKEY_CLAIMITEMATTACHMENT))
            attachment = (File)getIntent().getExtras().get(INTENTKEY_CLAIMITEM);

        claimHeader = app.getMyClaims().get(getIntent().getExtras().getInt(INTENTKEY_CLAIMHEADERPOS));
        offices = new ArrayList<Office>();

        new GetAllOffices(null).start();

        if(getIntent().hasExtra(INTENTKEY_CLAIMITEM)){
            claimItem = (ClaimItem) getIntent().getSerializableExtra(INTENTKEY_CLAIMITEM);
            if(claimItem.getCategoryTypeID() == Category.TYPE_MILEAGE) getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ItemInputFragmentMileage()).commit();
            else getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ItemInputFragmentClaims()).commit();
        }else{
            claimItem = new ClaimItem(claimHeader);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_view, new ClaimItemInputProject()).commit();
        }
    }

    public void updateAttachmentUri(File attachmentUri){this.attachment = attachmentUri; }
    public void setCameraListener(CameraCaptureInterface listener){ this.cameraCaptureListener = listener; }
    public void setFileSelectionListener(FileSelectionInterface listener){ this.fileSelectionListener = listener; }
    public File getAttachment(){ return attachment; }

    public void updateProjectList(ManageClaimItemActivity key, ArrayList<Project> projects){
        this.projects = new ArrayList<Project>();
        this.projects.addAll(projects);
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project lhs, Project rhs) {
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });
    }
    public void updateCategoryList(ManageClaimItemActivity key, ArrayList<Category> categories){
        this.categories = new ArrayList<Category>();
        this.categories.addAll(categories);
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName().toString());
            }
        });
    }

    public ArrayList<Project> getProjects(){ return projects; }
    public ArrayList<Category> getCategories(){ return categories; }
    public ArrayList<Office> getOffices(){ return offices; }

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

    public void reloadOffices(ClaimItemOfficeReloader reloader){
        new Thread().start();
    }

    private class GetAllOffices extends Thread{
        ClaimItemOfficeReloader reloader;
        private Object tempResult;

        public GetAllOffices(ClaimItemOfficeReloader reloader){
            this.reloader = reloader;
        }

        @Override
        public void run() {
            try {
                tempResult = app.onlineGateway.getAllOffices();
            }catch(Exception e){
                e.printStackTrace();
                tempResult = e.getMessage();
            }

            final Object result = tempResult;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(result instanceof String) {
                        if(reloader!=null)
                            reloader.onFailed(result.toString());
                        else
                            Toast.makeText(ManageClaimItemActivity.this, "Unable to retrieve offices "+result.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        offices.clear();
                        offices.addAll((ArrayList<Office>) result);
                        Collections.sort(offices, new Comparator<Office>() {
                            @Override
                            public int compare(Office lhs, Office rhs) {
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });
                        if(reloader !=null)
                            reloader.onSuccess();
                    }
                }
            });
        }
    }

    public interface ClaimItemOfficeReloader{
        void onSuccess();
        void onFailed(String message);
    }

}
