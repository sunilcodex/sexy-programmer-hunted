package com.hunted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class UIOverlayTest extends Activity {
	
	RelativeLayout _mainLayout;
	
    /** Called when the activity is first created. */
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
		ImageButton btnSinglePlayer = this.getButton(R.drawable.button_single_player);
		ImageButton btnNewGame = this.getButton(R.drawable.button_new_game);
		ImageButton btnJoinGame = this.getButton(R.drawable.button_join_game);
		
		//host game
		btnNewGame.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          {

        	    
        	            // new一個Intent物件，並指定要啟動的class 
        	            Intent intent = new Intent();
        	        	intent.setClass(UIOverlayTest.this, joib_game.class);        	        	  
        	        	//呼叫一個新的Activity 
        	        	startActivity(intent);
        	        	//關閉原本的Activity 
        	        	//UIOverlayTest.this.finish();
        			   
        			   //textOut.setText(editText.getText().toString());     
          }
        });
		
		
		
		//join button dialog
        btnJoinGame.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          {

        	   AlertDialog.Builder editDialog = new AlertDialog.Builder(UIOverlayTest.this); 
        	   editDialog.setTitle("Host ID:");  
        	   final EditText editText = new EditText(UIOverlayTest.this);
        	   //editText.setText(textOut.getText());  
        	   editDialog.setView(editText); 
        	   editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
        	   {    
        		   // do something when the button is clicked    
        		   public void onClick(DialogInterface arg0, int arg1) {  
        	            // new一個Intent物件，並指定要啟動的class 
        	            Intent intent = new Intent();
        	        	intent.setClass(UIOverlayTest.this, joib_game.class);        	        	  
        	        	//呼叫一個新的Activity 
        	        	startActivity(intent);
        	        	//關閉原本的Activity 
        	        	//UIOverlayTest.this.finish();
        			   
        			   //textOut.setText(editText.getText().toString());    
        		   }    
        	   });  
        	   editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        		   // do something when the button is clicked  
        		   public void onClick(DialogInterface arg0, int arg1) { 
        			   //...    
        		   }   
        	   });  
        	   editDialog.show();
          }
        });
		
		
		
		// set UI to proper location and size
		uiHelper.SetImageView(btnSinglePlayer, 720, 1280, 255, 710);
		uiHelper.SetImageView(btnNewGame, 720, 1280, 295, 810);
		uiHelper.SetImageView(btnJoinGame, 720, 1280, 310, 920);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		_mainLayout = (RelativeLayout)inflater.inflate(R.layout.main, null);
		_mainLayout.addView(btnSinglePlayer);
		_mainLayout.addView(btnNewGame);
		_mainLayout.addView(btnJoinGame);
		
        setContentView(_mainLayout);
 
        
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