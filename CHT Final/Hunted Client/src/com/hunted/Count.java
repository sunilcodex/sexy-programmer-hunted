package com.hunted;


import java.io.IOException;
import java.util.HashMap;

import com.hunted.JoinGameActivity.myThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class Count extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.count);

    	new Thread(new myThread()).start();

    }
    
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) { 
        	try {
    			Thread.sleep(6000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		Intent intent = new Intent();
          	intent.setClass(Count.this, GameActivity.class); 
          	intent.putExtra("single_player", true);
          	intent.putExtra("player_type", PlayerType.Player);
          	intent.putExtra("player_name", SocketConnect.Player);
          	
          	startActivity(intent); 
              
             super.handleMessage(msg); 
        } 
   };
   
   class myThread implements Runnable { 
       public void run() {
            while (!Thread.currentThread().isInterrupted()) { 
           	  
                 Message message = new Message(); 
                 message.what = JoinGameActivity.GUIUPDATEIDENTIFIER;                   
                 Count.this.myHandler.sendMessage(message);

                 try { 
                      Thread.sleep(5000);  
                 } catch (InterruptedException e) { 
                      Thread.currentThread().interrupt(); 
                 } 
            } 
       } 
  } 
   


    }
