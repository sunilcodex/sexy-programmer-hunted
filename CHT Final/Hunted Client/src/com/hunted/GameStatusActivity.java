package com.hunted;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class GameStatusActivity extends Activity
{
	private String _id;
	private Bitmap _qrCode;
	
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);

		setContentView(R.layout.game_status);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		int playerType = this.getIntent().getIntExtra("playertype", PlayerType.Player);
		String name = this.getIntent().getStringExtra("name");
		String id = this.getIntent().getStringExtra("id");
		
		((TextView)this.findViewById(R.id.txtPlayerName)).setText(name);
		
		
		if(playerType == PlayerType.Player)
		{
			((TextView)this.findViewById(R.id.txtPlayerGroup)).setText(this.getResources().getString(R.string.challenger));
			if(id != _id)
			{
				_id = id;
				
				File file = new File(this.getCacheDir(), "qrcode_"+id+".png");
				if(!file.exists())
				{
					_qrCode = QRCodeGenerator.encodeString2(_id);
					if(_qrCode != null)
					{
						
						try 
						{
							file.createNewFile();
							FileOutputStream fs = new FileOutputStream(file);
							_qrCode.compress(CompressFormat.PNG, 0, fs);
							fs.close();
						}
						catch (IOException e) 
						{
						}
						
					}
				}
				else
				{
					_qrCode = BitmapFactory.decodeFile(file.getAbsolutePath());
				}
				
				View viewe = this.findViewById(R.id.imgQrCode);
				((ImageView)this.findViewById(R.id.imgQrCode)).setImageBitmap(_qrCode);
			}
		}
		else
		{
			((TextView)this.findViewById(R.id.txtPlayerGroup)).setText(this.getResources().getString(R.string.hunter));
			((ImageView)this.findViewById(R.id.imgQrCode)).setVisibility(View.GONE);
		}
		
		
	}
}
