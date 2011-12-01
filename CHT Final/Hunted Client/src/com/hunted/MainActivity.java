package com.hunted;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import net.emome.hamiapps.sdk.SDKService;
import net.emome.hamiapps.sdk.SDKService;

public class MainActivity extends Activity implements View.OnTouchListener{
    /** Called when the activity is first created. */
	
	RelativeLayout _mainLayout;
	private ImageView bgimg;
	private UIHelper _uiHelper;
	private ImageView _loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hami SDK function
		/*
		String url = SDKService.getAMDownloadURL(MainActivity.this); 
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		intent.addCategory(Intent.CATEGORY_BROWSABLE); 
		startActivity(intent);
		Intent intent1 = SDKService.getUpdateAMIntent(MainActivity.this);
		startActivity(intent1);
		*/
		////////////////
		
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
		final ImageButton btnNewGame = this.getButton(R.drawable.button_new_game);
		final ImageButton btnJoinGame = this.getButton(R.drawable.button_join_game);
		
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
					final Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialog);
		        	//set ContentView
		        	  dialog.setContentView(R.layout.host_dialog);
		        	 final EditText input = (EditText) dialog.findViewById(R.id.editText1);
		        	  
		        	  
		        	  //OK button
		        	  Button OK = (Button) dialog.findViewById(R.id.ok2);
		        	  OK.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		        			  String name = input.getText().toString();
		        				
		  			            try {
									SocketConnect.Instance = new SocketConnect(SocketConnect.DefaultIP, SocketConnect.DefaultPort);
									String[] new_game;
									new_game = SocketConnect.Instance.NewGame(SocketConnect.Instance, name);
									SocketConnect.SessionID = new String[] { new_game[0], new_game[1] };
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
    
		      	            // new銝?ntent?拐辣嚗蒂??閬???class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainActivity.this, JoinGameActivity.class);
		      	        	
		      	        	Bundle bundle = new Bundle();
		      	        	bundle.putString("groupID", SocketConnect.SessionID[0]);
		      	        	//System.out.println(Nametext.getText().toString());
		      	        	bundle.putString("Name", name);
		      	        	intent.putExtras(bundle);
		      	        	//?澆銝??ctivity 
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
					final Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialog);
		        	//set ContentView
		        	  dialog.setContentView(R.layout.joindialog);
		        	 
		        	 final EditText dname = (EditText) dialog.findViewById(R.id.JoinDlgTxtName);
		        	 final EditText did = (EditText) dialog.findViewById(R.id.JoinDlgTxtHostID);
		        	  //OK button
		        	  Button OK = (Button) dialog.findViewById(R.id.JoinDlgBtnOk);
		        	  OK.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		        			  /*
		      	            // new銝?ntent?拐辣嚗蒂??閬???class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainActivity.this, JoinGameActivity.class);        	        	  
		      	        	//?澆銝??ctivity 
		      	        	startActivity(intent);
		      	        	*/

			        			try {
			        				String name = dname.getText().toString();
			        				String id = did.getText().toString();
			  			            SocketConnect.Instance = new SocketConnect(SocketConnect.DefaultIP, SocketConnect.DefaultPort);
			  			            String[] new_game = SocketConnect.Instance.JoinGame(SocketConnect.Instance, name,id);
			  			            SocketConnect.SessionID = new String[] { new_game[0], new_game[1] };
			  			            SocketConnect.Player = name;
				      	            
			  			            if(new_game[0] != ""){
			  			            Intent intent = new Intent();
				      	        	intent.setClass(MainActivity.this, ClientJoinGame.class);  
				      	        	
				      	        	
				      	        	Bundle bundle = new Bundle();
				      	        	bundle.putString("groupID", new_game[0]);
				      	        	
				      	        	
				      	        	
				      	        	//System.out.println(Nametext.getText().toString());
				      	        	bundle.putString("Name", name);
				      	        	intent.putExtras(bundle);
				      	        	
				      	        	
				      	        	//?澆銝??ctivity 
				      	        	startActivity(intent);
				      	        	
			  			            }
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		        			  
		                  }
		              });
		        	  
		        	  //Cancel button
		        	  Button cancel = (Button) dialog.findViewById(R.id.JoinDlgBtnCancel);
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
		/*
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
					final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
		        	//set ContentView
					
					AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

					alert.setTitle("Log In");
					alert.setMessage("Name:");

					// Set an EditText view to get user input 
					final EditText input = new EditText(MainActivity.this);
					alert.setView(input);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

	        			try {
	        				String name = input.getText().toString();
	        				
	  			            SocketConnect.Instance = new SocketConnect(SocketConnect.DefaultIP, SocketConnect.DefaultPort);
	  			            String[] new_game = SocketConnect.Instance.NewGame(SocketConnect.Instance, name);
	  			            SocketConnect.SessionID = new String[] { new_game[0], new_game[1] };
		      	            // new銝?ntent?拐辣嚗蒂??閬???class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainActivity.this, JoinGameActivity.class);  
		      	        					      	        	
		      	        	Bundle bundle = new Bundle();
		      	        	bundle.putString("groupID", new_game[0]);
		      	        	//System.out.println(Nametext.getText().toString());
		      	        	bundle.putString("Name", name);
		      	        	intent.putExtras(bundle);
		      	        	
		      	        	
		      	        	//?澆銝??ctivity 
		      	        	startActivity(intent); 
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  // Do something with value!
					  }
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    // Canceled.
					  }
					});

					alert.show();
					
					*/
					/*
		        	  dialog.setContentView(R.layout.host_dialog);
		        	 
		        	  //OK button
		        	  Button OK = (Button) dialog.findViewById(R.id.ok2);
		        	  OK.setOnClickListener(new Button.OnClickListener()
		              {
		        		  public void onClick(View v)
		                  {
		      	            // new一個Intent物件，並指定要啟動的class 
		      	            Intent intent = new Intent();
		      	        	intent.setClass(MainActivity.this, JoinGameActivity.class);        	        	  
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
		        	  */
		
		/*
                } 

        	  return false;   
          }
        });
		*/
		
		/*
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
					
					
					AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

					alert.setTitle("Log In");

					// Set an EditText view to get user input 
					LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					 LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.clientdia, null);

					alert.setView(layout);
					//setContentView(R.layout.clientdia);
					final EditText dname = (EditText) layout.findViewById(R.id.editText1);
					final EditText did = (EditText) layout.findViewById(R.id.editText2);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

	        			try {
	        				String name = dname.getText().toString();
	        				String id = did.getText().toString();
	  			            SocketConnect.Instance = new SocketConnect(SocketConnect.DefaultIP, SocketConnect.DefaultPort);
	  			            String[] new_game = SocketConnect.Instance.JoinGame(SocketConnect.Instance, name,id);
	  			            SocketConnect.SessionID = new String[] { new_game[0], new_game[1] };
	  			            SocketConnect.Player = name;
		      	            
	  			            if(new_game[0] != ""){
	  			            Intent intent = new Intent();
		      	        	intent.setClass(MainActivity.this, ClientJoinGame.class);  
		      	        	
		      	        	
		      	        	Bundle bundle = new Bundle();
		      	        	bundle.putString("groupID", new_game[0]);
		      	        	
		      	        	
		      	        	
		      	        	//System.out.println(Nametext.getText().toString());
		      	        	bundle.putString("Name", name);
		      	        	intent.putExtras(bundle);
		      	        	
		      	        	
		      	        	//?澆銝??ctivity 
		      	        	startActivity(intent);
	  			            }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                  
      				
					  // Do something with value!
					  }
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    // Canceled.
					  }
					});

					alert.show();
					*/
					
					
					/*
					final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
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
		      	        	intent.setClass(MainActivity.this, JoinGameActivity.class);        	        	  
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
		        	  */
		
		/*
				}
          
        	  
        	  return false; 
          }
        });
        
        */
		
		// set UI to proper location and size
		_uiHelper.SetImageView(btnSinglePlayer, 720, 1280, 255, 710);
		_uiHelper.SetImageView(btnNewGame, 720, 1280, 340, 820);
		_uiHelper.SetImageView(btnJoinGame, 720, 1280, 350, 920);
		
		_mainLayout.removeView(_loading);
		_mainLayout.addView(btnSinglePlayer);
		_mainLayout.addView(btnNewGame);
		_mainLayout.addView(btnJoinGame);

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
