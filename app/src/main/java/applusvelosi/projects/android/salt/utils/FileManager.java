package applusvelosi.projects.android.salt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import applusvelosi.projects.android.salt.models.claimitems.ClaimItem;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class FileManager {
	public static final String ACCEPTEDFILE_PDF = ".pdf";
	public static final String ACCEPTEDFILE_PNG = ".png";
	public static final String ACCEPTEDFILE_JPG = ".jpg";
	public static final String ACCEPTEDFILE_JPEG = ".jpeg";
	public static final String ACCEPTEDFILE_GIF = ".gif";
	
	private ArrayList<String> acceptedFiles;
	private final String DIR_DOWNLOADEDATTACHMENTS = Environment.getExternalStorageDirectory()+"/salt/downloads/attachments/";
	private final String DIR_CAPTUREDATTACHMENTS = Environment.getExternalStorageDirectory()+"/salt/captured/attachments/";

    private final String imageViewRoot = OnlineGateway.rootURL+"ImageViewer.aspx";
	
	public FileManager(){
		acceptedFiles = new ArrayList<String>();
		acceptedFiles.add(ACCEPTEDFILE_PDF);
		acceptedFiles.add(ACCEPTEDFILE_PNG);
		acceptedFiles.add(ACCEPTEDFILE_JPG);
		acceptedFiles.add(ACCEPTEDFILE_JPEG);
		acceptedFiles.add(ACCEPTEDFILE_GIF);
		
	}

	public String getDirForDownloadedAttachments(){
		File dir = new File(DIR_DOWNLOADEDATTACHMENTS);
		if(!dir.exists())
			dir.mkdirs();
		
		return DIR_DOWNLOADEDATTACHMENTS;
	}
	
	public String getDirForCapturedAttachments(){
		File dir = new File(DIR_CAPTUREDATTACHMENTS);
		if(!dir.exists())
			dir.mkdirs();
		
		return DIR_CAPTUREDATTACHMENTS;
	}
	
	//usable when clicking on the image link in the claimitemdetailsfragment
//	public void downloadAttachment(final HomeActivity activity, final ClaimItem claimItem, final SaltProgressDialog pd, final AttachmentDownloadListener adl) throws Exception{ //will download file if file does not exist yet otherwise just open the file from the system
//        final File file = new File(getDirForDownloadedAttachments()+claimItem.getAttachmentName());
//        if(!file.exists()){
//
//        	pd.show();
//        	new Thread(new Runnable() {
//				private String exception;
//
//				@Override
//				public void run() {
//		        	try {
//		                URL url = new URL("http://salttest.velosi.com/ImageViewer.aspx?id="+claimItem.getItemID()+"&dID="+claimItem.getAttachmentDocumentID());
//		                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//		                urlConnection.setRequestMethod("GET");
//		                urlConnection.setDoOutput(true);
//		                urlConnection.connect();
//
//		                FileOutputStream fileOutput = new FileOutputStream(new File(getDirForDownloadedAttachments(), claimItem.getAttachmentName()));
//		                InputStream inputStream = urlConnection.getInputStream();
//		                byte[] buffer = new byte[1024];
//		                int bufferLength = 0;
//
//		                while ( (bufferLength = inputStream.read(buffer)) > 0 )
//		                    fileOutput.write(buffer, 0, bufferLength);
//
//		                fileOutput.close();
//
//			        } catch (Exception e) {
//			        	e.printStackTrace();
//			        	exception = e.getMessage();
//			        }
//
//            		new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//            			@Override
//            			public void run() {
//            				pd.dismiss();
//            				if(exception == null)
//                				adl.onAttachmentDownloadFinish(file);
//            				else
//            					adl.onAttachmentDownloadFailed(exception);
//            			}
//            		});
//				}
//			}).start();
//        }else
//        	adl.onAttachmentDownloadFinish(file);
//	}

//	public void openAttachment(HomeActivity activity, String ext, File file) throws Exception{
//		if(!acceptedFiles.contains(ext.toLowerCase()))
//			throw new Exception("Cannot accept file with "+ext+" extension");
//
//    	if(ext.equals(ACCEPTEDFILE_PDF)){
//    		Uri path = Uri.fromFile(file);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(path, "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            try {
//                activity.startActivity(intent);
//            }
//            catch (ActivityNotFoundException e) {
//                Toast.makeText(activity,  "No Application Available to Open PDF",  Toast.LENGTH_SHORT).show();
//            }
//    	}else{
//    		Uri path = Uri.fromFile(file);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(path, "image/*");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            try {
//            	activity.startActivity(intent);
//            }
//            catch (ActivityNotFoundException e) {
//                Toast.makeText(activity,  "No Application Available to Open Image",  Toast.LENGTH_SHORT).show();
//            }
//    	}
//	}

	//usable when clicking on the image link in the claimitemdetailsfragment
	public void downloadDocument(final int documentID, final int refID, final int obTypeID, final String outputFilename, final AttachmentDownloadListener adl) throws Exception{ //will download file if file does not exist yet otherwise just open the file from the system
        if(obTypeID == 1) System.out.println("SALTX Claim");
        else if(obTypeID == 2) System.out.println("SALTX Leave");
        else if(obTypeID == 3) System.out.println("SALTX ClaimLineItem");
        else if(obTypeID == 5) System.out.println("SALTX Recruitment");
        else if(obTypeID == 7) System.out.println("SALTX Capex");
        else System.out.println("SALTX other");
        final File file = new File(getDirForDownloadedAttachments()+outputFilename);
//        if(!file.exists()){
            new Thread(new Runnable() {
                private String exception;

                @Override
                public void run() {
                    try {
						String fileUrl = imageViewRoot+"?dID="+documentID+"&refID="+refID+"&obTypeID="+obTypeID;
						System.out.println("Downloading file from "+fileUrl);
						URL url = new URL(fileUrl);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(true);
                        urlConnection.connect();

                        FileOutputStream fileOutput = new FileOutputStream(new File(getDirForDownloadedAttachments(), outputFilename));
                        InputStream inputStream = urlConnection.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;

                        while ( (bufferLength = inputStream.read(buffer)) > 0 )
                            fileOutput.write(buffer, 0, bufferLength);

                        fileOutput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            if(exception == null)
                                adl.onAttachmentDownloadFinish(file);
                            else
                                adl.onAttachmentDownloadFailed(exception);
                        }
                    });
                }
            }).start();
//        }else
//            adl.onAttachmentDownloadFinish(file);
	}

	public void openDocument(Context context, File documentFile){
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(documentFile).toString());
		String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		System.out.println("SALTX extension "+extension+" mimtype "+mimetype);
		intent.setDataAndType(Uri.fromFile(documentFile), (extension.equalsIgnoreCase("") || mimetype == null)?"text/*":mimetype);
		context.startActivity(intent);
	}

	public void saveToTextFile(String contents){
	    try{
	        File gpxfile = new File(getDirForCapturedAttachments(), "test.txt");
	        FileWriter writer = new FileWriter(gpxfile);
	        writer.append(contents);
	        writer.flush();
	        writer.close();
	    }catch(IOException e){
	         e.printStackTrace();
	    }
	}
	
	public interface AttachmentDownloadListener{
		
		public void onAttachmentDownloadFinish(File downloadedFile);
		public void onAttachmentDownloadFailed(String errorMessage);
	}
}
