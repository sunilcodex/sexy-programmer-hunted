package com.hunted;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.view.MotionEvent;

public class MainMenu extends Activity {
	public static final int requestCode01 = (int) 0x1001;
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
		final ImageButton btnSinglePlayer = this.getButton(R.drawable.button_single_player);
		final ImageButton btnNewGame = this.getButton(R.drawable.button_new_game);
		final ImageButton btnJoinGame = this.getButton(R.drawable.button_join_game);
				
		//Single player button  call 主畫面 project
		btnSinglePlayer.setOnTouchListener(new ImageButton.OnTouchListener(){
			@Override  
			 public boolean onTouch(View v, MotionEvent event) {
						
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnSinglePlayer.setImageResource(R.drawable.button_single_player2);
					
					// TODO Auto-generated method stub
			        try
			        {
			          Intent intent = new Intent();
			          // 傳入package名稱及package名稱加class名稱
			          intent.setClass(MainMenu.this, GameActivity.class);
			          // 將值傳給EX09_05
			          Bundle bundle = new Bundle();
			          //bundle.putString("STR_INPUT", "HI, 我來自EX03_25...");
			          intent.putExtras(bundle);

			          startActivityForResult(intent, requestCode01);
			        } catch (Exception e)
			        {
			          // Log.e(TAG, e.toString());
			         // e.printStackTrace();
			          //TextView01.setText("請先安裝EX03_25_B這支程式!");
			        }
					
					
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					btnSinglePlayer.setImageResource(R.drawable.button_single_player);
				}
				 return false;  
		}
    });
		
		
		//New game dialog
		btnNewGame.setOnTouchListener(new ImageButton.OnTouchListener()
{
			@Override  
			 public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnNewGame.setImageResource(R.drawable.button_new_game2);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					btnNewGame.setImageResource(R.drawable.button_new_game);
					final Dialog dialog = new Dialog(MainMenu.this, R.style.MyDialog);
		        	//set ContentView
		        	  dialog.setContentView(R.layout.host_dialog);
		        	 
		        	  //OK button
		        	  Button OK = (Button) dialog.findViewById(R.id.ok2);
		        	  OK.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		      	            // new一個Intent物件，並指定要啟動的class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainMenu.this, JoinGameActivity.class);        	        	  
		      	        	//呼叫一個新的Activity 
		      	        	startActivity(intent);  
		                  }
		              });
		        	  
		        	  //Cancel button
		        	  Button cancel = (Button) dialog.findViewById(R.id.cancel2);
		        	  cancel.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		      	           dialog.dismiss(); 
		                  }
		              });
		        	  dialog.show();
                } 

        	  return false;   
          }
        });
		
		
		
		//Join game dialog
        btnJoinGame.setOnTouchListener(new ImageButton.OnTouchListener()
        {
        	@Override  
			 public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnJoinGame.setImageResource(R.drawable.button_join_game2);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					btnJoinGame.setImageResource(R.drawable.button_join_game);
					final Dialog dialog = new Dialog(MainMenu.this, R.style.MyDialog);
		        	//set ContentView
		        	  dialog.setContentView(R.layout.joindialog);
		        	 
		        	  //OK button
		        	  Button OK = (Button) dialog.findViewById(R.id.ok);
		        	  OK.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		      	            // new一個Intent物件，並指定要啟動的class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainMenu.this, JoinGameActivity.class);        	        	  
		      	        	//呼叫一個新的Activity 
		      	        	startActivity(intent);  
		                  }
		              });
		        	  
		        	  //Cancel button
		        	  Button cancel = (Button) dialog.findViewById(R.id.cancel);
		        	  cancel.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		      	           dialog.dismiss(); 
		                  }
		              });
		        	  dialog.show();
				}
          
        	  
        	  return false; 
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