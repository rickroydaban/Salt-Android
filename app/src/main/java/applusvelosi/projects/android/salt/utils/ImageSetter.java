package applusvelosi.projects.android.salt.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class ImageSetter implements Runnable{
	private Options options;
	private Bitmap selectedBitmap;
	private HomeActivity activity;
	private Uri uri;
	private ImageView iv;
	
	public ImageSetter(HomeActivity activity, Uri uri, ImageView iv){
		this.activity = activity;
		this.uri = uri;
		this.iv = iv;
	}
	
	@Override
	public void run() {
  		try {
	  	    options = new BitmapFactory.Options();
	  	    options.inSampleSize = 4;
	  	    
	  	    selectedBitmap = android.provider.MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
	
	  		}catch (OutOfMemoryError e) {
	  	    e.printStackTrace();
	
	  	    System.gc();
	  	    
	  	    try {
  	    		options.inSampleSize = 8;
	  	        selectedBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
	  	    } catch (OutOfMemoryError e2) {
	  	      e2.printStackTrace();
	  	    }
  	 	} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
  		
        new Handler(Looper.getMainLooper()).post(new Runnable(){
	      	public void run() {
	      		iv.setImageBitmap(selectedBitmap);
	      		iv.setDrawingCacheEnabled(true);
	      		iv.buildDrawingCache(true);
	      		String filePath = Environment.getExternalStorageDirectory()+"/SALT/photos/";
	      		File path = new File(filePath);
	      		if(!path.exists())
	      			path.mkdirs();
	      		
	      		File newFile = new File(path, "TEST"+".jpg");
	      		FileOutputStream out;
				try {										
					out = new FileOutputStream(newFile);
	        		iv.getDrawingCache(true).compress(CompressFormat.JPEG, 100, out);
	        		out.flush();
	        		out.close();
	        		iv.setDrawingCacheEnabled(false);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
	      	}
        });
	}

}