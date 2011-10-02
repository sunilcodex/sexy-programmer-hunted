package com.hunted_free_v1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Message; 

public class ClientJoinGame extends ListActivity{
    /** Called when the activity is first created. */
	protected static final int GUIUPDATEIDENTIFIER = 0x101; 
	  private List<String> name=new ArrayList<String>();
	  private List<String> status=new ArrayList<String>();
	  private List<String> ready=new ArrayList<String>();
	  private String realname;
	  public GameRoomAdapter gameroom;
	  Button btnStart;
	 Thread myRefreshThread = null;
	 
	 boolean readyOK = false;
	  
	  private List<String[]> list = new ArrayList<String[]>();
	  private List<String[]> list2 = new ArrayList<String[]>();



      
    @Override 
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // create UI helper for ui scaling
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		UIHelper uiHelper = new UIHelper(display.getWidth(),display.getHeight());
		
		// basic display setting
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(1);
              
        setContentView(R.layout.join_game);
        TextView mtext = (TextView)findViewById(R.id.textView1);
        
        
        //get groupID and Name
        Bundle bundle = this.getIntent().getExtras();
        String groupID = bundle.getString("groupID");
        //set ID
        mtext.setText("ID : "+groupID);
        realname = bundle.getString("Name");
        
        
        
        //Start or Ready Button
       btnStart = (Button) findViewById(R.id.button_start);
       btnStart.setText("Ready");
        btnStart.setOnTouchListener(new Button.OnTouchListener()
        {
        	
        	@Override  
			 public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnStart.setBackgroundResource(R.drawable.botton3);
					
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					btnStart.setBackgroundResource(R.drawable.botton);
					try {
						clear_info();						
						if(readyOK){
							list2 = SocketConnect.Instance.Other_Cancel(SocketConnect.Instance, SocketConnect.SessionID);
							readyOK = false;
						}
			    		else{
			    			list2 = SocketConnect.Instance.Client_get_ready(SocketConnect.Instance, SocketConnect.SessionID);
							readyOK = true;
			    		}
			    		
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
        	  return false; 
          }
        });
        
			try {
				getPlayerList();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

    }
    
    //Ê°àË??ûÂª∫cancel host ?å‰∏≠?∑thread
    @Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
	   
	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
		System.out.println("QUIT");
		myRefreshThread.interrupt();
		list = new ArrayList<String[]>();
    	try {
    		if(readyOK){
    			list2 = SocketConnect.Instance.Other_Cancel(SocketConnect.Instance, SocketConnect.SessionID);
    			readyOK = false;	
    		}
    		else{
    		list2 = SocketConnect.Instance.Client_cancel(SocketConnect.Instance, SocketConnect.SessionID);    		
    		finish();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;   
	}   
	      
	return super.onKeyDown(keyCode, event);
    }
    ////////////////////////////////////////
    
    
    
    
    class myThread2 implements Runnable { 
        public void run() {
             while (!Thread.currentThread().isInterrupted()) { 
            	  
                  Message message = new Message(); 
                  message.what = JoinGameActivity.GUIUPDATEIDENTIFIER;                   
                  ClientJoinGame.this.myHandler.sendMessage(message);
 
                  try { 
                       Thread.sleep(1000);  
                  } catch (InterruptedException e) { 
                       Thread.currentThread().interrupt(); 
                  } 
             } 
        } 
   } 
    
    
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) { 
        	//?àÊ?Á©∫list
        	clear_info();
        	//?ê‰??†Êñ∞Ë≥áË?
        	try {
        		if(readyOK)
        		list = SocketConnect.Instance.Other_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);	
        		else       		
				list = SocketConnect.Instance.Client_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//name=new ArrayList<String>();
        	//status=new ArrayList<String>();
            //Á¨¨‰?Á≠ÜË®≠ÂÆöÁÇ∫[?ûÂà∞?πÁõÆ?Ñ] 
        	
        for(int i = 0;i < list.size();i++){
        	System.out.println(list.get(i)[1]);
         	name.add(list.get(i)[1]);
         	status.add(list.get(i)[2]);
            ready.add(list.get(i)[3]);
            }
        
	        gameroom.notifyDataSetChanged();
              
             super.handleMessage(msg); 
        } 
   };
    
    private void getPlayerList() throws IOException
    {
    	list = new ArrayList<String[]>();
    	list = SocketConnect.Instance.Client_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);

    	
    for(int i = 0;i < list.size();i++){	
     	name.add(list.get(i)[1]);
     	status.add(list.get(i)[2]);
        ready.add(list.get(i)[3]);
    }

     // Â∞áÊ??âÊ?Ê°àÂ??•ArrayList‰∏?
     
    //  ‰ΩøÁî®?™Â?Áæ©Á?MyAdapter‰æÜÂ?Ë≥áÊ??≥ÂÖ•ListActivity 
     gameroom = new GameRoomAdapter(this,name,status,ready);
     setListAdapter(gameroom); 
  
     myRefreshThread =  new Thread(new myThread2());
     myRefreshThread.start();
    }
    
    public void clear_info(){
    	name.clear();
    	status.clear();
    	ready.clear();
    	
    }
    		

}