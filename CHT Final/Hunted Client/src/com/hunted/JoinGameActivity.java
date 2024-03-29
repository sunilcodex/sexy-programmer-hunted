package com.hunted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Message; 

public class JoinGameActivity extends ListActivity{
    /** Called when the activity is first created. */
	protected static final int GUIUPDATEIDENTIFIER = 0x101; 
	  private List<String> name=new ArrayList<String>();
	  private List<String> status=new ArrayList<String>();
	  private List<String> ready=new ArrayList<String>();
	  private String realname;
	  public GameRoomAdapter gameroom;
	  public int type;
	  int allOK = 1;
	  int waiting_time = 5;
	  int start_count = 0;
	  Button btnStart;
	  int init = 0;
	  boolean thread_cancel = false;
	  
	 Thread myRefreshThread = null;
	 private Handler handler = new Handler(); 
	  
	  private List<String[]> list = new ArrayList<String[]>();
	  //private Context cc = this;

	 //public CustomAdapter adapter; 



      
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
       
        
        TextView r = (TextView) findViewById(R.id.textView2);
        r.setVisibility(View.GONE);
        
        
      //建立一個ArrayAdapter物件，並放置下拉選單的內容 
        Spinner spinner = (Spinner) findViewById(R.id.spinner1); 
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.settime_spinner,R.id.textview,new String[]{"設定遊戲時間(預設1分鐘)","1秒鐘","10秒鐘","30秒鐘","90秒鐘","300秒鐘"});
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, R.layout.settime_spinner,cursor, new String[] { PutFieldName }, new int[] {android.R.id.text1});

    //設定下拉選單的樣式 
      // adapter.setDropDownViewResource(android.R.layout.si); 
       
      spinner.setAdapter(adapter); 
     adapter.remove("1");
     spinner.setAdapter(adapter); 
      
      //設定項目被選取之後的動作
      //手動設定時間
      spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
          public void onItemSelected(AdapterView adapterView, View view, int position, long id){
            
        	  if(!adapterView.getSelectedItem().toString().equals("設定遊戲時間(預設1分鐘)")){
        	  String[] time = adapterView.getSelectedItem().toString().split("秒");
        	 // int int_time = Integer.valueOf(time[0]) * 60 + 2;
        	  int int_time = Integer.valueOf(time[0]) + 3;
        	  try {
				SocketConnect.Instance.HostGameTime(SocketConnect.Instance, SocketConnect.SessionID, String.valueOf(int_time));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  }
        	  else{
             	  try {
     				SocketConnect.Instance.HostGameTime(SocketConnect.Instance, SocketConnect.SessionID, "63");
     			} catch (IOException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}  
        		  
        		  
        	  }
        	  
          }
          public void onNothingSelected(AdapterView arg0) {
              //Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
          }
      });
 
        
        //get groupID and Name
        Bundle bundle = this.getIntent().getExtras();
        String groupID = bundle.getString("groupID");
        //set ID
        mtext.setText("Your Group ID : "+groupID);
        realname = bundle.getString("Name");
        
        
        
        //Start or Ready Button
        btnStart = (Button) findViewById(R.id.button_start);
        btnStart.setOnTouchListener(new Button.OnTouchListener()
        {
        	
        	@Override  
			 public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(allOK == 1)
					btnStart.setBackgroundResource(R.drawable.botton3);
					int i = 0;

				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					if(allOK == 1){
					btnStart.setBackgroundResource(R.drawable.botton);
					//go to count down
					try {
						
						list = SocketConnect.Instance.HostStart(SocketConnect.Instance, SocketConnect.SessionID);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					thread_cancel = true;
					myRefreshThread.interrupt();
					//設定定時要執行的方法
			        handler.removeCallbacks(updateTimer);
			        //設定Delay的時間
			        handler.postDelayed(updateTimer, 1000);

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
		Thread.currentThread().interrupt();
		thread_cancel = true;
		finish();
		list = new ArrayList<String[]>();
    	try {
			list = SocketConnect.Instance.HostCancel(SocketConnect.Instance, SocketConnect.SessionID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;    
	}   
	      
	return super.onKeyDown(keyCode, event);
    }
    
    //thread running
    class myThread implements Runnable { 
        public void run() {
             while (!Thread.currentThread().isInterrupted() || !thread_cancel) { 
            	  if(!thread_cancel){
                  Message message = new Message(); 
                  message.what = JoinGameActivity.GUIUPDATEIDENTIFIER;                   
                  JoinGameActivity.this.myHandler.sendMessage(message);
 
                  try { 
                       Thread.sleep(500);  
                  } catch (InterruptedException e) { 
                       Thread.currentThread().interrupt(); 
                  } 
             } 
             }
        } 
   } 
    
    //隨時更新清單
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) { 
        	//先清空list
        	allOK = 1;
        	clear_info();
        	//逐一加新資訊
        	try {
				list = SocketConnect.Instance.Host_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//name=new ArrayList<String>();
        	//status=new ArrayList<String>();
            //第一筆設定為[回到根目錄] 
        	
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
        
        //check Ready and Host
        for(int i = 0;i < list.size();i++){	
            if(!list.get(i)[1].toString().equals(realname)){
	            if(list.get(i)[3].toString().equals("X")){
	               allOK = 0;
	               btnStart.setBackgroundResource(R.drawable.bottonnopress);
	               break;
	            }
	            else 
	            	btnStart.setBackgroundResource(R.drawable.botton);
	        }
        }
  
	        gameroom.notifyDataSetChanged();
              
             super.handleMessage(msg); 
        } 
   };
   ///////////////////////////////////////////////
   
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
	          	intent.setClass(JoinGameActivity.this, GameActivity.class); 
	          	intent.putExtra("single_player", false);
	          	intent.putExtra("player_type", type);
	          	System.out.println("Type!!!!!!!:"+type);
	          	intent.putExtra("player_name", realname);
	          	startActivity(intent);
			}
	        start_count++;
	        
	        if(start_count <= 5)
			handler.postDelayed(this, 1000);
	        else
	        	finish();
       }
   };
/////////////////////////////////
   
   
    private void getPlayerList() throws IOException
    {
    	list = new ArrayList<String[]>();
    	list = SocketConnect.Instance.Host_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);

    	
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
  
     myRefreshThread =  new Thread(new myThread());
     myRefreshThread.start();
    }
    
    
    @Override
    protected void onListItemClick(ListView l,View v,
                                   int position,long id){
    	try {
			list = SocketConnect.Instance.Host_waiting_get(SocketConnect.Instance, SocketConnect.SessionID);
		
    	
    	String now_statue = list.get(position)[2];
    	String user = list.get(position)[0];
    	if(now_statue.equals("H"))
    		list = SocketConnect.Instance.HostWaitChange(SocketConnect.Instance, SocketConnect.SessionID, user, "P");
    	else
    		list = SocketConnect.Instance.HostWaitChange(SocketConnect.Instance, SocketConnect.SessionID, user, "H");
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void clear_info(){
    	name.clear();
    	status.clear();
    	ready.clear();
    	
    }
    		

}