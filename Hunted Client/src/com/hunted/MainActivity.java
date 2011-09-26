package com.hunted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	RelativeLayout _mainLayout;
	private ImageView bgimg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // create UI helper for ui scaling
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		UIHelper uiHelper = new UIHelper(display.getWidth(),display.getHeight());
		
		// basic display setting
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(1);
		
		// create button
		final ImageButton btnLoading = this.getButton(R.drawable.touch);

		// set UI to proper location and size
		uiHelper.SetImageView(btnLoading, 720, 1280, 210, 1100);

		LayoutInflater inflater = LayoutInflater.from(this);
		_mainLayout = (RelativeLayout)inflater.inflate(R.layout.main, null);
		_mainLayout.addView(btnLoading);

        setContentView(_mainLayout);
  
        //Touch to screen
		bgimg = (ImageView)findViewById(R.id.imageView1);
		bgimg.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
  	            // new一個Intent物件，並指定要啟動的class 
  	            Intent intent = new Intent();
  	        	intent.setClass(MainActivity.this, MainMenu.class);        	        	  
  	        	//呼叫一個新的Activity 
  	        	startActivity(intent); 
  	        	MainActivity.this.finish();
				return false;
			}

   });
 
        
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
    
}
