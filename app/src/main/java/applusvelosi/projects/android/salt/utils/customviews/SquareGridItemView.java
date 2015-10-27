package applusvelosi.projects.android.salt.utils.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareGridItemView extends RelativeLayout{

	public SquareGridItemView(Context context, AttributeSet st){
		super(context, st);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}	
}
