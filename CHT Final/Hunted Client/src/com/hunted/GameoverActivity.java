package com.hunted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

public class GameoverActivity extends Activity
{
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		boolean playerWin = this.getIntent().getBooleanExtra("player_win", true);
				
		setContentView(R.layout.game_over);
		
		ImageView imgGameover =(ImageView)this.findViewById(R.id.imgGameover);
		imgGameover.setImageResource(playerWin ? R.drawable.player_win : R.drawable.hunter_win);
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
           return true;

        return false;
    }
}
