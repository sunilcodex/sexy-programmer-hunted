package com.hunted;

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
import android.widget.Spinner;
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
	 private Handler handler = new Handler(); 
	  int waiting_time = 5;
	  int start_count = 0;
	 public int type;
	 boolean thread_cancel = false;
	  
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
        mtext.setText("Your Group ID : "+groupID);
        realname = bundle.getString("Name");
        
        
        //TextView r = (TextView) findViewById(R.id.textView2);
        //r.setText("請在Host確定身分後才按下Ready鍵");
        
        //消除spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner1); 
        spinner.setVisibility(View.GONE);
        
        
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
    
    //案返回建cancel host 和中斷thread
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
             while (!Thread.currentThread().isInterrupted() || !thread_cancel) {
            	 
            	 if(!thread_cancel){
                  Message message = new Message(); 
                  message.what = JoinGameActivity.GUIUPDATEIDENTIFIER;                   
                  ClientJoinGame.this.myHandler.sendMessage(message);
            	 }
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
        	boolean ww = false;
			try {
				ww = SocketConnect.Instance.wait(SocketConnect.Instance, SocketConnect.SessionID);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	if(ww){    
        		thread_cancel = true;
        	myRefreshThread.interrupt();
			//設定定時要執行的方法
	        handler.removeCallbacks(updateTimer);
	        //設定Delay的時間
	        handler.postDelayed(updateTimer, 1000);

        	}
        	
        	else{        	
        	//先清空list
        		
        	clear_info();
        	//逐一加新資訊
        	try {
        		if(readyOK)
        		list = SocketConnect.Instance.Other_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);	
        		else       		
				list = SocketConnect.Instance.Client_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println("get start!!!!!!!");	
        	//System.out.println("get start!!!!!!!" + list.get(0)[0]);
        	//如果傳回資訊為空字串則調出
        	
        	if( list.get(0)[0].equals("wrong")){
        		System.out.println("QUIT!!!!!!!");	
        		finish();
        	}
        	
        	
        else{
	
        for(int i = 0;i < list.size();i++){
        	if(list.get(i)[1].equals(realname)){
        		if(list.get(i)[2].equals("P"))
        			type = 0;
        		else
        			type = 1;		
        	}
        	//System.out.println(list.get(i)[1]);
         	name.add(list.get(i)[1]);
         	status.add(list.get(i)[2]);
            ready.add(list.get(i)[3]);
            }
        
	        gameroom.notifyDataSetChanged();
        	}
        	}
              
             super.handleMessage(msg); 
        } 
   };
    
   //遊戲開始倒數
   private Runnable updateTimer = new Runnable() {
       public void run() {
    	    try {
				list = SocketConnect.Instance.Client_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    TextView r = (TextView) findViewById(R.id.textView3);
	        String tmp = "剩餘" + String.valueOf(waiting_time - start_count) + "秒"; 
	        r.setText(tmp);


			if(start_count == waiting_time){
	    		Intent intent = new Intent();
	          	intent.setClass(ClientJoinGame.this, GameActivity.class); 
	          	intent.putExtra("single_player", false);
	          	intent.putExtra("player_type", type);
	          	intent.putExtra("player_name", realname);
	          	startActivity(intent);
			}
	        start_count++;
	        
	        if(start_count <= 5){
			handler.postDelayed(this, 1000);
	        }
	        
	        else
	        	finish();
       }
   };
/////////////////////////////////
   
   
    private void getPlayerList() throws IOException
    {
    	list = new ArrayList<String[]>();
    	list = SocketConnect.Instance.Client_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);

    	
    for(int i = 0;i < list.size();i++){	
    	if(list.get(i)[1].equals(realname)){
    		if(list.get(i)[2].equals("P"))
    			type = 0;
    		else
    			type = 1;		
    	}
     	name.add(list.get(i)[1]);
     	status.add(list.get(i)[2]);
        ready.add(list.get(i)[3]);
    }

     // 將所有檔案加入ArrayList中 
     
    //  使用自定義的MyAdapter來將資料傳入ListActivity 
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