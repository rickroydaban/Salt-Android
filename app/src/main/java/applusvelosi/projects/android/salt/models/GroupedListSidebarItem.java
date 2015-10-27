package applusvelosi.projects.android.salt.models;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import applusvelosi.projects.android.salt.R;
import applusvelosi.projects.android.salt.utils.interfaces.GroupedListItemInterface;
import applusvelosi.projects.android.salt.views.HomeActivity;

public class GroupedListSidebarItem implements GroupedListItemInterface{
	public static final String SELECTED = "itemselected";
	public static final String NOT_SELECTED = "itemnotselected";
	private String itemLabel;
	private Drawable drawable, drawableSelected;
	private HomeActivity activity;
	//views
	RelativeLayout view;
	private TextView label;
	private ImageView icon;
	
	public GroupedListSidebarItem(String itemLabel, Drawable drawable, Drawable drawableSelected){
		this.itemLabel = itemLabel;
		this.drawable = drawable;
		this.drawableSelected = drawableSelected;
	}
	
	@Override
	public View getTextView(Activity a){
		activity = (HomeActivity)a;
		view = (RelativeLayout)a.getLayoutInflater().inflate(R.layout.tv_sidebar_item, null);
		view.setTag(NOT_SELECTED);
		
		label = (TextView)view.findViewById(R.id.tviews_sidebar_item_label);
		icon = (ImageView)view.findViewById(R.id.ivies_sidebar_item_image);
		label.setText(itemLabel);
		if(view.getTag().toString().equals(NOT_SELECTED))
			displayAsNormalItem();
		else 
			displayAsSelectedItem();

//set how far should the main view from the sidebar
//		float totalWidth = 0;
//		for(int i=0; i<view.getChildCount(); i++) {
//			view.getChildAt(i).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//			totalWidth += view.getChildAt(i).getMeasuredWidth();
//		}
//		totalWidth+=100;
//		activity.updateMaxSidebarShowWidth(this, totalWidth);

		return view;
	}

	public String getLabel(){
		return itemLabel;
	}
	
	public void displayAsSelectedItem(){
		view.setTag(SELECTED);
		label.setTextColor(activity.getResources().getColor(R.color.orange_velosi));
		icon.setImageDrawable(drawableSelected);
	}
	
	public void displayAsNormalItem(){
		view.setTag(NOT_SELECTED);
//		view.setBackgroundColor(Color.WHITE);
		label.setTextColor(activity.getResources().getColor(R.color.black));
		icon.setImageDrawable(drawable);
	}

}
