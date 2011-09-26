package com.hunted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class JoinGameActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	  private List<String> name=null;
	  private List<String> status=null;
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
        final Button btnStart = (Button) findViewById(R.id.button_start);
        btnStart.setOnTouchListener(new Button.OnTouchListener()
        {
        	@Override  
			 public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnStart.setBackgroundResource(R.drawable.botton);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					btnStart.setBackgroundResource(R.drawable.botton2);
				}
        	  return false; 
          }
        });
        

        getPlayerList();
        
        
    }
    
    
    private void getPlayerList()
    {
    	
    	name=new ArrayList<String>();
    	status=new ArrayList<String>();

          /* 第一筆設定為[回到根目錄] */
    	name.add("b1");
    	status.add("h");
          /* 第二筆設定為[回到上一層] */
          name.add("b2");
          status.add("p");

          name.add("b2");
          status.add("p");
          
          name.add("b2");
          status.add("p");
        /* 將所有檔案加入ArrayList中 */
        
        /* 使用自定義的MyAdapter來將資料傳入ListActivity */
        setListAdapter(new GameRoomAdapter(this,name,status));   
    	
    }
    
    
}