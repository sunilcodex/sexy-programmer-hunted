package com.hunted;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class GameMapView extends View
{
	Bitmap _textureBitmap;
	MapView _mapview;
	UIHelper _uiHelper;
	float _angle = 0;
	long _lastDrawTime;
	HashMap<String,Player> _players;
	int _viewType;
	
	public GameMapView(Context context, MapView mapview, int viewType, UIHelper uiHelper, HashMap<String,Player> players)
	{
		super(context);
		_mapview = mapview;
		_players = players;
		_uiHelper = uiHelper;
		_viewType = viewType;
		_textureBitmap = BitmapFactory.decodeResource(this.getResources(), viewType == PlayerType.Player ? R.drawable.radar : R.drawable.radar_red);
		_lastDrawTime = SystemClock.currentThreadTimeMillis();
	}

	
	@Override
	protected void onDraw (Canvas canvas)
	{
		// Draw radar view
		Paint paint = new Paint();
		if(_viewType == PlayerType.Player)
			paint.setARGB(100, 77, 153, 89);
		else
			paint.setARGB(100, 178, 54, 54);
		
		paint.setStyle(Style.FILL);
		canvas.drawRect(this.getLeft(), this.getTop(),this.getRight(),this.getBottom(), paint);
		
		Point center = new Point(this.getLeft() + this.getWidth() / 2, this.getTop() + this.getHeight() / 2);
		
		long now = SystemClock.currentThreadTimeMillis();
		_angle += 0.0005 * (now - _lastDrawTime);
		_lastDrawTime = now;

		float d = this.getHeight() * 0.45f;
		float x = center.x + d * (float)Math.cos(_angle);
		float y = center.y + d * (float)Math.sin(_angle);
		
		float angle2 = _angle - 0.5f;
		float x2 = center.x + d * (float)Math.cos(angle2);
		float y2 = center.y + d * (float)Math.sin(angle2);

		float[] vertices = new float[] {
				center.x,  center.y,
				x2,  y2,
				center.x, center.y,
				x, y,
		};

		canvas.drawBitmapMesh(_textureBitmap, 1, 1, vertices, 0, null, 0, new Paint());
		
		// Draw players
		paint = new Paint();
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
        
		for (Player player : _players.values())
		{
			Projection projection = _mapview.getProjection();
	        Point screenPts = new Point();
	        projection.toPixels(player.getLocation(), screenPts);
	        
	        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), 
	        		player.PlayerType == PlayerType.Player ? R.drawable.boy_small : R.drawable.ninjaboy2_small
	        );           
	        canvas.drawBitmap(bmp, screenPts.x - bmp.getWidth() / 2, screenPts.y - bmp.getHeight(), null);
	        canvas.drawText(player.Name, screenPts.x - paint.measureText(player.Name) / 2, screenPts.y + paint.getTextSize(), strokPaint);
	        canvas.drawText(player.Name, screenPts.x - paint.measureText(player.Name) / 2, screenPts.y + paint.getTextSize(), paint);
	        
		}
		
		invalidate();
	}
}