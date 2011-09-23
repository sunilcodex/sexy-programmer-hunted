package com.hunted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class joib_game extends ListActivity {
    /** Called when the activity is first created. */
	
	
	private ImageView imageView=null;
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
        //�I���z��
        imageView=(ImageView)findViewById(R.id.imageView1);  
        //imageView.setAlpha(100);
        
        getPlayerList();
        
        
    }
    
    
    private void getPlayerList()
    {
    	
    	name=new ArrayList<String>();
    	status=new ArrayList<String>();

          /* �Ĥ@���]�w��[�^��ڥؿ�] */
    	name.add("b1");
    	status.add("h");
          /* �ĤG���]�w��[�^��W�@�h] */
          name.add("b2");
          status.add("p");

          name.add("b2");
          status.add("p");
          
          name.add("b2");
          status.add("p");
        /* �N�Ҧ��ɮץ[�JArrayList�� */
        
        /* �ϥΦ۩w�q��MyAdapter�ӱN��ƶǤJListActivity */
        setListAdapter(new game_room(this,name,status));   
    	
    }
    
    
}