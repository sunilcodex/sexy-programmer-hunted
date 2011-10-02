package com.hunted_free_v1;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.ScaleAnimation;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UIHelper
{
	int _dstResolutionWidth;
	int _dstResolutionHeight;
	
	public UIHelper(int dstResolutionWidth, int dstResolutionHeight)
	{
		_dstResolutionWidth = dstResolutionWidth;
		_dstResolutionHeight = dstResolutionHeight;
	}
	
	public int[] scaleSize(int srcResolutionWidth, int srcResolutionHeight, int srcWidth, int srcHeight)
	{
		int width = (int)(srcWidth * ((float)_dstResolutionWidth / srcResolutionWidth));
		int height = (int)(srcHeight * ((float)_dstResolutionHeight / srcResolutionHeight));
		
		return new int[] { width, height };
	}
	
	public int scaleHeight(int srcResolutionHeight, int srcHeight)
	{
		int height = (int)(srcHeight * ((float)_dstResolutionHeight / srcResolutionHeight));
		
		return height;
	}
	
	public int scaleWidth(int srcResolutionWidth, int srcWidth)
	{
		int width = (int)(srcWidth * ((float)_dstResolutionWidth / srcResolutionWidth));
		
		return width;
	}
	
	public int[] scalePoint(int srcResolutionWidth, int srcResolutionHeight, int x, int y)
	{
		return scaleSize(srcResolutionWidth, srcResolutionHeight, x, y);
	}
	
	public void SetImageView(ImageView view, int srcResolutionWidth, int srcResolutionHeight, int x, int y)
	{
		int[] newSize = this.scaleSize(srcResolutionWidth, srcResolutionHeight, view.getDrawable().getIntrinsicWidth(), view.getDrawable().getIntrinsicHeight());
		int[] newPos = this.scalePoint(srcResolutionWidth, srcResolutionHeight, x, y);
		
		view.setLayoutParams(new Gallery.LayoutParams(newSize[0], newSize[1]));
		
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(newSize[0], newSize[1]);
		param.setMargins(newPos[0], newPos[1], 0, 0);
		view.setLayoutParams(param);
		
	}
}
