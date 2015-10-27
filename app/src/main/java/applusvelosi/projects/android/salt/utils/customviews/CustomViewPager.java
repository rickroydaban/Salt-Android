package applusvelosi.projects.android.salt.utils.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isPagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(isPagingEnabled)
    		return super.onTouchEvent(event);
    	
        return isPagingEnabled;
    }

    //manage view pager item touches
    @Override 
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	if(isPagingEnabled)
    		return super.onInterceptTouchEvent(event);
    	
    	return !isPagingEnabled;
    }
    
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}