package com.hunted;

import java.net.ContentHandler;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PlayerMapOverlay extends Overlay
{
	Player _player;
	Context _context;
	UIHelper _uiHelper;
	
	public PlayerMapOverlay(Context context, UIHelper uiHelper, Player player)
	{
		_context = context;
		_uiHelper = uiHelper;
		_player = player;
	}
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);
        
        Projection projection = mapView.getProjection();
        Point screenPts = new Point();
        projection.toPixels(_player.getLocation(), screenPts);
        
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(_uiHelper.scaleHeight(1280, 30));
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        
        Paint strokPaint = new Paint();
        strokPaint.setColor(Color.WHITE);
        strokPaint.setTextSize(_uiHelper.scaleHeight(1280, 30));
        strokPaint.setAntiAlias(true);
        strokPaint.setStyle(Paint.Style.STROKE);
        strokPaint.setStrokeWidth(_uiHelper.scaleHeight(1280, 7));
        
        Rect textBoundRect = new Rect();
        //paint.getTextBounds(_player.Name, 0, _player.Name.length()-1, textBoundRect);
        
        Bitmap bmp = BitmapFactory.decodeResource(_context.getResources(), 
        		_player.PlayerType == PlayerType.Player ? R.drawable.boy_small : R.drawable.ninjaboy2_small
        );           
        canvas.drawBitmap(bmp, screenPts.x - bmp.getWidth() / 2, screenPts.y - bmp.getHeight(), null);
        canvas.drawText(_player.Name, screenPts.x - paint.measureText(_player.Name) / 2, screenPts.y + paint.getTextSize(), strokPaint);
        canvas.drawText(_player.Name, screenPts.x - paint.measureText(_player.Name) / 2, screenPts.y + paint.getTextSize(), paint);
        return true;
    }
}
