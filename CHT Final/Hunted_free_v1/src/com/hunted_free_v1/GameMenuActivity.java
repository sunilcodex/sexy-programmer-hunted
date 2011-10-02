package com.hunted_free_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.RelativeLayout;

public class GameMenuActivity extends Activity
{
	private GameMenuView _menuView;
	private UIHelper _uiHelper;

	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		// create UI helper for ui scaling
		Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		_uiHelper = new UIHelper(display.getWidth(), display.getHeight());
				
		// game menu
		_menuView = new GameMenuView(this);
		_menuView.setVerticalScrollBarEnabled(false);
		_menuView.setHorizontalScrollBarEnabled(false);
		_menuView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent();
				intent.putExtra("selected_menu_id", position);
                setResult(RESULT_OK, intent);
                finish();
			}
		});
		GameMenuAdapter menuAdapter = (GameMenuAdapter)_menuView.getAdapter();
		
		menuAdapter.setTextSize(_uiHelper.scaleHeight(1280, 60));
		menuAdapter.ItemHeight = _uiHelper.scaleHeight(1280, 150);
		menuAdapter.ItemPadding = _uiHelper.scaleHeight(1280, 15);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(display.getWidth() * 0.8), menuAdapter.getCount() * menuAdapter.ItemHeight + 5);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
		params.addRule(RelativeLayout.CENTER_VERTICAL, 0);
		_menuView.setLayoutParams(params);
		
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(_menuView);
		
		setContentView(layout);
	}
}
