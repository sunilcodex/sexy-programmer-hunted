package com.hunted_free_v1;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity implements View.OnTouchListener{
    /** Called when the activity is first created. */
	
	RelativeLayout _mainLayout;
	private ImageView bgimg;
	private UIHelper _uiHelper;
	private ImageView _loading;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create UI helper for ui scaling
		Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		_uiHelper = new UIHelper(display.getWidth(), display.getHeight());

		// basic display setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(1);

		// create button
		_loading = this.getImageView(R.drawable.touch);
		_loading.setOnTouchListener(this);
		
		// set UI to proper location and size
		_uiHelper.SetImageView(_loading, 720, 1280, 210, 1100);

		LayoutInflater inflater = LayoutInflater.from(this);
		_mainLayout = (RelativeLayout) inflater.inflate(R.layout.main, null);
		_mainLayout.addView(_loading);

		setContentView(_mainLayout);

		// Touch to screen
		bgimg = (ImageView) findViewById(R.id.imageView1);
		
		bgimg.setOnTouchListener(this);
		
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		this.finish();
    		return true;
    	}
    	else
    	{
    		return super.onKeyDown(keyCode, event);
    	}
    }
    
    @Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// create main buttons
		final ImageButton btnSinglePlayer = this.getButton(R.drawable.button_single_player);
		//final ImageButton btnNewGame = this.getButton(R.drawable.button_new_game);
		//final ImageButton btnJoinGame = this.getButton(R.drawable.button_join_game);
		
		btnSinglePlayer.setOnTouchListener(new ImageButton.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{

				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btnSinglePlayer.setImageResource(R.drawable.button_single_player2);

					Intent intent = new Intent();
					//SocketConnect.Instance = new SocketConnect(SocketConnect.DefaultIP, SocketConnect.DefaultPort);
					intent.setClass(MainActivity.this, GameActivity.class);
					intent.putExtra("single_player", true);
					intent.putExtra("player_type", PlayerType.Player);
					intent.putExtra("player_name", MainActivity.this.getResources().getString(R.string.test_man));
					startActivity(intent);

				}
				else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					btnSinglePlayer.setImageResource(R.drawable.button_single_player);
				}
				return false;
			}
    });
		
		// set UI to proper location and size
		_uiHelper.SetImageView(btnSinglePlayer, 720, 1280, 255, 710);
		//_uiHelper.SetImageView(btnNewGame, 720, 1280, 295, 810);
		//_uiHelper.SetImageView(btnJoinGame, 720, 1280, 310, 920);
		
		_mainLayout.removeView(_loading);
		_mainLayout.addView(btnSinglePlayer);
		//_mainLayout.addView(btnNewGame);
		//_mainLayout.addView(btnJoinGame);

		return false;
	}
    
    ImageButton getButton(int id)
    {
    	ImageButton button = new ImageButton(this);
    	button.setImageResource(id);
    	button.setBackgroundColor(Color.TRANSPARENT);
    	button.setPadding(0, 0, 0, 0);
    	button.setScaleType(ScaleType.FIT_XY);
		return button;
    }
    
    ImageView getImageView(int id)
	{
		ImageView image = new ImageView(this);
		image.setImageResource(id);
		image.setBackgroundColor(Color.TRANSPARENT);
		image.setPadding(0, 0, 0, 0);
		image.setScaleType(ScaleType.FIT_XY);
		return image;
	}
}
