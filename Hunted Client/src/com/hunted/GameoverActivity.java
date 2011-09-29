package com.hunted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class GameoverActivity extends Activity
{
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setContentView(R.layout.game_over);
	}
	
	public boolean onTouchEvent (MotionEvent event)
	{
		super.onTouchEvent(event);
		
		// go back to main activity
		Intent intent = new Intent();
		intent.setClass(GameoverActivity.this, MainActivity.class);
		startActivity(intent);
		
		return true;
	}
}
